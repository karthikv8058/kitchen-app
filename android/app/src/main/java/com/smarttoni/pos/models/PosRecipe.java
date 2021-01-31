package com.smarttoni.pos.models;


import com.google.gson.annotations.SerializedName;

public class PosRecipe {

    @SerializedName("uuid")
    private String id;
    private String name;
    private float quantity;
    private String unit;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
