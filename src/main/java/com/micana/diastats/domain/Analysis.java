package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Analysis {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User patient;

    private LocalDate dateAnalysis;

    private String analysisType;
    // Показатели печёночные
    private double ast;
    private double alt;
    private double ggtp;
    private double wsf;

    // Показатели обмена живор
    private double totalCholesterol;
    private double ldlCholesterol;
    private double hdlCholesterol;
    private double triglycerides;
    private double lpnp;

    // Анализ
    private double creatinine;
    private double urea;
    private double uricAcid;
    private  double glycatedHemoglobin;

    // Система собственного синтеза инсулина
    private double cPeptide;
    private double proinsulin;
    private double insulin;

    // Общий анализ крови
    private double hemoglobin;
    private double leukocytes;

    private double erythrocyteSedimentationRate;

    // Общий анализ мочи
    private double specificGravity;
    private double glucose;
    private double protein;
    private double urineLeukocytes;
    private double urineErythrocytes;
    private double bacteria;

    // Добавьте геттеры и сеттеры для всех полей




    public Analysis() {
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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
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

    public double getCreatinine() {
        return creatinine;
    }

    public void setCreatinine(double creatinine) {
        this.creatinine = creatinine;
    }

    public double getUrea() {
        return urea;
    }

    public void setUrea(double urea) {
        this.urea = urea;
    }

    public double getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(double uricAcid) {
        this.uricAcid = uricAcid;
    }

    public double getGlycatedHemoglobin() {
        return glycatedHemoglobin;
    }

    public void setGlycatedHemoglobin(double glycatedHemoglobin) {
        this.glycatedHemoglobin = glycatedHemoglobin;
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

