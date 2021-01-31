package com.smarttoni.utils;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.entities.Restaurant;
import com.smarttoni.pegion.PushMessage;
import com.smarttoni.http.ChefHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UdpManager {

    public void sendUdpResponse(String ip, Restaurant restaurant) {
        SmarttoniContext context = ServiceLocator.getInstance().getSmarttoniContext();

        if (context == null && !context.isServerRunning()) {
            return;
        }

        PushMessage serverFound = new PushMessage(PushMessage.TYPE_UDP, restaurant);
        new ChefHttpClient().getHttpClient(ip).push(serverFound).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
