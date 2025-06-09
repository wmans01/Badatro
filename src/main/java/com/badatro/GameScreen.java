package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameScreen {
    private final GameManager gameManager;
    private final Stage stage;
    private final List<Integer> selectedCardIndices;
    private final HBox handContainer;
    private final HBox jokersContainer;
    private final Text targetText;
    private final Shop shop;
    private final ShopScreen shopScreen;
    
    public GameScreen(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
        this.selectedCardIndices = new ArrayList<>();
        this.handContainer = new HBox(10);
        this.jokersContainer = new HBox(10);
        this.targetText = new Text();
        this.shopScreen = new ShopScreen(gameManager, stage);
        this.shop = new Shop(gameManager.getPlayer(), gameManager, shopScreen);
    }
    
    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2C3E50;");
        
        // Top section - Target info and active jokers
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);
        
        targetText.setText("Target: " + gameManager.getCurrentBlindTarget());
        targetText.setStyle("-fx-fill: white; -fx-font-size: 24px;");
        
        // Add jokers section
        Text jokersLabel = new Text("Active Jokers:");
        jokersLabel.setStyle("-fx-fill: white; -fx-font-size: 18px;");
        
        jokersContainer.setAlignment(Pos.CENTER);
        jokersContainer.setPadding(new Insets(10));
        updateJokersDisplay();
        
        topSection.getChildren().addAll(targetText, jokersLabel, jokersContainer);
        root.setTop(topSection);
        
        // Center section - Hand
        VBox centerSection = new VBox(20);
        centerSection.setPadding(new Insets(20));
        centerSection.setAlignment(Pos.CENTER);
        
        handContainer.setAlignment(Pos.CENTER);
        updateHandDisplay();
        
        centerSection.getChildren().add(handContainer);
        root.setCenter(centerSection);
        
        // Bottom section - Action buttons
        HBox bottomSection = new HBox(20);
        bottomSection.setPadding(new Insets(20));
        bottomSection.setAlignment(Pos.CENTER);
        
        Button playButton = new Button("Play Selected");
        playButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        playButton.setOnAction(e -> playSelectedCards());
        
        Button discardButton = new Button("Discard Selected");
        discardButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        discardButton.setOnAction(e -> discardSelectedCards());
        
        bottomSection.getChildren().addAll(playButton, discardButton);
        root.setBottom(bottomSection);
        
        // Show info panel
        gameManager.showInfoPanel();
        
        // Initial draw
        gameManager.drawCards(gameManager.getPlayer().getHandSize());
        updateHandDisplay();
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private void updateJokersDisplay() {
        jokersContainer.getChildren().clear();
        
        for (Joker joker : gameManager.getActiveJokers()) {
            ImageView jokerView = new ImageView(new Image(getClass().getResourceAsStream(joker.getImagePath())));
            jokerView.setFitWidth(100);
            jokerView.setPreserveRatio(true);
            
            // Add hover effect
            jokerView.setOnMouseEntered(e -> jokerView.setStyle("-fx-effect: dropshadow(gaussian, #ffd700, 10, 0, 0, 0);"));
            jokerView.setOnMouseExited(e -> jokerView.setStyle(""));
            
            // Add click handler for selling
            jokerView.setOnMouseClicked(e -> shop.showSellPrompt(joker));
            
            jokersContainer.getChildren().add(jokerView);
        }
    }
    
    private void updateHandDisplay() {
        handContainer.getChildren().clear();
        
        List<Card> currentHand = gameManager.getCurrentHand();
        for (int i = 0; i < currentHand.size(); i++) {
            final int index = i;
            Card card = currentHand.get(i);
            ImageView cardView = card.getImageView();
            cardView.setFitWidth(80);  // Adjust size as needed
            cardView.setPreserveRatio(true);
            
            // Add hover effect
            cardView.setOnMouseEntered(e -> {
                if (!card.isSelected()) {
                    cardView.setScaleX(1.1);
                    cardView.setScaleY(1.1);
                }
            });
            
            cardView.setOnMouseExited(e -> {
                if (!card.isSelected()) {
                    cardView.setScaleX(1.0);
                    cardView.setScaleY(1.0);
                }
            });
            
            // Add click handler
            cardView.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    toggleCardSelection(index);
                }
            });
            
            // Show selection state
            if (card.isSelected()) {
                cardView.setStyle("-fx-effect: dropshadow(gaussian, #ffd700, 10, 0, 0, 0);");
                cardView.setScaleX(1.1);
                cardView.setScaleY(1.1);
            } else {
                cardView.setStyle("");
                cardView.setScaleX(1.0);
                cardView.setScaleY(1.0);
            }
            
            handContainer.getChildren().add(cardView);
        }
    }
    
    private void toggleCardSelection(int index) {
        if (selectedCardIndices.contains(index)) {
            selectedCardIndices.remove(Integer.valueOf(index));
            gameManager.getHand().getCard(index).setSelected(false);
        } else if (selectedCardIndices.size() < 5) {
            selectedCardIndices.add(index);
            gameManager.getHand().getCard(index).setSelected(true);
        }
        updateHandDisplay();
    }
    
    private void playSelectedCards() {
        if (!selectedCardIndices.isEmpty()) {
            gameManager.playCards(new ArrayList<>(selectedCardIndices));
            selectedCardIndices.clear();
            // Clear selection state of all cards
            for (Card card : gameManager.getCurrentHand()) {
                card.setSelected(false);
            }
            updateGameState();
        }
    }
    
    private void discardSelectedCards() {
        if (gameManager.getPlayer().getDiscardableHands() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Discards Remaining");
            alert.setHeaderText(null);
            alert.setContentText("You have no discards remaining for this blind.");
            alert.showAndWait();
            return;
        }
        
        if (!selectedCardIndices.isEmpty()) {
            gameManager.discardCards(new ArrayList<>(selectedCardIndices));
            selectedCardIndices.clear();
            // Clear selection state of all cards
            for (Card card : gameManager.getCurrentHand()) {
                card.setSelected(false);
            }
            updateGameState();
        }
    }
    
    private void updateGameState() {
        targetText.setText("Target: " + gameManager.getCurrentBlindTarget());
        gameManager.updateInfoPanel();
        updateHandDisplay();
        updateJokersDisplay();
        
        if (gameManager.isGameOver()) {
            gameManager.hideInfoPanel();
            GameOverScreen gameOverScreen = new GameOverScreen(gameManager, stage, gameManager.getCurrentScore() >= gameManager.getCurrentBlindTarget());
            gameOverScreen.show();
        }
    }
} 