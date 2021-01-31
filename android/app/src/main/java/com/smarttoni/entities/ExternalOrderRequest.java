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

    private String requestedOrder;

    private String waitingExternalOrder;

    private String recipe;

    private float qty;

    @Generated(hash = 370315805)
    public ExternalOrderRequest(@NotNull String id, String requestedOrder,
            String waitingExternalOrder, String recipe, float qty) {
        this.id = id;
        this.requestedOrder = requestedOrder;
        this.waitingExternalOrder = waitingExternalOrder;
        this.recipe = recipe;
        this.qty = qty;
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

    public String getRequestedOrder() {
        return this.requestedOrder;
    }

    public void setRequestedOrder(String requestedOrder) {
        this.requestedOrder = requestedOrder;
    }

    public String getWaitingExternalOrder() {
        return this.waitingExternalOrder;
    }

    public void setWaitingExternalOrder(String waitingExternalOrder) {
        this.waitingExternalOrder = waitingExternalOrder;
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
