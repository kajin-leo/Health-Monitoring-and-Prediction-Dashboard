from flask import Flask, request, jsonify
import torch
import numpy as np
import pandas as pd
from utils import utils
from utils.hook_attcat import AttnHiddenCollector
from ts_transformer import TSTransformerEncoderClassiregressor
from data import Normalizer
import pickle
import os
from sqlalchemy import create_engine, text

app = Flask(__name__)

CHECKPOINT = r"../checkpoints/model_best.pth"     # 你的ckpt路径
# CSV_PATH   = r"dashboard-ml/SYNTH_000410.csv"  # 这次要预测的那份CSV
# ATTR_CSV   = r"C:/Users/wshiy/Desktop/USDY/COMP5703/data/CS79_1/individual_attributes.csv"


CLASS_NAMES = ["HFZ", "NIHR", "NI", "VL"]
NUM_CLASSES = len(CLASS_NAMES)
MAX_SEQ_LEN = 222          # 训练时用的序列对齐长度
CSV_HAS_HEADER = True     # 训练数据无表头就 False；有表头就 True
PAD_VALUE = 0.0
USE_STATIC = True

with open(r"../checkpoints/normalization.pickle", "rb") as f:
    norm_dict = pickle.load(f)

# 2. 用里面的参数初始化 Normalizer
normalizer = Normalizer(norm_dict["norm_type"],
                        mean=norm_dict.get("mean"),
                        std=norm_dict.get("std"),
                        min_val=norm_dict.get("min_val"),
                        max_val=norm_dict.get("max_val"))

# ======= 构建模型 & 载入权重 =======
device = "cuda" if torch.cuda.is_available() else "cpu"
model = TSTransformerEncoderClassiregressor(
    feat_dim=3, max_len=MAX_SEQ_LEN, d_model=64, n_heads=8, num_layers=3,
    dim_feedforward=256, num_classes=NUM_CLASSES,
    dropout=0.1, pos_encoding='fixed', activation='gelu', norm='BatchNorm', freeze=False,
    pooling='mean', static_features_dim=2 if USE_STATIC else 0
)
model = utils.load_model(model, CHECKPOINT)
model = model.to(device)
# ======= for collecting AttCAT =======
collector = AttnHiddenCollector(model, capture_grads=True, store_on_cpu=False).register()

# 推荐用环境变量注入，便于部署
DB_URL = "postgresql+psycopg2://cs79dashboard:interactivedashboard_cs79-1@127.0.0.1:5480/cs79-dashboard"
engine = create_engine(DB_URL, pool_pre_ping=True, future=True)
def load_timeseries_from_db(sid: str, table_name: str = "workout_amount") -> pd.DataFrame:
    sql = text(f"""
        SELECT date_time as ts, sum_seconds_light3 as f3, sum_secondsmvpa3 as f1, sum_secondssed60 as f2
        FROM {table_name}
        WHERE user_id = :sid
        ORDER BY ts ASC
    """)
    df = pd.read_sql(sql, con=engine, params={"sid": sid})
    if df.empty:
        raise ValueError(f"No timeseries found for sid={sid}")

    return df[["f1", "f2", "f3"]]

def load_attrs_from_db(sid: str, table_name: str = "individual_attributes") -> tuple[float, float]:
    sql = text(f"""
        SELECT age_year as age, "sex (1 male 2 female)" AS sex
        FROM {table_name}
        WHERE user_name = :sid
        LIMIT 1
    """)
    df = pd.read_sql(sql, con=engine, params={"sid": sid})
    if df.empty:
        raise ValueError(f"No attributes found for sid={sid}")
    age = float(df.loc[0, "age"])
    is_female = 1.0 if int(df.loc[0, "sex"]) == 2 else 0.0
    return age, is_female

@app.route("/predict", methods=["POST"])
def predict():

    # ① 获取 sid（比如请求体里传 {"sid":"SYNTH_000410"} 或 form 里传 sid=SYNTH_000410）
    sid = (request.values.get("sid")
           or (request.json.get("sid") if request.is_json else None))
    if not sid:
        return jsonify({"error": "missing 'sid'"}), 400

    try:
        df = load_timeseries_from_db(sid)      # 返回列正好是 f1,f2,f3
        age, is_female = load_attrs_from_db(sid)
    except Exception as e:
        return jsonify({"error": str(e)}), 404

    feat = df.copy()
    feat.columns = ["f1", "f2", "f3"]

    # ====== 归一化 ======
    feat = normalizer.normalize(feat)

    # ====== pad or truncate ======
    T = len(feat)
    L = MAX_SEQ_LEN
    if T >= L:
        feat = feat.iloc[:L, :].reset_index(drop=True)
    else:
        pad_rows = L - T
        pad_df = pd.DataFrame(PAD_VALUE, index=range(pad_rows), columns=feat.columns)
        feat = pd.concat([feat, pad_df], axis=0, ignore_index=True)

    valid_len = min(T, L)
    pad_len = L - valid_len
    padding_mask = torch.ones(L, dtype=torch.bool)
    if pad_len > 0:
        padding_mask[-pad_len:] = False
    padding_mask = padding_mask.unsqueeze(0).to(device)

    # ====== tensor ======
    x = torch.from_numpy(feat.values.astype(np.float32)).unsqueeze(0).to(device)
    x.requires_grad_(True)

    # ====== 静态特征（来自数据库）======
    s_static = torch.tensor([[age, is_female]], dtype=torch.float32).to(device)

    # ====== forward ======
    model.eval()
    logits = model(x, padding_masks=padding_mask, static_features=s_static)
    probs = torch.softmax(logits, dim=1).detach().cpu().numpy().flatten().tolist()

    # ====== backward for AttCAT ======
    target = logits.gather(1, logits.argmax(1, keepdim=True)).sum()
    model.zero_grad(set_to_none=True)
    target.backward()

    attns  = [d['weights'] for d in collector.attn_per_layer]
    hiddens = [rec['output'] for rec in collector.hidden_per_layer]

    cats = []
    for h in hiddens:
        g = h.grad
        if h.shape[0] != x.shape[0]:  # [T,B,D] → [B,T,D]
            h = h.permute(1,0,2).contiguous()
            g = g.permute(1,0,2).contiguous()
        cats.append(h * g)   # [B,T,D]

    att_scores = []
    for cat, attn in zip(cats, attns):
        a = attn.mean(1).mean(1).unsqueeze(-1)  # [B,T,1]
        score = (cat * a).squeeze(0).detach().cpu().numpy()  # [T,D]
        att_scores.append(score)

    impact = np.sum(att_scores, axis=0)  # [T,D]，D=3
    vmax = np.percentile(np.abs(impact), 99)
    if vmax == 0: vmax = 1e-6
    impact = np.clip(impact, -vmax, vmax) / vmax
    impact = impact[:valid_len, :].tolist()  # [T,3]

    return jsonify({
        "sid": sid,
        "probs": {cls: prob for cls, prob in zip(CLASS_NAMES, probs)},
        "impact": impact
    })