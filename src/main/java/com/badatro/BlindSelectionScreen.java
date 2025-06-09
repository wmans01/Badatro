package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BlindSelectionScreen {
    private final GameManager gameManager;
    private final Stage stage;
    
    public BlindSelectionScreen(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
    }
    
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2C3E50;");
        
        Text title = new Text("Select a Blind");
        title.setStyle("-fx-fill: white; -fx-font-size: 24px;");
        
        VBox blindButtons = new VBox(10);
        blindButtons.setAlignment(Pos.CENTER);
        
        // Small Blind Button
        Button smallBlind = createBlindButton("Small Blind", GameManager.BlindType.SMALL);
        updateButtonState(smallBlind, GameManager.BlindType.SMALL);
        
        // Big Blind Button
        Button bigBlind = createBlindButton("Big Blind", GameManager.BlindType.BIG);
        updateButtonState(bigBlind, GameManager.BlindType.BIG);
        
        // Boss Blind Button
        Button bossBlind = createBlindButton("Boss Blind", GameManager.BlindType.BOSS);
        updateButtonState(bossBlind, GameManager.BlindType.BOSS);
        
        blindButtons.getChildren().addAll(smallBlind, bigBlind, bossBlind);
        
        root.getChildren().addAll(title, blindButtons);
        
        // Show info panel
        gameManager.showInfoPanel();
        
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private void updateButtonState(Button button, GameManager.BlindType blindType) {
        boolean canSelect = gameManager.canSelectBlind(blindType);
        button.setDisable(!canSelect);
        
        if (canSelect) {
            button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        } else {
            button.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: #7f8c8d; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        }
    }
    
    private Button createBlindButton(String text, GameManager.BlindType blindType) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        
        button.setOnAction(e -> {
            if (gameManager.canSelectBlind(blindType)) {
                gameManager.startNewBlind(blindType);
                startGame();
            }
        });
        
        return button;
    }
    
    private void startGame() {
        GameScreen gameScreen = new GameScreen(gameManager, stage);
        gameScreen.show();
    }
} 