package com.example.sarabracha.planner;

import android.icu.util.TimeUnit;

/**
 * Created by Sara Bracha on 1/3/2017.
 */


public class Task {


    private String name;
    private String description;
    private boolean specificTime;
    private int priority;
    private TimeUnit startTime;
    private TimeUnit endTime;
    private TimeUnit amountOfTime;

    //Specific Time
    public Task(String name, String description, int priority, TimeUnit startTime, TimeUnit endTime)
    {
        this.name = name;
        this.description = description;
        specificTime = true;
        this.priority = priority;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //Amount of Time
    public Task(String name, String description, int priority, TimeUnit amountOfTime)
    {
        this.name = name;
        this.description = description;
        specificTime = true;
        this.priority = priority;
        this.amountOfTime = amountOfTime;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(this.name);
        sb.append("Description: ");
        sb.append(this.description);
        sb.append("Priority: ");
        sb.append(this.priority);
        sb.append("Start Time: ");
        sb.append(this.startTime);
        sb.append("End Time:");
        sb.append(this.endTime);

        return sb.toString();
    }


    public boolean isSpecificTime() {
        return specificTime;
    }

    public void setSpecificTime(boolean specificTime) {
        this.specificTime = specificTime;
    }

    public TimeUnit getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeUnit startTime) {
        this.startTime = startTime;
    }

    public TimeUnit getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeUnit endTime) {
        this.endTime = endTime;
    }

    public TimeUnit getAmountOfTime() {
        return amountOfTime;
    }

    public void setAmountOfTime(TimeUnit amountOfTime) {
        this.amountOfTime = amountOfTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
