package com.smarttoni.assignment.order;

import android.content.Context;

import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.assignment.util.ActivityLogger;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderReservation;
import com.smarttoni.utils.Strings;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Work;
import com.smarttoni.sync.SyncUpdater;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {


    SmarttoniContext context;

    public OrderManager(SmarttoniContext context) {
        this.context = context;
    }

    public void completeOrder(Context context, String userId, String orderId) {
        if (orderId == null) {
            return;
        }
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Order order = daoAdapter.getOrderById(orderId);
        int status = order.getStatus();
        if (status == Order.ORDER_DELETED || status == Order.ORDER_COMPLETED) {
            return;
        }
        List<Work> list = getQueue().getCloneQueue();
        for (Work t : list) {
            if (orderId.equals(t.getOrderId())) {
                TaskManger workHelper = this.context.getTaskManger();
                workHelper.updateWorkStatus(t, Work.COMPLETED);
                t.setUserId(userId);
                getQueue().remove(t);
                daoAdapter.updateWork(t);
                ActivityLogger.log(daoAdapter, t);
//                if (t.getMachine() != null) {
//                    t.getMachine().completeTask(t);
//                    daoAdapter.updateMachine(t.getMachine());
//                }
            }
        }
        _setOrderAsCompleted(order, false);
        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();

        if (order.getType() == Order.TYPE_EXTERNAL && order.getStatus() == Order.ORDER_COMPLETED) {
            //daoAdapter.loadChildOrders()

            List<Order> orders = daoAdapter.loadParentOrders(orderId);
            for (Order o : orders) {
                if (o != null) {
                    o.setChildOrderStatus(Order.EXTERNAL_ORDER_COMPLETED);
                    o.setProcessed(false);
                    daoAdapter.updateOrder(o);
//                    if (daoAdapter.loadIncompleteChildOrders(order.getParentOrderId()).isEmpty()) {
//                        if (o.getStatus() == Order.ORDER_DELETED) {
//                            List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());
//                            if (list.size() != orderLines.size()) {
//                                outLoop:
//                                for (OrderLine orderLine : orderLines) {
//                                    Recipe recipe = orderLine.getRecipe();
//                                    InventoryManagement.moveToInventory(orderId, recipe.getId(), orderLine.getQty() * recipe.getOutputQuantity(), daoAdapter);
//                                }
//                            }
//                            //TODO Refactor
//                        }
                    ExternalAvailableQuantity availableQuantity = daoAdapter.getExternalAvailableQuantity(orderId);
                    if(availableQuantity != null){
                        InventoryManagement.moveToInventory(orderId, availableQuantity.getRecipe(), availableQuantity.getQty(), daoAdapter);
                    }
                    //  AssignmentFactory.getInstance().processOrder(context, o);
                }
            }
        }

        System.gc();

        if (order.getIsInventory() && order.getStatus() == Order.ORDER_COMPLETED) {
            SyncUpdater.Companion.getInstance().syncInventory(context);
        }
    }

    private boolean _setOrderAsCompleted(Order order, boolean isRemoved) {

        TimerManager.getInstance().removeOrder(order.getId());
        ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                .getInterventionManager()
                .removeOrder(order.getId());

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        if (order == null) {
            return false;
        }
        if (order.getStatus() == Order.ORDER_DELETED || order.getStatus() == Order.ORDER_COMPLETED) {
            return true;
        }
        if (isRemoved) {
            order.setStatus(Order.ORDER_DELETED);
        } else {
            order.setStatus(Order.ORDER_COMPLETED);
        }
        unSetOnCall(order.getId());
        if (isRemoved || order.getIsInventory()) {
            updateInventory(daoAdapter, order, isRemoved);
        }
        order.setIsUpdated(true);
        daoAdapter.updateOrder(order);
        return !isRemoved;
    }

    private void unSetOnCall(String id) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Course> courses = daoAdapter.loadCourses(id);
        for (Course c : courses) {
            if (c.getIsOnCall()) {
                c.setIsOnCall(false);
                c.setActualDeliveryTime(System.currentTimeMillis());
                daoAdapter.updateCourse(c);
            }
        }
    }

    private void updateInventory(DaoAdapter daoAdapter, Order order, boolean isOrderRemoved) {

        List<Long> list = new ArrayList<>();
        List<Work> works = daoAdapter.getAllUnusedWorkEndNodes(order.getId());
        for (Work work : works) {
            if (work.getIsEndNode() && !work.getIsUsed()) {
                list.add(work.getOrderLineId());
                if (!isOrderRemoved || (isOrderRemoved && work.getStatus() == Work.COMPLETED)) {
                    Recipe recipe = work.getRecipe();
                    InventoryManagement.moveToInventory(order.getId(), recipe.getId(), work.getQuantity() * recipe.getOutputQuantity(), daoAdapter);
                }
            }
        }

        if (!isOrderRemoved) {
            List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());
            if (list.size() != orderLines.size()) {
                outLoop:
                for (OrderLine orderLine : orderLines) {
                    for (Long i : list) {
                        if (i.equals(orderLine.getId())) {
                            continue outLoop;
                        }
                    }
                    Recipe recipe = orderLine.getRecipe();
                    InventoryManagement.moveToInventory(order.getId(), recipe.getId(), orderLine.getQty() * recipe.getOutputQuantity(), daoAdapter);
                }
            }
        }

