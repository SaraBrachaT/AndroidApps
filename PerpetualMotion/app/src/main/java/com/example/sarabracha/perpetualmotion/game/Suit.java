package com.example.sarabracha.perpetualmotion.game;
import android.graphics.Color;

public enum Suit {
    HEARTS (Color.RED, '♥'), DIAMONDS (Color.RED, '♦'), CLUBS (Color.BLACK, '♣'), SPADES (Color.BLACK, '♠');
    private int color;
    private char character;

    Suit(int color, char c) {
        this.color=color;
        this.character = c;
    }

    public char getCharacter()
    {
        return  character;
    }

    public int getColor()
    {
        return color;
    }
}
