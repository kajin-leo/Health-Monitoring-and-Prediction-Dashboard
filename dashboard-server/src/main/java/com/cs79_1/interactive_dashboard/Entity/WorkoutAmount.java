package com.cs79_1.interactive_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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
}
