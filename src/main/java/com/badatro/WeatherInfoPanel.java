package com.badatro;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A floating window that displays weather and time information.
 * This panel shows current time, location, weather conditions, and score multipliers.
 */
public class WeatherInfoPanel {
    private final Stage stage;
    private final Label timeLabel;
    private final Label locationLabel;
    private final Label weatherLabel;
    private final Label multiplierLabel;
    private final WeatherManager weatherManager;

    /**
     * Creates a new WeatherInfoPanel with the specified WeatherManager.
     * Initializes the window and sets up auto-updating of weather information.
     *
     * @param weatherManager The WeatherManager instance to use for weather data
     */
    public WeatherInfoPanel(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
        this.stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);

        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 5;");

        // Title
        Label titleLabel = new Label("Weather & Time Info");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Time
        timeLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: white;");

        // Location
        locationLabel = new Label();
        locationLabel.setStyle("-fx-text-fill: white;");

        // Weather
        weatherLabel = new Label();
        weatherLabel.setStyle("-fx-text-fill: white;");

        // Multiplier
        multiplierLabel = new Label();
        multiplierLabel.setStyle("-fx-text-fill: white;");

        root.getChildren().addAll(titleLabel, timeLabel, locationLabel, weatherLabel, multiplierLabel);

        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);

        // Make the window draggable
        root.setOnMousePressed(event -> {
            root.setOnMouseDragged(e -> {
                stage.setX(e.getScreenX() - event.getSceneX());
                stage.setY(e.getScreenY() - event.getSceneY());
            });
        });

        // Update info every minute
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.minutes(1), e -> updateInfo())
        );
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timeline.play();

        updateInfo();
    }

    /**
     * Shows the weather info panel window.
     * Positions the window in the top-right corner of the primary screen.
     */
    public void show() {
        stage.show();
        // Position in top-right corner
        stage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth() - 20);
        stage.setY(20);
    }

    /**
     * Hides the weather info panel window.
     */
    public void hide() {
        stage.hide();
    }

    /**
     * Updates the displayed weather and time information.
     * Updates the score multiplier display with appropriate color coding.
     * Green for multipliers >= 1.0 (buffs), red for multipliers < 1.0 (debuffs).
     */
    private void updateInfo() {
        double multiplier = weatherManager.getScoreMultiplier();
        String multiplierText = String.format("Score Multiplier: %.2fx", multiplier);
        String multiplierStyle = multiplier >= 1.0 ? 
            "-fx-text-fill: #4CAF50;" : // Green for buffs
            "-fx-text-fill: #F44336;";  // Red for debuffs

        timeLabel.setText("Time: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        locationLabel.setText("Location: " + weatherManager.getLocation());
        weatherLabel.setText(weatherManager.getWeatherInfo());
        multiplierLabel.setText(multiplierText);
        multiplierLabel.setStyle(multiplierStyle);
    }
} 