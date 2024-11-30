package com.dm.labs.ryanairwebscrapper.clientmodel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fare {
    private LocalDate day;
    private Price price;
    private LocalDateTime departureDate;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "day='" + day + '\'' +
                ", price=" + price +
                '}';
    }
}