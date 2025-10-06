import { useEffect, useRef } from 'react';
import 'chartjs-plugin-dragdata';
import { Chart } from "chart.js/auto";
import { type ActivityData } from './SimulateActivity';

const SimulateActivityChart = ({data, DragEndCallback, resetTrigger} : {data: ActivityData, DragEndCallback? : (datasetIndex: number, index: number, value: number) => void, resetTrigger: number}) => {
    const chartRef = useRef<HTMLCanvasElement>(null);
    const chartInstance = useRef<Chart | null>(null);

    useEffect(() => {
        if (!chartRef.current) return;

        if (chartInstance.current) {
            chartInstance.current.destroy();
            chartInstance.current = null;
        }

        const ctx = chartRef.current.getContext('2d');
        if (!ctx) return;

        const chartData = {
            labels: [...data.description],
                datasets: [
                    {
                        label: 'MVPA',
                        data: [...data.mvpa],
                        borderColor: 'rgba(247, 128, 37, 1)',
                        backgroundColor: 'rgba(250, 160, 5, 0.6)',
                        fill: true,
                        tension: 0.4,
                    },
                    {
                        label: 'Light Activity',
                        data: [...data.light],
                        borderColor: 'rgba(152, 214, 19, 1)',
                        backgroundColor: 'rgba(217, 247, 131, 0.6)',
                        fill: true,
                        tension: 0.4,
                    }
                ]
        }

        chartInstance.current = new Chart(ctx, {
            type: 'line',
            data: chartData,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: false,
                        text: 'Workout Overview',
                    },
                    tooltip: {
                        enabled: true
                    },
                    dragData: {
                        round: 0,
                        dragX: false,
                        dragY: true,
                        onDragEnd: (e, datasetIndex, index, value) => {
                            // console.log('Drag End:', { index, value });
                            // setDragIndex(-1);

                            const numericValue = value != null ? Number(value) : 0;

                            if (DragEndCallback) {
                                DragEndCallback(datasetIndex, index, numericValue);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        title: {
                            display: true,
                            text: "Seconds"
                        },
                        beginAtZero: true,
                        min:0,
                        max:3600
                    }
                }
            }
        });

        return () => {
            if (chartInstance.current) {
                chartInstance.current.destroy();
            }
        };
    }, [data, resetTrigger]);

    return (
        <div className="w-full relative h-full">
            {
                <canvas ref={chartRef} ></canvas>
            }
        </div>
    );
}

export default SimulateActivityChart;