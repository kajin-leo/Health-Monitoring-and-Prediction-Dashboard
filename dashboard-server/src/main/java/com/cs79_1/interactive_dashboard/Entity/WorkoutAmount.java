package com.cs79_1.interactive_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class WorkoutAmount {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private int sumSecondsMVPA3;

    @Column(nullable = false)
    private int timesMVPA3;

    @Column(nullable = true)
    private int sumSecondsSED60;

    @Column(nullable = true)
    private int timesSED60;

    @Column(nullable = false)
    private int sumSecondsLight3;

    @Column(nullable = false)
    private int timesLight3;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public WorkoutAmount() {}

    public WorkoutAmount(User user) {
        this.user = user;
    }

    public WorkoutAmount(User user, int day, int hour, LocalDateTime dateTime) {
        this.user = user;
        this.day = day;
        this.hour = hour;
        this.dateTime = dateTime;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSumSecondsMVPA3() {
        return sumSecondsMVPA3;
    }

    public void setSumSecondsMVPA3(int sumSecondsMVPA3) {
        this.sumSecondsMVPA3 = sumSecondsMVPA3;
    }

    public int getTimesMVPA3() {
        return timesMVPA3;
    }

    public void setTimesMVPA3(int timesMVPA3) {
        this.timesMVPA3 = timesMVPA3;
    }

    public int getSumSecondsSED60() {
        return sumSecondsSED60;
    }

    public void setSumSecondsSED60(int sumSecondsSED60) {
        this.sumSecondsSED60 = sumSecondsSED60;
    }

    public int getTimesSED60() {
        return timesSED60;
    }

    public void setTimesSED60(int timesSED60) {
        this.timesSED60 = timesSED60;
    }

    public int getSumSecondsLight3() {
        return sumSecondsLight3;
    }

    public void setSumSecondsLight3(int sumSecondsLight3) {
        this.sumSecondsLight3 = sumSecondsLight3;
    }

    public int getTimesLight3() {
        return timesLight3;
    }

    public void setTimesLight3(int timesLight3) {
        this.timesLight3 = timesLight3;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
