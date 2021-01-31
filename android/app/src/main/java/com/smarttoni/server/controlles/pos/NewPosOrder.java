package com.smarttoni.server.controlles.pos;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.order.OrderManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.utils.Strings;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.CourseDao;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.MealDao;
import com.smarttoni.pos.models.PosOrderMeal;
import com.smarttoni.pos.models.NewPosOrderModel;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.OrderLineDao;
import com.smarttoni.entities.Recipe;
import com.smarttoni.pos.models.PosRecipe;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.sync.OrderSyncToWeb;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.utils.UnitHelper;

import org.greenrobot.greendao.query.DeleteQuery;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;


public class NewPosOrder extends HttpSecurityRequest {
    private Context context;
    private NewPosOrderModel order;
    private GreenDaoAdapter greenDaoAdapter;

    public NewPosOrder(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    private static final String ONCALL = "On call";
    private static final String ASAP = "ASAP";

    private final long HOUR = 60 * 60 * 1000;


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject requestJson = HttpHelper.postDataToJson(request);
        Gson gson = GSONBuilder.createGSON();
        order = null;
        try {
            order = gson.fromJson(String.valueOf(requestJson.getJSONObject("order")), NewPosOrderModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Order newOrder = null;
        PosOrderMeal posOrderMeal = null;
        if (order.getInventory() && order.getMeals().size() > 0) {
            int i = 0;
            for (PosOrderMeal orderMeal : order.getMeals()) {
                if (i == 0) {
                    newOrder = setOrderDetails(order, orderMeal);
                } else {
                    order.setOrderId("");
                    newOrder = setOrderDetails(order, orderMeal);
                }

                i++;
            }
        } else {
            newOrder = setOrderDetails(order, posOrderMeal);
        }

        PosUtils posUtils = new PosUtils(context);
        Type type = new TypeToken<NewPosOrderModel>() {
        }.getType();

        new OrderSyncToWeb().onSync(context,
                ServiceLocator.getInstance().getDatabaseAdapter(),
                new LocalStorage(context).getRestaurantId(),
                null, null);
        response.send(gson.toJson(posUtils.getPosStateByOrderId(newOrder.getId()), type));
    }

    private Order setOrderDetails(NewPosOrderModel order, PosOrderMeal orderMealItem) {
        Order newOrder = null;
        Course course = null;
        if (Strings.isEmpty(order.getOrderId()) || "0".equals(order.getOrderId())) {
            newOrder = this.createNewOrder(order);
            course = this.createCourse(newOrder, order);
        } else {
            newOrder = greenDaoAdapter.getOrderById(order.getOrderId());
            newOrder.setIsUpdated(true);
            newOrder.setTableNo(order.getTableNumber());
            newOrder.setModification(Order.MODIFICATION_UPDATED);
            newOrder.setProcessed(false);

            greenDaoAdapter.updateOrder(newOrder);
            course = DbOpenHelper.Companion.getDaoSession(context).getCourseDao().queryBuilder().where(CourseDao.Properties.OrderId.eq(newOrder.getId())).list().get(0);
            if (order.getDeliveryTime() != null && !order.getDeliveryTime().equals("0")) {
                if (order.getDeliverableType() != null && order.getDeliverableType().equals(ONCALL)) {
                    course.setIsOnCall(true);
                    course.setDeliveryTime(System.currentTimeMillis() + HOUR);
                } else if (order.getDeliverableType() != null && order.getDeliverableType().equals(ASAP)) {
                    course.setIsOnCall(false);
                    course.setDeliveryTime(System.currentTimeMillis());
                } else {
                    course.setIsOnCall(false);
                    course.setDeliveryTime(Long.parseLong(order.getDeliveryTime()));
                }
            }
            greenDaoAdapter.updateCourse(course);
        }

        this.clearExistingData(newOrder, course);
        if (orderMealItem != null) {
            setMealItem(orderMealItem, course);
        } else {
            for (PosOrderMeal orderMeal : order.getMeals()) {
                setMealItem(orderMeal, course);
            }
        }
        AssignmentFactory.getInstance().processOrder(context, newOrder);
        return newOrder;
    }

    private void setMealItem(PosOrderMeal orderMeal, Course course) {
        Meal meal = this.createNewMeal(course, orderMeal);
        for (PosRecipe recipe : orderMeal.getRecipes()) {
            OrderLine orderLine = new OrderLine();
            orderLine.setUuid(UUID.randomUUID().toString());
            orderLine.setMealId(meal.getId());
            orderLine.setRecipeId(recipe.getId());
            orderLine.setModifiers("");
            orderLine.setCourseId(course.getId());
            Recipe r = ServiceLocator.getInstance().getDatabaseAdapter().getRecipeById(recipe.getId());
            int actualQty = UnitHelper.getRecipeQty(recipe.getUnit(),r,recipe.getQuantity());



            orderLine.setQty(actualQty);
            orderLine.setOrderId(course.getOrderId());
            greenDaoAdapter.saveOrderLine(orderLine);
        }
    }


    private void clearExistingData(Order newOrder, Course course) {
        DeleteQuery<Meal> deleteQuery = DbOpenHelper.Companion.getDaoSession(context).queryBuilder(Meal.class)
                .where(MealDao.Properties.CourseId.eq(course.getId()))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        DbOpenHelper.Companion.getDaoSession(context).clear();

//        List<Meal> existingMeals = DbOpenHelper.Companion.getDaoSession(context).getMealDao().queryBuilder().where(MealDao.Properties.CourseId.eq(course.getId())).list();

//        for (Meal currentmeal : existingMeals) {
//            List<SelectedRecipe> selectedRecipes = currentmeal.getSelectedRecipes();
//            for (SelectedRecipe selectedRecipe : selectedRecipes) {
//                DeleteQuery<SelectedRecipeModifier> modifierDeleteQuery = DbOpenHelper.Companion.getDaoSession(context).queryBuilder(SelectedRecipeModifier.class)
//                        .where(SelectedRecipeModifierDao.Properties.SelectedRecipeId.eq(selectedRecipe.getId()))
//                        .buildDelete();
//                modifierDeleteQuery.executeDeleteWithoutDetachingEntities();
//            }

//            DeleteQuery<SelectedRecipe> mealDeleteQuery = DbOpenHelper.Companion.getDaoSession(context).queryBuilder(SelectedRecipe.class)
//                    .where(SelectedRecipeDao.Properties.MealsId.eq(currentmeal.getId()))
//                    .buildDelete();
//            mealDeleteQuery.executeDeleteWithoutDetachingEntities();
//            DbOpenHelper.Companion.getDaoSession(context).clear();
//        }

        DbOpenHelper.Companion.getDaoSession(context).queryBuilder(OrderLine.class)
                .where(OrderLineDao.Properties.OrderId.eq(newOrder.getId()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

        DbOpenHelper.Companion.getDaoSession(context).clear();
        OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
        orderManager.deleteOrderForEdit(context, newOrder.getId(), true);

    }


    private Meal createNewMeal(Course course, PosOrderMeal orderMeal) {
        Meal meal = new Meal();
        meal.setUuid(UUID.randomUUID().toString());
        meal.setCourseId(course.getId());
        meal.setCourseName(course.getCourseName());
        meal.setName(orderMeal.getRecipes().get(0).getName());
        meal.setTableNo(course.getTableNo());
        greenDaoAdapter.saveMeal(meal);
        return meal;
    }

    private Course createCourse(Order newOrder, NewPosOrderModel order) {
        Course course = new Course();
        course.setId(newOrder.getId());
        course.setCourseName("course 1");
        if (order.getDeliveryTime() != null && !order.getDeliveryTime().equals("0")) {
            if (order.getDeliverableType().equals(ONCALL)) {
                course.setDeliveryTime(0);
            } else if (order.getDeliverableType().equals(ASAP)) {
                course.setDeliveryTime(System.currentTimeMillis());
            } else {
                course.setDeliveryTime(Long.parseLong(order.getDeliveryTime()));
            }
        }
        course.setOrderId(newOrder.getId());
        course.setTableNo(newOrder.getTableNo());
        if (order.getDeliverableType() != null && !order.getDeliverableType().equals("") && order.getDeliverableType().equals(ONCALL)) {
            course.setIsOnCall(true);
            course.setDeliveryTime(System.currentTimeMillis() + HOUR);
        } else {
            course.setIsOnCall(false);
        }
        greenDaoAdapter.saveCourse(course);
        return course;
    }

    private Order createNewOrder(NewPosOrderModel order) {
        Order o = new Order();
        o.setIsInventory(order.isInventory());
        o.setCreatedAt(new Date().getTime());
        o.setTableNo(order.getTableNumber());
        o.setId(UUID.randomUUID().toString());
        o.setStatus(Order.ORDER_OPEN);
        o.setIsUpdated(true);
        greenDaoAdapter.saveOrder(o);
        return o;
    }
}

