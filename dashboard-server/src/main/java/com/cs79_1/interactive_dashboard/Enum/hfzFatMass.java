package com.cs79_1.interactive_dashboard.Enum;


public enum hfzFatMass {
    HFZ("Healthy Fitness Zone"),
    NI("Needs Improvement"),
    NIHR("Needs Improvement - Health Risk");

    private String description;

    hfzFatMass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
