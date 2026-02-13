package com.dm.labs.ryanairwebscrapper.clientmodel;

import java.util.ArrayList;
import java.util.List;

public class Outbound {
    private List<Fare> fares = new ArrayList<>();

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }
}
