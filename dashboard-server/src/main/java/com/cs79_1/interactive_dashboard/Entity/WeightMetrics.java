package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.WeightClassification;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WeightMetrics {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private double iotfL;

    @Column(nullable = false)
    private double iotfM;

    @Column(nullable = false)
    private double iotfS;

    @Column(nullable = false)
    private double iotfZ;

    @Column(nullable = false)
    private double iotfPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private WeightClassification iotfClassification;

    @Column(nullable = false)
    private double cacheraL;

    @Column(nullable = false)
    private double cacheraM;

    @Column(nullable = false)
    private double cacheraS;

    @Column(nullable = false)
    private double cacheraZ;

    @Column(nullable = false)
    private double cacheraPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private WeightClassification cacheraClassification;

    @Column(nullable = false)
    private double omsL;

    @Column(nullable = false)
    private double omsM;

    @Column(nullable = false)
    private double omsS;

    @Column(nullable = false)
    private double omsZ;

    @Column(nullable = false)
    private double omsPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private WeightClassification omsClassification;

    @Column(nullable = false)
    private double cdcL;

    @Column(nullable = false)
    private double cdcM;

    @Column(nullable = false)
    private double cdcS;

    @Column(nullable = false)
    private double cdcZ;

    @Column(nullable = false)
    private double cdcPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private WeightClassification cdcClassification;

    public WeightMetrics() {}

    public WeightMetrics(User user) {
        this.user = user;
    }
}
