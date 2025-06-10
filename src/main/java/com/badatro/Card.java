package com.badatro;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a playing card, including rank, suit, image, and selection state.
 */
public class Card {
    // Basic card attributes
    private final int rank;  // 2-14 (where 14 is Ace)
    private final int suit;  // Hearts, Diamonds, Clubs, Spades
    private final boolean isJoker;  // Whether this card is a joker
    
    private final ImageView imageView;
    private boolean isSelected;
    private static final double SELECTED_OFFSET = -20.0; // Pixels to move up when selected
    
    /**
     * Constructs a regular card with the given rank and suit.
     * @param rank The rank of the card (2-14, where 14 is Ace).
     * @param suit The suit of the card (0-3).
     */
    public Card(int rank, int suit) {
        if (rank < 2 || rank > 14) {
            throw new IllegalArgumentException("Rank must be between 2 and 14");
        }
        if (suit < 0 || suit > 3) {
            throw new IllegalArgumentException("Invalid suit");
        }
        
        this.rank = rank;
        this.suit = suit;
        this.isJoker = false;
        this.isSelected = false;
        
        // Load card image
        int imageRank;
        if (rank == 14) {         // Ace
            imageRank = 0;
        } else if (rank == 11) {  // Jack
            imageRank = 10;
        } else if (rank == 12) {  // Queen
            imageRank = 11;
        } else if (rank == 13) {  // King
            imageRank = 12;
        } else {
            imageRank = rank;
        }
        String imagePath = String.format("/com/badatro/Deck/%d-%d.jpg", imageRank, suit);
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                throw new RuntimeException("Failed to load image: " + imagePath);
            }
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(100);  // Adjust size as needed
            this.imageView.setPreserveRatio(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load card image: " + imagePath, e);
        }
    }
    
    /**
     * Constructs a joker card.
     */
    public Card() {
        this.rank = 0;
        this.suit = 0;
        this.isJoker = true;
        this.isSelected = false;
        
        // Load joker image
        String imagePath = "/com/badatro/Deck/0-0.jpg";
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                throw new RuntimeException("Failed to load joker image: " + imagePath);
            }
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(100);  // Adjust size as needed
            this.imageView.setPreserveRatio(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load joker image: " + imagePath, e);
        }
    }
    
    /**
     * Gets the rank of the card.
     * @return The rank of the card.
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * Gets the suit of the card.
     * @return The suit of the card.
     */
    public int getSuit() {
        return suit;
    }
    
    /**
     * Returns whether this card is a joker.
     * @return True if joker, false otherwise.
     */
    public boolean isJoker() {
        return isJoker;
    }
    
    /**
     * Gets the ImageView for this card.
     * @return The ImageView representing the card.
     */
    public ImageView getImageView() {
        return imageView;
    }
    
    /**
     * Gets the base chip value for this card.
     * @return The base chip value.
     */
    public int getBaseChips() {
        return rank;  // Base chips equal to rank
    }
    
    /**
     * Returns whether this card is selected.
     * @return True if selected, false otherwise.
     */
    public boolean isSelected() {
        return isSelected;
    }
    
    /**
     * Sets the selection state of this card.
     * @param selected True to select, false to deselect.
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            imageView.setTranslateY(SELECTED_OFFSET);
        } else {
            imageView.setTranslateY(0);
        }
    }
    
    /**
     * Toggles the selection state of this card.
     */
    public void toggleSelected() {
        setSelected(!isSelected);
    }
    
    /**
     * Returns a string representation of the card.
     * @return The string representation.
     */
    @Override
    public String toString() {
        if (isJoker) {
            return "Joker";
        }
        
        String rankStr;
        switch (rank) {
            case 11: rankStr = "J"; break;
            case 12: rankStr = "Q"; break;
            case 13: rankStr = "K"; break;
            case 14: rankStr = "A"; break;
            default: rankStr = String.valueOf(rank);
        }
        
        String suitStr;
        switch (suit) {
            case 0: suitStr = "♥"; break;
            case 1: suitStr = "♦"; break;
            case 2: suitStr = "♣"; break;
            case 3: suitStr = "♠"; break;
            default: suitStr = "?";
        }
        
        return rankStr + suitStr;
    }
} 