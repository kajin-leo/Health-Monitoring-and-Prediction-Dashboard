package com.cs79_1.interactive_dashboard.DTO;

import com.cs79_1.interactive_dashboard.Enum.HFZClassification;
import lombok.Data;

@Data
public class BodyMetricsSummaryDTO {
    double height;
    double weight;
    double waistSize;
    double bmi;
    HFZClassification classification;

    public BodyMetricsSummaryDTO() {

    }

    public BodyMetricsSummaryDTO(double height, double weight, double waistSize, double bmi, HFZClassification classification) {
        this.height = height;
        this.weight = weight;
        this.waistSize = waistSize;
        this.bmi = bmi;
        this.classification = classification;
    }
}
