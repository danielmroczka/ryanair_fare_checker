package com.dm.labs.ryanairwebscrapper.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Quote {

    @Id
    @GeneratedValue
    private Long id;

    private String origin;

    private String destination;

    private String dateMonth;

    private String dateYear;

    private LocalDateTime localDateTime;

    @OneToMany
    private List<Fare> fares;

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

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String month) {
        this.dateMonth = month;
    }

    public String getDateYear() {
        return dateYear;
    }

    public void setDateYear(String year) {
        this.dateYear = year;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
