package com.badatro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InfoPanel {
    private final GameManager gameManager;
    private final Stage infoStage;
    private final Text blindText;
    private final Text moneyText;
    private final Text anteText;
    private final Text playableHandsText;
    private final Text discardsText;
    private final Text chipsText;
    private final Text multText;
    
    public InfoPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.infoStage = new Stage();
        this.blindText = new Text();
        this.moneyText = new Text();
        this.anteText = new Text();
        this.playableHandsText = new Text();
        this.discardsText = new Text();
        this.chipsText = new Text();
        this.multText = new Text();
        
        setupStage();
    }
    
    private void setupStage() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #2C3E50;");
        
        // Style for all text elements
        String textStyle = "-fx-fill: white; -fx-font-size: 16px;";
        blindText.setStyle(textStyle);
        moneyText.setStyle(textStyle);
        anteText.setStyle(textStyle);
        playableHandsText.setStyle(textStyle);
        discardsText.setStyle(textStyle);
        chipsText.setStyle(textStyle);
        multText.setStyle(textStyle);
        
        root.getChildren().addAll(
            blindText,
            moneyText,
            anteText,
            playableHandsText,
            discardsText,
            chipsText,
            multText
        );
        
        Scene scene = new Scene(root, 200, 300);
        scene.getStylesheets().add(getClass().getResource("/com/badatro/styles.css").toExternalForm());
        
        infoStage.setScene(scene);
        infoStage.initStyle(StageStyle.UTILITY); // Removes window decorations
        infoStage.setAlwaysOnTop(true);
        infoStage.setResizable(false);
    }
    
    public void show() {
        updateInfo();
        infoStage.show();
    }
    
    public void hide() {
        infoStage.hide();
    }
    
    public void updateInfo() {
        blindText.setText("Blind: " + gameManager.getBlindType());
        moneyText.setText("Money: " + gameManager.getPlayer().getMoney());
        anteText.setText("Ante: " + gameManager.getPlayer().getAnte());
        playableHandsText.setText("Playable Hands: " + gameManager.getPlayer().getPlayableHands());
        discardsText.setText("Discards: " + gameManager.getPlayer().getDiscardableHands());
        chipsText.setText("Chips: " + gameManager.getCurrentScore());
        multText.setText("Mult: 0"); // TODO: Add multiplier tracking to GameManager
    }
    
    public void updateChipsAndMult(int chips, double mult) {
        chipsText.setText("Chips: " + chips);
        multText.setText("Mult: " + String.format("%.1f", mult));
    }
} 