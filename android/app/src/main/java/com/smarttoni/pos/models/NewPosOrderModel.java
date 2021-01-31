package com.smarttoni.pos.models;

import java.util.ArrayList;

public class NewPosOrderModel {

    Boolean isInventory, isOnCall, isWeb;
    String orderId;
    ArrayList<PosOrderMeal> meals;
    String tableNumber;
    String deliveryTime;
    String deliverableType;
    String orderTo;

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public Boolean getInventory() {
        return isInventory;
    }

    public void setInventory(Boolean inventory) {
        isInventory = inventory;
    }

    public Boolean getOnCall() {
        return isOnCall;
    }

    public void setOnCall(Boolean onCall) {
        isOnCall = onCall;
    }

    public String getDeliverableType() {
        return deliverableType;
    }

    public void setDeliverableType(String deliverableType) {
        this.deliverableType = deliverableType;
    }

    public boolean isInventory() {
        return isInventory;
    }

    public void setInventory(boolean inventory) {
        isInventory = inventory;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public ArrayList<PosOrderMeal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<PosOrderMeal> meals) {
        this.meals = meals;
    }
}
