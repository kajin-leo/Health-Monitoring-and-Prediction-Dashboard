package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AlteredActivityPredictionRequest {
    long userId;
    boolean isWeekdays;
    Map<Integer, Double> mvpa;
    Map<Integer, Double> light;

    public AlteredActivityPredictionRequest(long userId, boolean isWeekdays) {
        mvpa = new HashMap<>();
        light = new HashMap<>();
        this.userId = userId;
        this.isWeekdays = isWeekdays;
    }

    public void addMVPA(int startingHourIndex, double scale) {
        mvpa.put(startingHourIndex, scale);
    }

    public void addLight(int startingHourIndex, double scale) {
        light.put(startingHourIndex, scale);
    }
}
