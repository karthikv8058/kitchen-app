package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StorageSynWrapper implements Serializable {

    @SerializedName("recipe")
    private String recipeId;

    @SerializedName("room")
    private String roomId;

    @SerializedName("storage")
    private String storageId;

    @SerializedName("rack")
    private String rackId;

    @SerializedName("place")
    private String placeId;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
