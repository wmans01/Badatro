package com.badatro;

import javafx.scene.image.ImageView;

/**
 * Represents an item that can be bought or sold in the shop.
 */
public interface ShopItem {
    /**
     * Gets the name of the item.
     * @return The name of the item.
     */
    String getName();
    /**
     * Gets the description of the item.
     * @return The description of the item.
     */
    String getDescription();
    /**
     * Gets the cost of the item.
     * @return The cost of the item.
     */
    int getCost();
    /**
     * Gets the image path of the item.
     * @return The image path of the item.
     */
    String getImagePath();
} 