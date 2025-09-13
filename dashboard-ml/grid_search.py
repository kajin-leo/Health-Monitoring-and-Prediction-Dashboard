# tools/grid_search.py
import itertools, os, subprocess, sys
from datetime import datetime

# === 1) 基本路径 ===
PYTHON_EXE = sys.executable                       # 用当前解释器
REPO_DIR   = r"C:\Users\19590\OneDrive\Desktop\usyd\25s2\capstone\model\mvts_transformer"       # ← 改成你的仓库根目录
DATA_DIR   = r"C:\Users\19590\OneDrive\Desktop\usyd\25s2\capstone\model\mvts_transformer\data"                   # ← 改成你的数据根目录

# === 2) 固定不动的参数（按你现在的用法）===
BASE = dict(
    output_dir = os.path.join(REPO_DIR, "experiments"),
    data_dir   = DATA_DIR,
    data_class = "mycsv",
    task       = "regression",
    pattern    = "TRAIN",
    val_pattern= "VALID",
    #test_pattern="TEST",
    epochs     = 50,            # 训练轮数（可改）
    #lr_step    = 5,             # 若你使用分段lr，可留着；不用可以删
    optimizer  = "RAdam",
    #pos_encoding="fixed",
    records_file = "GridSearch_records.xls",   # 把指标都写进一个表里便于比较
    n_proc     = 0,             # DataLoader workers（Windows建⻔先用0）
    #print_interval = 1,
    #seed       = 42,
)

# === 3) 网格（按需增减）===
GRID = {
    "lr":          [1e-3, 5e-4, 1e-4],
    "batch_size":  [32 ,64],
    "num_layers":  [2, 3, 4],          # transformer
    "d_model":     [32, 64, 128],
    "dim_feedforward": [256, 512],
    "dropout":     [0.1, 0.2],

}

def run_one(cfg, name_suffix):
    # 组装命令行
    args = [
        PYTHON_EXE, os.path.join(REPO_DIR, "src", "main.py"),
        "--output_dir", cfg["output_dir"],
        "--data_dir",   cfg["data_dir"],
        "--data_class", cfg["data_class"],
        "--task",       cfg["task"],
        "--pattern",    cfg["pattern"],
        "--val_pattern",cfg["val_pattern"],
        #"--test_pattern",cfg["test_pattern"],
        "--epochs",     str(cfg["epochs"]),
        "--optimizer",  cfg["optimizer"],
        #"--pos_encoding", cfg["pos_encoding"],
        "--records_file", cfg["records_file"],
        "--n_proc",     str(cfg["n_proc"]),
        #"--print_interval", str(cfg["print_interval"]),
        #"--seed",       str(cfg["seed"]),
        "--lr",         str(cfg["lr"]),
        "--batch_size", str(cfg["batch_size"]),
        "--num_layers", str(cfg["num_layers"]),
        "--d_model",    str(cfg["d_model"]),
        "--dim_feedforward", str(cfg["dim_feedforward"]),
        "--dropout",    str(cfg["dropout"]),
        "--name",       f"gs_{name_suffix}",
    ]
    print("\n>>> Running:", " ".join(args))
    subprocess.run(args, check=True)

if __name__ == "__main__":
    stamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    keys  = list(GRID.keys())
    for values in itertools.product(*[GRID[k] for k in keys]):
        combo = dict(zip(keys, values))
        cfg = {**BASE, **combo}
        suffix = "_".join([f"{k}{combo[k]}" for k in keys]) + f"_{stamp}"
        run_one(cfg, suffix)

    print("\n=== 全部组合已完成。到 experiments 或 GridSearch_records.xlsx 里挑最优 ===")
