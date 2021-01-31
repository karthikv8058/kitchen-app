package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.utils.DateUtil;
import com.smarttoni.utils.Strings;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.Recipe;
import com.smarttoni.models.wrappers.CourseWrapper;
import com.smarttoni.models.wrappers.MealWrapper;
import com.smarttoni.models.wrappers.OrderOverViewWrapper;
import com.smarttoni.models.wrappers.OrderWrapper;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.UnitHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class LoadArchivedOrder extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;
    private Object ArchivedOrdersDao;

    public LoadArchivedOrder(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObjects = HttpHelper.postDataToJson(request);

        try {
            int isExternalOrder = jsonObjects.getInt("isExternalOrder");
            List<ArchivedOrders> archivedOrder = greenDaoAdapter.loadArchivedOrders();
            List<ArchivedOrders> archivedOrders = new ArrayList<>();


            for (ArchivedOrders archivedOrders1 : archivedOrder) {
                JSONObject jsonObject = new JSONObject(archivedOrders1.getOrderData());
                if (isExternalOrder == 0) {
                    if (jsonObject.optString("parentOrderUuid") != null) {
                        if (Strings.isNotEmpty((jsonObject.optString("parentOrderUuid")))) {
                            continue;
                        } else {
                            archivedOrders.add(archivedOrders1);
                        }
                    }

                } else {
                    if (jsonObject.optString("parentOrderUuid") != null) {
                        if (Strings.isNotEmpty((jsonObject.optString("parentOrderUuid")))) {
                            archivedOrders.add(archivedOrders1);
                        } else {
                            continue;
                        }
                    }
                }
            }


//            for (int i = 0; i < archivedOrders.size(); i++) {
//                if (archivedOrders.size() > 0 && i < (archivedOrders.size() - 1)) {
//                    JSONObject jsonObject = new JSONObject(archivedOrders.get(i).getOrderData());
//                    JSONArray courses = jsonObject.getJSONArray("courses");
//                    JSONObject object = courses.getJSONObject(0);
//                    JSONObject jsonObject1 = new JSONObject(archivedOrders.get(i + 1).getOrderData());
//                    JSONArray courses1 = jsonObject1.getJSONArray("courses");
//                    JSONObject object1 = courses1.getJSONObject(0);
//
//                    int compare = object.optString("deliveryDate").compareTo(object1.optString("deliveryDate"));
//                    if (compare > 0) {
//                        Collections.swap(archivedOrders, i, i + 1);
//                    }
//                }
//            }

            List<OrderWrapper> orderWrappers = new ArrayList<>();
            if (archivedOrders != null) {
                for (ArchivedOrders archivedOrders1 : archivedOrders) {
                    try {
                        JSONObject jsonObject = new JSONObject(archivedOrders1.getOrderData());
                        OrderWrapper orderWrapper = new OrderWrapper();
                        orderWrapper.setOrderId(jsonObject.getString("uuid"));
                        orderWrapper.setTableNumber(jsonObject.getString("table"));
                        orderWrapper.setOrderType("ORDER");
                        orderWrapper.setStatus(jsonObject.getInt("status"));
                        orderWrapper.setInventory(jsonObject.getBoolean("inventoryOrder"));
                        orderWrapper.setUuid(jsonObject.getString("uuid"));
                        List<CourseWrapper> courseWrappers = new ArrayList<>();
                        JSONArray courses = jsonObject.getJSONArray("courses");
                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject object = courses.getJSONObject(i);
                            CourseWrapper courseWrapper = new CourseWrapper();
                            courseWrapper.setCourseId(object.getString("uuid"));
                            //courseWrapper.setDeliveryTime((object.getString("deliveryDate")));
                            String dateString = object.getString("deliveryDate");
                            if(Strings.isNotEmpty(dateString)){
                                courseWrapper.setDeliveryTime(DateUtil.parse(dateString)+"");
                            }
                            List<MealWrapper> mealWrappers = new ArrayList<>();
                            JSONArray meals = object.getJSONArray("meals");
                            for (int j = 0; j < meals.length(); j++) {
                                JSONObject mealObject = meals.getJSONObject(j);
                                MealWrapper mealWrapper = new MealWrapper();
                                mealWrapper.setMealId(mealObject.getString("uuid"));
                                JSONArray orderLines = mealObject.getJSONArray("orderLines");
                                List<RecipeWrapper> recipeWrappers = new ArrayList<>();
                                for (int k = 0; k < orderLines.length(); k++) {
                                    RecipeWrapper recipeWrapper = new RecipeWrapper();
                                    JSONObject recipeObject = orderLines.getJSONObject(k);
                                    Recipe recipe = greenDaoAdapter.getRecipeById(recipeObject.getString("recipeUuid"));
                                    if(recipe == null){
                                        continue;
                                    }
                                    recipeWrapper.setRecipeName(recipe.getName());
                                    if (recipe.getOutputUnit() != null) {
                                        recipeWrapper.setOutPutUnit(recipe.getOutputUnit().getSymbol());
                                    }
                                    recipeWrapper.setImage(recipe.getImageUrl());
                                    float qty = Float.valueOf(recipeObject.optString("quantity"));
                                    recipeWrapper.setQty(UnitHelper.convertToString(recipe.getOutputQuantity() * qty, recipe.getOutputUnit()));
                                    recipeWrapper.setOutPutQuantity(Float.valueOf(recipeObject.optString("quantity")));

                                    if (recipeObject.getString("recipeUuid") != null) {
                                        recipeWrapper.setRecipeId(recipeObject.getString("recipeUuid"));
                                    }
                                    recipeWrappers.add(recipeWrapper);
                                }
                                mealWrapper.setRecipes(recipeWrappers);
                                mealWrappers.add(mealWrapper);
                            }
                            courseWrapper.setMeals(mealWrappers);
                            courseWrappers.add(courseWrapper);
                        }
                        orderWrapper.setCourses(courseWrappers);
                        orderWrappers.add(orderWrapper);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            OrderOverViewWrapper orderOverViewWrapper = new OrderOverViewWrapper();
            orderOverViewWrapper.orders = orderWrappers;
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<OrderOverViewWrapper>() {
            }.getType();
            response.send(gson.toJson(orderOverViewWrapper, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

