package com.cs79_1.interactive_dashboard.DTO.DietaryIntake;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FoodIntakeResultDto {
    private double energy;
    private double protective;
    private double bodyBuilding;

    // Recommended absolute amounts (derived from % of daily total)
    private double recEnergy;
    private double recProtective;
    private double recBodyBuilding;

    // Actual vs recommendation (%)
    private double pctEnergy;
    private double pctProtective;
    private double pctBodyBuilding;

    //share of daily (% actual / total).
    private double dailyPctEnergy;
    private double dailyPctProtective;
    private double dailyPctBodyBuilding;

}
