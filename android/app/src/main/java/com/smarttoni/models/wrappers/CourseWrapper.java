package com.smarttoni.models.wrappers;

import java.util.List;

public class CourseWrapper {
    String courseId;
    String deliveryTime;
    long ActualDeliveryTime;
    boolean isOnCall;
    public List<MealWrapper> meals;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getActualDeliveryTime() {
        return ActualDeliveryTime;
    }

    public void setActualDeliveryTime(long actualDeliveryTime) {
        ActualDeliveryTime = actualDeliveryTime;
    }

    public List<MealWrapper> getMeals() {
        return meals;
    }

    public void setMeals(List<MealWrapper> meals) {
        this.meals = meals;
    }

    public boolean isOnCall() {
        return isOnCall;
    }

    public void setOnCall(boolean onCall) {
        isOnCall = onCall;
    }
}
