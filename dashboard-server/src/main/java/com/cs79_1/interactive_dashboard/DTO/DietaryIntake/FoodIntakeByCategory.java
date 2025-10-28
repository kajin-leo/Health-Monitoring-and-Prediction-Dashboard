package com.cs79_1.interactive_dashboard.DTO.DietaryIntake;

import lombok.Data;

@Data
public class FoodIntakeByCategory {
    private String group;
    private double actual;
    private double recommended;

    public FoodIntakeByCategory(String group, double actual, double recommended) {
        this.group = group;
        this.actual = actual;
        this.recommended = recommended;
    }
}
