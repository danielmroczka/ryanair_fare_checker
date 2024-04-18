package com.dm.labs.ryanairwebscrapper.entity;

import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    private String origin;

    private String destination;

    private String date;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Price> fares = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Price> getFares() {
        return fares;
    }

    public void setFares(List<Price> fares) {
        this.fares = fares;
    }
}
