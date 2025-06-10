package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controls the main menu screen and handles navigation to other screens.
 * Manages the display of menu options and the weather info panel.
 */
public class MainMenuController {
    private final GameManager gameManager;
    private final Stage stage;
    private final WeatherInfoPanel weatherInfoPanel;

    /**
     * Creates a new MainMenuController with the specified GameManager and Stage.
     *
     * @param gameManager The GameManager instance to use for game state
     * @param stage The Stage to display the main menu on
     */
    public MainMenuController(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.stage = stage;
        this.weatherInfoPanel = new WeatherInfoPanel(gameManager.getWeatherManager());
    }
    
    /**
     * Shows the main menu screen.
     * Displays the game title and menu options.
     * Shows the weather info panel in a separate window.
     */
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Title
        Text title = new Text("BADATRO");
        title.setStyle("-fx-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");
        
        // Button container
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMaxWidth(300);
        
        Button playButton = createMenuButton("Play");
        playButton.setOnAction(e -> onPlayClick());
        
        Button continueButton = createMenuButton("Continue");
        continueButton.setOnAction(e -> onContinueClick());
        
        Button quitButton = createMenuButton("Quit");
        quitButton.setOnAction(e -> onQuitClick());
        
        buttonContainer.getChildren().addAll(playButton, continueButton, quitButton);
        root.getChildren().addAll(title, buttonContainer);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setTitle("Badatro");
        stage.setScene(scene);
        stage.show();

        // Show weather panel
        weatherInfoPanel.show();
    }

    /**
     * Creates a styled menu button with the specified text.
     *
     * @param text The text to display on the button
     * @return A styled Button instance
     */
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-min-width: 200px;");
        return button;
    }
    
    /**
     * Handles the Play button click event.
     * Hides the weather panel, shows the blind selection screen, and shows the weather panel again.
     */
    private void onPlayClick() {
        weatherInfoPanel.hide();
        BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
        blindSelectionScreen.show();
        weatherInfoPanel.show();
    }
    
    /**
     * Handles the Continue button click event.
     * Currently a placeholder for future implementation.
     */
    private void onContinueClick() {
        // TODO: Implement continue game logic
        System.out.println("Continuing previous game...");
    }
    
    /**
     * Handles the Quit button click event.
     * Hides the weather panel and closes the application.
     */
    private void onQuitClick() {
        weatherInfoPanel.hide();
        stage.close();
    }
} 