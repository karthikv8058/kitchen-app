package com.smarttoni.server.controlles.pos;

import android.content.Context;

import com.google.gson.Gson;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.CourseDao;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.MealDao;
import com.smarttoni.entities.Modifier;
import com.smarttoni.entities.ModifierDao;
import com.smarttoni.pos.models.PosOrderMeal;
import com.smarttoni.pos.models.NewPosOrderModel;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.pos.models.PosRecipe;

import java.util.ArrayList;
import java.util.List;

public class PosUtils {

    Context context;

    public PosUtils(Context context) {
        this.context = context;
    }

    public NewPosOrderModel getPosStateByOrderId(String orderId) {
//                get order
        Order order = DbOpenHelper.Companion.getDaoSession(context).getOrderDao().load(orderId);
        Course course = DbOpenHelper.Companion.getDaoSession(context).getCourseDao().queryBuilder().where(CourseDao.Properties.OrderId.eq(orderId)).list().get(0);
        List<Meal> meals = DbOpenHelper.Companion.getDaoSession(context).getMealDao().queryBuilder().where(MealDao.Properties.CourseId.eq(course.getId())).list();

        NewPosOrderModel newPosOrderModel = new NewPosOrderModel();
        newPosOrderModel.setOrderId(orderId);
        newPosOrderModel.setInventory(order.getIsInventory());
        newPosOrderModel.setOnCall(course.getIsOnCall());
        newPosOrderModel.setMeals(new ArrayList<PosOrderMeal>());
        newPosOrderModel.setTableNumber(order.getTableNo());
        newPosOrderModel.setDeliveryTime(String.valueOf(course.getDeliveryTime()));

        for (Meal meal : meals) {
            List<PosRecipe> recipes = new ArrayList<>();
            recipes.clear();
            meal.getOrderLine();
            for (OrderLine orderLine : meal.getOrderLine()) {
                Recipe recipe = orderLine.getRecipe();
                Gson gson = new Gson();
                String jsonString = gson.toJson(recipe);
                PosRecipe recipe1 = gson.fromJson(jsonString, PosRecipe.class);
                recipe1.setQuantity(orderLine.getQty());
                List<Modifier> mods = recipe.getModifiers();
                if (orderLine.getModifiers() != null) {
                    String[] items = orderLine.getModifiers().split("\\s*,\\s*");
                    List<Modifier> modifierArrayList = new ArrayList<>();
                    modifierArrayList.clear();
                    for (String modifierId : items) {
                        Modifier modifier = DbOpenHelper.Companion.getDaoSession(context).getModifierDao().queryBuilder()
                                .where(ModifierDao.Properties.Id.eq(modifierId)).unique();
                        if (mods != null && mods.size() > 0) {
                            for (Modifier mod : mods) {
                                Gson gson1 = new Gson();
                                String jsonString1 = gson1.toJson(mod);
                                Modifier mod1 = gson1.fromJson(jsonString1, Modifier.class);
                                if (mod1.getId().equals(modifier.getId())) {
                                    mod1.setSelected(true);
                                } else {
                                    mod1.setSelected(false);
                                }
                                modifierArrayList.add(mod1);
                            }
                        }

                    }
                }
                recipes.add(recipe1);
                orderLine.getModifiers();
            }


            PosOrderMeal posMeal = new PosOrderMeal();
            posMeal.setRecipes(recipes);
            newPosOrderModel.getMeals().add(posMeal);
        }
        return newPosOrderModel;
    }
}
