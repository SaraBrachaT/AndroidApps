package com.example.sarabracha.homework3;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private View mSB_Container;
    private GridGameAdapter objGridGameAdapter;
    private int mTurnsTaken;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBoard();
        mSB_Container = findViewById(R.id.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    //called by main activity
    private void startGame() {
        mTurnsTaken = 0;
        objGridGameAdapter.startNewGame();
        Snackbar.make(mSB_Container, "Welcome to a New Game", Snackbar.LENGTH_SHORT).show();
    }

    public void setupBoard()
    {
        int squares = 16;
        int rows = (int) (squares / Math.sqrt(squares));
        // Create a reference to our RV and a new instance of a LayoutManager and our Adapter class
        RecyclerView objRecyclerView = (RecyclerView) findViewById (R.id.recycler_view);
        objRecyclerView.setHasFixedSize (true);
        RecyclerView.LayoutManager objLayoutManager = new GridLayoutManager(this, rows); // cols/rows
        objLayoutManager.setAutoMeasureEnabled (true);
        objGridGameAdapter = new GridGameAdapter (squares);
    // put all three objects together
        objRecyclerView.setLayoutManager (objLayoutManager);
        objRecyclerView.setAdapter(objGridGameAdapter);

    }

    public void buttonHandler(View view) {
        showGuessResults((Button) view);
        incrementGuessesCounterAndUpdateStatusBar();
    }

    private void incrementGuessesCounterAndUpdateStatusBar() {
        TextView tvStatusBar = (TextView) findViewById(R.id.status_bar);

        tvStatusBar.setText("Guesses taken: " + ++mTurnsTaken);
    }

    private void showGuessResults(Button view)
    {
        View sbContainer = findViewById(R.id.activity_main);
        Button currentButton = (Button) view;
        String msg = "You clicked on " + currentButton.getText().toString() + "\n";

        msg+= objGridGameAdapter.checkIfWinner(Integer.parseInt(currentButton.getText().toString())-1)
                ?
                "You won" : "Try again";

        Snackbar.make(sbContainer, msg, Snackbar.LENGTH_SHORT).show();

    }

    public void newGame(MenuItem item) {
        objGridGameAdapter.startNewGame();

        //reset status bar
        mTurnsTaken = -1;
        incrementGuessesCounterAndUpdateStatusBar();

        View sbContainer = findViewById(R.id.activity_main);
        Snackbar.make(sbContainer, "Welcome to a New Game!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState (Bundle outstate)
    {
        super.onSaveInstanceState(outstate);
        outstate.putInt("WINNING_NUMBER", objGridGameAdapter.getWinningNumber());
        outstate.putInt("CURRENT_GUESSES", mTurnsTaken);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        objGridGameAdapter.overwriteWinningNumber(savedInstanceState.getInt("WINNING_NUMBER"));
        mTurnsTaken = savedInstanceState.getInt("CURRENT_GUESSES")-1;
        incrementGuessesCounterAndUpdateStatusBar();
    }

}
