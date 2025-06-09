package com.badatro;

import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Platform;

public class GameManager {
    private Player player;
    private final User user;
    private final List<Blind> blinds;
    private int currentBlindIndex;
    private final DatabaseManager dbManager;
    private Deck deck;
    private Hand hand;
    private ToBeScored toBeScored;
    private DiscardPile discardPile;
    private int currentBlindTarget;    // Chips required to clear this blind
    private int roundNumber;
    private BlindType blindType;       // SMALL, BIG, BOSS
    private boolean isGameOver;
    private Stage stage;
    private InfoPanel infoPanel;
    private double currentMultiplier;
    private int currentScore;
    private boolean smallBlindCompleted;
    private boolean bigBlindCompleted;
    private boolean bossBlindCompleted;
    private ShopScreen shopScreen;
    private User currentUser;
    private int currentAnte;
    private int currentBlind;
    private int currentMoney;
    private List<Joker> activeJokers;
    private GameOverScreen gameOverScreen;
    
    public enum BlindType {
        SMALL,
        BIG,
        BOSS
    }
    
    public GameManager(User user, Stage stage) {
        this.user = user;
        this.currentUser = user;  // Initialize currentUser
        this.player = new Player();
        this.blinds = new ArrayList<>();
        this.currentBlindIndex = 0;
        this.dbManager = new DatabaseManager();
        this.stage = stage;
        initializeBlinds();
        this.deck = new Deck();
        this.hand = new Hand();
        this.discardPile = new DiscardPile();
        this.toBeScored = new ToBeScored();
        this.roundNumber = 1;
        this.isGameOver = false;
        this.currentScore = 0;
        this.currentMultiplier = 0.0;
        this.infoPanel = new InfoPanel(this);
        this.smallBlindCompleted = false;
        this.bigBlindCompleted = false;
        this.bossBlindCompleted = false;
        this.shopScreen = new ShopScreen(this, stage);
        this.activeJokers = new ArrayList<Joker>();
        this.gameOverScreen = new GameOverScreen(this, stage, false);
        
        // Initialize game state from user
        this.currentAnte = user.getCurrentAnte();
        this.currentBlind = user.getCurrentBlind();
        this.currentMoney = user.getCurrentMoney();
        
        // Set player state from saved data
        player.setMoney(currentMoney);
        player.setAnte(currentAnte);
        player.setCurrentBlindLevel(currentBlind);
        
        // Set blind type based on saved state
        if (currentBlind % 3 == 0) {
            blindType = BlindType.BIG;
        } else if (currentBlind % 3 == 1) {
            blindType = BlindType.SMALL;
        } else {
            blindType = BlindType.BOSS;
        }
        
        // Load active jokers
        for (String jokerName : user.getActiveJokers()) {
            Joker joker = createJoker(jokerName);
            if (joker != null) {
                activeJokers.add(joker);
            }
        }
        
        updateBlindTarget();
    }
    
    private void initializeBlinds() {
        // Add blinds in order of difficulty
        blinds.add(new Blind("Small Blind", 1, "Start your journey"));
        blinds.add(new Blind("Big Blind", 2, "Getting serious"));
        blinds.add(new Blind("The Flop", 3, "Three cards to start"));
        blinds.add(new Blind("The Turn", 4, "Fourth card changes everything"));
        blinds.add(new Blind("The River", 5, "Final card, make it count"));
        blinds.add(new Blind("The Showdown", 6, "Time to prove yourself"));
        blinds.add(new Blind("The Champion", 7, "Only the best survive"));
        blinds.add(new Blind("The Legend", 8, "Become a legend"));
        blinds.add(new Blind("The Myth", 9, "Beyond legendary"));
        blinds.add(new Blind("The God", 10, "Ascend to godhood"));
    }
    
    public void showInfoPanel() {
        infoPanel.show();
    }
    
    public void hideInfoPanel() {
        infoPanel.hide();
    }
    
    public void updateInfoPanel() {
        infoPanel.updateInfo();
    }
    
    public void updateChipsAndMult(int chips, double mult) {
        currentMultiplier = mult;
        infoPanel.updateChipsAndMult(chips, mult);
    }
    
