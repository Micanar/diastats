package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double height;

    @Column
    private double weight;

    @Column
    private double bmi;

    @Column
    private String gender;

    @Column
    private LocalDate birthdate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Конструкторы, геттеры и сеттеры

    public UserProfile() {
    }

    public UserProfile(Integer id, int height, int weight, double bmi, String gender, LocalDate birthdate, User user) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.gender = gender;
        this.birthdate = birthdate;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Дополнительные методы, если необходимо
}