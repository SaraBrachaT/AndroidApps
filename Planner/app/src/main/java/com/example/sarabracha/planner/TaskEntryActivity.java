package com.example.sarabracha.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class TaskEntryActivity extends AppCompatActivity {

    boolean timeSpecificActivity;
    Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_entry);

        processIncomingData();
    }

    private void processIncomingData() {
        mSchedule = Schedule.restoreFromJSON(getIntent().getStringExtra("Schedule"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save here whatever fields you have;
        // The views get saved automatically
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
        // TODO: take the current data and add it to a new task object
        // mSchedule.addTask(theNewTask);
        // clear the fields (.setText(""));
    }

    public void create_schedule(View view) {
        createSchedule();
    }

    public void createSchedule() {
        finish();
    }

    @Override
    public void finish() {
        Intent results = new Intent();
        results.putExtra("Schedule", Schedule.getJSONof(mSchedule));
        setResult(RESULT_OK, results);
        super.finish();
    }

}
