package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.ArchivedOrder;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.http.HttpClient;
import com.smarttoni.models.wrappers.CourseWrapper;
import com.smarttoni.models.wrappers.MealWrapper;
import com.smarttoni.models.wrappers.OrderWrapper;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.RequestCallback;
import com.smarttoni.sync.orders.SyncCourse;
import com.smarttoni.sync.orders.SyncMeal;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.sync.orders.SyncOrderLine;
import com.smarttoni.utils.DateUtil;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.UnitHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOrders extends RequestCallback {


    private Context context;

    public GetOrders(Context context) {
        this.context = context;
    }

    //@Override
    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {


        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        int type = 0;
        try {
            type = jsonObject.getInt("type");
        } catch (JSONException e) {}

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

//        new HttpClient(context).getHttpClient().loadOrders(ServiceLocator.getInstance().getSmarttoniContext().getRestaurant().getUuid(), "", 10,0).enqueue(new Callback<List<SyncOrder>>() {
//            @Override
//            public void onResponse(Call<List<SyncOrder>> call, Response<List<SyncOrder>> response) {
//
//                Gson gsons = GSONBuilder.createGSON();
//
//                for (SyncOrder o : response.body()) {
//                    ArchivedOrder archivedOrder = new ArchivedOrder();
//                    archivedOrder.setOrderId(o.uuid);
//                    archivedOrder.setOrder(gsons.toJson(o));
//                    archivedOrder.setType(0);
//                    daoAdapter.saveArchivedOrder(archivedOrder);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<SyncOrder>> call, Throwable throwable) {
//
//            }
//        });


//        StringBuilder builder = new StringBuilder("[");
//
//
//        List<ArchivedOrder> orders = daoAdapter.listArchivedOrder();
//
//
//        JsonParser jp = new JsonParser();
//
//        JSONArray _orders = new JSONArray();
//        for(ArchivedOrder order : orders){
//
//
//            JsonObject jsonObject =  jp.parse(order.getOrder()).getAsJsonObject();
//            jp.parse(order.getOrder()).getAsJsonObject();
//            _orders.put(jp.parse(order.getOrder()).getAsJsonObject());
//
//            builder.append(jp.parse(order.getOrder()).getAsJsonObject().toString());
//            builder.append(",");
//        }
//
//        builder.setLength(builder.length() - 1);
//        builder.append("]");


        List<Order> orders = daoAdapter.listOrdersByType(type);

        List<SyncOrder> orderWrappers = new ArrayList<>();


        for (Order order : orders) {
            if (order.getCourses().size() > 0) {
                SyncOrder orderWrapper = new SyncOrder();
                orderWrapper.uuid = order.getId();
                orderWrapper.table = order.getTableNo();
                orderWrapper.status = order.getStatus();
                orderWrapper.inventoryOrder = order.getIsInventory();
                orderWrapper.uuid = order.getId();
                orderWrapper.uuid = order.getId();

                List<SyncCourse> courseWrappers = new ArrayList<>();
                for (Course course : order.getCourses()) {
                    SyncCourse courseWrapper = new SyncCourse();
                    courseWrapper.uuid = course.getId();
                    courseWrapper.deliveryDate = DateUtil.formatDate(course.getDeliveryTime(),DateUtil.STANDARD_DATE_FORMAT);
                    courseWrapper.expectedDate = DateUtil.formatDate(course.getActualDeliveryTime(),DateUtil.STANDARD_DATE_FORMAT);
                    courseWrapper.uuid = course.getId();
                    courseWrapper.onCall = course.getIsOnCall();

                    List<SyncMeal> mealWrappers = new ArrayList<>();

                    Course c = daoAdapter.getCourseById(course.getId());
                    for (Meal meal : c.getMeals()) {
                        SyncMeal mealWrapper = new SyncMeal();
                        mealWrapper.uuid = meal.getUuid();
                        List<SyncOrderLine> recipeWrappers = new ArrayList<>();
                        for (OrderLine orderLine : meal.getOrderLine()) {
                            SyncOrderLine recipeWrapper = new SyncOrderLine();

                            Recipe r = orderLine.getRecipe();
                            if(r != null) {
                                recipeWrapper.recipeName = r.getName();
                                recipeWrapper.qty = UnitHelper.convertToString(orderLine.getRecipe().getOutputQuantity() * orderLine.getQty(),orderLine.getRecipe().getOutputUnit());
                                recipeWrapper.image = r.getImageUrl();
                            }else{
                                recipeWrapper.recipeName = "** Recipe Not Found **";
                            }
                            recipeWrappers.add(recipeWrapper);
                        }
                        mealWrapper.orderLines = recipeWrappers;
                        mealWrappers.add(mealWrapper);
                    }
                    courseWrapper.meals = mealWrappers;
                    courseWrappers.add(courseWrapper);
                }
                orderWrapper.courses = courseWrappers;
                orderWrappers.add(orderWrapper);
            }
        }




        Collections.reverse(orderWrappers);

        Gson gson = GSONBuilder.createGSON();
        Type t = new TypeToken<List<SyncOrder>>() {
        }.getType();
        response.send(gson.toJson(orderWrappers, t));
    }
}
