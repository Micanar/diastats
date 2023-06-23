package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Creatinine {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private double creatinine;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    public Creatinine() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getCreatinine() {
        return creatinine;
    }

    public void setCreatinine(double creatinine) {
        this.creatinine = creatinine;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public LocalDate getDateAnalysis() {
        return dateAnalysis;
    }

    public void setDateAnalysis(LocalDate dateAnalysis) {
        this.dateAnalysis = dateAnalysis;
    }
}
