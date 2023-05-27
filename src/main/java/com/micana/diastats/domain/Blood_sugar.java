package com.micana.diastats.domain;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


@Entity
public class Blood_sugar {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private double sugar;

    LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;


    public Blood_sugar() {
    }

    public Blood_sugar(double sugar, LocalDateTime dateTime,User user) {
        this.patient = user;
        this.sugar = sugar;
        this.dateTime=dateTime;
    }



    public String getPatientName(){
        return patient!=null ? patient.getUsername() : "<none>";
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
}
