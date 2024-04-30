package com.dm.labs.ryanairwebscrapper.model;

public class Price {
    private String value;

    private String currencyCode;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "'" + value + currencyCode + '\'';
    }
}
