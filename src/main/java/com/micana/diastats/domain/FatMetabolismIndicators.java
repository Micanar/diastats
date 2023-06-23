package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class FatMetabolismIndicators {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    private double totalCholesterol;
    private double ldlCholesterol;
    private double hdlCholesterol;
    private double triglycerides;
    private double lpnp;

    public FatMetabolismIndicators() {
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

    public double getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(double totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public double getLdlCholesterol() {
        return ldlCholesterol;
    }

    public void setLdlCholesterol(double ldlCholesterol) {
        this.ldlCholesterol = ldlCholesterol;
    }

    public double getHdlCholesterol() {
        return hdlCholesterol;
    }

    public void setHdlCholesterol(double hdlCholesterol) {
        this.hdlCholesterol = hdlCholesterol;
    }

    public double getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(double triglycerides) {
        this.triglycerides = triglycerides;
    }

    public double getLpnp() {
        return lpnp;
    }

    public void setLpnp(double lpnp) {
        this.lpnp = lpnp;
    }
}