//        outLoop:
//        for (OrderLine orderLine : orderLines) {
//            Recipe recipe = orderLine.getRecipe();
//            if (notCompleted != null) {
//                for (String s : notCompleted) {
//                    if (s.equals(orderLine.getMealId() + "*" + recipe.getId())) {
//                        continue outLoop;
//                    }
//                }
//            }
//
//            InventoryManagement.moveToInventory(context, recipe.getId(), orderLine.getMealId(),  recipe.getOutputQuantity());
//            //TODO recipe.setOrderId(order.getId());
//            DbOpenHelper
//                    .getDaoSession(context)
//                    .getRecipeDao()
//                    .update(recipe);
//        }

        //TODO SyncUpdater.Companion.getInstance().syncInventory(context);
    }

    public boolean isOrderStarted(Context context, String orderId) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Order order = daoAdapter.getOrderById(orderId);

        List<Work> list = daoAdapter.getWorkByStatus();
        for (Work t : list) {
            if (t.getOrder() != null) {
                if (t.getOrder().getId().equals(orderId)) {
                    order.setIsUpdated(true);
                    daoAdapter.updateOrder(order);
                    return true;
                }
            }
        }

        return order.getIsStarted();
    }

    public void removeWorksForOrder(String orderId, List<String> nonCompleted) {

        TaskManger workHelper = context.getTaskManger();
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<Work> list = getQueue().getCloneQueue();

        for (Work t : list) {
            boolean isNotCompletedTasks = t.getStatus() != Work.COMPLETED;
            if (orderId.equals(t.getOrderId())) {
                if ((t.getTransportType() & Work.TRANSPORT_FROM_INVENTORY) > 0) {
                    Recipe recipe = t.getRecipe();
                    float qty = t.getQuantity();
                    InventoryManagement.moveToInventory(orderId, recipe.getId(), t.getQuantity() * recipe.getOutputQuantity(), daoAdapter);
                }
                if (isNotCompletedTasks) {
                    if (t.getStatus() == Work.SYNERGY) {
                        Work parent = t.getSynergyParent();
                        if (parent != null && parent.getSynergyList() != null) {
                            parent.getSynergyList().remove(t);
                        }
                    } else if (t.getSynergyList() != null) {
                        if (t.getSynergyList().size() > 0) {
                            Work newParent = t.getSynergyList().get(0);
                            workHelper.updateWorkStatus(newParent, t.getStatus());
                            t.getSynergyList().remove(newParent);
                            newParent.setSynergyList(t.getSynergyList());
                        }
                    }
                    if (nonCompleted != null) {
                        nonCompleted.add(t.getMealsId() + "*" + t.getRecipeId());
                    }
                    workHelper.updateWorkStatus(t, Work.REMOVED);
                    getQueue().remove(t);
                    daoAdapter.updateWork(t);
//                    if (t.getMachine() != null) {
//                        t.getMachine().completeTask(t);
//                        daoAdapter.updateMachine(t.getMachine());
//                    }
                }
            }
        }
    }

    public void deleteOrderForEdit(Context context, String orderId, boolean deleteOrderFromDatabase) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Order> childOrders = daoAdapter.loadChildOrders(orderId);
        if (childOrders != null) {
            for (Order o : childOrders) {
                o.setModification(Order.MODIFICATION_DELETED);
                o.setStatus(Order.ORDER_DELETED);
                daoAdapter.updateOrder(o);
            }
        }

        Queue queue = getQueue();
        List<Work> list = queue.getCloneQueue();
        for (Work work : list) {
            if (orderId.equals(work.getOrderId())) {
                queue.remove(work);
            }
        }
        if (deleteOrderFromDatabase) {

            daoAdapter.deleteWorksForOrder(orderId);
        }
    }

    public void removeOrder(Context context, String orderId) {
        if (orderId == null) {
            return;
        }
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<Order> orders = daoAdapter.loadChildOrders(orderId);

        for (Order _order : orders) {
            if (_order.getStatus() == Order.ORDER_OPEN) {
                _order.setStatus(Order.ORDER_DELETED);
                _order.setModification(Order.MODIFICATION_DELETED);
                _order.setUpdatedAt(System.currentTimeMillis());
                daoAdapter.updateOrder(_order);
            }
        }


        List<InventoryReservation> inventoryReservations = daoAdapter.listInventoryReservations(orderId);
        for (InventoryReservation ir : inventoryReservations) {
            InventoryManagement.moveToInventory(orderId, ir.getRecipeId(), ir.getQty(), daoAdapter);
        }
        daoAdapter.removeInventoryReservations(orderId);

        List<String> nonCompleted = new ArrayList<>();

        removeWorksForOrder(orderId, nonCompleted);

        Order order = daoAdapter.getOrderById(orderId);
        _setOrderAsCompleted(order, true);
        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();
    }


    public boolean checkOrderCompleted(Order order, Meal meal) {
        boolean isCompleted = false;
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        boolean orderCompleted = true;
        boolean mealsCompleted = true;
        List<Work> list = getQueue().getCloneQueue();
        for (Work queue : list) {
            if (queue.getOrder().getId().equals(order.getId()) && queue.getStatus() != Work.COMPLETED) {
                orderCompleted = false;
            }
            if (meal != null && queue.getMeal() != null && queue.getMeal().getId().equals(meal.getId()) && queue.getStatus() != Work.COMPLETED) {
                mealsCompleted = false;
            }
        }
        if (orderCompleted) {
            isCompleted = _setOrderAsCompleted(order, false);
        }
        if (mealsCompleted) {
            if (meal != null) {
                meal = daoAdapter.getMealById(meal.getId());
                meal.setStatus(1);
                daoAdapter.updateMeal(meal);
            }
        }
        return isCompleted;
    }

    private Queue getQueue() {
        return ServiceLocator.getInstance().getQueue();
    }
}
