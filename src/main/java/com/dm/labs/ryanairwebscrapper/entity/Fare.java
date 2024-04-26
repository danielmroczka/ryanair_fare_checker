package com.dm.labs.ryanairwebscrapper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Fare {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="trip_id", nullable=false)
//    private Trip trip;

    private double price;

    private String currency;

    private LocalDateTime date;

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

//    public Trip getTrip() {
//        return trip;
//    }
//
//    public void setTrip(Trip trip) {
//        this.trip = trip;
//    }
}
