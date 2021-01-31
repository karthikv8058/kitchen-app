package com.smarttoni.server.controlles.chat;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.http.HttpClient;
import com.smarttoni.models.Chat;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostChat extends HttpSecurityRequest {


    private Context context;

    public PostChat(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse r) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String query = jsonObject.getString("query");
            String session = jsonObject.getString("session");
            LocalStorage storage = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);
            String restaurant = storage.getRestaurantId();
            new HttpClient(context).getHttpClient().chat(query, session + getUser().getUsername()).enqueue(new Callback<Chat>() {
                @Override
                public void onResponse(Call<Chat> call, Response<Chat> response) {
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Chat>() {
                    }.getType();
                    r.send(gson.toJson(response.body(), type));
                }

                @Override
                public void onFailure(Call<Chat> call, Throwable throwable) {
                    Chat chat = new Chat();
                    chat.setResponse("I'm offline, Please check your network connection");
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    r.send(gson.toJson(false, type));
                }
            });
        } catch (JSONException e) {
            Chat chat = new Chat();
            chat.setResponse("I'm offline, Please check your network connection");
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            r.send(gson.toJson(false, type));
        }
    }
}
