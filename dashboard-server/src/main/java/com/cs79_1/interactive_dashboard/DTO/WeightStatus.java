package com.cs79_1.interactive_dashboard.DTO;

import lombok.Data;

@Data
public class WeightStatus {
    double iotfL;
    double iotfM;
    double iotfS;
    double iotfZ;
    double iotfP;
    String iotfC;

    double cacheraL;
    double cacheraM;
    double cacheraS;
    double cacheraZ;
    double cacheraP;
    String cacheraC;

    double omsL;
    double omsM;
    double omsS;
    double omsZ;
    double omsP;
    String omsC;

    double cdcL;
    double cdcM;
    double cdcS;
    double cdcZ;
    double cdcP;
    String cdcC;

    public WeightStatus() {

    }
}