    public void setBlindType(BlindType type) {
        this.blindType = type;
    }
    
    public void updateBlindTarget() {
        // Base target increases with ante and blind type
        int baseTarget = getBaseTargetForAnte(player.getCurrentBlindLevel());
        
        // Apply blind type multiplier
        switch (blindType) {
            case SMALL:
                baseTarget = baseTarget;  // 1x
                break;
            case BIG:
                baseTarget = (int) (baseTarget * 1.5);  // 1.5x
                break;
            case BOSS:
                baseTarget = baseTarget * 2;  // 2x
                break;
        }
        
        currentBlindTarget = baseTarget;
    }
    
    private int getBaseTargetForAnte(int ante) {
        switch (ante) {
            case 1: return 300;
            case 2: return 800;
            case 3: return 2000;
            case 4: return 5000;
            case 5: return 11000;
            case 6: return 20000;
            case 7: return 35000;
            case 8: return 50000;
            default: return 300;
        }
    }
    
    public void drawCards(int n) {
        for (int i = 0; i < n && !deck.isEmpty(); i++) {
            Card card = deck.drawCard();
            if (card != null) {
                hand.addCard(card);
            }
        }
    }
    
    public void playCards(List<Integer> cardIndices) {
        if (cardIndices.isEmpty() || player.getPlayableHands() <= 0) {
            return;
        }
        
        // Limit to 5 cards
        if (cardIndices.size() > 5) {
            cardIndices = cardIndices.subList(0, 5);
        }
        
        // Move selected cards to toBeScored
        List<Card> cards = new ArrayList<>();
        for (int index : cardIndices) {
            cards.add(hand.getCard(index));
        }
        toBeScored.addCards(cards);
        
        // Remove cards from hand
        hand.removeCards(cardIndices);
        
        // Evaluate score
        int score = evaluateScore();
        
        // Clear scored cards after evaluation
        toBeScored.clear();
        
        // Update current score
        currentScore += score;
        
        // Decrease playable hands
        player.setPlayableHands(player.getPlayableHands() - 1);
        
        // Draw new cards to replace played ones
        drawCards(cards.size());
        
        // Check if blind is cleared
        if (checkBlindCleared(currentScore)) {
            blindCleared();
        } else {
            // Check for game over after playing cards
            if (player.getPlayableHands() <= 0) {
                Platform.runLater(() -> {
                    hideInfoPanel();
                    gameOver(false);
                });
            }
        }
        
        // Update UI
        updateInfoPanel();
    }
    
    public void discardCards(List<Integer> cardIndices) {
        if (cardIndices.isEmpty() || player.getDiscardableHands() <= 0) {
            return;
        }
        
        // Move selected cards to discard pile
        List<Card> selectedCards = new ArrayList<>();
        for (int index : cardIndices) {
            selectedCards.add(hand.getCard(index));
        }
        discardPile.addCards(selectedCards);
        
        // Remove cards from hand
        hand.removeCards(cardIndices);
        
        // Decrease discardable hands counter
        player.setDiscardableHands(player.getDiscardableHands() - 1);
        
        // Replenish hand
        drawCards(selectedCards.size());
        
        // Save player data
        currentUser.setCurrentMoney(player.getMoney());
        currentUser.setCurrentAnte(player.getAnte());
        currentUser.setCurrentBlind(player.getCurrentBlindLevel());
        dbManager.updateUser(currentUser);
    }
    
