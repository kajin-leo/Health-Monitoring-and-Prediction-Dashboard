package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.MentalStrength;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MentalHealthAndDailyRoutine {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = true)
    private double selfesteemScore;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private MentalStrength selfesteemStrength;

    @Column(nullable = true)
    private double procrastinationScore;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private MentalStrength procrastinationStrength;

    @Column(nullable = true)
    private double weekdaySleepingAvgDuration;

    @Column(nullable = true)
    private double weekendSleepingAvgDuration;

    @Column(nullable = true)
    private double totalSleepingDuration;

    public MentalHealthAndDailyRoutine() {}

    public MentalHealthAndDailyRoutine(User user) {
        this.user = user;
    }
}
