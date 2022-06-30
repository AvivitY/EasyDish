package com.example.easydish;

public class Ingredient {
    private double qty;
    private String measurement;
    private String name;

    public Ingredient() {
    }

    public Ingredient setQty(double qty) {
        this.qty = qty;
        return this;
    }

    public Ingredient setMeasurement(String measurement) {
        this.measurement = measurement;
        return this;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public double getQty() {
        return qty;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getName() {
        return name;
    }
}
