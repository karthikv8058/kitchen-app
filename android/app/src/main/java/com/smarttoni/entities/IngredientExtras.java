package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "IngredientExtras")
public class IngredientExtras{

    @Id(autoincrement = true)
    private Long id;

    private Long workId;

    private String recipeId;

    private float quantity;

    @Generated(hash = 2118332121)
    public IngredientExtras(Long id, Long workId, String recipeId, float quantity) {
        this.id = id;
        this.workId = workId;
        this.recipeId = recipeId;
        this.quantity = quantity;
    }

    @Generated(hash = 781543870)
    public IngredientExtras() {
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
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
