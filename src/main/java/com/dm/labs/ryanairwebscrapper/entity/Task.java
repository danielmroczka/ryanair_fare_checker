package com.dm.labs.ryanairwebscrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Entity
@Validated
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private int intervals = 3600;

    @NotNull
    @Size(min=3, max=5, message = "Origin should be 3 characters long")
    private String origin;

    @NotNull
    @Min(3)
    private String destination;

    @Max(12)
    private int monthsAhead = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
