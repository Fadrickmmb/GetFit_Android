package com.example.getfit_android;

import java.util.List;

public class Day {
    private String dayId;
    private String userEmail;
    private List<Meal> meals;
    private String dayCalories;
    private String dayProtein;
    private String dayFat;
    private String dayCarbs;

    public Day(String dayId, String userEmail, List<Meal> meals, String dayCalories, String dayProtein, String dayFat, String dayCarbs) {
        this.dayId = dayId;
        this.userEmail = userEmail;
        this.meals = meals;
        this.dayCalories = dayCalories;
        this.dayProtein = dayProtein;
        this.dayFat = dayFat;
        this.dayCarbs = dayCarbs;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public String getDayCalories() {
        return dayCalories;
    }

    public void setDayCalories(String dayCalories) {
        this.dayCalories = dayCalories;
    }

    public String getDayProtein() {
        return dayProtein;
    }

    public void setDayProtein(String dayProtein) {
        this.dayProtein = dayProtein;
    }

    public String getDayFat() {
        return dayFat;
    }

    public void setDayFat(String dayFat) {
        this.dayFat = dayFat;
    }

    public String getDayCarbs() {
        return dayCarbs;
    }

    public void setDayCarbs(String dayCarbs) {
        this.dayCarbs = dayCarbs;
    }
}
