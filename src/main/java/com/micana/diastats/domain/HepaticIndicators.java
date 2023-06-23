package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;


//Показатели печёночные
@Entity
public class HepaticIndicators {
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        private Integer id;


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private User patient;

        private LocalDate dateAnalysis;

    private double ast;
    private double alt;
    private double ggtp;
    private double wsf;

    public HepaticIndicators() {
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

    public double getAst() {
        return ast;
    }

    public void setAst(double ast) {
        this.ast = ast;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getGgtp() {
        return ggtp;
    }

    public void setGgtp(double ggtp) {
        this.ggtp = ggtp;
    }

    public double getWsf() {
        return wsf;
    }

    public void setWsf(double wsf) {
        this.wsf = wsf;
    }
}
