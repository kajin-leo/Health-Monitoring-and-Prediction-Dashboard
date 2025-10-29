package com.cs79_1.interactive_dashboard.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class WeeklyIntake {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = true)
    private Double cereals;

    @Column(nullable = true)
    private Double vegetablesAndLegumes;

    @Column(nullable = true)
    private Double fruit;

    @Column(nullable = true)
    private Double dairy;

    @Column(nullable = true)
    private Double fatsOils;

    @Column(nullable = true)
    private Double meatFishPoultryEggs;

    @Column(nullable = true)
    private Double drinks;

    @Column(nullable = true)
    private Double extras;

    @Column(nullable = true)
    private Double other;

    @Column(nullable = true)
    private Double water;

    @Column(nullable = true)
    private Double sugarSweetenedBeverages;

    @Column(nullable = true)
    private Double energyGroupAvgDaily;

    @Column(nullable = true)
    private Double protectiveGroupAvgDaily;

    @Column(nullable = true)
    private Double bodybuildingGroupAvgDaily;

    @Column(nullable = true)
    private Double limitedFoodAvgDaily;

    @Column(nullable = true)
    private Double limitedBeveragesAvgDaily;

    @Column(nullable = true)
    private Double waterAvgDaily;

    public WeeklyIntake() {}

    public WeeklyIntake(User user) {
        this.user = user;
    }
}
