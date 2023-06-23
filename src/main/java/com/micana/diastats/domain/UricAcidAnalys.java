package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class UricAcidAnalys {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private double uricAcid;

    private LocalDate dateAnalysis;

    public UricAcidAnalys() {
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

    public double getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(double uricAcid) {
        this.uricAcid = uricAcid;
    }

    public LocalDate getDateAnalysis() {
        return dateAnalysis;
    }

    public void setDateAnalysis(LocalDate dateAnalysis) {
        this.dateAnalysis = dateAnalysis;
    }
}
