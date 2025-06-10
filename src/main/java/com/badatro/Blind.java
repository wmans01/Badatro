package com.badatro;

/**
 * Represents a blind in the game, including its name, ante, description, and completion status.
 */
public class Blind {
    private final String name;
    private final int ante;
    private final String description;
    private boolean isCleared;
    
    /**
     * Constructs a Blind with the given name, ante, and description.
     * @param name The name of the blind.
     * @param ante The ante value for the blind.
     * @param description The description of the blind.
     */
    public Blind(String name, int ante, String description) {
        this.name = name;
        this.ante = ante;
        this.description = description;
        this.isCleared = false;
    }
    
    /**
     * Gets the name of the blind.
     * @return The name of the blind.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the ante value for the blind.
     * @return The ante value.
     */
    public int getAnte() {
        return ante;
    }
    
    /**
     * Gets the description of the blind.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns whether the blind has been cleared.
     * @return True if cleared, false otherwise.
     */
    public boolean isCleared() {
        return isCleared;
    }
    
    /**
     * Sets the cleared status of the blind.
     * @param cleared True if cleared, false otherwise.
     */
    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }
} 