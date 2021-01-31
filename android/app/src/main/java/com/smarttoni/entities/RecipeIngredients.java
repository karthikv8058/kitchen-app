package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "recipe_ingredient")
public class RecipeIngredients {

    @SerializedName("recipe_uuid")
    @Property(nameInDb = "id")
    private String id;

    @SerializedName("quantity")
    @Property(nameInDb = "quantity")
    private Float quantity;

    @Property(nameInDb = "recipeId")
    private String recipeId;

    @Generated(hash = 333700824)
    public RecipeIngredients() {
    }

    @Generated(hash = 780178826)
    public RecipeIngredients(String id, Float quantity, String recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.recipeId = recipeId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }




}
