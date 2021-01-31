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
import com.smarttoni.entities.Order;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class CheckOrderStarted extends HttpSecurityRequest {

    private Context context;

    public CheckOrderStarted(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        boolean isStarted = false;
        try {
            String orderId = jsonObject.getString("orderId");
            OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
            isStarted = orderManager.isOrderStarted(context, orderId);
            if(!isStarted){
                List<Order> orders= ServiceLocator.getInstance().getDatabaseAdapter().loadChildOrders(orderId);
                if(orders != null){
                    for(Order order : orders){
                        if( order.getStatus() != Order.ORDER_OPEN){
                            isStarted = true;
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<Boolean>() {
        }.getType();
        response.send(gson.toJson(isStarted, type));
    }
}
