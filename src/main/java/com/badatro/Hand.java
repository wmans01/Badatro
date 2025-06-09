package com.badatro;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;
    private static final int MAX_HAND_SIZE = 8;  // Allow up to 8 cards in hand
    private static final int MAX_PLAY_SIZE = 5;  // Only allow playing up to 5 cards at once
    
    public Hand() {
        cards = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        if (cards.size() < MAX_HAND_SIZE) {
            cards.add(card);
        }
    }
    
    public void removeCard(Card card) {
        cards.remove(card);
    }
    
    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        }
    }
    
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
    
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public int getSize() {
        return cards.size();
    }
    
    public boolean isFull() {
        return cards.size() >= MAX_HAND_SIZE;
    }
    
    public boolean canPlayCards(int numCards) {
        return numCards <= MAX_PLAY_SIZE;
    }
    
    public void clear() {
        cards.clear();
    }
    
    public boolean contains(Card card) {
        return cards.contains(card);
    }
} 