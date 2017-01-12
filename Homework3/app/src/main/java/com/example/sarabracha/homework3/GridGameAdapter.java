package com.example.sarabracha.homework3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

/**
 * Created by Sara Bracha on 12/27/2016.
 */

public class GridGameAdapter extends RecyclerView.Adapter<GridGameAdapter.ViewHolder> {

    public  boolean [] mSquares;
    private  static int DEFAULT_ELEMENTS = 16;
    private int winningNumber, mElements;
    private Random randomGen;

    public GridGameAdapter(){this(DEFAULT_ELEMENTS);}

    public  GridGameAdapter(int elements)
    {
        if(elements % Math.sqrt(elements) == 0)
        {
            mSquares = new boolean[elements];
            mElements = elements;
            randomGen = new Random();
            startNewGame();
        }
        else
        {
            throw new IllegalArgumentException("Number of Squares must allow for a perfect square board");
        }
   }


    public void overwriteWinningNumber(int newWinningNumber)
    {
        if(newWinningNumber >= 0 && newWinningNumber < mSquares.length)
        {
            endCurrentGame();
            startsGameWith(newWinningNumber);
        }
        else
        {
            throw new IllegalArgumentException("This number is not a valid winning number");
        }
    }

    private void endCurrentGame()
    {
        mSquares[winningNumber] = false;
    }

    public void startNewGame() {
        endCurrentGame();
        startGame();
        winningNumber =  randomGen.nextInt(mElements);
        mSquares[winningNumber] = true;
    }

    private void startGame() {

    startsGameWith(randomGen.nextInt(mElements)) ;
    }

    private void startsGameWith(int winningNumber)
    {
        winningNumber = winningNumber;
        mSquares[winningNumber] = true;

    }




    public GridGameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from (parent.getContext ()).inflate (
                R.layout.rv_item, parent, false);
        return new GridGameAdapter.ViewHolder(itemLayoutView);
    }


    public void onBindViewHolder(GridGameAdapter.ViewHolder holder, int position) {
        holder.mButton.setText(Integer.toString(position+1));
    }

    public boolean checkIfWinner(int elementNum)
    {
       return mSquares[elementNum];
    }
    @Override
    public int getItemCount() {
        return mSquares.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mButton = (Button) itemView.findViewById(R.id.button);
        }
    }

    public int getWinningNumber()
    {
        return winningNumber;
    }
}
