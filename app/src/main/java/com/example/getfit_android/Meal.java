// Meal.java
package com.example.getfit_android;

public class Meal {
    private String name;
    private String details;

    public Meal(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }
}
