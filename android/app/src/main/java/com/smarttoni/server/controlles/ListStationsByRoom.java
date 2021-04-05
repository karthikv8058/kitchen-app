package com.smarttoni.server.controlles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Station;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class ListStationsByRoom extends HttpSecurityRequest {

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        String roomId = jsonObject.getString("roomId");
        List<Station> stations = ServiceLocator.getInstance().getDatabaseAdapter().loadStations(roomId);
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<Station>>() {
        }.getType();
        response.send(gson.toJson(stations, type));
    }
}
