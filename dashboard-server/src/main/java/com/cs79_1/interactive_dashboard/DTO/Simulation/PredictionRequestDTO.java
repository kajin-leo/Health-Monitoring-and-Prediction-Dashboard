package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

@Data
public class PredictionRequestDTO {
    boolean isWeekend = false;
    int[] mvpa = new int[24];
    int[] light = new int[24];

    public PredictionRequestDTO() {}
}
