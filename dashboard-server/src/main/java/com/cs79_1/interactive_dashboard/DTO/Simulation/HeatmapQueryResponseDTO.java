package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

@Data
public class HeatmapQueryResponseDTO {
    Object data;
    boolean fromCache = false;
    String taskId;

    public HeatmapQueryResponseDTO(Object data, boolean fromCache, String taskId) {
        this.data = data;
        this.fromCache = fromCache;
        this.taskId = taskId;
    }
}
