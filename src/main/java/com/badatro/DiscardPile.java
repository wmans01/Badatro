package com.badatro;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private List<Card> cards;
    
    public DiscardPile() {
        this.cards = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
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