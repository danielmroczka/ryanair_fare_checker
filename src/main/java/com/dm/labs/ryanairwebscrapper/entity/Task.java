package com.dm.labs.ryanairwebscrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private int intervals = 3600;

    private String origin;

    private String destination;

    private int monthsAhead;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int interval) {
        this.intervals = interval;
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

    public void setDestination(String to) {
        this.destination = to;
    }

    public int getMonthsAhead() {
        return monthsAhead;
    }

    public void setMonthsAhead(int monthsAhead) {
        this.monthsAhead = monthsAhead;
    }
}
