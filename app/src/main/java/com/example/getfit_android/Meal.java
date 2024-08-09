// Meal.java
package com.example.getfit_android;

public class Meal {
    private String name;
    private String details;
    private String mealCalories;
    private String mealFat;
    private String mealFiber;
    private String mealCarb;
    private String mealProtein;

    public Meal(String name, String details, String mealCalories, String mealFat, String mealFiber, String mealCarb, String mealProtein) {
        this.name = name;
        this.details = details;
        this.mealCalories = mealCalories;
        this.mealFat = mealFat;
        this.mealFiber = mealFiber;
        this.mealCarb = mealCarb;
        this.mealProtein = mealProtein;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getMealCalories() {
        return mealCalories;
    }

    public String getMealFat() {
        return mealFat;
    }

    public String getMealFiber() {
        return mealFiber;
    }

    public String getMealCarb() {
        return mealCarb;
    }

    public String getMealProtein() {
        return mealProtein;
    }
}
