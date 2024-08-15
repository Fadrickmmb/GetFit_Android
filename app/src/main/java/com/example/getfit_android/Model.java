package com.example.getfit_android;

import java.util.List;

public class Model {

    String email, name, password;
    Integer calorieGoal;
    List<Day> days;

    public Model(String email, String name, String password, Integer calorieGoal, List<Day> days) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.calorieGoal = calorieGoal;
        this.days = days;
    }

    // Existing getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(Integer calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