    private void blindCleared() {
        try {
            // Calculate money rewards
            int baseReward = switch (blindType) {
                case SMALL -> 3;
                case BIG -> 4;
                case BOSS -> 5;
            };
            
            // Add money for remaining hands
            int remainingHandsReward = player.getPlayableHands() + player.getDiscardableHands();
            int totalReward = baseReward + remainingHandsReward;
            
            // Calculate interest (1$ per 5$, capped at 5)
            int currentMoney = player.getMoney();
            int interest = Math.min(5, currentMoney / 5);
            totalReward += interest;
            
            // Add the reward to player's money
            player.setMoney(currentMoney + totalReward);
            
            // Mark current blind as completed
            switch (blindType) {
                case SMALL:
                    smallBlindCompleted = true;
                    break;
                case BIG:
                    bigBlindCompleted = true;
                    break;
                case BOSS:
                    bossBlindCompleted = true;
                    // Increment ante and reset blind completion status
                    player.setCurrentBlindLevel(player.getCurrentBlindLevel() + 1);
                    player.setAnte(player.getAnte() + 1);
                    smallBlindCompleted = false;
                    bigBlindCompleted = false;
                    bossBlindCompleted = false;
                    break;
            }
            
            // Update user's game state
            currentUser.setCurrentAnte(player.getAnte());
            currentUser.setCurrentBlind(player.getCurrentBlindLevel());
            currentUser.setCurrentMoney(player.getMoney());
            
            // Save active jokers
            List<String> activeJokerNames = new ArrayList<>();
            for (Joker joker : activeJokers) {
                activeJokerNames.add(joker.getName());
            }
            currentUser.setActiveJokers(activeJokerNames);
            
            // Save to database
            dbManager.updateUser(currentUser);
            
            // Show shop screen
            shopScreen.show();
        } catch (Exception e) {
            System.err.println("Error in blindCleared: " + e.getMessage());
            e.printStackTrace();
            // Try to save one more time
            try {
                dbManager.updateUser(currentUser);
            } catch (Exception ex) {
                System.err.println("Failed to save user data after error: " + ex.getMessage());
            }
        }
    }
    
