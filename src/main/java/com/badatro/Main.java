package com.badatro;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.setResizable(false);
        
        // Start with the login screen
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 