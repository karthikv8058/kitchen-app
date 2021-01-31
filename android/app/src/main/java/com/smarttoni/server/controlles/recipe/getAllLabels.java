package com.smarttoni.server.controlles.recipe;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Label;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;
import java.util.List;


public class getAllLabels extends HttpSecurityRequest {

    private Context context;

    public getAllLabels(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Label> labels = daoAdapter.loadLabels();
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<Label>>() {
        }.getType();

        response.send(gson.toJson(labels, type));
    }


}