    private void gameOver(boolean isWin) {
        isGameOver = true;
        
        if (!isWin) {
            // Reset money to 4
            player.setMoney(4);
            currentUser.setCurrentMoney(4);
            
            // Reset blind level to 1
            currentBlind = 1;
            player.setCurrentBlindLevel(1);
            currentUser.setCurrentBlind(1);
            
            // Clear all jokers
            activeJokers.clear();
            currentUser.setActiveJokers(new ArrayList<>());
            
            // Save the reset state
            try {
                dbManager.updateUser(currentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Hide info panel
        hideInfoPanel();
        
        // Create and show game over screen
        Platform.runLater(() -> {
            GameOverScreen gameOverScreen = new GameOverScreen(this, stage, isWin);
            gameOverScreen.show();
        });
    }
    
    public int evaluateScore() {
        List<Card> cards = toBeScored.getCards();
        if (cards.isEmpty()) {
            return 0;
        }
        
        System.out.println("\n=== Score Calculation ===");
        System.out.println("Cards played:");
        for (Card card : cards) {
            System.out.println("- " + card.getRank() + " of " + card.getSuit());
        }
        
        // Evaluate poker hand
        HandEvaluator.HandResult result = HandEvaluator.evaluateHand(cards);
        System.out.println("\nPoker hand: " + result.getHandType());
        
        // Calculate base score
        int baseChips = result.getBaseChips();
        double baseMult = result.getBaseMult();
        System.out.println("Base chips: " + baseChips);
        System.out.println("Base multiplier: " + baseMult);
        
        // Add individual card values for all hands
        int cardChips = 0;
        for (Card card : cards) {
            int cardValue = HandEvaluator.calculateCardChips(card);
            cardChips += cardValue;
            System.out.println(card.getRank() + " of " + card.getSuit() + " adds " + cardValue + " chips");
        }
        baseChips += cardChips;
        System.out.println("Total card chips: " + cardChips);
        System.out.println("Total base chips: " + baseChips);
        
        // Apply active joker effects additively
        double totalMult = baseMult;
        int totalChipBonus = 0;
        
        System.out.println("\nActive Jokers:");
        if (activeJokers.isEmpty()) {
            System.out.println("No active jokers");
        }
        
        // First pass: calculate all multipliers additively
        for (Joker joker : activeJokers) {
            if (joker.isActive()) {
                double multBonus = joker.calculateMultBonus(cards);
                totalMult += multBonus;
                System.out.println(joker.getName() + " adds " + multBonus + "x multiplier");
            }
        }
        
        // Second pass: add all chip bonuses
        for (Joker joker : activeJokers) {
            if (joker.isActive()) {
                int chipBonus = joker.calculateChipBonus(cards);
                totalChipBonus += chipBonus;
                System.out.println(joker.getName() + " adds " + chipBonus + " chips");
            }
        }
        
        // Apply multiplier and chip bonus
        int finalScore = (int) Math.round(baseChips * totalMult) + totalChipBonus;
        
        System.out.println("\nFinal Calculation:");
        System.out.println("Base chips: " + baseChips);
        System.out.println("Total multiplier: " + totalMult);
        System.out.println("Total chip bonus: " + totalChipBonus);
        System.out.println("Final score: " + finalScore);
        System.out.println("===================\n");
        
        // Update UI
        updateChipsAndMult(baseChips, totalMult);
        
        // Check if game is over (no more playable hands and not enough chips)
        if (player.getPlayableHands() <= 0 && finalScore < currentBlindTarget) {
            System.out.println("Game Over - Not enough chips to clear blind!");
            gameOver(false);
        }
        
        return finalScore;
    }
    
    public void advanceBlind() {
        switch (blindType) {
            case SMALL:
                blindType = BlindType.BIG;
                break;
            case BIG:
                blindType = BlindType.BOSS;
                break;
            case BOSS:
                // Move to next ante
                player.setCurrentBlindLevel(player.getCurrentBlindLevel() + 1);
                player.setAnte(player.getAnte() + 1);  // Increase ante by 1
                blindType = BlindType.SMALL;
                break;
        }
        roundNumber++;
        updateBlindTarget();
        
        // Reset playable and discardable hands
        player.setPlayableHands(3);
        player.setDiscardableHands(3);
        
        // Reset score for new blind
        currentScore = 0;
    }
    
    public boolean checkBlindCleared(int score) {
        return score >= currentBlindTarget;
    }
    
    public void endGame() {
        System.out.println("DEBUG: End Game called");
        Platform.runLater(() -> {
            System.out.println("DEBUG: Inside Platform.runLater for endGame");
            gameOver(false);
        });
    }
    
    public void reset() {
        this.player = new Player();
        this.deck = new Deck();
        this.hand = new Hand();
        this.discardPile = new DiscardPile();
        this.toBeScored = new ToBeScored();
        this.roundNumber = 1;
        this.blindType = BlindType.SMALL;
        this.isGameOver = false;
        this.currentScore = 0;
        this.currentMultiplier = 0.0;
        this.infoPanel = new InfoPanel(this);
        this.smallBlindCompleted = false;
        this.bigBlindCompleted = false;
        this.bossBlindCompleted = false;
        this.shopScreen = new ShopScreen(this, stage);
        this.gameOverScreen = new GameOverScreen(this, stage, false);
        
        // Restore active jokers
        activeJokers.clear();
        for (String jokerName : currentUser.getActiveJokers()) {
            Joker joker = createJoker(jokerName);
            if (joker != null) {
                activeJokers.add(joker);
                System.out.println("Restored joker after reset: " + jokerName);
            }
        }
        
        updateBlindTarget();
    }
    
    public boolean canSelectBlind(BlindType type) {
        // If no blinds are completed, only allow SMALL
        if (!smallBlindCompleted && !bigBlindCompleted && !bossBlindCompleted) {
            return type == BlindType.SMALL;
        }
        
        // If small blind is completed, only allow BIG
        if (smallBlindCompleted && !bigBlindCompleted && !bossBlindCompleted) {
            return type == BlindType.BIG;
        }
        
        // If big blind is completed, only allow BOSS
        if (smallBlindCompleted && bigBlindCompleted && !bossBlindCompleted) {
            return type == BlindType.BOSS;
        }
        
        // If all blinds are completed, allow any blind
        return true;
    }
    
    public void startNewBlind(BlindType type) {
        // Check if this blind can be selected
        if (!canSelectBlind(type)) {
            System.out.println("Cannot select " + type + " blind yet. Complete the previous blind first.");
            return;
        }
        
        // Reset game state for new blind
        deck = new Deck();
        hand = new Hand();
        discardPile = new DiscardPile();
        toBeScored = new ToBeScored();
        currentScore = 0;
        currentMultiplier = 1;
        player.setPlayableHands(3);
        player.setDiscardableHands(3);
        
        // Set blind type and update target
        blindType = type;
        updateBlindTarget();
        
        // Restore active jokers from user state
        activeJokers.clear();
        for (String jokerName : currentUser.getActiveJokers()) {
            Joker joker = createJoker(jokerName);
            if (joker != null) {
                activeJokers.add(joker);
                System.out.println("Restored joker for new blind: " + jokerName);
            }
        }
        
        // Update UI
        updateInfoPanel();
    }
    
    public boolean isBlindCompleted(BlindType type) {
        switch (type) {
            case SMALL:
                return smallBlindCompleted;
            case BIG:
                return bigBlindCompleted;
            case BOSS:
                return bossBlindCompleted;
            default:
                return false;
        }
    }
    
    // Getters
    public Player getPlayer() {
        return player;
    }
    
    public Deck getDeck() {
        return deck;
    }
    
    public Hand getHand() {
        return hand;
    }
    
    public ToBeScored getToBeScored() {
        return toBeScored;
    }
    
    public DiscardPile getDiscardPile() {
        return discardPile;
    }
    
    public int getCurrentBlindTarget() {
        return currentBlindTarget;
    }
    
    public int getCurrentScore() {
        return currentScore;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
    
    public BlindType getBlindType() {
        return blindType;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public List<Card> getCurrentHand() {
        return hand.getCards();
    }
    
    public void updateUserStats() {
        // Update user statistics
        user.setHighScore(Math.max(user.getHighScore(), player.getMoney()));
        user.addTotalMoney(player.getMoney());
        user.incrementGamesPlayed();
        if (currentBlindIndex >= blinds.size()) {
            user.incrementGamesWon();
        }
        
        // Save to database
        dbManager.updateUser(user);
    }
    
    public User getUser() {
        return user;
    }
    
    public void close() {
        dbManager.close();
    }
    
    public void startNewGame(User user) {
        this.currentUser = user;
        this.player = new Player();
        this.deck = new Deck();
        this.hand = new Hand();
        this.discardPile = new DiscardPile();
        this.toBeScored = new ToBeScored();
        this.roundNumber = 1;
        this.isGameOver = false;
        this.currentScore = 0;
        this.currentMultiplier = 1;
        player.setPlayableHands(3);
        player.setDiscardableHands(3);
        
        // Set blind type and update target
        this.blindType = BlindType.SMALL;  // Start with SMALL blind (0)
        this.currentBlind = 0;
        player.setCurrentBlindLevel(0);
        updateBlindTarget();
        
        // Load active jokers
        activeJokers.clear();
        for (String jokerName : user.getActiveJokers()) {
            Joker joker = createJoker(jokerName);
            if (joker != null) {
                activeJokers.add(joker);
            }
        }
        
        // Update UI
        updateInfoPanel();
    }
    
    private Joker createJoker(String name) {
        Joker joker = switch (name) {
            case "Wrathful Joker" -> Joker.createWrathful(this);
            case "Greedy Joker" -> Joker.createGreedy(this);
            case "Jolly Joker" -> Joker.createJolly(this);
            case "Droll Joker" -> Joker.createDroll(this);
            default -> null;
        };
        
        if (joker != null) {
            joker.setActive(true);
        }
        return joker;
    }
    
    private void handleBlindComplete() {
        // Update user's game state
        currentUser.setCurrentAnte(currentAnte);
        currentUser.setCurrentBlind(currentBlind);
        currentUser.setCurrentMoney(currentMoney);
        
        // Save active jokers
        List<String> activeJokerNames = new ArrayList<>();
        for (Joker joker : activeJokers) {
            activeJokerNames.add(joker.getName());
        }
        currentUser.setActiveJokers(activeJokerNames);
        
        // Save to database
        dbManager.updateUser(currentUser);
        
        // Show game over screen
        gameOverScreen.show();
    }
    
    private void updateUI() {
        // Update UI elements with current game state
        if (infoPanel != null) {
            infoPanel.updateInfo();
            infoPanel.updateChipsAndMult(currentScore, currentMultiplier);
        }
    }
    
    public List<Joker> getActiveJokers() {
        return activeJokers;
    }
} 