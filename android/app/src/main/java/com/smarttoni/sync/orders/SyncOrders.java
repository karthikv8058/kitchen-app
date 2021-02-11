package com.smarttoni.sync.orders;

import com.smarttoni.utils.Strings;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncOrders {
    public List<SyncOrder> sync(DaoAdapter daoAdapter, List<Order> orders) {
        List<SyncOrder> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == Order.ORDER_COMPLETED && !order.getIsUpdated()) {
                continue;
            }
            SyncOrder o = new SyncOrder();
            o.uuid = order.getId();
            o.updatedAt = DateUtil.formatStandardDate(new Date(order.getUpdatedAt()));
            o.createdAt = DateUtil.formatStandardDate(new Date(order.getCreatedAt()));
//            if(Strings.isEmpty(o.parentOrderUuid)){
//                o.status = order.getStatus() == Order.ORDER_OPEN && order.getIsStarted() ? Order.ORDER_STARTED : order.getStatus();
//            }else{
//                o.status = order.getStatus();
//            }
            o.status = order.getStatus();
            o.inventoryOrder = order.getIsInventory();
            o.childOrderStatus = order.getChildOrderStatus();
            o.type = order.getType();
            if (order.getIsUpdated()) {
                o.table = order.getTableNo();
                o.courses = new ArrayList<>();
                for (Course course : order.getCourses()) {
                    SyncCourse c = new SyncCourse();
                    c.uuid = course.getId();
                    c.onCall = course.getIsOnCall();
                    c.deliveryDate = DateUtil.formatStandardDate(new Date(course.getActualDeliveryTime()));
                    c.meals = new ArrayList<>();
                    for (Meal meal : course.getMeals()) {
                        SyncMeal m = new SyncMeal();
                        m.uuid = meal.getUuid();
                        m.name = meal.getName();
                        m.orderLines = new ArrayList<>();
                        for (OrderLine orderLine : meal.getOrderLine()) {
                            SyncOrderLine ol = new SyncOrderLine();
                            ol.uuid = orderLine.getUuid();
                            ol.recipeUuid = orderLine.getRecipeId();
                            ol.quantity = orderLine.getQty();
                            m.orderLines.add(ol);
                        }
                        c.meals.add(m);
                    }
                    o.courses.add(c);
                }
            }// if is updated
            list.add(o);
            order.setIsUpdated(false);
            daoAdapter.updateOrder(order);
        }
        return list;
    }

}
