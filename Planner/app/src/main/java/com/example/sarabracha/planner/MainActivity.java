package com.example.sarabracha.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> mCurrentSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentSchedule = new ArrayList<Task>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void createNewSchedule(MenuItem item) {

        Intent intent = new Intent(this, ScheduleTimeEntryActivity.class);
        Log.d("ACTIVITIES", "About to Create new Schedule");
        startActivityForResult(intent, 0);
        // create a schedule
        mCurrentSchedule = new ArrayList<Task>();
    }

    public void editSchedule(MenuItem item) {
        addTasksToSchedule();
    }

    private void addTasksToSchedule() {

        Intent intent = new Intent(this, TaskEntryActivity.class);

        // add that schedule to the intent
        intent.putExtra("SCHEDULE", getJSONof(mCurrentSchedule));

        Log.d("ACTIVITIES", "About to Create new Tasks");
        // Launch the activity
        startActivityForResult(intent, 1);
    }

    private String getJSONof(ArrayList<Task> obj)
    {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 0:
                Log.d("ACTIVITIES", "Returned from new Schedule");
                processIncomingScheduleData();
                break;
            case 1:
                Log.d("ACTIVITIES", "Returned from new Tasks");
                processIncomingTaskData();
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processIncomingTaskData() {
    }


    private void processIncomingScheduleData() {
    }

}
