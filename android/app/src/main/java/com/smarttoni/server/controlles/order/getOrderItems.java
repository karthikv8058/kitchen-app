package com.smarttoni.server.controlles.order;


import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.R;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.ArchivedOrdersDao;
import com.smarttoni.entities.OrderDao;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.react.modules.SpeechRecognizerKeenAsr;
import com.smarttoni.utils.DateUtil;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.Strings;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.models.wrappers.CourseWrapper;
import com.smarttoni.models.wrappers.MealWrapper;
import com.smarttoni.models.wrappers.OrderOverViewWrapper;
import com.smarttoni.models.wrappers.OrderWrapper;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.UnitHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class getOrderItems extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public getOrderItems(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject requestData = HttpHelper.postDataToJson(request);
        try {
            int pageCount = requestData.getInt("pageCount");
            List<Order> ordersList = new ArrayList<>();
//            if (pageCount == -1) {
//                ordersList = greenDaoAdapter.loadnonArchivedOrders();
//            } else {
//                ordersList = DbOpenHelper.Companion.getDaoSession(context)
//                        .getOrderDao()
//                        .queryBuilder().orderDesc(OrderDao.Properties.CreatedAt)
//                        .where(OrderDao.Properties.IsArchive.notEq(1))
//                        .offset(10 * (pageCount - 1)).limit(10).list();
//            }


            ordersList = DbOpenHelper.Companion.getDaoSession(context)
                    .getOrderDao()
                    .queryBuilder().orderDesc(OrderDao.Properties.CreatedAt)
                    //.where(OrderDao.Properties.IsArchive.notEq(1))
                    .offset(10 * pageCount).limit(10).list();

//        List<Order> ordersList = greenDaoAdapter.loadnonArchivedOrders();
            List<OrderWrapper> orderWrappers = new ArrayList<>();
//            for (int i = 0; i < ordersList.size(); i++) {
//                if ( i < (ordersList.size() - 1)) {
//                    Date date=new Date(ordersList.get(i).getUpdatedAt());
//                    Date date2=new Date(ordersList.get(i+1).getUpdatedAt());
//                    if ( date.after(date2) ) {
//                        Collections.swap(ordersList, i, i + 1);
//                    }
//                }
//            }


            Collections.sort(ordersList, new Comparator<Order>() {
                @Override
                public int compare(Order o1, Order o2) {
                    Date date = new Date(o1.getCreatedAt());
                    Date date2 = new Date(o2.getCreatedAt());
                    return date.equals(date2) ? 0 : date.after(date2) ? -1 : 1;
                }
            });

            if (ordersList != null)
                for (Order order : ordersList) {


                    if (order.getIsArchive()) {

                        ///////


                        List<ArchivedOrders> archivedOrder =  DbOpenHelper.Companion.getDaoSession(context)
                                .getArchivedOrdersDao()
                                .queryBuilder()
                                .where(ArchivedOrdersDao.Properties.Id.eq(order.getId()))
                                .list();

                        if(archivedOrder == null && archivedOrder.size() == 0 || archivedOrder.get(0) == null){
                            continue;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(archivedOrder.get(0).getOrderData());
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

                        //////
                    } else {


                        if (order.getCourses().size() > 0) {
                            OrderWrapper orderWrapper = new OrderWrapper();
                            if (order.getType() == Order.TYPE_EXTERNAL) {
                                continue;
                            }
                            orderWrapper.setOrderId(order.getId());
                            orderWrapper.setTableNumber(order.getTableNo());
                            orderWrapper.setOrderType("ORDER");
                            orderWrapper.setStatus(order.getStatus());
                            orderWrapper.setInventory(order.getIsInventory());
                            orderWrapper.setUuid(order.getId());
                            List<CourseWrapper> courseWrappers = new ArrayList<>();
                            for (Course course : order.getCourses()) {
                                CourseWrapper courseWrapper = new CourseWrapper();
                                courseWrapper.setCourseId(course.getId());
                                courseWrapper.setActualDeliveryTime(course.getActualDeliveryTime());
                                courseWrapper.setDeliveryTime(String.valueOf(course.getDeliveryTime()));
                                courseWrapper.setOnCall(course.getIsOnCall());
                                List<MealWrapper> mealWrappers = new ArrayList<>();
                                for (Meal meal : course.getMeals()) {
                                    MealWrapper mealWrapper = new MealWrapper();
                                    mealWrapper.setMealId(meal.getUuid());
                                    List<RecipeWrapper> recipeWrappers = new ArrayList<>();
                                    for (OrderLine orderLine : meal.getOrderLine()) {
                                        RecipeWrapper recipeWrapper = new RecipeWrapper();
                                        if (orderLine.getRecipe().getOutputUnit() != null) {
                                            recipeWrapper.setOutPutUnit(orderLine.getRecipe().getOutputUnit().getSymbol());
                                        }
                                        recipeWrapper.setRecipeName(orderLine.getRecipe().getName());
                                        recipeWrapper.setImage(orderLine.getRecipe().getImageUrl());
                                        recipeWrapper.setRecipeId(orderLine.getRecipe().getId());
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
                        }
                    }
                }

            //OrderOverViewWrapper orderOverViewWrapper = new OrderOverViewWrapper();
            //orderOverViewWrapper.orders = orderWrappers;
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<List<OrderWrapper>>() {
            }.getType();
            response.send(gson.toJson(orderWrappers, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
