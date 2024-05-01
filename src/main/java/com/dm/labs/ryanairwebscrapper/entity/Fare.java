package com.dm.labs.ryanairwebscrapper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
public class Fare {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private double price;

    private String currency;

    private LocalDateTime date;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double change;

    public Fare(double price, String currency, LocalDateTime date) {
        this.price = price;
        this.currency = currency;
        this.date = date;
    }

    public Fare() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
