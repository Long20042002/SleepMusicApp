package com.prox.appsleep.model;

public class DayAlarm {
    private String day;
    private int resourceDay;


    public DayAlarm(String day, int resourceDay) {
        this.day = day;
        this.resourceDay = resourceDay;
    }

    public int getResourceDay() {
        return resourceDay;
    }

    public void setResourceDay(int resourceDay) {
        this.resourceDay = resourceDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
