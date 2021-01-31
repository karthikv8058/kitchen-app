package com.smarttoni.entities;


import com.smarttoni.assignment.service.ServiceLocator;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;


@Entity(nameInDb = "inventory")
public class Inventory {

    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "recipeUuid")
    private String recipeUuid;

    @Transient
    Recipe recipe;

    @Property(nameInDb = "modifier")
    private String modifier;

    @Property(nameInDb = "quantity")
    private float quantity;

    @Property(nameInDb = "isUpdated")
    private String isUpdated;

    @Property(nameInDb = "createdAt")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    @Property(nameInDb = "isDeleted")
    private boolean isDeleted;

    @Property(nameInDb = "recipeName")
    private String recipeName;

    @Property(nameInDb = "image")
    private String image;

    @Generated(hash = 375708430)
    public Inventory() {
    }

    @Generated(hash = 974058631)
    public Inventory(@NotNull String id, String recipeUuid, String modifier, float quantity,
            String isUpdated, long createdAt, long updatedAt, boolean isDeleted, String recipeName,
            String image) {
        this.id = id;
        this.recipeUuid = recipeUuid;
        this.modifier = modifier;
        this.quantity = quantity;
        this.isUpdated = isUpdated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.recipeName = recipeName;
        this.image = image;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeUuid() {
        return this.recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe getRecipe() {
        if (recipe == null) {
            recipe = ServiceLocator.getInstance().getDatabaseAdapter().getRecipeById(recipeUuid);
        }
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.recipeUuid = recipe.getId();
    }

    public String getIsUpdated() {
        return this.isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }
}
