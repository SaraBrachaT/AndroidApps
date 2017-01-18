package com.example.sarabracha.planner;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Schedule mCurrentSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void createNewSchedule(MenuItem item) {
        createSchedule();
    }

    private void createSchedule() {
        Intent intent = new Intent(this, ScheduleTimeEntryActivity.class);

        // Launch the activity
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 0:
                processIncomingNewSchedule(data);

            case 1:
                processIncomingTasksData(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processIncomingTasksData(Intent data) {
        mCurrentSchedule = Schedule.restoreFromJSON(data.getStringExtra("Schedule"));
        viewSchedule();
    }


    private void processIncomingNewSchedule(Intent data) {
        mCurrentSchedule = Schedule.restoreFromJSON(data.getStringExtra("Schedule"));
        createTasksForSchedule();

    }

    private void createTasksForSchedule() {
        Intent intent = new Intent(this, TaskEntryActivity.class);
        intent.putExtra("Schedule", Schedule.getJSONof(mCurrentSchedule));

        // Launch the activity
        startActivityForResult(intent, 1);
    }

    public void viewSchedule(MenuItem item) {
        viewSchedule();
    }

    private void viewSchedule() {
        if (mCurrentSchedule==null || mCurrentSchedule.getNumberOfTasks() < 1)
        {
            Snackbar.make(findViewById(R.id.activity_main),"Please create a schedule first.",
                    Snackbar.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), DisplayScheduleActivity.class);
            intent.putExtra("Schedule", Schedule.getJSONof(mCurrentSchedule));
            startActivity(intent);
        }
    }
}
