package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.HFZClassification;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BodyComposition {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = true)
    private double fatPercentage;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzFatPercentage;

    @Column(nullable = true)
    private double fatMass;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzFatMass;

    @Column(nullable = true)
    private double BMI;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzBMI;

    @Column(nullable = true)
    private double fatAmount;

    @Column(nullable = true)
    private double ffmAmount; //Fat-free Mass

    @Column(nullable = true)
    private double muscleAmount;

    @Column(nullable = true)
    private double waterAmount;

    @Column(nullable = true)
    private double waterPercentage;

    @Column(nullable = true)
    private double wlgr625; // Whole-body bioelectrical impedance - resistance 6.25khz

    @Column(nullable = true)
    private double wlgx625; // Whole-body bioelectrical impedance - reactance 6.25khz

    @Column(nullable = true)
    private double wlgr50; // 50khz

    @Column(nullable = true)
    private double wlgx50; // 50khz

    public BodyComposition() {}

    public BodyComposition(User user) {
        this.user = user;
    }
}
