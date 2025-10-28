package com.cs79_1.interactive_dashboard.DTO.Workout;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class WorkoutHeatmapDTO {

    private Map<Integer, Map<String, Double>> mvpaHeatmap = new LinkedHashMap<>();
    private Map<Integer, Map<String, Double>> lightHeatmap = new LinkedHashMap<>();

    public void addData(int hour, String binLabel, double mvpaAvg, double lightAvg) {
        mvpaHeatmap
                .computeIfAbsent(hour, k -> new LinkedHashMap<>())
                .put(binLabel, mvpaAvg);
        lightHeatmap
                .computeIfAbsent(hour, k -> new LinkedHashMap<>())
                .put(binLabel, lightAvg);
    }
}
