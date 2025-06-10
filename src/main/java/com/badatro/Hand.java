package com.badatro;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's hand of cards, including adding, removing, and checking cards.
 */
public class Hand {
    private List<Card> cards;
    private static final int MAX_HAND_SIZE = 8;  // Allow up to 8 cards in hand
    private static final int MAX_PLAY_SIZE = 5;  // Only allow playing up to 5 cards at once
    
    /**
     * Constructs an empty hand.
     */
    public Hand() {
        cards = new ArrayList<>();
    }
    
    /**
     * Adds a card to the hand if there is space.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        if (cards.size() < MAX_HAND_SIZE) {
            cards.add(card);
        }
    }
    
    /**
     * Removes a card from the hand.
     * @param card The card to remove.
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }
    
    /**
     * Removes a card at the specified index from the hand.
     * @param index The index of the card to remove.
     */
    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        }
    }
    
    /**
     * Removes multiple cards from the hand by their indices.
     * @param indices The indices of the cards to remove.
     */
    public void removeCards(List<Integer> indices) {
        // Sort indices in descending order to avoid index shifting
        List<Integer> sortedIndices = new ArrayList<>(indices);
        sortedIndices.sort((a, b) -> b - a);
        
        for (int index : sortedIndices) {
            if (index >= 0 && index < cards.size()) {
                cards.remove(index);
            }
        }
    }
    
    /**
     * Gets the card at the specified index.
     * @param index The index of the card to get.
     * @return The card at the specified index, or null if out of bounds.
     */
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }
    
    /**
     * Gets a copy of the list of cards in the hand.
     * @return A list of cards in the hand.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Gets the number of cards in the hand.
     * @return The number of cards in the hand.
     */
    public int getSize() {
        return cards.size();
    }
    
    /**
     * Checks if the hand is full.
     * @return True if the hand is full, false otherwise.
     */
    public boolean isFull() {
        return cards.size() >= MAX_HAND_SIZE;
    }
    
    /**
     * Checks if the specified number of cards can be played at once.
     * @param numCards The number of cards to check.
     * @return True if the number can be played, false otherwise.
     */
    public boolean canPlayCards(int numCards) {
        return numCards <= MAX_PLAY_SIZE;
    }
    
    /**
     * Clears all cards from the hand.
     */
    public void clear() {
        cards.clear();
    }
    
    /**
     * Checks if the hand contains the specified card.
     * @param card The card to check for.
     * @return True if the card is in the hand, false otherwise.
     */
    public boolean contains(Card card) {
        return cards.contains(card);
    }
} 