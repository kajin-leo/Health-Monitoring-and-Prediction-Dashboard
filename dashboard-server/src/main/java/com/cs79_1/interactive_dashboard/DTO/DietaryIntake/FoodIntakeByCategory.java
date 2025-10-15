package com.cs79_1.interactive_dashboard.DTO.DietaryIntake;

public class FoodIntakeByCategory {
    
    private String group;
    private double actual;

    public FoodIntakeByCategory(String group, double actual) {
        this.group = group;
        this.actual = actual;
    }

    public String getGroup() {
        return group;
    }

    public double getActual() {
        return actual;
    }
}
