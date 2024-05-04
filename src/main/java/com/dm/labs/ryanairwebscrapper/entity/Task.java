package com.dm.labs.ryanairwebscrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private int interval;

    private String origin;

    private String to;

    private int daysAheadFrom;
    private int daysAheadTo;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getDaysAheadFrom() {
        return daysAheadFrom;
    }

    public void setDaysAheadFrom(int daysAheadFrom) {
        this.daysAheadFrom = daysAheadFrom;
    }

    public int getDaysAheadTo() {
        return daysAheadTo;
    }

    public void setDaysAheadTo(int daysAheadTo) {
        this.daysAheadTo = daysAheadTo;
    }
}
