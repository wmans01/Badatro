package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Represents the shop screen where players can interact with the shop.
 */
public class ShopScreen {
    private final GameManager gameManager;
    private final Stage stage;
    private final Shop shop;
    private Scene scene;
    private BorderPane root;
    
    /**
     * Constructs a ShopScreen with the given GameManager and Stage.
     * @param gameManager The GameManager instance.
     * @param stage The primary stage.
     */
    public ShopScreen(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
        this.shop = new Shop(gameManager.getPlayer(), gameManager, this);
    }
    
    /**
     * Shows the shop screen UI.
     */
    public void show() {
        if (root == null) {
            root = new BorderPane();
            root.setStyle("-fx-background-color: #2C3E50;");
            
            // Top section - Money display
            VBox topSection = new VBox(10);
            topSection.setPadding(new Insets(20));
            topSection.setAlignment(Pos.CENTER);
            
            Button backButton = new Button("Back to Game");
            backButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> {
                shop.hide();
                BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
                blindSelectionScreen.show();
            });
            
            topSection.getChildren().add(backButton);
            root.setTop(topSection);
        }
        
        if (scene == null) {
            scene = new Scene(root, 800, 600);
        }
        
        stage.setScene(scene);
        shop.show();
    }
} 