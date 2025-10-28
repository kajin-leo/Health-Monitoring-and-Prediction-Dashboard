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

    @Column(nullable = false)
    private double cereals;

    @Column(nullable = false)
    private double vegetablesAndLegumes;

    @Column(nullable = false)
    private double fruit;

    @Column(nullable = false)
    private double dairy;

    @Column(nullable = false)
    private double fatsOils;

    @Column(nullable = false)
    private double meatFishPoultryEggs;

    @Column(nullable = false)
    private double drinks;

    @Column(nullable = false)
    private double extras;

    @Column(nullable = false)
    private double other;

    @Column(nullable = false)
    private double water;

    @Column(nullable = false)
    private double sugarSweetenedBeverages;

    @Column(nullable = false)
    private double energyGroupAvgDaily;

    @Column(nullable = false)
    private double protectiveGroupAvgDaily;

    @Column(nullable = false)
    private double bodybuildingGroupAvgDaily;

    @Column(nullable = false)
    private double limitedFoodAvgDaily;

    @Column(nullable = false)
    private double limitedBeveragesAvgDaily;

    @Column(nullable = false)
    private double waterAvgDaily;

    public WeeklyIntake() {}

    public WeeklyIntake(User user) {
        this.user = user;
    }
}
