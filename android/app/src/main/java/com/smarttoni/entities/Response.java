package com.smarttoni.entities;

import com.smarttoni.models.Data;

public class Response {
    private Data data;

    private RestaurantSettings restaurantSettings;

    public RestaurantSettings getRestaurantSettings() {
        return restaurantSettings;
    }

    public void setRestaurantSettings(RestaurantSettings restaurantSettings) {
        this.restaurantSettings = restaurantSettings;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "data = '" + data + '\'' +
                        "}";
    }
}
