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

    @Column(nullable = true)
    private Double iotfL;

    @Column(nullable = true)
    private Double iotfM;

    @Column(nullable = true)
    private Double iotfS;

    @Column(nullable = true)
    private Double iotfZ;

    @Column(nullable = true)
    private Double iotfPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private WeightClassification iotfClassification;

    @Column(nullable = true)
    private Double cacheraL;

    @Column(nullable = true)
    private Double cacheraM;

    @Column(nullable = true)
    private Double cacheraS;

    @Column(nullable = true)
    private Double cacheraZ;

    @Column(nullable = true)
    private Double cacheraPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private WeightClassification cacheraClassification;

    @Column(nullable = true)
    private Double omsL;

    @Column(nullable = true)
    private Double omsM;

    @Column(nullable = true)
    private Double omsS;

    @Column(nullable = true)
    private Double omsZ;

    @Column(nullable = true)
    private Double omsPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private WeightClassification omsClassification;

    @Column(nullable = true)
    private Double cdcL;

    @Column(nullable = true)
    private Double cdcM;

    @Column(nullable = true)
    private Double cdcS;

    @Column(nullable = true)
    private Double cdcZ;

    @Column(nullable = true)
    private Double cdcPercentile;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private WeightClassification cdcClassification;

    public WeightMetrics() {}

    public WeightMetrics(User user) {
        this.user = user;
    }
}
