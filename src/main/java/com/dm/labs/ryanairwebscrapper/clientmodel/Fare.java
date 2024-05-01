package com.dm.labs.ryanairwebscrapper.clientmodel;

public class Fare {
    private String day;
    private Price price;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "day='" + day + '\'' +
                ", price=" + price +
                '}';
    }
}