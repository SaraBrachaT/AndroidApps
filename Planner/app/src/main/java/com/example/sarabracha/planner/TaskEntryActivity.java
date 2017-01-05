package com.example.sarabracha.planner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class TaskEntryActivity extends AppCompatActivity {

    ArrayList<Task> timeSpecific;
    ArrayList<Task> notTimeSpecific;
    boolean timeSpecificActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_entry);
        getTask();
    }

    @Override
    public void getTask() {
        if(R.t)
    }

    public void activity_specificTime_clicked(View view) {
        timeSpecificActivity = true;
        findViewById(R.id.startTime).setVisibility(View.VISIBLE);
        findViewById(R.id.endTime).setVisibility(View.VISIBLE);
    }

    public void activity_notSpecificTime_clicked(View view) {
        timeSpecificActivity = false;
    }
}
