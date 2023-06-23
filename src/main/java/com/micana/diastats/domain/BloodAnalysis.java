package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class BloodAnalysis {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    private double hemoglobin;
    private double leukocytes;
    private double erythrocyteSedimentationRate;

    public BloodAnalysis() {
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

    public double getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(double hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public double getLeukocytes() {
        return leukocytes;
    }

    public void setLeukocytes(double leukocytes) {
        this.leukocytes = leukocytes;
    }

    public double getErythrocyteSedimentationRate() {
        return erythrocyteSedimentationRate;
    }

    public void setErythrocyteSedimentationRate(double erythrocyteSedimentationRate) {
        this.erythrocyteSedimentationRate = erythrocyteSedimentationRate;
    }
}
