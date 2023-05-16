package com.micana.diastats.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Blood_sugar {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String sugar;
    private String data;
    private String time;

    public Blood_sugar() {
    }

    public Blood_sugar(String sugar, String data, String time) {
        this.sugar = sugar;
        this.data = data;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
