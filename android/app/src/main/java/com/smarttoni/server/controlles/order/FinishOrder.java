package com.smarttoni.server.controlles.order;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.order.OrderManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class FinishOrder extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public FinishOrder(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);

    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String orderId = jsonObject.getString("orderId");
            String userId = getUser().getId();
            Order order = greenDaoAdapter.getOrderById(orderId);


            OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
            orderManager.completeOrder(context, userId, orderId);
//            if (order.getOrderType().equals(Order.TYPE_PRINTER)) {
//                order.setStatus(Order.ORDER_COMPLETED);
//                greenDaoAdapter.saveOrderDao(order);
//            }

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
