package com.example.sarabracha.planner;

import android.content.Intent;
import android.os.health.TimerStat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class ScheduleTimeEntryActivity extends AppCompatActivity {

    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time_entry);

        mSchedule = new Schedule();
    }

    public void action_start_adding_tasks(View view) {
        TimePicker tp_start, tp_end;
        tp_start = (TimePicker) findViewById(R.id.day_start_time);
        tp_end = (TimePicker) findViewById(R.id.day_end_time);

        // TODO: Data validation here - if no valid start/end time entered, then "cancel"

        // Requires API 23 for getHour() and getMinute() so we use the older deprecated equivalent
        Log.d("TP", "Start Hour: " + tp_start.getCurrentHour());
        mSchedule.setStartTime(
                tp_start.getCurrentHour(), tp_start.getCurrentMinute());
        mSchedule.setEndTime(
                tp_end.getCurrentHour(), tp_end.getCurrentMinute());

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
