package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuController {
    private final GameManager gameManager;
    private final Stage stage;
    private boolean isLoggedIn = false;
    
    public MainMenuController(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
    }
    
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2C3E50;");
        
        Text title = new Text("BADATRO");
        title.setStyle("-fx-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");
        
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMaxWidth(300);
        
        Button playButton = createMenuButton("Play");
        playButton.setOnAction(e -> onPlayClick());
        
        Button continueButton = createMenuButton("Continue");
        continueButton.setOnAction(e -> onContinueClick());
        
        Button loginButton = createMenuButton("Login");
        loginButton.setOnAction(e -> onLoginClick());
        
        Button quitButton = createMenuButton("Quit");
        quitButton.setOnAction(e -> onQuitClick());
        
        buttonContainer.getChildren().addAll(playButton, continueButton, loginButton, quitButton);
        root.getChildren().addAll(title, buttonContainer);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setTitle("Badatro");
        stage.setScene(scene);
        stage.show();
    }
    
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        return button;
    }
    
    private void onPlayClick() {
        BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
        blindSelectionScreen.show();
    }
    
    private void onContinueClick() {
        // TODO: Implement continue game logic
        System.out.println("Continuing previous game...");
    }
    
    private void onLoginClick() {
        if (!isLoggedIn) {
            // TODO: Implement login logic
            System.out.println("Opening login dialog...");
            isLoggedIn = true;
        } else {
            // TODO: Implement stats view
            System.out.println("Opening stats...");
        }
    }
    
    private void onQuitClick() {
        stage.close();
    }
} 