package com.smarttoni.server_app.models.UsersResponse;

import com.google.gson.annotations.SerializedName;


public class InventoryList {

    @SerializedName("uuid")
    private String id;

    @SerializedName("itemUuid")
    private String recipeId;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("modifiers")
    private String modifier;

    @SerializedName("isItemDeleted")
    private boolean isDeleted;

    /**
     * Sample JSON
     *  "{\"uuid\":\"267b8b9a-4a8f-41ea-be0c-98a789a3c670\",\"image_url\":null,\"en\":{\"name\":\"Test(Removed)\"}}"
     */
    @SerializedName("itemData")
    private String deletedDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getDeletedDetails() {
        return deletedDetails;
    }

    public void setDeletedDetails(String deletedDetails) {
        this.deletedDetails = deletedDetails;
    }
}