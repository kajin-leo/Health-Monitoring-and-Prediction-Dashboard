package com.cs79_1.interactive_dashboard.DTO.Workout;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutDailyDTO {
    List<HourlyDailyWorkoutData> dataList;

    public WorkoutDailyDTO() {
        this.dataList = new ArrayList<>();
    }

    public void addData(HourlyDailyWorkoutData data){
        dataList.add(data);
    }
}
