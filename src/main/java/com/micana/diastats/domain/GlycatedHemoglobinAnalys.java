package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class GlycatedHemoglobinAnalys {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;
    private  double glycatedHemoglobin;

    public GlycatedHemoglobinAnalys() {
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

    public double getGlycatedHemoglobin() {
        return glycatedHemoglobin;
    }

    public void setGlycatedHemoglobin(double glycatedHemoglobin) {
        this.glycatedHemoglobin = glycatedHemoglobin;
    }
}
