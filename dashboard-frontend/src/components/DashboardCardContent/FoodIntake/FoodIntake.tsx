import React, { useEffect, useState } from "react";
import { apiClient } from "../../../service/axios";
import { background } from "storybook/internal/theming";
import { plugins, type ChartOptions } from "chart.js";
import { Doughnut } from "react-chartjs-2";

interface FoodIntakeResultDto {
  energy: number;
  protective: number;
  bodyBuilding: number;
  recEnergy: number;
  recProtective: number;
  recBodyBuilding: number;
  pctEnergy: number;
  pctProtective: number;
  pctBodyBuilding: number;
  dailyPctEnergy?: number;
  dailyPctProtective?: number;
  dailyPctBodyBuilding?: number;
}


// Colors: darker = recommended track, lighter = actual track
const ringColors = [
  { actual: "#dfff60", recommended: "#e0e4a5ff" }, // Energy
  { actual: "#94ff9e", recommended: "#b7deb9ff" }, // Protective
  { actual: "#15eca1", recommended: "#a4d3cae5" }, // Body Building
];

const radii = [60, 48, 36];
const labels = ["Energy", "Protective", "Body Building"] as const;
const TRACK_WIDTH = 14;


function RingLayer({
  cx,
  cy,
  r,
  trackColor,
  fillColor,
  arcPct,
}: {
  cx: number;
  cy: number;
  r: number;
  trackColor: string;
  fillColor: string;
  arcPct: number;
}) {
  const circumference = 2 * Math.PI * r;
  const pct = Math.max(0, Math.min(arcPct, 1));
  const dash = pct * circumference;

  return (
    <g>
      {/* Recommended track*/}
      <circle
        cx={cx}
        cy={cy}
        r={r}
        fill="none"
        stroke={trackColor} strokeWidth={TRACK_WIDTH} />
      {/* Actual arc*/}
      <circle
        cx={cx}
        cy={cy}
        r={r}
        fill="none"
        stroke={fillColor}
        strokeWidth={TRACK_WIDTH}
        strokeLinecap="round"
        strokeDasharray={`${dash} ${Math.max(0, circumference - dash)}`}
        transform={`rotate(-90 ${cx} ${cy})`}
        opacity={0.9}
      />
    </g>
  );
}

function toFixed1(n: number) { return Number.isFinite(n) ? n.toFixed(1) : "0.0"; }

const FoodIntakeRings: React.FC<FoodIntakeResultDto> = (data) => {
  
  const pctOfRec = [data.pctEnergy, data.pctProtective, data.pctBodyBuilding];

  const chartData = {
    labels: labels.map((label) => label),
    datasets: labels.map((label, index)=>({
      label: label,
      data: [pctOfRec[index], Math.max(0, 100 - pctOfRec[index])],
      backgroundColor: [ringColors[index].actual, ringColors[index].recommended],
      borderWidth: 0,
      cutout: `${40}%`,
      circumference: 360,
      rotation: 0
    }))
  };

  const chartOptions:ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      },
      tooltip: {
        enabled: false
      }
    },
    elements: {
      arc: {
        borderRadius: 10,
        borderColor: "#ffffff",
        borderWidth: 10
      }
    }
  };

  return (
    <div className='flex flex-col w-full h-full'>
      <h1 className="w-fit opacity-100 rounded-lg text-gray-800 dark:text-gray-200 select-none pl-1 text-lg tracking-tight font-bold font-[Nunito] min-h-10 flex-shrink-0">
        Dietary Intake
      </h1>
      <div className="h-full w-full flex gap-0 items-center justify-between flex-1 p-2">
        <div className="h-full w-full min-w-0 min-h-0 drop-shadow-lg">
          <Doughnut data={chartData} options={chartOptions} className="dark:brightness-70 dark:saturate-120 dark:contrast-150"/>
        </div>

        {/* Legend */}
        <div className="h-full flex-shrink-0 flex-col flex justify-center pr-5" style={{ fontSize: 14, lineHeight: 1.6 }}>
          {labels.map((label, idx) => (
            <div key={label} style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 6 }}>
              <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                {/* <span title="Recommended (track)" style={{ width: 12, height: 12, background: ringColors[idx].recommended, display: "inline-block", borderRadius: 2 }} /> */}
                <span title="Actual (arc)" className="rounded-full border-1 border-black/20 shadow-md w-5 h-3 dark:brightness-70 dark:saturate-120 dark:contrast-150" style={{ background: ringColors[idx].actual, display: "inline-block" }} />
              </div>
              <div className="flex flex-col">
                <h1 className="text-md font-semibold">{label}</h1>
                <h2>{toFixed1(pctOfRec[idx] ?? 0)}%</h2>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>

  );
};

const FoodIntake: React.FC = () => {
  const [data, setData] = useState<FoodIntakeResultDto | null>(null);
  const [error, setError] = useState(false);


  useEffect(() => {
    async function fetchData() {
      try {
        const response = await apiClient.get("/food-intake/rings");
        console.log("food-intake/rings response:", response);
        const data = response.data as FoodIntakeResultDto;
        setData({
          energy: data.energy,
          protective: data.protective,
          bodyBuilding: data.bodyBuilding,
          recEnergy: data.recEnergy,
          recProtective: data.recProtective,
          recBodyBuilding: data.recBodyBuilding,
          pctEnergy: data.pctEnergy,
          pctProtective: data.pctProtective,
          pctBodyBuilding: data.pctBodyBuilding,
          dailyPctEnergy: data.dailyPctEnergy ?? 0,
          dailyPctProtective: data.dailyPctProtective ?? 0,
          dailyPctBodyBuilding: data.dailyPctBodyBuilding ?? 0,
        });
      } catch (e) {
        console.error("food-intake api error:", e);
        setError(true);
      }
    }
    fetchData();
  }, []);

  if (error || !data) {
    return (
      <div className="w-full h-full flex items-center justify-center backdrop-blur-sm text-gray-400 dark:text-gray-300">
        No data
      </div>
    );
  }

  return (
    <div id="Dietary Intake Container" className="w-full h-full">
      <FoodIntakeRings {...data} />
    </div>
  );
};

export default FoodIntake;
