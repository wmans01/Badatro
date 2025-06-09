package com.badatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Deck {
    private List<Card> cards;
    private Set<String> usedCards;  // Track used cards by their string representation
    
    public Deck() {
        cards = new ArrayList<>();
        usedCards = new HashSet<>();
        initializeDeck();
    }
    
    private void initializeDeck() {
        // 0: Hearts, 1: Diamonds, 2: Clubs, 3: Spades
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 2; rank <= 14; rank++) {
                cards.add(new Card(rank, suit));
            }
        }
        
        shuffle();
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.remove(0);
        usedCards.add(card.toString());  // Track the drawn card
        return card;
    }
    
    public void addCard(Card card) {
        if (!usedCards.contains(card.toString())) {
            cards.add(card);
            usedCards.add(card.toString());
        }
    }
    
    public void returnCard(Card card) {
        usedCards.remove(card.toString());  // Remove from used cards when returned
    }
    
    public int getRemainingCards() {
        return cards.size();
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);  // Return a copy to prevent external modification
    }
    
    public boolean isCardUsed(Card card) {
        return usedCards.contains(card.toString());
    }
} 