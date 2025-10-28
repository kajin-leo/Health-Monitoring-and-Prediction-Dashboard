package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

@Data
public class HeatmapTaskResponse {
    long userId;
    Object probs;
    Object mvpa_impact;
    Object light_impact;

    public HeatmapTaskResponse() {
    }

    public HeatmapTaskResponse(long userId, Object probs, Object mvpa_impact, Object light_impact) {
        this.userId = userId;
        this.probs = probs;
        this.mvpa_impact = mvpa_impact;
        this.light_impact = light_impact;
    }
}
