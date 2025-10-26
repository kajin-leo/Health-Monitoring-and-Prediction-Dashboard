package com.cs79_1.interactive_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
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

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getWaistSize() {
        return waistSize;
    }

    public void setWaistSize(double waistSize) {
        this.waistSize = waistSize;
    }

    public double getBicipital() {
        return bicipital;
    }

    public void setBicipital(double bicipital) {
        this.bicipital = bicipital;
    }

    public double getTricipital() {
        return tricipital;
    }

    public void setTricipital(double tricipital) {
        this.tricipital = tricipital;
    }

    public double getSupraIliac() {
        return supraIliac;
    }

    public void setSupraIliac(double supraIliac) {
        this.supraIliac = supraIliac;
    }

    public double getSubscapularis() {
        return subscapularis;
    }

    public void setSubscapularis(double subscapularis) {
        this.subscapularis = subscapularis;
    }

    public double getTannerStage() {
        return tannerStage;
    }

    public void setTannerStage(double tannerStage) {
        this.tannerStage = tannerStage;
    }

    public double getSkinFoldsSum() {
        return skinFoldsSum;
    }

    public void setSkinFoldsSum(double skinFoldsSum) {
        this.skinFoldsSum = skinFoldsSum;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
