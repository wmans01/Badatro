package com.badatro;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

/**
 * Represents a joker card that can be bought in the shop and provides various bonuses during gameplay.
 */
public class Joker implements ShopItem {
    private final String name;
    private final String description;
    private final int cost;
    private final String imagePath;
    private boolean isActive;
    private ImageView imageView;
    private final GameManager gameManager;
    
    /**
     * Constructs a new joker with the specified properties.
     * @param name The name of the joker.
     * @param description The description of the joker's effect.
     * @param cost The cost to buy the joker.
     * @param imagePath The path to the joker's image.
     * @param gameManager The game manager instance.
     */
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
    
    /**
     * Gets the name of the joker.
     * @return The joker's name.
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description of the joker.
     * @return The joker's description.
     */
    @Override
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the cost of the joker.
     * @return The joker's cost.
     */
    @Override
    public int getCost() {
        return cost;
    }
    
    /**
     * Gets the image path of the joker.
     * @return The path to the joker's image.
     */
    @Override
    public String getImagePath() {
        return imagePath;
    }
    
    /**
     * Gets the ImageView for the joker.
     * @return The ImageView displaying the joker.
     */
    public ImageView getImageView() {
        return imageView;
    }
    
    /**
     * Checks if the joker is currently active.
     * @return True if the joker is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the active state of the joker.
     * @param active True to activate the joker, false to deactivate it.
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Calculates the chip bonus provided by the joker based on the played cards.
     * @param cards The list of cards being played.
     * @return The chip bonus amount.
     */
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
    
    /**
     * Calculates the multiplier bonus provided by the joker based on the played cards.
     * @param cards The list of cards being played.
     * @return The multiplier bonus amount.
     */
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
    
    /**
     * Counts the number of cards of a specific suit in the given list.
     * @param cards The list of cards to check.
     * @param suit The suit to count.
     * @return The number of cards with the specified suit.
     */
    private int countSuit(List<Card> cards, int suit) {
        return (int) cards.stream()
                         .filter(card -> card.getSuit() == suit)
                         .count();
    }
    
    /**
     * Checks if the given cards contain a pair.
     * @param cards The list of cards to check.
     * @return True if there is a pair, false otherwise.
     */
    private boolean hasPair(List<Card> cards) {
        return cards.stream()
                   .collect(java.util.stream.Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .anyMatch(list -> list.size() >= 2);
    }
    
    /**
     * Checks if the given cards form a flush.
     * @param cards The list of cards to check.
     * @return True if the cards form a flush, false otherwise.
     */
    private boolean hasFlush(List<Card> cards) {
        if (cards.size() < 5) return false;
        int firstSuit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == firstSuit);
    }
    
    /**
     * Gets the number of remaining discards for the player.
     * @return The number of remaining discards.
     */
    private int getRemainingDiscards() {
        return gameManager.getPlayer().getDiscardableHands();
    }
    
    /**
     * Creates a basic joker.
     * @param gameManager The game manager instance.
     * @return A new basic joker.
     */
    public static Joker createJoker(GameManager gameManager) {
        return new Joker(
            "Joker",
            "Adds +4 mult",
            2,
            "/com/badatro/Jokers/joker.jpg",
            gameManager
        );
    }
    
    /**
     * Creates a greedy joker that provides bonuses for diamond cards.
     * @param gameManager The game manager instance.
     * @return A new greedy joker.
     */
    public static Joker createGreedy(GameManager gameManager) {
        return new Joker(
            "Greedy Joker",
            "Each diamond card played gives +3 mult when scored",
            5,
            "/com/badatro/Jokers/greedy.jpg",
            gameManager
        );
    }
    
    /**
     * Creates a lusty joker that provides bonuses for heart cards.
     * @param gameManager The game manager instance.
     * @return A new lusty joker.
     */
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