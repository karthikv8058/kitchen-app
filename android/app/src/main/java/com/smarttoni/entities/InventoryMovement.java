package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class InventoryMovement {


    public static final int INVENTORY_IN = 1;
    public static final int INVENTORY_OUT = 2;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    private int type;

    private String order;

    private String inventory;

    private float quantity;

    private String inventoryCreatedAt;


    @Generated(hash = 1029004905)
    public InventoryMovement() {
    }

    @Generated(hash = 10979455)
    public InventoryMovement(@NotNull String id, int type, String order,
            String inventory, float quantity, String inventoryCreatedAt) {
        this.id = id;
        this.type = type;
        this.order = order;
        this.inventory = inventory;
        this.quantity = quantity;
        this.inventoryCreatedAt = inventoryCreatedAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getInventory() {
        return this.inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getInventoryCreatedAt() {
        return this.inventoryCreatedAt;
    }

    public void setInventoryCreatedAt(String inventoryCreatedAt) {
        this.inventoryCreatedAt = inventoryCreatedAt;
    }



}
