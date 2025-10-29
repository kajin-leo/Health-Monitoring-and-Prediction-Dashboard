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
    private Double selfesteemScore;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private MentalStrength selfesteemStrength;

    @Column(nullable = true)
    private Double procrastinationScore;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private MentalStrength procrastinationStrength;

    @Column(nullable = true)
    private Double weekdaySleepingAvgDuration;

    @Column(nullable = true)
    private Double weekendSleepingAvgDuration;

    @Column(nullable = true)
    private Double totalSleepingDuration;

    public MentalHealthAndDailyRoutine() {}

    public MentalHealthAndDailyRoutine(User user) {
        this.user = user;
    }
}
