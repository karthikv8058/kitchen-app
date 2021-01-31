package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class DeletePrinterMessage extends HttpSecurityRequest {
    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public DeletePrinterMessage(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        Order result = null;

        Type type = new TypeToken<Boolean>() {
        }.getType();
        try {
            String orderId = jsonObject.getString("orderId");
            Order order = greenDaoAdapter.getOrderById(orderId);
            order.setStatus(Order.ORDER_DELETED);
            greenDaoAdapter.updateOrderDao(order);

            Gson gson = GSONBuilder.createGSON();
            response.send(gson.toJson(true, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
