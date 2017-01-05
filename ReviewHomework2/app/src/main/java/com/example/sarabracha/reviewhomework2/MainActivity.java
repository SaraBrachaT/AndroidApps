package com.example.sarabracha.reviewhomework2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random mGenerator;
    private int mWinningNumber;
    private View mSB_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGenerator = new Random();
        mSB_container = findViewById(R.id.cl_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkIfWinner(View view) {
        Button currentB = (Button)view;
        int num = Integer.parseInt(currentB.getText().toString());
        num -= 1;
        if(num == mWinningNumber){
            Snackbar.make(mSB_container, "You won", Snackbar.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(mSB_container, "Try Again", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void startGame()
    {
        mWinningNumber = mGenerator.nextInt(2);
        Snackbar.make(mSB_container, "Welcome to a New Game", Snackbar.LENGTH_SHORT).show(); //creates new snackbar(container, text, duration) and shows it
    }
}
