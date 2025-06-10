package com.badatro;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of cards that are to be scored, including adding, clearing, and retrieving cards.
 */
public class ToBeScored {
    private List<Card> cards;
    
    /**
     * Constructs an empty ToBeScored collection.
     */
    public ToBeScored() {
        this.cards = new ArrayList<>();
    }
    
    /**
     * Adds a card to the collection.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }
    
    /**
     * Adds multiple cards to the collection.
     * @param cards The list of cards to add.
     */
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    
    /**
     * Gets a copy of the list of cards in the collection.
     * @return A list of cards in the collection.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Clears all cards from the collection.
     */
    public void clear() {
        cards.clear();
    }
    
    /**
     * Calculates the total chip value of all cards in the collection.
     * @return The total chip value.
     */
    public int getTotalChips() {
        return cards.stream()
                   .mapToInt(Card::getBaseChips)
                   .sum();
    }
    
    /**
     * Calculates the final score for the collection.
     * @return The final score.
     */
    public int getFinalScore() {
        return getTotalChips();
    }
    
    /**
     * Removes all cards from the collection and returns them.
     * @return The list of removed cards.
     */
    public List<Card> removeAllCards() {
        List<Card> removedCards = new ArrayList<>(cards);
        cards.clear();
        return removedCards;
    }
} 