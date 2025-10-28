package com.cs79_1.interactive_dashboard.DTO;


import lombok.Data;

import java.util.Map;

@Data
public class FoodFrequencyRequest {
    private Long userId;
    private Map<String, String> foodFrequency;
    private Map<String, Double> servings;
}
