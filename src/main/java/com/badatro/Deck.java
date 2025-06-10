package com.badatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a deck of playing cards, including shuffling, drawing, and tracking used cards.
 */
public class Deck {
    private List<Card> cards;
    private Set<String> usedCards;  // Track used cards by their string representation
    
    /**
     * Constructs a new deck and initializes it with cards.
     */
    public Deck() {
        cards = new ArrayList<>();
        usedCards = new HashSet<>();
        initializeDeck();
    }
    
    /**
     * Initializes the deck with all standard cards and shuffles them.
     */
    private void initializeDeck() {
        // 0: Hearts, 1: Diamonds, 2: Clubs, 3: Spades
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 2; rank <= 14; rank++) {
                cards.add(new Card(rank, suit));
            }
        }
        
        shuffle();
    }
    
    /**
     * Shuffles the deck of cards.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    /**
     * Draws a card from the top of the deck.
     * @return The drawn card, or null if the deck is empty.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.remove(0);
        usedCards.add(card.toString());  // Track the drawn card
        return card;
    }
    
    /**
     * Adds a card to the deck if it hasn't been used yet.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        if (!usedCards.contains(card.toString())) {
            cards.add(card);
            usedCards.add(card.toString());
        }
    }
    
    /**
     * Returns a card to the deck, removing it from the used cards set.
     * @param card The card to return.
     */
    public void returnCard(Card card) {
        usedCards.remove(card.toString());  // Remove from used cards when returned
    }
    
    /**
     * Gets the number of remaining cards in the deck.
     * @return The number of remaining cards.
     */
    public int getRemainingCards() {
        return cards.size();
    }
    
    /**
     * Checks if the deck is empty.
     * @return True if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    /**
     * Gets a copy of the list of cards in the deck.
     * @return A list of cards in the deck.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);  // Return a copy to prevent external modification
    }
    
    /**
     * Checks if a card has already been used.
     * @param card The card to check.
     * @return True if the card is used, false otherwise.
     */
    public boolean isCardUsed(Card card) {
        return usedCards.contains(card.toString());
    }
    
    /**
     * Resets the deck by creating a new set of cards and shuffling them.
     */
    public void reset() {
        // Implementation needed
    }
} 