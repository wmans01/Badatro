package com.badatro;

public class Blind {
    private final String name;
    private final int ante;
    private final String description;
    private boolean isCleared;
    
    public Blind(String name, int ante, String description) {
        this.name = name;
        this.ante = ante;
        this.description = description;
        this.isCleared = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAnte() {
        return ante;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCleared() {
        return isCleared;
    }
    
    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }
} 