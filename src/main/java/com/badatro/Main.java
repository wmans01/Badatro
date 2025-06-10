package com.badatro;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main application class that initializes and starts the game.
 */
public class Main extends Application {
    /**
     * Initializes and displays the main application window.
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.setResizable(false);
        
        // Start with the main menu
        GameManager gameManager = new GameManager(stage);
        
        // Prompt for location
        LocationDialog locationDialog = new LocationDialog(stage);
        String location = locationDialog.showAndWait();
        if (location != null && !location.isEmpty()) {
            gameManager.getWeatherManager().setLocation(location);
        }
        
        MainMenuController mainMenu = new MainMenuController(gameManager, stage);
        mainMenu.show();
    }

    /**
     * The main entry point for the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }
} 