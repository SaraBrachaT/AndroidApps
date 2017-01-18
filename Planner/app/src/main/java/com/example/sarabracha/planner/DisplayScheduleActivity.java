package com.example.sarabracha.planner;

import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DisplayScheduleActivity extends AppCompatActivity {

    private PlannerAdapter mAdapter;
    private RecyclerView mRvPlanner;
    private ArrayList<Task> mCurrentSchedule;
    private String[] mListForAdapter;
    private int mStartHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getIncomingData();
        setupPlanner();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Back?
            }
        });
    }

    private void getIncomingData() {
        Intent incoming = getIntent();
        mCurrentSchedule = restoreScheduleFromJSON(incoming.getStringExtra("Schedule"));
        mListForAdapter = new String[mCurrentSchedule.size()];
        for (int i = 0; i < mCurrentSchedule.size(); i++) {
            mListForAdapter[i] = mCurrentSchedule.get(i).toString();
        }

        //TimeUnit scheduleStart = mSchedule.get(0).getStartTime();
        //mStartHour = scheduleStart.
    }


    private ArrayList<Task> restoreScheduleFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ArrayList.class);
    }


    private void setupPlanner() {
        // start time, hours in day, sections per hour, schedule
        mAdapter = new PlannerAdapter(11, 3, 4, mListForAdapter);
        mRvPlanner = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false);
        lm.setAutoMeasureEnabled(true);

        mRvPlanner.setHasFixedSize(true);
        mRvPlanner.setLayoutManager(lm);
        mRvPlanner.setAdapter(mAdapter);
    }

    public void changeTask(View view) {
        View sbContainer = findViewById(R.id.activity_main);

        // get the tag from the item clicked on
        Integer position = (Integer) view.getTag();
        Snackbar.make(sbContainer, "Position in adapter: " + position, Snackbar.LENGTH_SHORT).show();

        // call the change event from the adapter to change this position's event/task
        mAdapter.changeTimeSlotEvent(position, "Changed");
    }

}
