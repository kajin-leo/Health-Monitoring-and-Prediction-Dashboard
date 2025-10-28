package com.cs79_1.interactive_dashboard.DTO;

import lombok.Data;

@Data
public class BodyCompositionSummary {
    private double fatPct;
    private double musclePct;
    private double waterPct;
    
    private double wlgr625;
    private double wlgr50;
    private double wlgx625;
    private double wlgx50;
    private double BMI;
}
