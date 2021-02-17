package com.smarttoni.sync;

import android.content.Context;

import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.order.OrderManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.http.HttpClient;
import com.smarttoni.sync.orders.SyncCourse;
import com.smarttoni.sync.orders.SyncMeal;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.sync.orders.SyncOrderLine;
import com.smarttoni.sync.orders.SyncOrderWrapper;
import com.smarttoni.sync.orders.SyncOrders;
import com.smarttoni.utils.AppState;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.utils.DateUtil;
import com.smarttoni.utils.Strings;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderSyncToWeb implements AbstractSyncAdapter {

    @Override
    public void onSync(@NotNull Context context, @NotNull DaoAdapter daoAdapter, @NotNull String restaurantId, @NotNull SyncSuccessListener successListener, @NotNull SyncFailListener failListener) {
        List<Order> orders = daoAdapter.loadOrders();


        SyncOrderWrapper wrapper = new SyncOrderWrapper();




        SyncOrders s = new SyncOrders();
        List<SyncOrder> os = s.sync(daoAdapter, daoAdapter.loadOrders());


        wrapper.setOrders(os);
        wrapper.setAvailableQuantity(daoAdapter.listExternalAvailableQuantity());
        wrapper.setRequestedQuantity(daoAdapter.listExternalOrderRequest());


        LocalStorage ls = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);

        new HttpClient(context).getHttpClient().syncOrders(ls.getRestaurantId(), !AppState.initialOrderSyncCompleted, wrapper).enqueue(new Callback<SyncOrderWrapper>() {

            @Override
            public void onResponse(Call<SyncOrderWrapper> call, Response<SyncOrderWrapper> response) {
                if (response.body() != null) {

                    SyncOrderWrapper orderWrapper = response.body();

                    if(!AppState.initialOrderSyncCompleted){
                        List<ExternalAvailableQuantity> eaqs = orderWrapper.getAvailableQuantity();
                        daoAdapter.insertAllExternalAvailableQuantity(eaqs);

                        List<ExternalOrderRequest> eoqs = orderWrapper.getRequestedQuantity();
                        daoAdapter.insertAllExternalOrderRequest(eoqs);
                    }

                    AppState.initialOrderSyncCompleted = true;

                    List<SyncOrder> orders = orderWrapper.getOrders();

                    for (SyncOrder o : orders) {
                        Order _order = daoAdapter.getOrderById(o.uuid);
                        if(_order != null && _order.getStatus() != Order.ORDER_OPEN){
                            continue;
                        }
                        if (!daoAdapter.deleteOrder(o)) {
                            return;
                        }

                        OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
                        orderManager.removeExternalOrderRequests(o.uuid);
                        orderManager.removeWorksForOrder(o.uuid);

                        Order order = new Order();
                        order.setId(o.uuid);
                        order.setUpdatedAt(DateUtil.parse(o.updatedAt));
                        order.setStatus(o.status);
                        if(o.type == Order.TYPE_INTERNAL && o.status == Order.ORDER_STARTED){
                            order.setStatus(Order.ORDER_OPEN); // TO BUILD WORK TREE , Order Started Never build tree
                        }
                        order.setType(o.type);
                        order.setIsInventory(o.inventoryOrder);
                        order.setTableNo(o.table);
                        order.setProcessed(false);
                        order.setChildOrderStatus(o.childOrderStatus);

                        order.setCreatedAt(DateUtil.parse(o.createdAt));

                        daoAdapter.saveOrder(order, true);

                        if (o.courses == null) {
                            continue;
                        }

                        for (SyncCourse c : o.courses) {
                            Course course = new Course();
                            course.setOrderId(order.getId());
                            course.setId(o.uuid);//TODO
                            course.setIsOnCall(c.onCall);
                            course.setDeliveryTime(DateUtil.parse(c.deliveryDate));
                            course.setCourseName("course1");
                            //TODO  Check
                            daoAdapter.saveCourse(course);

                            if (c.meals == null) {
                                continue;
                            }

                            for (SyncMeal m : c.meals) {
                                Meal meal = new Meal();
                                meal.setCourseId(course.getId());
                                meal.setUuid(m.uuid);
                                meal.setName(m.name);
                                meal.setTableNo("");
                                meal.setCourseName("");


                                daoAdapter.saveMeal(meal);

                                if (m.orderLines == null) {
                                    continue;
                                }

                                for (SyncOrderLine ol : m.orderLines) {
                                    OrderLine orderLine = new OrderLine();
                                    orderLine.setMealId(meal.getId());
                                    orderLine.setUuid(ol.uuid);

                                    orderLine.setRecipeId(ol.recipeUuid);
                                    orderLine.setQty(ol.quantity);
                                    orderLine.setOrderId(order.getId());
                                    orderLine.setModifiers("");
                                    orderLine.setCourseId(course.getId());


                                    daoAdapter.saveOrderLine(orderLine);
                                }
                            }
                            AssignmentFactory.getInstance().processOrder(context, order);
                        }
                    }
                }
                if (orders.size() > 0) {
                    for (Order order : orders) {
                        order.setModification(Order.MODIFICATION_NO);
                        daoAdapter.updateOrder(order);
                    }
                }
                if (successListener != null) {
                    successListener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<SyncOrderWrapper> call, Throwable t) {
                if (failListener != null) {
                    failListener.onFail();
                }
            }
        });
    }
}


