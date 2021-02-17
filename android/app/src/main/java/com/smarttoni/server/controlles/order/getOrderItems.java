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
import com.smarttoni.entities.OrderDao;
import com.smarttoni.entities.Printer;
import com.smarttoni.react.modules.SpeechRecognizerKeenAsr;
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
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            int pageCount = jsonObject.getInt("pageCount");
            List<Order> ordersList = new ArrayList<>();
            if (pageCount == 0) {
                ordersList = greenDaoAdapter.loadnonArchivedOrders();
            } else {
                ordersList = DbOpenHelper.Companion.getDaoSession(context)
                        .getOrderDao()
                        .queryBuilder().orderDesc(OrderDao.Properties.CreatedAt)
                        .where(OrderDao.Properties.IsArchive.notEq(1)).offset(10 * (pageCount - 1)).limit(10).list();
            }

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
