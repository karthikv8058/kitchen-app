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
import com.smarttoni.sync.orders.SyncCourse;
import com.smarttoni.sync.orders.SyncMeal;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.sync.orders.SyncOrderLine;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.Strings;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        String orderId = "";
        boolean isExternal = false;
        try {
            orderId = jsonObject.getString("lastOrder");
            isExternal = jsonObject.getBoolean("isExternal");
        } catch (JSONException e) {}

        new HttpClient(context).getHttpClient().loadOrders(ServiceLocator.getInstance().getSmarttoniContext().getRestaurant().getUuid(), orderId, 10, isExternal).enqueue(new Callback<List<SyncOrder>>() {
            @Override
            public void onResponse(Call<List<SyncOrder>> call, Response<List<SyncOrder>> response) {

                Gson gson = GSONBuilder.createGSON();

                StringBuilder builder = new StringBuilder("[");

                for (SyncOrder o : response.body()) {
                    for(SyncCourse c:o.courses){
                        for(SyncMeal m: c.meals){
                            for (SyncOrderLine ol:m.orderLines){
                                ol.qty = (ol.outputQuantity *  ol.quantity) + ol.outputUnitSymbol;
                            }
                            if(m.orderLines == null){
                                m.orderLines = new ArrayList<>();
                            }
                            if(m.orderLines.isEmpty()){
                                SyncOrderLine ol = new SyncOrderLine();
                                ol.recipeName = "** Deleted Recipe(s) **";
                                m.orderLines.add(ol);
                            }
                        }
                    }
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
