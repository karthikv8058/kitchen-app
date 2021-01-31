package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

public class StorageItems {

    @SerializedName("storage_uuid")
    private String storageId;
    @SerializedName("storage_rack_uuid")
    private String rackId;
    @SerializedName("storage_place_uuid")
    private String placeId;

    @SerializedName("room_uuid")
    private String roomId;

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
