package com.badatro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a player in the game, managing their state including money, hands, jokers, and poker hand levels.
 */
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
    
    /**
     * Constructs a new player with default values.
     */
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
    
    /**
     * Gets the player's ante value.
     * @return The ante value.
     */
    public int getAnte() {
        return ante;
    }
    
    /**
     * Sets the player's ante value.
     * @param ante The new ante value.
     */
    public void setAnte(int ante) {
        this.ante = ante;
    }
    
    /**
     * Gets a copy of the player's joker cards.
     * @return A list of joker cards.
     */
    public List<Card> getJokers() {
        return new ArrayList<>(jokers);
    }
    
    /**
     * Gets a copy of the player's active jokers.
     * @return A list of active jokers.
     */
    public List<Joker> getActiveJokers() {
        return new ArrayList<>(activeJokers);
    }
    
    /**
     * Adds a joker card to the player's collection.
     * @param joker The joker card to add.
     */
    public void addJoker(Card joker) {
        jokers.add(joker);
    }
    
    /**
     * Adds an active joker to the player's collection.
     * @param joker The joker to add.
     */
    public void addJoker(Joker joker) {
        joker.setActive(true);
        activeJokers.add(joker);
    }
    
    /**
     * Removes a joker card at the specified index.
     * @param index The index of the joker to remove.
     */
    public void removeJoker(int index) {
        if (index >= 0 && index < jokers.size()) {
            jokers.remove(index);
        }
    }
    
    /**
     * Removes an active joker at the specified index.
     * @param index The index of the active joker to remove.
     */
    public void removeActiveJoker(int index) {
        if (index >= 0 && index < activeJokers.size()) {
            activeJokers.remove(index);
        }
    }
    
    /**
     * Removes a specific joker from the active jokers.
     * @param joker The joker to remove.
     */
    public void removeJoker(Joker joker) {
        activeJokers.remove(joker);
    }
    
    /**
     * Gets the player's current blind level.
     * @return The current blind level.
     */
    public int getCurrentBlindLevel() {
        return currentBlindLevel;
    }
    
    /**
     * Sets the player's current blind level.
     * @param level The new blind level.
     */
    public void setCurrentBlindLevel(int level) {
        this.currentBlindLevel = level;
    }
    
    /**
     * Gets the player's current money.
     * @return The amount of money.
     */
    public int getMoney() {
        return money;
    }
    
    /**
     * Sets the player's money.
     * @param money The new amount of money.
     */
    public void setMoney(int money) {
        this.money = money;
    }
    
    /**
     * Attempts to spend money from the player's balance.
     * @param amount The amount to spend.
     * @return True if the player had enough money, false otherwise.
     */
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Gets the player's hand size.
     * @return The hand size.
     */
    public int getHandSize() {
        return handSize;
    }
    
    /**
     * Sets the player's hand size.
     * @param size The new hand size.
     */
    public void setHandSize(int size) {
        this.handSize = size;
    }
    
    /**
     * Gets the number of playable hands remaining.
     * @return The number of playable hands.
     */
    public int getPlayableHands() {
        return playableHands;
    }
    
    /**
     * Sets the number of playable hands.
     * @param hands The new number of playable hands.
     */
    public void setPlayableHands(int hands) {
        this.playableHands = hands;
    }
    
    /**
     * Gets the number of discardable hands remaining.
     * @return The number of discardable hands.
     */
    public int getDiscardableHands() {
        return discardableHands;
    }
    
    /**
     * Sets the number of discardable hands.
     * @param hands The new number of discardable hands.
     */
    public void setDiscardableHands(int hands) {
        this.discardableHands = hands;
    }
    
    /**
     * Gets the level for a specific poker hand type.
     * @param handType The type of poker hand.
     * @return The level for that hand type.
     */
    public int getPokerHandLevel(String handType) {
        return pokerHandLevels.getOrDefault(handType, 1);
    }
    
    /**
     * Sets the level for a specific poker hand type.
     * @param handType The type of poker hand.
     * @param level The new level.
     */
    public void setPokerHandLevel(String handType, int level) {
        pokerHandLevels.put(handType, level);
    }
    
    /**
     * Increments the level for a specific poker hand type.
     * @param handType The type of poker hand.
     */
    public void incrementPokerHandLevel(String handType) {
        pokerHandLevels.put(handType, pokerHandLevels.getOrDefault(handType, 1) + 1);
    }
    
    /**
     * Gets the player's current hand.
     * @return The list of cards in the hand.
     */
    public List<Card> getHand() {
        return hand;
    }
    
    /**
     * Sets the player's hand.
     * @param hand The new hand.
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }
    
    /**
     * Gets the player's current multiplier.
     * @return The multiplier value.
     */
    public double getMultiplier() {
        return multiplier;
    }
    
    /**
     * Sets the player's multiplier.
     * @param multiplier The new multiplier value.
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
    
    /**
     * Gets the player's current chips.
     * @return The number of chips.
     */
    public int getChips() {
        return chips;
    }
    
    /**
     * Sets the player's chips.
     * @param chips The new number of chips.
     */
    public void setChips(int chips) {
        this.chips = chips;
    }
    
    /**
     * Adds chips to the player's total.
     * @param amount The amount of chips to add.
     */
    public void addChips(int amount) {
        this.chips += amount;
    }
    
    /**
     * Adds to the player's multiplier.
     * @param amount The amount to add to the multiplier.
     */
    public void addMultiplier(double amount) {
        this.multiplier *= amount;
    }
} 