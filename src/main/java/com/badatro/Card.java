package com.badatro;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
    // Basic card attributes
    private final int rank;  // 2-14 (where 14 is Ace)
    private final int suit;  // Hearts, Diamonds, Clubs, Spades
    private final boolean isJoker;  // Whether this card is a joker
    
    private final ImageView imageView;
    private boolean isSelected;
    private static final double SELECTED_OFFSET = -20.0; // Pixels to move up when selected
    
    // Constructor for regular cards
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
    
    // Constructor for jokers
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
    
    // Getters
    public int getRank() {
        return rank;
    }
    
    public int getSuit() {
        return suit;
    }
    
    public boolean isJoker() {
        return isJoker;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    // Method to get card's base value in chips
    public int getBaseChips() {
        return rank;  // Base chips equal to rank
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            imageView.setTranslateY(SELECTED_OFFSET);
        } else {
            imageView.setTranslateY(0);
        }
    }
    
    public void toggleSelected() {
        setSelected(!isSelected);
    }
    
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