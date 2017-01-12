package com.example.sarabracha.perpetualmotion.game;
import com.example.sarabracha.perpetualmotion.game.exceptions.InvalidDataException;
import com.example.sarabracha.perpetualmotion.game.exceptions.MissingDataException;
public class Card {

    private Rank rank;
    private Suit suit;

    public Card(String rank, String suit){
		this(Rank.valueOf(rank), Suit.valueOf(suit));

		/*if(rank == null || suit == null || color == null)
		{
			throw new MissingDataException();
		}*/
    }
    
    public Card(Rank r, Suit s){
	//no need to validate because it's already a card
		this.rank = r;
		this.suit = s;
    }
    
    
    public Rank getRank(){
	return this.rank;
    }
    
    public Suit getSuit(){
	return this.suit;
    }
    
    public int getColor(){
		return suit.getColor();
    }
    
    public String toString(){	//not used in this implementation of the game
	StringBuilder s = new StringBuilder();
	s.append("\nCard Description");
	s.append("\nRank: ");
	s.append(this.rank);
	s.append("\nSuit: ");
	s.append(this.suit);
	s.append("\nColor: ");
	s.append(this.getColor());
	
	return s.toString();
    }
}
