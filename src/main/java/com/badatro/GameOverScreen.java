package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOverScreen {
    private final GameManager gameManager;
    private final Stage stage;
    private final boolean isWin;
    
    public GameOverScreen(GameManager gameManager, Stage stage, boolean isWin) {
        this.gameManager = gameManager;
        this.stage = stage;
        this.isWin = isWin;
    }
    
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2C3E50;");
        
        Text gameOverText = new Text(isWin ? "Victory!" : "Game Over");
        gameOverText.setStyle("-fx-fill: " + (isWin ? "#27ae60" : "#e74c3c") + "; -fx-font-size: 48px; -fx-font-weight: bold;");
        
        Text statsText = new Text(String.format(
            "Final Score: %d\n" +
            "Blind Level: %d\n" +
            "Ante: %d",
            gameManager.getCurrentScore(),
            gameManager.getPlayer().getCurrentBlindLevel(),
            gameManager.getPlayer().getAnte()
        ));
        statsText.setStyle("-fx-fill: white; -fx-font-size: 24px;");
        
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMaxWidth(300);
        
        Button playAgainButton = createMenuButton("Play Again");
        playAgainButton.setOnAction(e -> onPlayAgainClick());
        
        Button mainMenuButton = createMenuButton("Main Menu");
        mainMenuButton.setOnAction(e -> onMainMenuClick());
        
        buttonContainer.getChildren().addAll(playAgainButton, mainMenuButton);
        
        root.getChildren().addAll(gameOverText, statsText, buttonContainer);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        return button;
    }
    
    private void onPlayAgainClick() {
        gameManager.reset();
        BlindSelectionScreen blindSelection = new BlindSelectionScreen(gameManager, stage);
        blindSelection.show();
    }
    
    private void onMainMenuClick() {
        gameManager.reset();
        MainMenuController mainMenu = new MainMenuController(gameManager, stage);
        mainMenu.show();
    }
} 