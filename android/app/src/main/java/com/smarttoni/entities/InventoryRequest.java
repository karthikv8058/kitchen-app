package com.smarttoni.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InventoryRequest {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String orderId;

    private String recipeId;

    private float qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }
}
