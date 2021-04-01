package com.smarttoni.server.controlles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.entities.Room;
import com.smarttoni.server.GSONBuilder;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

public class ListRooms extends HttpSecurityRequest {

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException {
        List<Room> rooms = ServiceLocator.getInstance().getDatabaseAdapter().loadRooms();
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<Room>>() {
        }.getType();
        response.send(gson.toJson(rooms, type));
    }
}
