package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.MentalStrength;
import jakarta.persistence.*;

@Entity
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

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getSelfesteemScore() {
        return selfesteemScore;
    }

    public void setSelfesteemScore(double selfesteemScore) {
        this.selfesteemScore = selfesteemScore;
    }

    public MentalStrength getSelfesteemStrength() {
        return selfesteemStrength;
    }

    public void setSelfesteemStrength(MentalStrength selfesteemStrength) {
        this.selfesteemStrength = selfesteemStrength;
    }

    public double getProcrastinationScore() {
        return procrastinationScore;
    }

    public void setProcrastinationScore(double procrastinationScore) {
        this.procrastinationScore = procrastinationScore;
    }

    public MentalStrength getProcrastinationStrength() {
        return procrastinationStrength;
    }

    public void setProcrastinationStrength(MentalStrength procrastinationStrength) {
        this.procrastinationStrength = procrastinationStrength;
    }

    public double getWeekdaySleepingAvgDuration() {
        return weekdaySleepingAvgDuration;
    }

    public void setWeekdaySleepingAvgDuration(double weekdaySleepingAvgDuration) {
        this.weekdaySleepingAvgDuration = weekdaySleepingAvgDuration;
    }

    public double getWeekendSleepingAvgDuration() {
        return weekendSleepingAvgDuration;
    }

    public void setWeekendSleepingAvgDuration(double weekendSleepingAvgDuration) {
        this.weekendSleepingAvgDuration = weekendSleepingAvgDuration;
    }

    public double getTotalSleepingDuration() {
        return totalSleepingDuration;
    }

    public void setTotalSleepingDuration(double totalSleepingDuration) {
        this.totalSleepingDuration = totalSleepingDuration;
    }
}
