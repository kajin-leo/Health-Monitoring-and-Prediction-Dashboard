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
    private Double waistSize;

    @Column(nullable = true)
    private Double bicipital;

    @Column(nullable = true)
    private Double tricipital;

    @Column(nullable = true)
    private Double supraIliac;

    @Column(nullable = true)
    private Double subscapularis;

    @Column(nullable = true)
    private Double tannerStage;

    @Column(nullable = true)
    private Double skinFoldsSum;

    @Column(nullable = true)
    private Double height;

    @Column(nullable = true)
    private Double weight;

    public BodyMetrics() {}

    public BodyMetrics(User user) {
        this.user = user;
    }
}
