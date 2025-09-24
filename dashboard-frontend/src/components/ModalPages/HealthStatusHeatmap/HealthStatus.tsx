import React from "react";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
} from "chart.js";
import { MatrixController, MatrixElement } from "chartjs-chart-matrix";
import { Chart } from "react-chartjs-2";

ChartJS.register(
    CategoryScale,
    LinearScale,
    MatrixController,
    MatrixElement,
    Tooltip,
    Legend
);

const generateData = () => {
    const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
    const data = [];
    const maxSec = 3600;

    for (let d = 0; d < 7; d++) {
        for (let h = 0; h < 24; h++) {
            const seconds = Math.floor(Math.random() * maxSec);
            const score = Math.random() * 2 - 1;
            data.push({
                x:days[d],
                y: h,
                score,
                seconds,

            });
        }
    }
    return data;
};

const data = {
    datasets: [
        {
            label: "Activity Heatmap",
            data: generateData(),
            backgroundColor:(ctx) =>{
                const v = ctx.raw.score; // [-1,1]
                if (v <= 0) {
                    // 蓝 → 白
                    const t = (v + 1) / 1; // [-1,0] → [0,1]
                    const r = Math.round(255 * t);
                    const g = Math.round(255 * t);
                    const b = 255;
                    return `rgb(${r},${g},${b})`;
                } else {
                    // 白 → 红
                    const t = v / 1; // [0,1]
                    const r = 255;
                    const g = Math.round(255 * (1 - t));
                    const b = Math.round(255 * (1 - t));
                    return `rgb(${r},${g},${b})`;
                }
            },
            // backgroundColor(ctx) {
            //     const value = ctx.raw.v;
            //     const t = (value + 1) / 2;
            //     const r = Math.round(255 * t);
            //     const g = Math.round(255 * (1 - Math.abs(value)));
            //     const b = Math.round(255 * (1 - t));
            //     return `rgb(${r},${g},${b})`;
            // },
            // backgroundColor(ctx) {
            //     const v = ctx.raw.score;
            //     if (v <= 0) {
            //         // 青 → 白
            //         const t = (v + 1) / 1; // [-1,0] → [0,1]
            //         const r = Math.round(255 * t);
            //         const g = 255;
            //         const b = 255;
            //         return `rgb(${r},${g},${b})`;
            //     } else {
            //         // 白 → 粉
            //         const t = v / 1;
            //         const r = 255;
            //         const g = Math.round(255 * (1 - 0.28 * t)); // 255 → ~182
            //         const b = Math.round(255 * (1 - 0.24 * t)); // 255 → ~193
            //         return `rgb(${r},${g},${b})`;
            //     }
            // },
             borderWidth: 1,
             width: 20,
             height: 20,
            // width(ctx) {
            //     const a = ctx.chart.chartArea;
            //     if (!a) return 0;
            //     return (a.right - a.left) / 7 - 2;
            // },
            // height(ctx) {
            //     const a = ctx.chart.chartArea;
            //     if (!a) return 0;
            //     return (a.bottom - a.top) / 24 - 2;
            // },
        },
    ],
};

const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
        x: {// 24hours(week days average weekends average each hour)
            type: "category",
            labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
            offset: true,
            title: { display: true, text: "Day of Week" },
        },
        y: {//sum of seconds 3600 max an hour MVPA CPA
            type: "linear",
            min: 0,
            max: 23,
            ticks: {
                stepSize: 1,
                callback: (v) => `${v}:00`,
            },
            title: { display: true, text: "Hour of Day" },
        },
    },
    plugins: {
        tooltip: {
            callbacks: {
                label(ctx) {
                    const { x, y, seconds,score } = ctx.raw;
                    return `${x} ${y}:00 → ${seconds} s (score=${score.toFixed(2)})`;
                },
            },
        },
        legend: { display: false },
    },
};

export default function HeatmapChart() {
    return (
        <div style={{ width: "200px", height: "500px" }}>
            <Chart type="matrix" data={data} options={options} />
        </div>
    );
}
