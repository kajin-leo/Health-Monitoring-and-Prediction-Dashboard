package com.cs79_1.interactive_dashboard.DTO.Workout;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutTimeOfDayDTO {
    List<DailyWorkoutData> dataList;

    public WorkoutTimeOfDayDTO(){
        dataList = new ArrayList<>();
    }

    public void addData(DailyWorkoutData data){
        dataList.add(data);
    }
}
