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
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Supplier;
import com.smarttoni.models.wrappers.CourseWrapper;
import com.smarttoni.models.wrappers.MealWrapper;
import com.smarttoni.models.wrappers.OrderOverViewWrapper;
import com.smarttoni.models.wrappers.OrderWrapper;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.UnitHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class getExternalOrders extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public getExternalOrders(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        List<Order> ordersList = greenDaoAdapter.loadnonArchivedOrders();
        List<Order> externalOrders = new ArrayList<>();
        for (Order o : ordersList) {
            if (o.getType() == Order.TYPE_EXTERNAL) {
                externalOrders.add(o);
            }
        }
        List<OrderWrapper> orderWrappers = new ArrayList<>();
        if (externalOrders != null) {
            for (Order order : externalOrders) {
                OrderWrapper orderWrapper = new OrderWrapper();
                orderWrapper.setOrderId(order.getId());
                orderWrapper.setTableNumber(order.getTableNo());
                orderWrapper.setOrderType("ORDER");
                orderWrapper.setStatus(order.getStatus());
                orderWrapper.setInventory(order.getIsInventory());
                orderWrapper.setUuid(order.getId());
                orderWrapper.setSupplier(greenDaoAdapter.getSupplierNameByOrderId(order.getId()));

                List<CourseWrapper> courseWrappers = new ArrayList<>();
                for (Course course : order.getCourses()) {
                    CourseWrapper courseWrapper = new CourseWrapper();
                    courseWrapper.setCourseId(course.getId());
                    courseWrapper.setActualDeliveryTime(course.getActualDeliveryTime());
                    courseWrapper.setDeliveryTime(String.valueOf(course.getDeliveryTime()));
                    List<MealWrapper> mealWrappers = new ArrayList<>();
                    for (Meal meal : course.getMeals()) {
                        MealWrapper mealWrapper = new MealWrapper();
                        mealWrapper.setMealId(meal.getUuid());
                        List<RecipeWrapper> recipeWrappers = new ArrayList<>();
                        for (OrderLine orderLine : meal.getOrderLine()) {
                            RecipeWrapper recipeWrapper = new RecipeWrapper();
//                            if (orderLine.getRecipe().getOutputUnit()!=null){
//                                recipeWrapper.setOutPutUnit(orderLine.getRecipe().getOutputUnit().getSymbol());
//                            }
//                            if (orderLine.getRecipe().getOutputUnit()!=null){
//                                recipeWrapper.setOutPutUnit(orderLine.getRecipe().getOutputUnit().getSymbol());
//                            }
                            recipeWrapper.setRecipeName(orderLine.getRecipe().getName());
                            recipeWrapper.setRecipeId(orderLine.getRecipe().getId());
                            recipeWrapper.setImage(orderLine.getRecipe().getImageUrl());
                            recipeWrapper.setOutPutQuantity(orderLine.getRecipe().getOutputQuantity() * orderLine.getQty());
                            recipeWrapper.setSupplier(orderLine.getRecipe().getSupplier());
                            recipeWrapper.setQty(UnitHelper.convertToString(orderLine.getRecipe().getOutputQuantity() * orderLine.getQty(),orderLine.getRecipe().getOutputUnit()));
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

        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<OrderWrapper>>() {
        }.getType();
        response.send(gson.toJson(orderWrappers, type));
    }
}
