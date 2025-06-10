package com.badatro;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a discard pile where cards are placed after being discarded.
 */
public class DiscardPile {
    private List<Card> cards;
    
    /**
     * Constructs an empty discard pile.
     */
    public DiscardPile() {
        this.cards = new ArrayList<>();
    }
    
    /**
     * Adds a card to the discard pile.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }
    
    /**
     * Adds multiple cards to the discard pile.
     * @param cards The list of cards to add.
     */
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    
    /**
     * Gets a copy of the list of cards in the discard pile.
     * @return A list of cards in the discard pile.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Clears all cards from the discard pile.
     */
    public void clear() {
        cards.clear();
    }
    
    public int getSize() {
        return cards.size();
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
} 