package com.smarttoni.server.controlles.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.UserRights;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;
import java.util.List;


public class GetUserRights extends HttpSecurityRequest {

    private Context context;
    private GreenDaoAdapter daoAdapter;

    public GetUserRights(Context context) {
        this.context = context;
        this.daoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String userId = getUser().getId();
        List<UserRights> userRights = daoAdapter.getUserRightsById(userId);
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<UserRights>>() {
        }.getType();
        response.send(gson.toJson(userRights, type));
    }
}
