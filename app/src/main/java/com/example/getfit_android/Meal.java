package com.example.getfit_android;

public class Meal {

    private String name;
    private String description;
    private double calories;
    private double protein_g;
    private double fat_total_g;
    private double carbohydrates_total_g;

    public Meal() {

    }

    public Meal(String name, String description, double calories, double protein_g, double fat_total_g, double carbohydrates_total_g) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.protein_g = protein_g;
        this.fat_total_g = fat_total_g;
        this.carbohydrates_total_g = carbohydrates_total_g;
    }

    // Getters and setters for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein_g() {
        return protein_g;
    }

    public void setProtein_g(double protein_g) {
        this.protein_g = protein_g;
    }

    public double getFat_total_g() {
        return fat_total_g;
    }

    public void setFat_total_g(double fat_total_g) {
        this.fat_total_g = fat_total_g;
    }

    public double getCarbohydrates_total_g() {
        return carbohydrates_total_g;
    }

    public void setCarbohydrates_total_g(double carbohydrates_total_g) {
        this.carbohydrates_total_g = carbohydrates_total_g;
    }

    public String getDetails() {
        return description;
    }
}
