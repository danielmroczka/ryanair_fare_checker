package com.dm.labs.ryanairwebscrapper.model;

import java.util.ArrayList;
import java.util.List;

public class Fares {
    private List<Fare> fares = new ArrayList<>();

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }
}

