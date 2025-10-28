package com.cs79_1.interactive_dashboard.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "food_frequency_records")
public class FoodFrequencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String foodFrequencyJson;

    @Column(columnDefinition = "TEXT")
    private String servingsJson;

    private LocalDateTime createdAt;

    public FoodFrequencyRecord() {}

    public FoodFrequencyRecord(Long userId, String foodFrequencyJson, String servingsJson) {
        this.userId = userId;
        this.foodFrequencyJson = foodFrequencyJson;
        this.servingsJson = servingsJson;
        this.createdAt = LocalDateTime.now();
    }
}
