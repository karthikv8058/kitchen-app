package com.smarttoni.server.controlles.devtools;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.entities.Station;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.RequestCallback;

import java.lang.reflect.Type;
import java.util.List;

public class DevStationList extends RequestCallback {

    private Context context;
    private static DaoSession daoSession;
    GreenDaoAdapter greenDaoAdapter;

    public DevStationList(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<Station>>() {
        }.getType();
        response.send(gson.toJson(greenDaoAdapter.loadStations(), type));
    }
}
