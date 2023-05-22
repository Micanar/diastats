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

    @ManyToOne
    private User user;

    public ProductConsumption() {
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
}
