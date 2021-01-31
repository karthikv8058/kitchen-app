package com.smarttoni.server.controlles.settings;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.sync.SyncFromWeb;

import org.json.JSONException;

import java.lang.reflect.Type;

public class SyncRequest extends HttpSecurityRequest {

    private Context context;

    public SyncRequest(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException {
        SyncFromWeb.Companion.syncCloudDb(context);
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<Boolean>() {
        }.getType();
        response.send(gson.toJson(true, type));
    }
}
