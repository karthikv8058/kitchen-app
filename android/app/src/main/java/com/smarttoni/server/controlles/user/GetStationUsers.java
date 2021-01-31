package com.smarttoni.server.controlles.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.User;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class GetStationUsers extends HttpSecurityRequest {
    private Context context;
    private GreenDaoAdapter daoAdapter;

    public GetStationUsers(Context context) {
        this.daoAdapter = new GreenDaoAdapter(context);
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String stationId = jsonObject.getString("stationId");
            Station station = daoAdapter.getStationById(stationId);
            List<User> userList = daoAdapter.getUsersForStation(station.getId());
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<List<User>>() {
            }.getType();
            response.send(gson.toJson(userList, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
