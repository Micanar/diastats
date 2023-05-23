package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class ProductConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private PFC product;

    private double grams;

    private LocalDate consumptionDate;
    private LocalTime consumptionTime;

    @ManyToOne
    private User user;

    private double proteins;
    private double fats;
    private double carbohydrates;
    private double breadUnits;

    public ProductConsumption() {
    }

    public ProductConsumption(PFC product, double grams, LocalDate consumptionDate, LocalTime consumptionTime, User user) {
        this.product= product;
        this.grams = grams;
        this.consumptionDate = consumptionDate;
        this.consumptionTime = consumptionTime;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PFC getProduct() {
        return product;
    }

    public void setProduct(PFC product) {
        this.product = product;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public LocalDate getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(LocalDate consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public LocalTime getConsumptionTime() {
        return consumptionTime;
    }

    public void setConsumptionTime(LocalTime consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getBreadUnits() {
        return breadUnits;
    }

    public void setBreadUnits(double breadUnits) {
        this.breadUnits = breadUnits;
    }
}
