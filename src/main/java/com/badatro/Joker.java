package com.badatro;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class Joker implements ShopItem {
    private final String name;
    private final String description;
    private final int cost;
    private final String imagePath;
    private boolean isActive;
    private ImageView imageView;
    private final GameManager gameManager;
    
    private Joker(String name, String description, int cost, String imagePath, GameManager gameManager) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.imagePath = imagePath;
        this.isActive = false;
        this.gameManager = gameManager;
        
        // Try to load the image, but don't fail if it's not found
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            this.imageView = new ImageView(image);
        } catch (Exception e) {
            // If image loading fails, create a default image view
            this.imageView = new ImageView();
            System.err.println("Failed to load image for " + name + ": " + e.getMessage());
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public int getCost() {
        return cost;
    }
    
    @Override
    public String getImagePath() {
        return imagePath;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    // Calculate bonus based on the joker's effect
    public int calculateChipBonus(List<Card> cards) {
        if (!isActive) return 0;
        
        switch (name) {
            case "Sly Joker":
                return hasFlush(cards) ? 50 : 0;
            case "Banner Joker":
                return getRemainingDiscards() * 30;
            case "Mystic Summit":
                return getRemainingDiscards() == 0 ? 1000 : 0;
            default:
                return 0;
        }
    }
    
    public double calculateMultBonus(List<Card> cards) {
        if (!isActive) return 0.0;
        
        switch (name) {
            case "Joker":
                return 4.0;
            case "Wrathful Joker":
                return cards.stream()
                    .filter(card -> card.getSuit() == 2) // 2 is spades (♠)
                    .count() * 3.0;
            case "Lusty Joker":
                return cards.stream()
                    .filter(card -> card.getSuit() == 0) // 0 is hearts (♥)
                    .count() * 3.0;
            case "Greedy Joker":
                return cards.stream()
                    .filter(card -> card.getSuit() == 1) // 1 is diamonds (♦)
                    .count() * 3.0;
            case "Gluttonous Joker":
                return cards.stream()
                    .filter(card -> card.getSuit() == 3) // 3 is clubs (♣)
                    .count() * 3.0;
            case "Jolly Joker":
                return hasPair(cards) ? 8.0 : 0.0;
            case "Droll Joker":
                return hasFlush(cards) ? 10.0 : 0.0;
            case "Mystic Summit":
                return getRemainingDiscards() == 0 ? 15.0 : 0.0;
            case "Pair Joker":
                return hasPair(cards) ? 2.0 : 0.0;
            case "Flush Joker":
                return hasFlush(cards) ? 3.0 : 0.0;
            default:
                return 0.0;
        }
    }
    
    private int countSuit(List<Card> cards, int suit) {
        return (int) cards.stream()
                         .filter(card -> card.getSuit() == suit)
                         .count();
    }
    
    private boolean hasPair(List<Card> cards) {
        return cards.stream()
                   .collect(java.util.stream.Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .anyMatch(list -> list.size() >= 2);
    }
    
    private boolean hasFlush(List<Card> cards) {
        if (cards.size() < 5) return false;
        int firstSuit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == firstSuit);
    }
    
    private int getRemainingDiscards() {
        return gameManager.getPlayer().getDiscardableHands();
    }
    
    // Factory methods for creating different types of jokers
    public static Joker createJoker(GameManager gameManager) {
        return new Joker(
            "Joker",
            "Adds +4 mult",
            2,
            "/com/badatro/Jokers/joker.jpg",
            gameManager
        );
    }
    
    public static Joker createGreedy(GameManager gameManager) {
        return new Joker(
            "Greedy Joker",
            "Each diamond card played gives +3 mult when scored",
            5,
            "/com/badatro/Jokers/greedy.jpg",
            gameManager
        );
    }
    
    public static Joker createLusty(GameManager gameManager) {
        return new Joker(
            "Lusty Joker",
            "Each heart card played gives +3 mult when scored",
            5,
            "/com/badatro/Jokers/lusty.jpg",
            gameManager
        );
    }
    
    public static Joker createWrathful(GameManager gameManager) {
        return new Joker(
            "Wrathful Joker",
            "Each spade card played gives +3 mult when scored",
            5,
            "/com/badatro/Jokers/wrathful.jpg",
            gameManager
        );
    }
    
    public static Joker createGluttonous(GameManager gameManager) {
        return new Joker(
            "Gluttonous Joker",
            "Each club card played gives +3 mult when scored",
            5,
            "/com/badatro/Jokers/gluttonous.jpg",
            gameManager
        );
    }
    
    public static Joker createJolly(GameManager gameManager) {
        return new Joker(
            "Jolly Joker",
            "+8 mult if hand contains pair",
            3,
            "/com/badatro/Jokers/jolly.jpg",
            gameManager
        );
    }
    
    public static Joker createDroll(GameManager gameManager) {
        return new Joker(
            "Droll Joker",
            "+10 mult if hand contains flush",
            4,
            "/com/badatro/Jokers/droll.jpg",
            gameManager
        );
    }
    
    public static Joker createSly(GameManager gameManager) {
        return new Joker(
            "Sly Joker",
            "+50 chips if hand contains flush",
            3,
            "/com/badatro/Jokers/sly.jpg",
            gameManager
        );
    }
    
    public static Joker createBanner(GameManager gameManager) {
        return new Joker(
            "Banner Joker",
            "+30 chips for each remaining discard",
            5,
            "/com/badatro/Jokers/banner.jpg",
            gameManager
        );
    }
    
    public static Joker createMystic(GameManager gameManager) {
        return new Joker(
            "Mystic Summit",
            "+15 mult when 0 discards remaining",
            5,
            "/com/badatro/Jokers/mystic.jpg",
            gameManager
        );
    }
} 