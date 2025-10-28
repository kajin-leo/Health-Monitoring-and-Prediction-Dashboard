package com.cs79_1.interactive_dashboard.DTO.Simulation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StructuredActivityDTO {
    List<Integer> MVPA;
    List<Integer> Light;
    List<String> description;

    public StructuredActivityDTO() {
        MVPA = new ArrayList<>();
        Light = new ArrayList<>();
        description = new ArrayList<>();
    }

    public void addMVPA(int MVPA) {
        this.MVPA.add(MVPA);
    }

    public void addLight(int Light) {
        this.Light.add(Light);
    }

    public void addDescription(String description) {
        this.description.add(description);
    }
}
