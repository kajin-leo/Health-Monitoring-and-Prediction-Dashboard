package com.cs79_1.interactive_dashboard.DTO.Workout;

import lombok.Data;

@Data
public class HourlyDailyWorkoutData {
    String timeSegmentStarting;
    int mvpa;
    int light;

    public HourlyDailyWorkoutData() {
    }

    public HourlyDailyWorkoutData(String timeSegmentStarting, int mvpa, int light) {
        this.timeSegmentStarting = timeSegmentStarting;
        this.light = light;
        this.mvpa = mvpa;
    }
}
