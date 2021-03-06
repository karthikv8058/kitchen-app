package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "IngredientRequirement")
public class IngredientRequirement {

    @Id(autoincrement = true)
    private Long id;

    private String orderId;

    private String recipeId;

    private float quantity;

    @Generated(hash = 1992482551)
    public IngredientRequirement(Long id, String orderId, String recipeId,
            float quantity) {
        this.id = id;
        this.orderId = orderId;
        this.recipeId = recipeId;
        this.quantity = quantity;
    }

    @Generated(hash = 1271352810)
    public IngredientRequirement() {
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
