package com.badatro;

import java.util.ArrayList;
import java.util.List;

public class ToBeScored {
    private List<Card> cards;
    
    public ToBeScored() {
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
    
    public int getTotalChips() {
        return cards.stream()
                   .mapToInt(Card::getBaseChips)
                   .sum();
    }
    
    public int getFinalScore() {
        return getTotalChips();
    }
    
    public List<Card> removeAllCards() {
        List<Card> removedCards = new ArrayList<>(cards);
        cards.clear();
        return removedCards;
    }
} 