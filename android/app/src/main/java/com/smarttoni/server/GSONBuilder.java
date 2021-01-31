package com.smarttoni.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSONBuilder {
    public static Gson createGSON() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }
}
