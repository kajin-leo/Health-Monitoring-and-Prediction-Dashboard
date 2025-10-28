package com.cs79_1.interactive_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BodyMetrics {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = true)
    private double waistSize;

    @Column(nullable = true)
    private double bicipital;

    @Column(nullable = true)
    private double tricipital;

    @Column(nullable = true)
    private double supraIliac;

    @Column(nullable = true)
    private double subscapularis;

    @Column(nullable = true)
    private double tannerStage;

    @Column(nullable = true)
    private double skinFoldsSum;

    @Column(nullable = true)
    private double height;

    @Column(nullable = true)
    private double weight;

    public BodyMetrics() {}

    public BodyMetrics(User user) {
        this.user = user;
    }
}
