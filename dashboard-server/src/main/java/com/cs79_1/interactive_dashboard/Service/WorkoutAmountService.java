package com.cs79_1.interactive_dashboard.Service;

import com.cs79_1.interactive_dashboard.DTO.Simulation.StructuredActivityDTO;
import com.cs79_1.interactive_dashboard.DTO.Workout.*;
import com.cs79_1.interactive_dashboard.Entity.WorkoutAmount;
import com.cs79_1.interactive_dashboard.Repository.WorkoutAmountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WorkoutAmountService {
    private static final Logger log = LoggerFactory.getLogger(WorkoutAmountService.class);
    @Autowired
    private WorkoutAmountRepository workoutAmountRepository;
    @Autowired
    private RedisService redisService;

    List<WorkoutAmount> getWorkoutAmountByUserIdDesc(Long userId) {
        return workoutAmountRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    List<WorkoutAmount> getWorkoutAmountByUserIdAsc(Long userId) {
        return workoutAmountRepository.findByUserIdOrderByDateTimeAsc(userId);
    }

    public Map<DayOfWeek, Map<Integer, DailyWorkoutData>> getAveragedTimeSegmentedData(Long userId) {
        List<WorkoutAmount> workoutAmounts = getWorkoutAmountRawData(userId);
        Map<DayOfWeek, Map<Integer, DailyWorkoutData>> result = new HashMap<>();
        Map<DayOfWeek, Map<Integer, Integer>> count = new HashMap<>();

        for (WorkoutAmount workoutAmount : workoutAmounts) {
            DayOfWeek dayOfWeek = workoutAmount.getDateTime().getDayOfWeek();

            int hour = workoutAmount.getDateTime().getHour();
            int mvpa = workoutAmount.getSumSecondsMVPA3();
            int light = workoutAmount.getTimesLight3();

            count.computeIfAbsent(dayOfWeek, k -> new HashMap<>()).merge(hour, 1, Integer::sum);
            result.computeIfAbsent(dayOfWeek, k -> new HashMap<>()).computeIfAbsent(hour, k -> new DailyWorkoutData(dayOfWeek)).addMVPA(mvpa).addLight(light);
        }

        log.info("Detailed Map: {}", result);

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            for (int i = 0; i < 24; i++) {
                if(count.get(dayOfWeek).containsKey(i)) {
                    DailyWorkoutData dailyWorkoutData = result.get(dayOfWeek).get(i);
                    dailyWorkoutData.divideMVPA(count.get(dayOfWeek).get(i)).divideLight(count.get(dayOfWeek).get(i));
                }
            }
        }

        return result;
    }

    public WorkoutOverviewDTO getOverview(Long userId) {
        WorkoutOverviewDTO overviewDTO = new WorkoutOverviewDTO();

        Map<DayOfWeek, DailyWorkoutData> dailyWorkoutDataMap = new HashMap<>();
        Map<DayOfWeek, Map<Integer, DailyWorkoutData>> averagedWorkoutDataMap = getAveragedTimeSegmentedData(userId);
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            for(int i = 0; i < 24; i++) {
                if(!averagedWorkoutDataMap.get(dayOfWeek).containsKey(i)) continue;

                int mvpa = averagedWorkoutDataMap.get(dayOfWeek).get(i).getMVPA();
                int light = averagedWorkoutDataMap.get(dayOfWeek).get(i).getLight();
                dailyWorkoutDataMap.computeIfAbsent(dayOfWeek, k -> new DailyWorkoutData(dayOfWeek)).addMVPA(mvpa).addLight(light);
            }
        }

        log.info("Overview Map: {}", dailyWorkoutDataMap);

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            overviewDTO.addData(dailyWorkoutDataMap.get(dayOfWeek));
        }

        return overviewDTO;
    }

    public WorkoutDailyDTO getDailyWorkoutDetail(Long userId, int requestedDayOfWeek) {
        Map<Integer, DailyWorkoutData> workoutDataMap = getAveragedTimeSegmentedData(userId).get(DayOfWeek.of(requestedDayOfWeek + 1));

        WorkoutDailyDTO workoutDailyDTO = new WorkoutDailyDTO();

        for(int i = 0; i < 24; i++) {
            if(!workoutDataMap.containsKey(i)) continue;
            DailyWorkoutData dailyWorkoutData = workoutDataMap.get(i);
            int mvpa = dailyWorkoutData.getMVPA();
            int light = dailyWorkoutData.getLight();

            String segment = String.format("%d:00 - %d:00", i, i + 1);
            HourlyDailyWorkoutData data = new HourlyDailyWorkoutData(segment, mvpa, light);
            workoutDailyDTO.addData(data);
        }

        return workoutDailyDTO;
    }

    public WorkoutTimeOfDayDTO getTimeOfDay(Long userId, int requestedTimeOfDay) {
        Map<DayOfWeek, Map<Integer, DailyWorkoutData>> averagedWorkoutDataMap = getAveragedTimeSegmentedData(userId);
        WorkoutTimeOfDayDTO timeOfDayDTO = new WorkoutTimeOfDayDTO();

        for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if(!averagedWorkoutDataMap.get(dayOfWeek).containsKey(requestedTimeOfDay)) continue;
            DailyWorkoutData workoutData = averagedWorkoutDataMap.get(dayOfWeek).get(requestedTimeOfDay);

            int mvpa = workoutData.getMVPA();
            int light = workoutData.getLight();

            DailyWorkoutData data = new DailyWorkoutData(dayOfWeek, mvpa, light);
            timeOfDayDTO.addData(data);
        }

        return timeOfDayDTO;
    }

    public List<WeeklyAggregatedHourDetails> getStructuredWorkoutDetail(Long userId) {
        List<WorkoutAmount> workoutAmounts = getWorkoutAmountRawData(userId);
        List<WeeklyAggregatedHourDetails> weeklyAggregatedHourDetailsList = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            weeklyAggregatedHourDetailsList.add(new WeeklyAggregatedHourDetails(i));
        }

        for (WorkoutAmount workoutAmount : workoutAmounts) {
            int dayOfWeek = workoutAmount.getDateTime().getDayOfWeek().ordinal();

            int hour = workoutAmount.getDateTime().getHour();
            int mvpa = workoutAmount.getSumSecondsMVPA3();
            int light = workoutAmount.getTimesLight3();
            long id = workoutAmount.getId();

            weeklyAggregatedHourDetailsList.get(hour).addDay(mvpa, light, id, dayOfWeek);
        }

        return weeklyAggregatedHourDetailsList;
    }

    public StructuredActivityDTO getStructuredActivityData(Long userId, boolean isWeekend) {
        List<WeeklyAggregatedHourDetails> weeklyAggregatedHourDetailsList = getStructuredWorkoutDetailFromRedis(userId);
        return getStructuredActivityData(weeklyAggregatedHourDetailsList, isWeekend);
    }

    public StructuredActivityDTO getStructuredActivityData(List<WeeklyAggregatedHourDetails> weeklyAggregatedHourDetails, boolean isWeekend) {
        StructuredActivityDTO structuredActivityDTO = new StructuredActivityDTO();
        for(int i = 0; i < 24; i++) {
            List<Integer> mvpa = weeklyAggregatedHourDetails.get(i).getMVPA();
            List<Integer> light = weeklyAggregatedHourDetails.get(i).getLight();
            List<Integer> daysOfWeek = weeklyAggregatedHourDetails.get(i).getDaysOfWeek();
            int count = weeklyAggregatedHourDetails.get(i).getCount();

            int mvpaSum = 0;
            int lightSum = 0;
            for(int j = 0; j < weeklyAggregatedHourDetails.get(i).getCount(); j++){
                if((!isWeekend && daysOfWeek.get(j) > 4) || (isWeekend && daysOfWeek.get(j) <= 4)) {
                    count--;
                    continue;
                }
                mvpaSum += mvpa.get(j);
                lightSum += light.get(j);
            }

            structuredActivityDTO.addMVPA(mvpaSum / count);
            structuredActivityDTO.addLight(lightSum / count);
            structuredActivityDTO.addDescription(weeklyAggregatedHourDetails.get(i).getDescription());
        }

        return structuredActivityDTO;
    }

    public List<WeeklyAggregatedHourDetails> getStructuredWorkoutDetailFromRedis(Long userId) {
        String redisKey = "activity::" + userId;
        List<WeeklyAggregatedHourDetails> data = redisService.getList(redisKey, WeeklyAggregatedHourDetails.class);
        if(data == null) {
            data = getStructuredWorkoutDetail(userId);
            redisService.saveWithExpire(redisKey, data, 30, TimeUnit.MINUTES);
        }

        return data;
    }

    public List<WorkoutAmount> getWorkoutAmountRawData(Long userId) {
        String redisKey = "activityRaw::" + userId;
        List<WorkoutAmount> data = redisService.getList(redisKey, WorkoutAmount.class);
        if(data == null) {
            data = getWorkoutAmountByUserIdAsc(userId);
            redisService.saveWithExpire(redisKey, data, 30, TimeUnit.MINUTES);
        }

        return data;
    }

    public List<WeeklyAggregatedHourDetails> resetStructuredWorkoutDetailInRedis(Long userId) {
        String redisKey = "activity::" + userId;
        List<WeeklyAggregatedHourDetails> data = getStructuredWorkoutDetail(userId);
        redisService.saveObject(redisKey, data);
        return data;
    }

    public WorkoutHeatmapDTO getHeatmapFiltered(Long userId, boolean isWeekend) {
        List<WorkoutAmount> workoutAmounts = getWorkoutAmountRawData(userId);
        WorkoutHeatmapDTO heatmapDTO = new WorkoutHeatmapDTO();

        final int BIN_SIZE = 600;
        final int MAX_SECONDS = 3600;

        List<int[]> bins = new ArrayList<>();
        for (int start = 0; start < MAX_SECONDS; start += BIN_SIZE) {
            bins.add(new int[]{start, start + BIN_SIZE});
        }

        Map<Integer, Map<String, List<Integer>>> mvpaMap = new HashMap<>();
        Map<Integer, Map<String, List<Integer>>> lightMap = new HashMap<>();

        for (WorkoutAmount w : workoutAmounts) {
            DayOfWeek day = w.getDateTime().getDayOfWeek();
            boolean weekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);

            if (weekend != isWeekend) continue; // 只要 weekday 或 weekend

            int hour = w.getHour();
            int mvpa = w.getSumSecondsMVPA3();
            int light = w.getSumSecondsLight3();

            for (int[] bin : bins) {
                String binLabel = bin[0] + "-" + bin[1];

                if (mvpa >= bin[0] && mvpa < bin[1]) {
                    mvpaMap.computeIfAbsent(hour, k -> new HashMap<>())
                            .computeIfAbsent(binLabel, k -> new ArrayList<>())
                            .add(mvpa);
                }
                if (light >= bin[0] && light < bin[1]) {
                    lightMap.computeIfAbsent(hour, k -> new HashMap<>())
                            .computeIfAbsent(binLabel, k -> new ArrayList<>())
                            .add(light);
                }
            }
        }

        for (int hour = 0; hour < 24; hour++) {
            for (int[] bin : bins) {
                String binLabel = bin[0] + "-" + bin[1];

                double mvpaAvg = mvpaMap.getOrDefault(hour, Map.of())
                        .getOrDefault(binLabel, List.of())
                        .stream().mapToInt(Integer::intValue).average().orElse(0);

                double lightAvg = lightMap.getOrDefault(hour, Map.of())
                        .getOrDefault(binLabel, List.of())
                        .stream().mapToInt(Integer::intValue).average().orElse(0);

                heatmapDTO.addData(hour, binLabel, mvpaAvg, lightAvg);
            }
        }
        return heatmapDTO;
    }

    public WorkoutHeatmapDTO getHeatmapWeekdays(Long userId) {
        return getHeatmapFiltered(userId, false);
    }

    public WorkoutHeatmapDTO getHeatmapWeekends(Long userId) {
        return getHeatmapFiltered(userId, true);
    }
}
