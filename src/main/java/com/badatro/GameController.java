package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controls the main game screen and manages the game state.
 * Handles the display of game elements and navigation between screens.
 */
public class GameController {
    private final GameManager gameManager;
    private final Stage stage;
    private final WeatherInfoPanel weatherInfoPanel;

    /**
     * Creates a new GameController with the specified GameManager and Stage.
     *
     * @param gameManager The GameManager instance to use for game state
     * @param stage The Stage to display the game screen on
     */
    public GameController(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
        this.weatherInfoPanel = new WeatherInfoPanel(gameManager.getWeatherManager());
    }

    /**
     * Shows the game screen.
     * Displays the game interface and shows the weather info panel.
     * Sets up the back button to return to the main menu.
     */
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        backButton.setOnAction(e -> {
            weatherInfoPanel.hide();
            MainMenuController mainMenu = new MainMenuController(gameManager, stage);
            mainMenu.show();
        });

        root.getChildren().add(backButton);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Show weather panel
        weatherInfoPanel.show();
    }
} 