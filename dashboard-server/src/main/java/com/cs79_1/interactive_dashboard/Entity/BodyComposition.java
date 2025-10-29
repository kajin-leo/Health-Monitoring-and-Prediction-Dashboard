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
    private Double fatPercentage;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzFatPercentage;

    @Column(nullable = true)
    private Double fatMass;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzFatMass;

    @Column(nullable = true)
    private Double BMI;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private HFZClassification hfzBMI;

    @Column(nullable = true)
    private Double fatAmount;

    @Column(nullable = true)
    private Double ffmAmount; //Fat-free Mass

    @Column(nullable = true)
    private Double muscleAmount;

    @Column(nullable = true)
    private Double waterAmount;

    @Column(nullable = true)
    private Double waterPercentage;

    @Column(nullable = true)
    private Double wlgr625; // Whole-body bioelectrical impedance - resistance 6.25khz

    @Column(nullable = true)
    private Double wlgx625; // Whole-body bioelectrical impedance - reactance 6.25khz

    @Column(nullable = true)
    private Double wlgr50; // 50khz

    @Column(nullable = true)
    private Double wlgx50; // 50khz

    public BodyComposition() {}

    public BodyComposition(User user) {
        this.user = user;
    }
}
