package com.example.getfit_android;

import java.util.Date;

public class DayModel {

    String dayCalories, dayProtein, dayFat, dayCarbs;
    Date date;


    public void dayModel(String dCal, String dProt, String dFat, String dCarb ){

        dayCalories = dCal;
        dayProtein = dProt;
        dayFat = dFat;
        dayCarbs = dCarb;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}


