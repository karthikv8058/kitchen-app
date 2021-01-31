package com.smarttoni.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalStorage {

    private SharedPreferences prefs;

    private static String USERNAME = "username";
    private static String AUTH_TOKEN = "token";
    private static String RESTAURANT_ID = "restaurantId";
    //private static String RESTAURANT_NAME = "restaurantName";

    public static String LAST_SYNC_INTERVENTION = "lastInterventionSync";
    public static String LAST_SYNC_RECIPE = "lastRecipeSync";
    public static String LAST_SYNC_SEGMENTS = "lastSegmentsSync";
    public static String LAST_SYNC_SUPPLIER = "LAST_SYNC_SUPPLIER";



    public static String RESTAURANT_UUID = "RESTAURANT_ID";
    public static String RESTAURANT_NAME = "RESTAURANT_NAME";
    public static String RESTAURANT_TOKEN = "RESTAURANT_TOKEN";

    public LocalStorage(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUsername(String username) {
        prefs.edit().putString(LocalStorage.USERNAME, username).apply();
    }

    public String getUsername() {
        String username = prefs.getString(LocalStorage.USERNAME, "");
        return username;
    }

    public String getAuthToken() {
        String authToken = prefs.getString(LocalStorage.AUTH_TOKEN, "");
        return authToken;
    }

    public void setAuthToken(String authToken) {
        prefs.edit().putString(LocalStorage.AUTH_TOKEN, authToken).apply();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }


    public long getLong(String key) {
        return prefs.getLong(key, 0);
    }

    public void setLong(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public void setRestaurant(String id,String name) {
        prefs.edit().putString(LocalStorage.RESTAURANT_ID, id).apply();
        prefs.edit().putString(LocalStorage.RESTAURANT_NAME, name).apply();
    }

    public String getRestaurantId() {
        return prefs.getString(LocalStorage.RESTAURANT_ID, "");
    }

    public String getRestaurantName() {
        return prefs.getString(LocalStorage.RESTAURANT_NAME, "");
    }


    ///

    public boolean putInt(String key, int value) {
        return prefs.edit().putInt(key, value).commit();
    }

    public boolean putString(String key, String value) {
        return prefs.edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultvalue) {
        return prefs.getString(key, defaultvalue);
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }
}

