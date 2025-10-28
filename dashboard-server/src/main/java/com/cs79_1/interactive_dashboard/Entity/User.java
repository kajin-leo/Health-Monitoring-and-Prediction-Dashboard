package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    private String avatarUrl;

    //    @Column(nullable = false)
    private double ageMonth;

//    @Column(nullable = false)
    private int ageYear;

//    @Column(nullable = false)
    private int sex; // 1 - Male 2 - Female

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BodyMetrics bodyMetrics;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BodyComposition bodyComposition;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WeightMetrics weightMetrics;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WeeklyIntake weeklyIntake;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MentalHealthAndDailyRoutine mentalHealthAndDailyRoutine;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WorkoutAmount> WorkoutAmounts;

    public User() {}

    public User(String username, String password, int sex) {
        this.username = username;
        this.password = password;
        this.sex = sex;
    }
}
