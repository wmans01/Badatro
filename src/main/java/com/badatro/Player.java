package com.badatro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int ante;  // Difficulty multiplier
    private List<Card> jokers;  // Joker cards
    private List<Joker> activeJokers;  // Active jokers from shop
    private int currentBlindLevel;
    private int money;
    private int handSize;
    private int playableHands;
    private int discardableHands;
    private List<Card> hand;
    private double multiplier;
    private int chips;
    
    // Track levels for each poker hand type
    private Map<String, Integer> pokerHandLevels;
    
    public Player() {
        this.ante = 1;
        this.jokers = new ArrayList<>();
        this.activeJokers = new ArrayList<>();
        this.currentBlindLevel = 1;
        this.money = 4;  // Starting money
        this.handSize = 8;  // Starting hand size
        this.playableHands = 3;  // Default playable hands
        this.discardableHands = 3;  // Default discardable hands
        this.hand = new ArrayList<>();
        this.multiplier = 1.0;
        this.chips = 0;
        
        // Initialize poker hand levels
        this.pokerHandLevels = new HashMap<>();
        pokerHandLevels.put("High Card", 1);
        pokerHandLevels.put("Pair", 1);
        pokerHandLevels.put("Two Pair", 1);
        pokerHandLevels.put("Three of a Kind", 1);
        pokerHandLevels.put("Straight", 1);
        pokerHandLevels.put("Flush", 1);
        pokerHandLevels.put("Full House", 1);
        pokerHandLevels.put("Four of a Kind", 1);
        pokerHandLevels.put("Straight Flush", 1);
        pokerHandLevels.put("Royal Flush", 1);
    }
    
    // Getters and setters
    public int getAnte() {
        return ante;
    }
    
    public void setAnte(int ante) {
        this.ante = ante;
    }
    
    public List<Card> getJokers() {
        return new ArrayList<>(jokers);
    }
    
    public List<Joker> getActiveJokers() {
        return new ArrayList<>(activeJokers);
    }
    
    public void addJoker(Card joker) {
        jokers.add(joker);
    }
    
    public void addJoker(Joker joker) {
        joker.setActive(true);
        activeJokers.add(joker);
    }
    
    public void removeJoker(int index) {
        if (index >= 0 && index < jokers.size()) {
            jokers.remove(index);
        }
    }
    
    public void removeActiveJoker(int index) {
        if (index >= 0 && index < activeJokers.size()) {
            activeJokers.remove(index);
        }
    }
    
    public void removeJoker(Joker joker) {
        activeJokers.remove(joker);
    }
    
    public int getCurrentBlindLevel() {
        return currentBlindLevel;
    }
    
    public void setCurrentBlindLevel(int level) {
        this.currentBlindLevel = level;
    }
    
    public int getMoney() {
        return money;
    }
    
    public void setMoney(int money) {
        this.money = money;
    }
    
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
    
    public int getHandSize() {
        return handSize;
    }
    
    public void setHandSize(int size) {
        this.handSize = size;
    }
    
    public int getPlayableHands() {
        return playableHands;
    }
    
    public void setPlayableHands(int hands) {
        this.playableHands = hands;
    }
    
    public int getDiscardableHands() {
        return discardableHands;
    }
    
    public void setDiscardableHands(int hands) {
        this.discardableHands = hands;
    }
    
    public int getPokerHandLevel(String handType) {
        return pokerHandLevels.getOrDefault(handType, 1);
    }
    
    public void setPokerHandLevel(String handType, int level) {
        pokerHandLevels.put(handType, level);
    }
    
    public void incrementPokerHandLevel(String handType) {
        pokerHandLevels.put(handType, pokerHandLevels.getOrDefault(handType, 1) + 1);
    }
    
    public List<Card> getHand() {
        return hand;
    }
    
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
    
    public int getChips() {
        return chips;
    }
    
    public void setChips(int chips) {
        this.chips = chips;
    }
    
    public void addChips(int amount) {
        this.chips += amount;
    }
    
    public void addMultiplier(double amount) {
        this.multiplier *= amount;
    }
} 