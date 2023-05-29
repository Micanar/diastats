package com.micana.diastats.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProductConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private PFC product;

    private double grams;

    private LocalDateTime consumptionDateTime;
    private String carbohydrateType;

    @ManyToOne
    private User user;

    private String name;

    public ProductConsumption(User user, String name, double grams, double proteins, double fats, double carbohydrates, LocalDateTime dateTime) {
        this.user=user;
        this.name=name;
        this.grams=grams;
        this.proteins=proteins;
        this.fats=fats;
        this.carbohydrates=carbohydrates;
        this.consumptionDateTime=dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double proteins;
    private double fats;
    private double carbohydrates;
    private double breadUnits;

    public ProductConsumption() {
    }

    public String getCarbohydrateType() {
        return carbohydrateType;
    }

    public void setCarbohydrateType(String carbohydrateType) {
        this.carbohydrateType = carbohydrateType;
    }

    public ProductConsumption(PFC product, double grams, LocalDateTime consumptionDateTime, User user) {
        this.product = product;
        this.grams = grams;
        this.consumptionDateTime = consumptionDateTime;
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

    public LocalDateTime getConsumptionDateTime() {
        return consumptionDateTime;
    }

    public void setConsumptionDateTime(LocalDateTime consumptionDateTime) {
        this.consumptionDateTime = consumptionDateTime;
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
