package com.smarttoni.models.wrappers;

import java.util.List;

public class OrderWrapper {
    String orderId;
    String tableNumber;
    String OrderType;
    int status;
    boolean isInventory;
    String uuid;
    public List<CourseWrapper> courses;
    String supplier;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isInventory() {
        return isInventory;
    }

    public void setInventory(boolean inventory) {
        isInventory = inventory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public List<CourseWrapper> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseWrapper> courses) {
        this.courses = courses;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
