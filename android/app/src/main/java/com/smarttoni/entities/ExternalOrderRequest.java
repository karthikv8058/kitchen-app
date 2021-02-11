package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExternalOrderRequest {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    private String parentOrder;

    private String externalOrder;

    private String recipe;

    private float quantity;

    @Generated(hash = 483152173)
    public ExternalOrderRequest(@NotNull String id, String parentOrder,
            String externalOrder, String recipe, float quantity) {
        this.id = id;
        this.parentOrder = parentOrder;
        this.externalOrder = externalOrder;
        this.recipe = recipe;
        this.quantity = quantity;
    }

    @Generated(hash = 753142193)
    public ExternalOrderRequest() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getParentOrder() {
        return this.parentOrder;
    }

    public void setParentOrder(String parentOrder) {
        this.parentOrder = parentOrder;
    }

    public String getExternalOrder() {
        return this.externalOrder;
    }

    public void setExternalOrder(String externalOrder) {
        this.externalOrder = externalOrder;
    }
}
