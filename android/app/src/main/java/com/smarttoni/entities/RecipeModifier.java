package com.smarttoni.entities;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "recipe_modifiers")

public class RecipeModifier {


    @Id(autoincrement = true)
    private Long id;


    @Property(nameInDb = "created_at")
    @SerializedName("created_at")
    private String mCreatedAt;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMCreatedAt() {
        return this.mCreatedAt;
    }

    public void setMCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getMModifier() {
        return this.mModifier;
    }

    public void setMModifier(String mModifier) {
        this.mModifier = mModifier;
    }

    public Long getMRecipeId() {
        return this.mRecipeId;
    }

    public void setMRecipeId(Long mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public String getMUpdatedAt() {
        return this.mUpdatedAt;
    }

    public void setMUpdatedAt(String mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Transient
    private boolean isSelected;


    @Property(nameInDb = "modifier")
    @SerializedName("modifier")
    private String mModifier;

    @Property(nameInDb = "recipe_id")
    @SerializedName("recipe_id")
    private Long mRecipeId;

    @Property(nameInDb = "updated_at")
    @SerializedName("updated_at")
    private String mUpdatedAt;

    @Property(nameInDb = "orderId")
    private String orderId;

    @Generated(hash = 1224318606)
    public RecipeModifier(Long id, String mCreatedAt, String mModifier,
                          Long mRecipeId, String mUpdatedAt, String orderId) {
        this.id = id;
        this.mCreatedAt = mCreatedAt;
        this.mModifier = mModifier;
        this.mRecipeId = mRecipeId;
        this.mUpdatedAt = mUpdatedAt;
        this.orderId = orderId;
    }

    @Generated(hash = 1934233167)
    public RecipeModifier() {
    }

}
