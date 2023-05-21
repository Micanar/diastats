package com.micana.diastats.domain;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


@Entity
public class Blood_sugar {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private double sugar;

    private LocalDate data;

    private LocalTime time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;


    public Blood_sugar() {
    }

    public Blood_sugar(double sugar, LocalDate data, LocalTime time,User user) {
        this.patient = user;
        this.sugar = sugar;
        this.data = data;
        this.time = time;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
