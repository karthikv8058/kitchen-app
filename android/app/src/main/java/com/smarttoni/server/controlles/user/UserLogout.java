package com.smarttoni.server.controlles.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.User;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;

public class UserLogout extends HttpSecurityRequest {

    private Context context;
    private GreenDaoAdapter greenDaoAdapter;

    public UserLogout(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String userId = getUser().getId();
        User user = greenDaoAdapter.getUserById(userId);
        if (user != null) {
            user.setIpAddress("");
            user.setOnline(false);
            greenDaoAdapter.updateUser(user);
            greenDaoAdapter.deleteWebAppDataandUserDataByUserId(userId);

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        }
    }
}
