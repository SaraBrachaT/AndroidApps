package com.example.sarabracha.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

public class TaskEntryActivity extends AppCompatActivity {

    ArrayList<Task> timeSpecific;
    ArrayList<Task> notTimeSpecific;
    boolean timeSpecificActivity;
    ArrayList<Task> mCurrentSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_entry);
        getTask();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save here whatever fields you have;
        // The views get saved automatically
    }

    public void getTask() {
        //if(R.t)
    }


    private void editSchedule() {

        Intent intent = new Intent(this, TaskEntryActivity.class);

        // add that schedule to the intent
        intent.putExtra("SCHEDULE", getJSONof(mCurrentSchedule));

        // Launch the activity
        startActivityForResult(intent, 0);
    }
    private ArrayList<Task> restoreScheduleFromJSON(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, ArrayList.class);
    }

    public void activity_specificTime_clicked(View view) {
        timeSpecificActivity = true;
        findViewById(R.id.Enter_start_time).setVisibility(View.VISIBLE);
        findViewById(R.id.startTime).setVisibility(View.VISIBLE);
        findViewById(R.id.Enter_end_time).setVisibility(View.VISIBLE);
        findViewById(R.id.endTime).setVisibility(View.VISIBLE);

        findViewById(R.id.enter_amount_of_time).setVisibility(View.GONE);
        findViewById(R.id.amountOfTime).setVisibility(View.GONE);
    }

    public void activity_notSpecificTime_clicked(View view) {
        timeSpecificActivity = false;
        findViewById(R.id.Enter_start_time).setVisibility(View.GONE);
        findViewById(R.id.startTime).setVisibility(View.GONE);
        findViewById(R.id.Enter_end_time).setVisibility(View.GONE);
        findViewById(R.id.endTime).setVisibility(View.GONE);
        findViewById(R.id.enter_amount_of_time).setVisibility(View.VISIBLE);
        findViewById(R.id.amountOfTime).setVisibility(View.VISIBLE);
    }

    public void action_additional_task(View view) {
      }

    public void create_schedule(View view) {
        createSchedule();
    }

    public void createSchedule()
    {

     //   if(R.id.radioButtonSpecific = 1)
       //     mCurrentSchedule.add(new Task((R.id.taskName, R.id.taskDescription, R.id.)));
    }


    private String getJSONof(ArrayList<Task> obj)
    {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
