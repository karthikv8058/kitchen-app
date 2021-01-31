package com.smarttoni.server.controlles;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.http.HttpClient;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreToAnalyze extends HttpSecurityRequest {
    private Context context;
    private LocalStorage localStorage;
    GreenDaoAdapter greenDaoAdapter;

    public StoreToAnalyze(Context context) {
        this.context = context;
        this.localStorage = new LocalStorage(context);
        greenDaoAdapter = new GreenDaoAdapter(context);
    }


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);

        final Type type = new TypeToken<Boolean>() {
        }.getType();
        try {
            String orderId = jsonObject.getString("orderId");
            Order order = greenDaoAdapter.getOrderById(orderId);
            PrinterData printerData = order.getPrinterData();
            printerData.received_on = HttpHelper.convertMillisecondsToMysqlDateTimeString(printerData.getTimestamp());
            printerData.ip_address = printerData.getIp();

            JsonObject message = new JsonObject();
            message.addProperty("message", printerData.getMessage());
            message.addProperty("ip_address", printerData.ip_address);
            message.addProperty("received_on", printerData.received_on);
            new HttpClient(context).getHttpClient().storePrinterMessage(localStorage.getRestaurantId(), message)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> responseFromWeb) {
                            Gson gson = GSONBuilder.createGSON();
                            response.send(gson.toJson(true, type));
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
