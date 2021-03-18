package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderData;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetOrderDetails extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public GetOrderDetails(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String orderId = jsonObject.getString("orderId");
            OrderData orderData = new OrderData();
            Order order = greenDaoAdapter.getOrderById(orderId);

            if(order != null){
                List<Work> workList = new ArrayList<>();
                order.getCourses();
                for (Course course : order.getCourses()) {
                    course.getMeals();
                    for (Meal meal : course.getMeals()) {
                        meal.getOrderLine();
                        for (OrderLine orderLine : meal.getOrderLine()) {
                            orderLine.getRecipe();
                            orderLine.getModifiers();
                            List<Work> works = greenDaoAdapter.getworkByorderLine(orderLine.getId());
                            for (Work work : works) {
                                Work w = work.cloneWithoutNextTask();
                                w.getOrderLineId();
                                w.updateOutput();
                                workList.add(w);
                            }
                        }
                    }
                }
                orderData.order = order;
                orderData.work = workList;
                orderData.chefActivityLogs = greenDaoAdapter.getChefActivityByOrderId(orderId);
            }

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<OrderData>() {
            }.getType();
            response.send(gson.toJson(orderData, type));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}