package com.smarttoni.models.wrappers;

import java.util.List;

public class RecipeWrapper {
    String recipeId;
    String recipeName;
    Float outPutQuantity;
    String OutPutUnit;
    String qty;

    int inventoryType;
    int type;
    String supplier;
    String image;
    String labelId;
    String description;
    String uuid;
    String colors;
    String recipeLabels;
    String roomId;
    String storageId;
    String rackId;
    String placeId;
    String productBarcode;
    boolean isExpanded;
    float inventoryQuantity;
    List<OpenOrderWrapper> openOrderWrappers;
    List<OpenOrderWrapper> externalOrderWrappers;
    private String expectedInventory;

    String currentInventory;

    String unitId;

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public int getRecipeType() {
        return type;
    }

    public void setRecipeType(int recipeType) {
        this.type = recipeType;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(int inventoryType) {
        this.inventoryType = inventoryType;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<OpenOrderWrapper> getOpenOrderWrappers() {
        return openOrderWrappers;
    }

    public void setOpenOrderWrappers(List<OpenOrderWrapper> openOrderWrappers) {
        this.openOrderWrappers = openOrderWrappers;
    }

    public List<OpenOrderWrapper> getExternalOrderWrappers() {
        return externalOrderWrappers;
    }

    public void setExternalOrderWrappers(List<OpenOrderWrapper> externalOrderWrappers) {
        this.externalOrderWrappers = externalOrderWrappers;
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

    public String getRecipeLabels() {
        return recipeLabels;
    }

    public void setRecipeLabels(String recipeLabels) {
        this.recipeLabels = recipeLabels;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getOutPutQuantity() {
        return outPutQuantity;
    }

    public void setOutPutQuantity(Float outPutQuantity) {
        this.outPutQuantity = outPutQuantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }


    public String getOutPutUnit() {
        return OutPutUnit;
    }

    public void setOutPutUnit(String outPutUnit) {
        OutPutUnit = outPutUnit;
    }

    public Float getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Float inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public String getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(String currentInventory) {
        this.currentInventory = currentInventory;
    }

    public String getExpectedInventory() {
        return expectedInventory;
    }

    public void setExpectedInventory(String expectedInventory) {
        this.expectedInventory = expectedInventory;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
