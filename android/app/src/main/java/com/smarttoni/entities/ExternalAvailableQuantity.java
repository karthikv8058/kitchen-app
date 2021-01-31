package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExternalAvailableQuantity {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    private String order;

    private String recipe;

    private float qty;

    @Generated(hash = 226156430)
    public ExternalAvailableQuantity(@NotNull String id, String order,
            String recipe, float qty) {
        this.id = id;
        this.order = order;
        this.recipe = recipe;
        this.qty = qty;
    }

    @Generated(hash = 517333499)
    public ExternalAvailableQuantity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public float getQty() {
        return this.qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }
}
