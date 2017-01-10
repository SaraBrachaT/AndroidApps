package com.example.sarabracha.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> mCurrentSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentSchedule = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void createNewSchedule(MenuItem item) {

        // create a schedule
        mCurrentSchedule = new ArrayList<>();

        editSchedule();
    }

    public void editSchedule(MenuItem item) {
        editSchedule();
    }

    private void editSchedule() {
        Intent intent = new Intent(this, TaskEntryActivity.class);

        // add that schedule to the intent
        intent.putParcelableArrayListExtra("SCHEDULE", mCurrentSchedule);

        // Launch the activity
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 0:
                processIncomingData();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void processIncomingData() {
    }
}
