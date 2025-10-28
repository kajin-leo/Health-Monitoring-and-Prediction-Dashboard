package com.cs79_1.interactive_dashboard.DTO;

import lombok.Data;

@Data
public class SleepSummary {
    private int thisWeekAvgMin;
    private double schoolNightAvgHrs;
    private double weekendNightAvgHrs;
}
