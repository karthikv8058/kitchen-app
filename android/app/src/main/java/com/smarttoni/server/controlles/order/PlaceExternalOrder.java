package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.sync.OrderSyncToWeb;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class PlaceExternalOrder extends HttpSecurityRequest {

    private Context context;

    public PlaceExternalOrder(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String orderId = jsonObject.getString("orderId");
            DaoAdapter adapter = ServiceLocator.getInstance().getDatabaseAdapter();
            Order order =adapter.getOrderById(orderId);
            if(order.getStatus() == Order.ORDER_OPEN){
                order.setStatus(Order.ORDER_STARTED);
                adapter.updateOrder(order);

                new OrderSyncToWeb().onSync(context,
                        ServiceLocator.getInstance().getDatabaseAdapter(),
                        new LocalStorage(context).getRestaurantId(),
                        null, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<Boolean>() {
        }.getType();
        response.send(gson.toJson(true, type));
    }
}
