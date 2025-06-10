package com.badatro;

import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Platform;

/**
 * Manages the overall game state, player progress, blinds, scoring, and weather effects.
 */
public class GameManager {
    private Player player;
    private final List<Blind> blinds;
    private int currentBlindIndex;
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
    private int currentAnte;
    private int currentBlind;
    private int currentMoney;
    private List<Joker> activeJokers;
    private GameOverScreen gameOverScreen;
    private WeatherManager weatherManager;
    
    public enum BlindType {
        SMALL,
        BIG,
        BOSS
    }
    
    /**
     * Initializes the GameManager with the given stage and sets up the game state.
     * @param stage The primary stage for the game.
     */
    public GameManager(Stage stage) {
        this.player = new Player();
        this.blinds = new ArrayList<>();
        this.currentBlindIndex = 0;
        this.stage = stage;
        this.weatherManager = new WeatherManager();
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
        
        // Initialize game state
        this.currentAnte = 1;
        this.currentBlind = 1;
        this.currentMoney = 4;
        
        // Set player state
        player.setMoney(currentMoney);
        player.setAnte(currentAnte);
        player.setCurrentBlindLevel(currentBlind);
        
        // Set initial blind type
        blindType = BlindType.SMALL;
        
        updateBlindTarget();
    }
    
    /**
     * Initializes the list of blinds for the game.
     */
    private void initializeBlinds() {
        // Add blinds in order of difficulty
        blinds.add(new Blind("Small Blind", 1, "Start your journey"));
        blinds.add(new Blind("Big Blind", 2, "Getting serious"));
        blinds.add(new Blind("Boss Blind", 3, "The ultimate challenge"));
    }
    
    /**
     * Shows the info panel window.
     */
    public void showInfoPanel() {
        infoPanel.show();
    }
    
    /**
     * Hides the info panel window.
     */
    public void hideInfoPanel() {
        infoPanel.hide();
    }
    
    /**
     * Updates the info panel with the latest game information.
     */
    public void updateInfoPanel() {
        infoPanel.updateInfo();
    }
    
    /**
     * Updates the chips and multiplier display in the info panel.
     * @param chips The number of chips to display.
     * @param mult The multiplier to display.
     */
    public void updateChipsAndMult(int chips, double mult) {
        currentMultiplier = mult;
        infoPanel.updateChipsAndMult(chips, mult);
    }
    
    /**
     * Sets the current blind type.
     * @param type The blind type to set.
     */
    public void setBlindType(BlindType type) {
        this.blindType = type;
    }
    
    /**
     * Updates the target chips required to clear the current blind.
     */
    public void updateBlindTarget() {
        // Base target increases with ante and blind type
        int baseTarget = getBaseTargetForAnte(player.getCurrentBlindLevel());
        
        // Apply blind type multiplier
        switch (blindType) {
            case SMALL:
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
    
    /**
     * Gets the base target for a given ante level.
     * @param ante The ante level.
     * @return The base target chips for the ante.
     */
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
    
    /**
     * Draws n cards from the deck into the player's hand.
     * @param n The number of cards to draw.
     */
    public void drawCards(int n) {
        for (int i = 0; i < n && !deck.isEmpty(); i++) {
            Card card = deck.drawCard();
            if (card != null) {
                hand.addCard(card);
            }
        }
    }
    
    /**
     * Plays the selected cards, evaluates the score, and updates the game state.
     * @param cardIndices The indices of the cards to play.
     */
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
    
    /**
     * Discards the selected cards and replenishes the hand.
     * @param cardIndices The indices of the cards to discard.
     */
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
    }
    
    /**
     * Handles logic for when a blind is cleared, including rewards and progression.
     */
    private void blindCleared() {
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
        
        // Show shop screen
        shopScreen.show();
    }
    
    private void gameOver(boolean isWin) {
        isGameOver = true;
        
        if (!isWin) {
            // Reset money to 4
            player.setMoney(4);
            
            // Reset blind level to 1
            currentBlind = 1;
            player.setCurrentBlindLevel(1);
            
            // Clear all jokers
            activeJokers.clear();
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
        
        // Evaluate poker hand
        HandEvaluator.HandResult result = HandEvaluator.evaluateHand(cards);
        
        // Calculate base score
        int baseChips = result.getBaseChips();
        double baseMult = result.getBaseMult();
        
        // Add individual card values for all hands
        int cardChips = 0;
        for (Card card : cards) {
            int cardValue = HandEvaluator.calculateCardChips(card);
            cardChips += cardValue;
        }
        baseChips += cardChips;
        
        // Apply active joker effects additively
        double totalMult = baseMult;
        int totalChipBonus = 0;
        
        // First pass: calculate all multipliers additively
        for (Joker joker : activeJokers) {
            if (joker.isActive()) {
                double multBonus = joker.calculateMultBonus(cards);
                totalMult += multBonus;
            }
        }
        
        // Second pass: add all chip bonuses
        for (Joker joker : activeJokers) {
            if (joker.isActive()) {
                int chipBonus = joker.calculateChipBonus(cards);
                totalChipBonus += chipBonus;
            }
        }

        // Apply weather and time multiplier
        totalMult *= weatherManager.getScoreMultiplier();
        
        // Apply multiplier and chip bonus
        int finalScore = (int) Math.round(baseChips * totalMult) + totalChipBonus;
        
        // Update UI
        updateChipsAndMult(baseChips, totalMult);
        
        // Check if game is over (no more playable hands and not enough chips)
        if (player.getPlayableHands() <= 0 && finalScore < currentBlindTarget) {
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
        Platform.runLater(() -> {
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
        
        // Reset game state
        this.currentAnte = 1;
        this.currentBlind = 1;
        this.currentMoney = 4;
        
        // Set player state
        player.setMoney(currentMoney);
        player.setAnte(currentAnte);
        player.setCurrentBlindLevel(currentBlind);
        
        // Clear active jokers
        activeJokers.clear();
        
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
    
    public List<Joker> getActiveJokers() {
        return activeJokers;
    }
    
    public WeatherManager getWeatherManager() {
        return weatherManager;
    }
} 