package com.example.sarabracha.perpetualmotion.game.exceptions;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(){
	super("Item Not Found");
    }
}
