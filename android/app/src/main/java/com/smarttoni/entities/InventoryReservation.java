package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "InventoryReservation")
public class InventoryReservation{

    @Id(autoincrement = true)
    private Long id;
    
    private String orderId;

    private String recipeId;

    private float qty;

    @Generated(hash = 1183255241)
    public InventoryReservation(Long id, String orderId, String recipeId,
            float qty) {
        this.id = id;
        this.orderId = orderId;
        this.recipeId = recipeId;
        this.qty = qty;
    }

    @Generated(hash = 852356338)
    public InventoryReservation() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
