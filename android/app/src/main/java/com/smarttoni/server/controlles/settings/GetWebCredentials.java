package com.smarttoni.server.controlles.settings;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.WebAppData;
import com.smarttoni.entities.WebCredentialData;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;

public class GetWebCredentials extends HttpSecurityRequest {
    private Context context;
    private GreenDaoAdapter greenDaoAdapter;

    public GetWebCredentials(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String userId = getUser().getId();
        WebAppData webAppCredentials = greenDaoAdapter.getWebAppCredentials(userId);
        WebCredentialData webCredentialData = new WebCredentialData();
        webCredentialData.webCredentialData = webAppCredentials;
        webCredentialData.recipeMode = greenDaoAdapter.getRestaurantSettings().getEnabledRecipeEdit();
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<WebCredentialData>() {
        }.getType();
        response.send(gson.toJson(webCredentialData, type));
    }
}

