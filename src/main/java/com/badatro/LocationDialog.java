package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A dialog for prompting the user to enter their location, supporting both zip code and city/state formats.
 */
public class LocationDialog {
    private final Stage stage;
    private String location;
    private final ToggleGroup formatGroup;
    private final TextField zipCodeField;
    private final TextField cityField;
    private final ComboBox<String> stateComboBox;

    /**
     * Constructs a new LocationDialog with the given stage.
     * @param parentStage The primary stage for the dialog.
     */
    public LocationDialog(Stage parentStage) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Enter Location");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Format selection
        Label formatLabel = new Label("Select location format:");
        formatGroup = new ToggleGroup();
        RadioButton zipCodeRadio = new RadioButton("Zip Code");
        RadioButton cityStateRadio = new RadioButton("City + State");
        zipCodeRadio.setToggleGroup(formatGroup);
        cityStateRadio.setToggleGroup(formatGroup);
        zipCodeRadio.setSelected(true);

        // Zip code input
        zipCodeField = new TextField();
        zipCodeField.setPromptText("Enter zip code (e.g., 12345)");
        zipCodeField.setMaxWidth(200);

        // City and state inputs
        cityField = new TextField();
        cityField.setPromptText("Enter city name");
        cityField.setMaxWidth(200);
        cityField.setDisable(true);

        stateComboBox = new ComboBox<>();
        stateComboBox.getItems().addAll(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
        );
        stateComboBox.setPromptText("Select state");
        stateComboBox.setMaxWidth(200);
        stateComboBox.setDisable(true);

        // Format change listener
        formatGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isZipCode = newVal == zipCodeRadio;
            zipCodeField.setDisable(!isZipCode);
            cityField.setDisable(isZipCode);
            stateComboBox.setDisable(isZipCode);
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (zipCodeRadio.isSelected()) {
                String zipCode = zipCodeField.getText().trim();
                if (zipCode.matches("\\d{5}")) {
                    location = zipCode;
                    stage.close();
                } else {
                    showError("Please enter a valid 5-digit zip code");
                }
            } else {
                String city = cityField.getText().trim();
                String state = stateComboBox.getValue();
                if (!city.isEmpty() && state != null) {
                    location = city + ", " + state;
                    stage.close();
                } else {
                    showError("Please enter both city and state");
                }
            }
        });

        root.getChildren().addAll(
            formatLabel,
            zipCodeRadio,
            cityStateRadio,
            zipCodeField,
            cityField,
            stateComboBox,
            submitButton
        );

        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows the dialog and waits for user input.
     * @return The entered location, or null if cancelled.
     */
    public String showAndWait() {
        stage.showAndWait();
        return location;
    }

    /**
     * Validates the entered zip code.
     * @param zipCode The zip code to validate.
     * @return True if the zip code is valid, false otherwise.
     */
    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("\\d{5}");
    }

    /**
     * Validates the entered city and state.
     * @param city The city to validate.
     * @param state The state to validate.
     * @return True if the city and state are valid, false otherwise.
     */
    private boolean isValidCityState(String city, String state) {
        return !city.isEmpty() && state != null;
    }
} 