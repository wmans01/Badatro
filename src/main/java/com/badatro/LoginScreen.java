package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private final Stage stage;
    private final DatabaseManager dbManager;
    private User currentUser;
    
    public LoginScreen(Stage stage) {
        this.stage = stage;
        this.dbManager = new DatabaseManager();
    }
    
    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");
        
        // Title
        Label titleLabel = new Label("Badatro");
        titleLabel.getStyleClass().add("title-label");
        
        // Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);
        
        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("menu-button");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
        
        // Register button
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("menu-button");
        registerButton.setOnAction(e -> handleRegister(usernameField.getText(), passwordField.getText()));
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        
        // Add components to root
        root.getChildren().addAll(
            titleLabel,
            usernameField,
            passwordField,
            loginButton,
            registerButton,
            errorLabel
        );
        
        // Create and show scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    
    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        User user = dbManager.loginUser(username, password);
        if (user != null) {
            currentUser = user;
            startGame();
        } else {
            showError("Invalid username or password");
        }
    }
    
    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        if (dbManager.registerUser(username, password)) {
            showError("Registration successful! Please login.");
        } else {
            showError("Username already exists");
        }
    }
    
    private void showError(String message) {
        Label errorLabel = (Label) stage.getScene().lookup(".error-label");
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }
    
    private void startGame() {
        // Close database connection
        dbManager.close();
        
        // Start the game with the current user
        GameManager gameManager = new GameManager(currentUser, stage);
        BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
        blindSelectionScreen.show();
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
} 