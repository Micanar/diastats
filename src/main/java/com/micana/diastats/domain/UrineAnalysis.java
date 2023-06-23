package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class UrineAnalysis {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    private double specificGravity;
    private double glucose;
    private double protein;
    private double urineLeukocytes;
    private double urineErythrocytes;
    private double bacteria;

    public UrineAnalysis() {
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

    public double getSpecificGravity() {
        return specificGravity;
    }

    public void setSpecificGravity(double specificGravity) {
        this.specificGravity = specificGravity;
    }

    public double getGlucose() {
        return glucose;
    }

    public void setGlucose(double glucose) {
        this.glucose = glucose;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getUrineLeukocytes() {
        return urineLeukocytes;
    }

    public void setUrineLeukocytes(double urineLeukocytes) {
        this.urineLeukocytes = urineLeukocytes;
    }

    public double getUrineErythrocytes() {
        return urineErythrocytes;
    }

    public void setUrineErythrocytes(double urineErythrocytes) {
        this.urineErythrocytes = urineErythrocytes;
    }

    public double getBacteria() {
        return bacteria;
    }

    public void setBacteria(double bacteria) {
        this.bacteria = bacteria;
    }
}
