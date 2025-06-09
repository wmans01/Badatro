package com.badatro;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String passwordHash;
    private int highScore;
    private List<String> unlockedJokers;
    private int totalMoney;
    private int gamesPlayed;
    private int gamesWon;
    
    // Game state
    private int currentAnte;
    private int currentBlind;
    private int currentMoney;
    private List<String> activeJokers;
    
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.highScore = 0;
        this.unlockedJokers = new ArrayList<>();
        this.totalMoney = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        
        // Initialize game state
        this.currentAnte = 1;
        this.currentBlind = 1;
        this.currentMoney = 0;
        this.activeJokers = new ArrayList<>();
    }
    
    // Convert User to MongoDB Document
    public Document toDocument() {
        Document doc = new Document()
            .append("username", username)
            .append("passwordHash", passwordHash)
            .append("highScore", highScore)
            .append("unlockedJokers", unlockedJokers)
            .append("totalMoney", totalMoney)
            .append("gamesPlayed", gamesPlayed)
            .append("gamesWon", gamesWon)
            .append("currentAnte", currentAnte)
            .append("currentBlind", currentBlind)
            .append("currentMoney", currentMoney)
            .append("activeJokers", activeJokers != null ? activeJokers : new ArrayList<>());
            
        // Print saved data for debugging
        System.out.println("Saving user data for " + username + ":");
        System.out.println("Money: " + currentMoney);
        System.out.println("Ante: " + currentAnte);
        System.out.println("Blind: " + currentBlind);
        System.out.println("Active Jokers: " + activeJokers);
        
        return doc;
    }
    
    // Create User from MongoDB Document
    public static User fromDocument(Document doc) {
        if (doc == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        
        User user = new User(
            doc.getString("username"),
            doc.getString("passwordHash")
        );
        
        // Load statistics with defaults
        user.highScore = doc.getInteger("highScore", 0);
        user.unlockedJokers = doc.getList("unlockedJokers", String.class);
        if (user.unlockedJokers == null) {
            user.unlockedJokers = new ArrayList<>();
        }
        user.totalMoney = doc.getInteger("totalMoney", 0);
        user.gamesPlayed = doc.getInteger("gamesPlayed", 0);
        user.gamesWon = doc.getInteger("gamesWon", 0);
        
        // Load game state with defaults
        user.currentAnte = doc.getInteger("currentAnte", 1);
        user.currentBlind = doc.getInteger("currentBlind", 1);
        user.currentMoney = doc.getInteger("currentMoney", 0);
        
        // Load active jokers
        List<String> savedJokers = doc.getList("activeJokers", String.class);
        user.activeJokers = savedJokers != null ? new ArrayList<>(savedJokers) : new ArrayList<>();
        
        // Print loaded data for debugging
        System.out.println("Loaded user data for " + user.username + ":");
        System.out.println("Money: " + user.currentMoney);
        System.out.println("Ante: " + user.currentAnte);
        System.out.println("Blind: " + user.currentBlind);
        System.out.println("Active Jokers: " + user.activeJokers);
        
        return user;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public int getHighScore() {
        return highScore;
    }
    
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    public List<String> getUnlockedJokers() {
        return unlockedJokers;
    }
    
    public void addUnlockedJoker(String jokerName) {
        if (!unlockedJokers.contains(jokerName)) {
            unlockedJokers.add(jokerName);
        }
    }
    
    public int getTotalMoney() {
        return totalMoney;
    }
    
    public void addTotalMoney(int amount) {
        this.totalMoney += amount;
    }
    
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
    
    public int getGamesWon() {
        return gamesWon;
    }
    
    public void incrementGamesWon() {
        this.gamesWon++;
    }
    
    // Game state getters and setters
    public int getCurrentAnte() {
        return currentAnte;
    }
    
    public void setCurrentAnte(int ante) {
        this.currentAnte = ante;
    }
    
    public int getCurrentBlind() {
        return currentBlind;
    }
    
    public void setCurrentBlind(int blind) {
        this.currentBlind = blind;
    }
    
    public int getCurrentMoney() {
        return currentMoney;
    }
    
    public void setCurrentMoney(int money) {
        this.currentMoney = money;
    }
    
    public List<String> getActiveJokers() {
        return activeJokers;
    }
    
    public void setActiveJokers(List<String> jokers) {
        this.activeJokers = new ArrayList<>(jokers);
    }
    
    public void addActiveJoker(String joker) {
        if (!activeJokers.contains(joker)) {
            activeJokers.add(joker);
        }
    }
    
    public void removeActiveJoker(String joker) {
        activeJokers.remove(joker);
    }
    
    public void clearActiveJokers() {
        activeJokers.clear();
    }
} 