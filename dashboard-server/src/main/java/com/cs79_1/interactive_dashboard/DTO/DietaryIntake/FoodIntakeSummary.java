package com.cs79_1.interactive_dashboard.DTO.DietaryIntake;

import lombok.Data;

@Data
public class FoodIntakeSummary {
    private double energy;
    private double protective;
    private double bodyBuilding;
    private double limitedFood;
    private double limitedBeverages;
}
