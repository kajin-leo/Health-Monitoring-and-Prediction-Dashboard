package com.cs79_1.interactive_dashboard.DTO.Workout;

import lombok.Data;

import java.time.DayOfWeek;

@Data
public class DailyWorkoutData {
    DayOfWeek dayOfWeek;
    int MVPA;
    int Light;

    public DailyWorkoutData() {
    }

    public DailyWorkoutData(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        MVPA = 0;
        Light = 0;
    }

    public DailyWorkoutData(DayOfWeek dayOfWeek, int MVPA, int Light) {
        this.dayOfWeek = dayOfWeek;
        this.MVPA = MVPA;
        this.Light = Light;
    }

    public DailyWorkoutData addMVPA(int mVPA) {
        MVPA += mVPA;
        return this;
    }

    public DailyWorkoutData addLight(int light) {
        Light += light;
        return this;
    }

    public DailyWorkoutData divideMVPA(int divider) {
        MVPA /= divider;
        return this;
    }

    public DailyWorkoutData divideLight(int divider) {
        Light /= divider;
        return this;
    }
}
