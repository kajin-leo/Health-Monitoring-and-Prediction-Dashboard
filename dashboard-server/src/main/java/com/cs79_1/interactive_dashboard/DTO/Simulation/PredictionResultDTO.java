package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

@Data
public class PredictionResultDTO {
    String classification;
    double probability;
    String taskId;

    public PredictionResultDTO() {
    }

    public PredictionResultDTO(String classification, double probability, String taskId) {
        this.classification = classification;
        this.probability = probability;
        this.taskId = taskId;
    }
}
