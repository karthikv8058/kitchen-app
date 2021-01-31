package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.User;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class CheckDetailsToOpen extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public CheckDetailsToOpen(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        Boolean checkDetailsToOpen = false;

        try {
            String userId = jsonObject.getString("userId");
            User user = greenDaoAdapter.getUserById(userId);

            checkDetailsToOpen = user.getTaskAutoOpen();

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(checkDetailsToOpen, type));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
