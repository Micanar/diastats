package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class InsulinSynthesis {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    // Система собственного синтеза инсулина
    private double cPeptide;
    private double proinsulin;
    private double insulin;

    public InsulinSynthesis() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public double getcPeptide() {
        return cPeptide;
    }

    public void setcPeptide(double cPeptide) {
        this.cPeptide = cPeptide;
    }

    public double getProinsulin() {
        return proinsulin;
    }

    public void setProinsulin(double proinsulin) {
        this.proinsulin = proinsulin;
    }

    public double getInsulin() {
        return insulin;
    }

    public void setInsulin(double insulin) {
        this.insulin = insulin;
    }
}
