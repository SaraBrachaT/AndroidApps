package com.example.sarabracha.planner;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Sara Bracha on 1/12/2017.
 */

public class Schedule {

    private int startTimeHour, startTimeMinute;
    private int endTimeHour, endTimeMinute;

    private ArrayList<Task> timeBoundTasks, nonTimeBoundTasks;
    private ArrayList<Task> combinedTasks;

    public Schedule()
    {
        timeBoundTasks = new ArrayList<>();
        nonTimeBoundTasks = new ArrayList<>();
        combinedTasks = new ArrayList<>();
    }

    public int getNumberOfTasks ()
    {
        return combinedTasks.size();
    }

    public void addTask(Task task)
    {
        if (task.isSpecificTime())
        {
            timeBoundTasks.add(task);
        }
        else
        {
            nonTimeBoundTasks.add(task);
        }
    }

    public void setStartTime (int hour, int minute)
    {
        startTimeHour = hour;
        startTimeMinute = minute;
    }

    public void setEndTime (int hour, int minute)
    {
        endTimeHour = hour;
        endTimeMinute = minute;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public ArrayList<Task> getCombinedTasks() {
        // TODO: Put in time-bound
        // and also fit in non-time-bound as per start/end time limits

        return combinedTasks;
    }

    public static String getJSONof(Schedule obj)
    {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Schedule restoreFromJSON (String serializedSchedule)
    {
        Gson gson = new Gson();
        return gson.fromJson(serializedSchedule,Schedule.class);
    }

}
