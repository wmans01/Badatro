package com.badatro;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import java.util.*;

/**
 * Represents the shop where players can buy and sell jokers.
 */
public class Shop {
    private final Player player;
    private final GameManager gameManager;
    private final ShopScreen shopScreen;
    private final Stage stage;
    private final VBox shopContainer;
    private final List<ShopItem> availableItems;
    private Scene scene;
    private Label moneyLabel;
    
    /**
     * Constructs a Shop with the given player, GameManager, and ShopScreen.
     * @param player The player.
     * @param gameManager The GameManager instance.
     * @param shopScreen The ShopScreen instance.
     */
    public Shop(Player player, GameManager gameManager, ShopScreen shopScreen) {
        this.player = player;
        this.gameManager = gameManager;
        this.shopScreen = shopScreen;
        this.stage = new Stage();
        this.shopContainer = new VBox(20);
        this.availableItems = new ArrayList<>();
        
        initializeItems();
        setupShopUI();
    }
    
    /**
     * Initializes the available items in the shop.
     */
    private void initializeItems() {
        // Add all jokers to a list
        List<Joker> allJokers = new ArrayList<>();
        allJokers.add(Joker.createJoker(gameManager));
        allJokers.add(Joker.createGreedy(gameManager));
        allJokers.add(Joker.createLusty(gameManager));
        allJokers.add(Joker.createWrathful(gameManager));
        allJokers.add(Joker.createGluttonous(gameManager));
        allJokers.add(Joker.createJolly(gameManager));
        allJokers.add(Joker.createDroll(gameManager));
        allJokers.add(Joker.createSly(gameManager));
        allJokers.add(Joker.createBanner(gameManager));
        allJokers.add(Joker.createMystic(gameManager));
        
        // Shuffle the list
        Collections.shuffle(allJokers);
        
        // Clear previous items
        availableItems.clear();
        
        // Add only 2 random jokers to available items
        availableItems.addAll(allJokers.subList(0, 2));
    }
    
    /**
     * Sets up the shop UI.
     */
    private void setupShopUI() {
        shopContainer.setPadding(new Insets(20));
        shopContainer.setAlignment(Pos.CENTER);
        shopContainer.setStyle("-fx-background-color: #2C3E50;");
        
        // Title
        Label titleLabel = new Label("Shop");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        
        // Money display
        moneyLabel = new Label("Money: $" + player.getMoney());
        moneyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        
        // Return button
        Button returnButton = new Button("Return to Blind Selection");
        returnButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        returnButton.setOnAction(e -> {
            hide();
            BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
            blindSelectionScreen.show();
        });
        
        // Items container
        VBox itemsContainer = new VBox(10);
        itemsContainer.setAlignment(Pos.CENTER);
        
        for (ShopItem item : availableItems) {
            HBox itemBox = createItemBox(item);
            itemsContainer.getChildren().add(itemBox);
        }
        
        shopContainer.getChildren().addAll(titleLabel, moneyLabel, itemsContainer, returnButton);
    }
    
    /**
     * Creates a box for displaying a shop item.
     * @param item The shop item to display.
     * @return The HBox containing the item display.
     */
    private HBox createItemBox(ShopItem item) {
        HBox itemBox = new HBox(20);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-background-color: #34495E; -fx-background-radius: 5;");
        
        ImageView itemImage = new ImageView(new Image(getClass().getResourceAsStream(item.getImagePath())));
        itemImage.setFitWidth(50);
        itemImage.setFitHeight(50);
        
        VBox itemInfo = new VBox(5);
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label descLabel = new Label(item.getDescription());
        descLabel.setStyle("-fx-text-fill: #BDC3C7;");
        Label costLabel = new Label("Cost: $" + item.getCost());
        costLabel.setStyle("-fx-text-fill: #2ECC71;");
        
        itemInfo.getChildren().addAll(nameLabel, descLabel, costLabel);
        
        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");
        buyButton.setOnAction(e -> {
            if (player.getMoney() >= item.getCost()) {
                player.setMoney(player.getMoney() - item.getCost());
                if (item instanceof Joker) {
                    player.addJoker((Joker) item);
                }
                hide();
                BlindSelectionScreen blindSelectionScreen = new BlindSelectionScreen(gameManager, stage);
                blindSelectionScreen.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Enough Money");
                alert.setHeaderText(null);
                alert.setContentText("You don't have enough money to buy this item!");
                alert.showAndWait();
            }
        });
        
        itemBox.getChildren().addAll(itemImage, itemInfo, buyButton);
        return itemBox;
    }
    
    /**
     * Shows the shop UI.
     */
    public void show() {
        // Reinitialize items to get new random jokers
        initializeItems();
        
        // Clear and rebuild the shop UI
        shopContainer.getChildren().clear();
        setupShopUI();
        
        // Update money display
        moneyLabel.setText("Money: $" + player.getMoney());
        
        if (scene == null) {
            scene = new Scene(shopContainer, 800, 600);
        }
        stage.setScene(scene);
        stage.setTitle("Shop");
        stage.show();
    }
    
    /**
     * Hides the shop UI.
     */
    public void hide() {
        stage.hide();
    }
    
    /**
     * Shows a prompt to sell a joker.
     * @param joker The joker to sell.
     */
    public void showSellPrompt(Joker joker) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sell Joker");
        alert.setHeaderText("Sell " + joker.getName() + "?");
        alert.setContentText("You will receive " + (joker.getCost() - 1) + " money.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                player.setMoney(player.getMoney() + (joker.getCost() - 1));
                player.removeJoker(joker);
            }
        });
    }
} 