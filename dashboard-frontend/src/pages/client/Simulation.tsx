import React, { useState } from "react";
import HeatmapChart from "../../components/DashboardCardContent/HealthStatusHeatmap/HealthStatus";

const SimulationView = () => {
    const [group, setGroup] = useState<"weekdays" | "weekends">("weekdays");

    return (
        <div className="w-full h-full flex flex-col items-center justify-center gap-6">
            <div className="w-full flex justify-end pr-6">
            <div className="flex gap-2 mt-2">
                <button
                className={`px-3 py-1 rounded-lg shadow-md text-sm ${
                    group === "weekdays"
                    ? "bg-gradient-to-r from-blue-400 to-blue-300 text-white"
                    : "bg-gray-100"
                }`}
                onClick={() => setGroup("weekdays")}
                >
                Weekdays
                </button>
                <button
                className={`px-3 py-1 rounded-lg shadow-md text-sm ${
                    group === "weekends"
                    ? "bg-gradient-to-r from-blue-400 to-blue-300 text-white"
                    : "bg-gray-100"
                }`}
                onClick={() => setGroup("weekends")}
                >
                Weekends
                </button>
            </div>
        </div>

            <div className="w-[95%] h-[450px] rounded-lg shadow relative p-4">
                 
                <div className="w-full h-full flex items-center justify-center">
                    <p className="text-gray-500">Placeholder for actitvity linechart</p>
                </div>
            </div>

            
            <div className="bg-white/50 backdrop-blur-md shadow-md p-10 rounded-lg w-[95%] h-[300px] flex items-center justify-center">
                <div className="grid grid-cols-2 h-full w-full">
                    <HeatmapChart group={group} />
                    <HeatmapChart group={group} />
                </div>
            </div>
        </div>

    );
};

export default SimulationView;
