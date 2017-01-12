package com.example.sarabracha.perpetualmotion.game;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> deck;
	protected int numCards;

    public Deck() {
		final int DEFFAULT_NUM_CARDS = 52;
	deck = new ArrayList<Card>(DEFFAULT_NUM_CARDS);
		numCards = DEFFAULT_NUM_CARDS;
		for(Rank r: Rank.values()){
			for(Suit s : Suit.values())
			{
				Card c = new Card(r,s);
				deck.add(c);
			}
		}
	}
    public void shuffle() {
	Random rand = new Random();
	for(int i = 0; i < this.deck.size(); i++){
	    int num = rand.nextInt(51);
	    Card holder = this.deck.get(i);
	    deck.set(i, deck.get(num));
	    deck.set(num, holder);
	}
    }
    
    public Card deal(){
	if(!deck.isEmpty()){
	    return this.deck.remove(0);
	}
	return null;
    }
    
    public boolean isEmpty(){
	return this.deck.isEmpty();
    }
    
    public int getSize(){
	return deck.size();
    }

	public void returnCardToDeck(Card c){
		deck.set(0, c);
	}
    /*public static void main(String[] args) {
	Deck deck = new Deck();
	for (int i = 0; i < deck.deck.size(); i++) {
	//    System.out.println(deck.deck.get(i).toString());
	}
	deck.shuffle();
	for (int i = 0; i < deck.deck.size(); i++) {
	//    System.out.println(deck.deck.get(i).toString());
	}
	System.out.println("Deal: " + deck.deal());
	System.out.println("Deal: " + deck.deal());
	System.out.println("Deal: " + deck.deal());
	System.out.println("Deal: " + deck.deal());
	System.out.println("Deal: " + deck.deal());
	System.out.println("Deal: " + deck.deal());
	
	System.out.println(deck.deck.size());
	
    }*/

}
