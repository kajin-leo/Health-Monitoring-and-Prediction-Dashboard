import { Clock } from 'lucide-react';
import { apiClient } from '../../../service/axios';
import { useEffect, useState, useRef } from 'react';
import RadarChart from './RadarChart';
// import background from '../../assets/DataAcknowledgeBG.png'

interface ZScores {
    iotf: number;
    cachera: number;
    oms: number;
    cdc: number;
}

interface weightmetrics {
    iotfL: number,
    iotfM: number,
    iotfS: number,
    iotfP: number,
    iotfC: string,
    cacheraL: number,
    cacheraM: number,
    cacheraS: number,
    cacheraP: number,
    cacheraC: string,
    omsL: number,
    omsM: number,
    omsS: number,
    omsP: number,
    omsC: string,
    cdcL: number,
    cdcM: number,
    cdcS: number,
    cdcP: number,
    cdcC: string,
}

const WeightMetrics = () => {
    const [zScores, setZScores] = useState<ZScores | null>(null);
    const [weightMetric, setWeightMetric] = useState<weightmetrics | null>(null);

    useEffect(() => {
        const fetchWeightStatus = async () => {
            try {
                const response = await apiClient.get('/static/weight-metrics');
                const data = response.data;
                const zdata: ZScores = {
                    iotf: data.iotfZ,
                    cachera: data.cacheraZ,
                    oms: data.omsZ,
                    cdc: data.cdcZ
                }
                const weightdata: weightmetrics = {
                    iotfC: data.iotfC,
                    iotfL: data.iotfL,
                    iotfM: data.iotfM,
                    iotfS: data.iotfS,
                    iotfP: data.iotfP,
                    cacheraC: data.cacheraC,
                    cacheraL: data.cacheraL,
                    cacheraM: data.cacheraM,
                    cacheraS: data.cacheraS,
                    cacheraP: data.cacheraP,
                    omsC: data.omsC,
                    omsL: data.omsL,
                    omsM: data.omsM,
                    omsS: data.omsS,
                    omsP: data.omsP,
                    cdcC: data.cdcC,
                    cdcL: data.cdcL,
                    cdcM: data.cdcM,
                    cdcS: data.cdcS,
                    cdcP: data.cdcP,
                }
                console.log(data);
                setWeightMetric(weightdata);
                setZScores(zdata);
            } catch (error) {
                const data: ZScores = {
                    iotf: 0,
                    cachera: 0,
                    oms: 0,
                    cdc: 0
                };

                const weightdata: weightmetrics = {
                    iotfC: "No Comment",
                    iotfL: 0,
                    iotfM: 0,
                    iotfS: 0,
                    iotfP: 0,
                    cacheraC: "No Comment",
                    cacheraL: 0,
                    cacheraM: 0,
                    cacheraS: 0,
                    cacheraP: 0,
                    omsC: "No Comment",
                    omsL: 0,
                    omsM: 0,
                    omsS: 0,
                    omsP: 0,
                    cdcC: "No Comment",
                    cdcL: 0,
                    cdcM: 0,
                    cdcS: 0,
                    cdcP: 0,
                }
                setWeightMetric(weightdata);
                setZScores(data);
            }
        };
        fetchWeightStatus();
    }, []);

    const metrics = ['IOTF', 'Cachera', 'OMS', 'CDC'];
    const vars = ['L', 'M', 'S'];
    const decimalCount = 2;

    return (
        <div className='w-full h-full justify-between gap-2 flex flex-col'>
            <h1 className="text-2xl mt-2 ml-2 flex-shrink-0">
                Weight Status
            </h1>
            <div className='h-full flex justify-between items-center gap-2 overflow-hidden'>
                {
                    zScores && (
                        <RadarChart data={zScores} title='Z-Score' />
                    )
                }
                <div className='flex flex-col gap-0.5'>
                    {
                        weightMetric != null ? metrics.map((name) => {
                            return (
                                <div className='flex flex-col justify-start'>
                                    <h2 className='text-[15px] font-bold'>{name}</h2>
                                    <div className='flex justify-between items-center gap-2'>
                                        {
                                            vars.map((varName) => {
                                                return (<h3 className='text-sm'>L: {weightMetric[name.toLowerCase() + varName].toFixed(decimalCount)}</h3>)
                                            })
                                        }
                                        <h3 className='text-sm'>Percentile: {weightMetric[name.toLowerCase() + 'P'].toFixed(decimalCount)}</h3>
                                        <h3 className='text-sm'>Comment: {weightMetric[name.toLowerCase() + 'C']}</h3>
                                    </div>
                                </div>
                            )
                        }) : ''
                        /*
                        weightMetric && (
                            <>
                                <div className='flex flex-col justify-start'>
                                    <h2 className='text-l font-bold'>IOTF</h2>
                                    <div className='flex justify-between items-center gap-2'>
                                        <h3 className='text-sm'>L: {weightMetric.iotfL.toFixed(decimalCount)}</h3>
                                        <h3 className='text-sm'>M: {weightMetric.iotfM.toFixed(decimalCount)}</h3>
                                        <h3 className='text-sm'>S: {weightMetric.iotfS.toFixed(decimalCount)}</h3>
                                        <h3 className='text-sm'>Percentile: {weightMetric.iotfP.toFixed(decimalCount)}</h3>
                                    </div>
                                    <h3 className='text-sm'>Comment: {weightMetric.iotfC}</h3>
                                </div>

                                <div className='flex flex-col justify-start'>
                                    <h2 className='text-l font-bold'>Cachera</h2>
                                    <div className='flex justify-between items-center gap-2'>
                                        <h3>L: {weightMetric.cacheraL.toFixed(decimalCount)}</h3>
                                        <h3>M: {weightMetric.cacheraM.toFixed(decimalCount)}</h3>
                                        <h3>S: {weightMetric.cacheraS.toFixed(decimalCount)}</h3>
                                        <h3>Percentile: {weightMetric.cacheraP.toFixed(decimalCount)}</h3>
                                    </div>
                                    <h3>Comment: {weightMetric.cacheraC}</h3>
                                </div>

                                <div className='flex flex-col justify-start'>
                                    <h2 className='text-l font-bold'>OMS</h2>
                                    <div className='flex justify-between items-center gap-2'>
                                        <h3>L: {weightMetric.omsL.toFixed(decimalCount)}</h3>
                                        <h3>M: {weightMetric.omsM.toFixed(decimalCount)}</h3>
                                        <h3>S: {weightMetric.omsS.toFixed(decimalCount)}</h3>
                                        <h3>Percentile: {weightMetric.omsP.toFixed(decimalCount)}</h3>
                                    </div>
                                    <h3>Comment: {weightMetric.omsC}</h3>
                                </div>

                                <div className='flex flex-col justify-start'>
                                    <h2 className='text-l font-bold'>CDC</h2>
                                    <div className='flex justify-between items-center gap-2'>
                                        <h3>L: {weightMetric.cdcL.toFixed(decimalCount)}</h3>
                                        <h3>M: {weightMetric.cdcM.toFixed(decimalCount)}</h3>
                                        <h3>S: {weightMetric.cdcS.toFixed(decimalCount)}</h3>
                                        <h3>Percentile: {weightMetric.cdcP.toFixed(decimalCount)}</h3>
                                    </div>
                                    <h3>Comment: {weightMetric.cdcC}</h3>
                                </div>
                            </>
                        )
                            */
                    }
                </div>
            </div>
        </div>
    )
};

export default WeightMetrics;