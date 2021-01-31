package com.smarttoni.server_app.models.UsersResponse;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.entities.RestaurantSettings;

import java.util.List;

@SuppressWarnings("unused")
public class UsersResponse {

    @SerializedName("uuid")
    private String uuId;
    @SerializedName("role")
    private String role;
    @SerializedName("status")
    private int status;
    @SerializedName("user")
    private UserDetails user;
    @SerializedName("rights")
    private List<String> userRightsList;

    public List<String> getUserRightsList() {
        return userRightsList;
    }

    public void setUserRightsList(List<String> userRightsList) {
        this.userRightsList = userRightsList;
    }

    @SerializedName("settings")
    private RestaurantSettings restaurantSettings;

    public RestaurantSettings getRestaurantSettings() {
        return restaurantSettings;
    }

    public void setRestaurantSettings(RestaurantSettings restaurantSettings) {
        this.restaurantSettings = restaurantSettings;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
