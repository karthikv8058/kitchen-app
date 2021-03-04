package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.http.HttpClient;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.RequestCallback;
import com.smarttoni.sync.orders.SyncOrder;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetArchivedOrder extends RequestCallback {


    private Context context;

    public GetArchivedOrder(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse r) {
        new HttpClient(context).getHttpClient().loadOrders(ServiceLocator.getInstance().getSmarttoniContext().getRestaurant().getUuid(), "", 10, 0).enqueue(new Callback<List<SyncOrder>>() {
            @Override
            public void onResponse(Call<List<SyncOrder>> call, Response<List<SyncOrder>> response) {

                Gson gson = GSONBuilder.createGSON();


                StringBuilder builder = new StringBuilder("[");

                for (SyncOrder o : response.body()) {
                    builder.append(gson.toJson(o));
                    builder.append(",");
                }

                builder.setLength(builder.length() - 1);
                builder.append("]");

                r.send(builder.toString());
            }

            @Override
            public void onFailure(Call<List<SyncOrder>> call, Throwable throwable) {
                r.send("[]");
            }
        });
    }
}
