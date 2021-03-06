package com.example.sarabracha.perpetualmotion.game;

import java.util.Stack;
public class Game {
    private Deck deck;
    private Stack<Card>[] stacks; // array of 4 separate stacks of cards
    private int deckCount;
    private int cardsLeft;
    private boolean lastTurnCanBeUndone, lastTurnWasADiscard;
    private Card[] tentativeTopCardsAtLastTurn, finalTopCardsAtLastTurn;

    private final int NUMBER_OF_STACKS = 4;

    public Game() {
        deck = new Deck();
        deckCount = 52;
        cardsLeft = 52;

        deck.shuffle();
        stacks = (Stack<Card>[]) new Stack[NUMBER_OF_STACKS];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new Stack<Card>();
            stacks[i].push(deck.deal());
        }
        deckCount -= 4;
        setupUndo();
    }

    private void setupUndo ()
    {
        tentativeTopCardsAtLastTurn = new Card[NUMBER_OF_STACKS];
        finalTopCardsAtLastTurn = tentativeTopCardsAtLastTurn.clone ();

        lastTurnWasADiscard = false;
        lastTurnCanBeUndone = false;
    }

    public void discard(int stack1, int stack2) {
	if (stacks[stack1].equals(stacks[stack2])) {
        return;
	}

	if (stacks[stack1].isEmpty() || stacks[stack2].isEmpty()) {
	    return;
	}

	if (stacks[stack1].peek().getRank().getRankNumber() == (stacks[stack2].peek().getRank().getRankNumber())) {
        saveTentativePriorTurnInformation ();
	    stacks[stack1].pop();
	    stacks[stack2].pop();
        commitPriorTurnInformationAndSetUndoTypeTo (true);
	    cardsLeft -= 2;
	}
    else if (stacks[stack1].peek().getSuit().equals(stacks[stack2].peek().getSuit())) {	//the player has to find the 2 stacks of the same suit. Otherwise, they could just click around...not as fun
	    if (stacks[stack1].peek().getRank().getRankNumber() < (stacks[stack2].peek().getRank().getRankNumber())) {
            saveTentativePriorTurnInformation ();
		stacks[stack1].pop();
            commitPriorTurnInformationAndSetUndoTypeTo (true);
            cardsLeft--;
	    } else if (stacks[stack1].peek().getRank()
		    .getRankNumber() > (stacks[stack2].peek().getRank().getRankNumber())) {
            saveTentativePriorTurnInformation ();
		stacks[stack2].pop();
            commitPriorTurnInformationAndSetUndoTypeTo (true);
            cardsLeft--;
	    }
	}
    }

    public void clickDeal() {
        saveTentativePriorTurnInformation ();
	for (int i = 0; i < stacks.length; i++) {
	    Card c = deck.deal();
	    stacks[i].push(c);
	}
        deckCount -=4;
        commitPriorTurnInformationAndSetUndoTypeTo (false);

	if (deck.isEmpty()) {
	    return;
	}

    }
    
    public boolean gameWon(){
	if(deckCount == 0){
	    return true;
	}
	return false;
    }

	public int getNumberOfCardsLeftInDeck() {
		return deckCount;
	}

       public void undoLatestTurn ()
        {
            if (lastTurnCanBeUndone) {
                if (!(getNumCardsLeftToDiscard () == deck.getSize()
                        && getNumberOfCardsLeftInAllStacks () == stacks.length)) {

                    doUndoATurnOfEitherDiscardOrDeal ();
                }
                else {
                    throw new UnsupportedOperationException ("This is the beginning of the game.");
                }
            }
            else {
                throw new UnsupportedOperationException ("Already undid the latest turn.");
            }
        }

        private void doUndoATurnOfEitherDiscardOrDeal ()
        {
            if (lastTurnWasADiscard) {
                doUndoATurnOfTypeDiscard ();
            }
            else {
                doUndoATurnOfTypeDeal ();
            }

            lastTurnCanBeUndone = false;
        }

        private void doUndoATurnOfTypeDiscard ()
        {
            for (int i = 0; i < finalTopCardsAtLastTurn.length; i++) {
                if (finalTopCardsAtLastTurn[i] != null) {
                    if (stacks[i].size () == 0
                            || !(finalTopCardsAtLastTurn[i].equals (stacks[i].peek ())))
                    {
                        stacks[i].push (finalTopCardsAtLastTurn[i]);
                    }
                }
            }
        }

        private void doUndoATurnOfTypeDeal ()
        {
            for (int i = stacks.length - 1; i >= 0; i--) {
                deck.returnCardToDeck (stacks[i].pop ());
            }

        }

        private void saveTentativePriorTurnInformation ()
        {
            for (int i = 0; i < tentativeTopCardsAtLastTurn.length; i++) {
                tentativeTopCardsAtLastTurn[i] =
                        stacks[i].size () != 0 ? stacks[i].peek () : null;
            }
        }

        private void commitPriorTurnInformationAndSetUndoTypeTo (boolean turnWasADiscard)
        {
            lastTurnCanBeUndone = true;
            lastTurnWasADiscard = turnWasADiscard;

            System.arraycopy (tentativeTopCardsAtLastTurn, 0, finalTopCardsAtLastTurn, 0,
                    tentativeTopCardsAtLastTurn.length);
        }


      /*  public void discardOneLowestOfSameSuit (int pileNo)
        {
            boolean moveFound=false;

            Suit theSuit = stacks[pileNo].peek ().getSuit ();//suit of card to be discarded
            Rank theRank = stacks[pileNo].peek ().getRank ();//rank of the card to be discarded

            //now check which other pile has a card on top with the same suit
            for (int i = 0; i < stacks.length; i++) {
                if (i != pileNo && !stacks[i].isEmpty ()) {
                    if (stacks[i].peek ().getSuit ().equals (theSuit)) {

                        //this pile has a second Card with the same suit;
                        // now check if the current card's rank is lower
                        if (theRank.getRankNumber () < stacks[i].peek ().getRank ().getRankNumber()) {
                            saveTentativePriorTurnInformation ();

                            stacks[pileNo].pop ();

                            commitPriorTurnInformationAndSetUndoTypeTo (true);
                            moveFound = true;
                        }
                    }
                }
            }

            if (!moveFound)
            {
                throw new UnsupportedOperationException
                        ("To remove one card, there must also be another visible card " +
                                "of the same suit with a higher rank.");
            }
        }

        /**
         * Discards the top card of the same rank from two stacks (order is not relevant).
         * Saves undo state information before attempting to remove the cards.
         * Commits that information only after removal is successful
         * @param pileNo1 1st stack containing the card with same rank as the other stack
         * @param pileNo2 2nd stack containing the card with same rank as the other stack
         * @throws UnsupportedOperationException if the two selected cards are not the same rank
         */
        /*public void discardBothOfSameRank (int pileNo1, int pileNo2)
        {
            //verify that this is ok, that both Cards have to have the same rank value
            if (stacks[pileNo1].peek ().getRank ().getRankNumber() ==
                    stacks[pileNo2].peek ().getRank ().getRankNumber ()) {

                saveTentativePriorTurnInformation ();

                stacks[pileNo1].pop ();
                stacks[pileNo2].pop ();

                commitPriorTurnInformationAndSetUndoTypeTo (true);
            }
            else
            {
                throw new UnsupportedOperationException
                        ("To remove two cards, both selected cards must have the same rank.");
            }
        }

        */
       public int getNumberOfCardsLeftInAllStacks ()
        {
            int cardsInStacks = 0;
            for (Stack<Card> stack : stacks) {
                cardsInStacks += stack.size ();
            }
            return cardsInStacks;
        }

        /**
         * Gets an array containing the current top card of each of the piles (0-3).
         * In MainActivity, we pass this to the adapter to replace the current "board" with these cards.
         * @return array of 4 card tops (elements 0-3)
         */
        public Card[] getCurrentStacksTopIncludingNulls ()
        {
            Card[] currentStacksTop = new Card[4];
            for (int i = 0; i < 4; i++) {
                currentStacksTop[i] = stacks[i].isEmpty () ? null : stacks[i].peek ();
            }
            return currentStacksTop;
        }

        /**
         * Gets the number of cards left in a particular stack, including ones below the top.
         * @param position stack number (0-3) of stack whose total will be returned
         * @return total number of cards in a particular stack
         */
        public int getNumberOfCardsInStackAtPosition (int position)
        {
            return stacks[position].size ();
        }

        /**
         * Gets the total number of cards to discard, which is the totals of both the deck & all stacks.
         * @return total number of cards needed to discard
         */
        public int getNumCardsLeftToDiscard ()
        {
            return cardsLeft;
        }


        /**
         * Gets the Game Over status, which is true if Deck is empty and there are no more doable moves.
         * @return game over status
         */
       public boolean isGameOver ()
        {
            return deck.getSize () == 0 &&
                    (getNumberOfCardsLeftInAllStacks () == 0 ||
                            !hasAtLeastOneValidMoveInCurrentStackTops ());
        }

        /**
         * Gets the availability of at least one move in the current Stack tops
         * @return true if there is one move in the current Stack tops
         */
        public boolean hasAtLeastOneValidMoveInCurrentStackTops ()
        {
            boolean hasAvailableTurn = false;
            for (int i = 0; i < stacks.length && !hasAvailableTurn; i++) {
                for (int j = 0; j < stacks.length && !hasAvailableTurn; j++) {
                    if (i != j && stacks[i] != null && stacks[j] !=null)
                    {
                        if (stacks[i].peek ().getRank ().equals (stacks[j].peek ().getRank ()) ||
                                stacks[i].peek ().getSuit ().equals (stacks[j].peek ().getSuit ()))
                        {
                            hasAvailableTurn = true;
                        }
                    }
                }
            }
            return hasAvailableTurn;
        }

        /**
         * Gets the status of the game having been won, as opposed to in progress or game lost.
         * @return true if no more cards left to discard; otherwise, false.
         */
        public boolean isWinner()
        {
            return cardsLeft == 0;
        }

        /**
         * Gets the rules of the game.
         * @return game's rules
         */
        public String getRules ()
        {
            // This should really be a string in XML, but it came (almost) like this from Java-only...
            return "The goal of the game is to discard all cards " +
                    "until both the deck and all four piles are empty."
                    + "\n\nAfter all 52 cards have been dealt from deck, game-play can continue until:"
                    + "\n1. All cards have been discarded from all four stacks (a winner!). - OR -"
                    + "\n2. None of the remaining top cards in any pile can be discarded (not a winner)."
                    + "\n\nEach pile initially contains one card at the top, " +
                    "which leaves 48 cards remaining in the deck."
                    + "\n\nFor each turn taken, there are three potential options from which to choose:"
                    + "\n1. If there are two cards of same suit showing, discard the lower-ranked card."
                    + "\n2. If there are two cards with same rank showing, discard both of those cards."
                    + "\n3. Deal four new cards from the deck, one on top of each stack.";
        }


    }