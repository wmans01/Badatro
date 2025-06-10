package com.badatro;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Evaluates poker hands and calculates their scores.
 */
public class HandEvaluator {
    public static class HandResult {
        private final String handType;
        private final int baseChips;
        private final int baseMult;
        private final List<Card> cards;
        
        public HandResult(String handType, int baseChips, int baseMult, List<Card> cards) {
            this.handType = handType;
            this.baseChips = baseChips;
            this.baseMult = baseMult;
            this.cards = new ArrayList<>(cards);
        }
        
        public String getHandType() { return handType; }
        public int getBaseChips() { return baseChips; }
        public int getBaseMult() { return baseMult; }
        public List<Card> getCards() { return new ArrayList<>(cards); }
    }
    
    /**
     * Evaluates a hand of cards and returns its score.
     * @param cards The list of cards to evaluate.
     * @return The score of the hand.
     */
    public static HandResult evaluateHand(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return new HandResult("Invalid", 0, 0, new ArrayList<>());
        }
        
        // Check for Royal Flush
        if (isRoyalFlush(cards)) {
            return new HandResult("Royal Flush", 100, 8, cards);
        }
        
        // Check for Straight Flush
        if (isStraightFlush(cards)) {
            return new HandResult("Straight Flush", 100, 8, cards);
        }
        
        // Check for Four of a Kind
        if (isFourOfAKind(cards)) {
            return new HandResult("Four of a Kind", 60, 7, cards);
        }
        
        // Check for Full House
        if (isFullHouse(cards)) {
            return new HandResult("Full House", 40, 4, cards);
        }
        
        // Check for Flush
        if (isFlush(cards)) {
            return new HandResult("Flush", 35, 4, cards);
        }
        
        // Check for Straight
        if (isStraight(cards)) {
            return new HandResult("Straight", 30, 4, cards);
        }
        
        // Check for Three of a Kind
        if (isThreeOfAKind(cards)) {
            return new HandResult("Three of a Kind", 30, 3, cards);
        }
        
        // Check for Two Pair
        if (isTwoPair(cards)) {
            return new HandResult("Two Pair", 20, 2, cards);
        }
        
        // Check for Pair
        if (isPair(cards)) {
            return new HandResult("Pair", 10, 2, cards);
        }
        
        // High Card - base chips of 5, multiplier of 1
        return new HandResult("High Card", 5, 1, cards);
    }
    
    /**
     * Checks if a hand is a royal flush.
     * @param cards The list of cards to check.
     * @return True if the hand is a royal flush, false otherwise.
     */
    private static boolean isRoyalFlush(List<Card> cards) {
        if (!isStraightFlush(cards)) return false;
        List<Integer> ranks = cards.stream()
            .map(Card::getRank)
            .sorted()
            .collect(Collectors.toList());
        return ranks.get(0) == 10 && ranks.get(4) == 14;
    }
    
    /**
     * Checks if a hand is a straight flush.
     * @param cards The list of cards to check.
     * @return True if the hand is a straight flush, false otherwise.
     */
    private static boolean isStraightFlush(List<Card> cards) {
        return isFlush(cards) && isStraight(cards);
    }
    
    /**
     * Checks if a hand is a four of a kind.
     * @param cards The list of cards to check.
     * @return True if the hand is a four of a kind, false otherwise.
     */
    private static boolean isFourOfAKind(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getRank,
                Collectors.counting()
            ));
        return rankCounts.values().stream().anyMatch(count -> count >= 4);
    }
    
    /**
     * Checks if a hand is a full house.
     * @param cards The list of cards to check.
     * @return True if the hand is a full house, false otherwise.
     */
    private static boolean isFullHouse(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getRank,
                Collectors.counting()
            ));
        return rankCounts.values().stream().anyMatch(count -> count == 3) &&
               rankCounts.values().stream().anyMatch(count -> count == 2);
    }
    
    /**
     * Checks if a hand is a flush.
     * @param cards The list of cards to check.
     * @return True if the hand is a flush, false otherwise.
     */
    private static boolean isFlush(List<Card> cards) {
        if (cards.size() < 5) return false;
        int firstSuit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == firstSuit);
    }
    
    /**
     * Checks if a hand is a straight.
     * @param cards The list of cards to check.
     * @return True if the hand is a straight, false otherwise.
     */
    private static boolean isStraight(List<Card> cards) {
        if (cards.size() < 5) return false;
        List<Integer> ranks = cards.stream()
            .map(Card::getRank)
            .sorted()
            .collect(Collectors.toList());
        
        // Check for A-2-3-4-5 straight
        if (ranks.contains(14)) { // Ace
            List<Integer> lowAceRanks = new ArrayList<>(ranks);
            lowAceRanks.remove(Integer.valueOf(14));
            lowAceRanks.add(1);
            Collections.sort(lowAceRanks);
            if (isConsecutive(lowAceRanks)) return true;
        }
        
        return isConsecutive(ranks);
    }
    
    private static boolean isConsecutive(List<Integer> numbers) {
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) != numbers.get(i-1) + 1) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a hand is a three of a kind.
     * @param cards The list of cards to check.
     * @return True if the hand is a three of a kind, false otherwise.
     */
    private static boolean isThreeOfAKind(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getRank,
                Collectors.counting()
            ));
        return rankCounts.values().stream().anyMatch(count -> count >= 3);
    }
    
    /**
     * Checks if a hand is a two pair.
     * @param cards The list of cards to check.
     * @return True if the hand is a two pair, false otherwise.
     */
    private static boolean isTwoPair(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getRank,
                Collectors.counting()
            ));
        long pairCount = rankCounts.values().stream()
            .filter(count -> count >= 2)
            .count();
        return pairCount >= 2;
    }
    
    /**
     * Checks if a hand is a pair.
     * @param cards The list of cards to check.
     * @return True if the hand is a pair, false otherwise.
     */
    private static boolean isPair(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getRank,
                Collectors.counting()
            ));
        return rankCounts.values().stream().anyMatch(count -> count >= 2);
    }
    
    public static int calculateCardChips(Card card) {
        int rank = card.getRank();
        // Face cards (J, Q, K) are worth 10
        if (rank >= 11 && rank <= 13) {
            return 10;
        }
        // Ace is worth 11
        if (rank == 14) {
            return 11;
        }
        // Number cards are worth their face value
        return rank;
    }
} 