package com.smarttoni.assignment.order;

import android.content.Context;
import android.util.ArraySet;

import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.assignment.util.ActivityLogger;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.IngredientRequirement;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderManager {


    SmarttoniContext context;

    public OrderManager(SmarttoniContext context) {
        this.context = context;
    }


    public void removeExternalOrderRequests(String orderId) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<ExternalOrderRequest> eors = daoAdapter.listExternalOrderRequestForOrder(orderId);
        for (ExternalOrderRequest eor : eors) {
            List<ExternalAvailableQuantity> eaqs = daoAdapter.listExternalAvailableQuantity(eor.getExternalOrder(), eor.getRecipe());
            for (ExternalAvailableQuantity eaq : eaqs) {
                eaq.setQuantity(eaq.getQuantity() + eor.getQuantity());
                daoAdapter.updateExternalAvailableQuantity(eaq);
                break;
            }
        }
        daoAdapter.deleteExternalOrderRequestForParentOrder(orderId);
    }

    public void completeOrder(Context context, String userId, String orderId) {

        if (orderId == null) {
            return;
        }

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        Order order = daoAdapter.getOrderById(orderId);
        int status = order.getStatus();

        //if already deleted or completed , skip
        if (status == Order.ORDER_DELETED || status == Order.ORDER_COMPLETED) {
            return;
        }


        //Complete all tasks
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


        if (order.getType() == Order.TYPE_EXTERNAL) {

            if (order.getStatus() == Order.ORDER_COMPLETED) {

                List<ExternalOrderRequest> eors = daoAdapter.listExternalOrderRequestForExternalOrder(order.getId());

                Set<String> orderIds = new HashSet<>();
                for (ExternalOrderRequest eor : eors) {
                    orderIds.add(eor.getParentOrder());
                }

                List<ExternalAvailableQuantity> eaqs = daoAdapter.listExternalAvailableQuantityForOrder(order.getId());

                for (ExternalAvailableQuantity eaq : eaqs) {
                    InventoryManagement.moveToInventory(orderId, eaq.getRecipe(), eaq.getQuantity());
                }

                daoAdapter.deleteExternalOrderRequestForExternalOrder(order.getId());
                daoAdapter.deleteExternalAvailableQuantityForExternalOrder(order.getId());

                for (String oId : orderIds) {
                    Order o = daoAdapter.getOrderById(oId);
                    List<ExternalOrderRequest> _eors = daoAdapter.listExternalOrderRequestForOrder(o.getId());
                    if (_eors.isEmpty()) {
                        o.setChildOrderStatus(Order.EXTERNAL_ORDER_COMPLETED);
                        o.setProcessed(false);
                        daoAdapter.updateOrder(o);

                        AssignmentFactory.getInstance().processOrder(context, o);
                    }
                }

            }
        }


        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();

        System.gc();

        if (order.getIsInventory()) {
            SyncUpdater.Companion.getInstance().syncInventory(context);
        }
    }

    public boolean removeOrder(String orderId) {

        if (orderId == null) {
            return false;
        }

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        Order order = daoAdapter.getOrderById(orderId);

        int status = order.getStatus();
        //if already deleted or completed , skip
        if (status == Order.ORDER_DELETED || status == Order.ORDER_COMPLETED) {
            return false;
        }


        if (order.getType() == Order.TYPE_INTERNAL) {
//            List<Order> orders = daoAdapter.loadChildOrders(orderId);
//            for (Order _order : orders) {
//                List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(_order.getId());
//                if (_order.getStatus() == Order.ORDER_OPEN) {
//                    _order.setStatus(Order.ORDER_DELETED);
//                    _order.setModification(Order.MODIFICATION_DELETED);
//                    _order.setUpdatedAt(System.currentTimeMillis());
//                    daoAdapter.updateOrder(_order);
//                }
//            }
        } else if (order.getType() == Order.TYPE_EXTERNAL) {
            List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(orderId);
            for (OrderLine orderLine : orderLines) {
                if (orderLine.getRecipe() != null && orderLine.getRecipe().getInventoryType() == Recipe.INVENTORY_NO_STOCK) {
                    return false;
                }
            }
        }


        List<InventoryReservation> inventoryReservations = daoAdapter.listInventoryReservations(orderId);
        for (InventoryReservation ir : inventoryReservations) {
            InventoryManagement.moveToInventory(orderId, ir.getRecipeId(), ir.getQty());
        }
        //daoAdapter.removeInventoryReservations(orderId);

        removeWorksForOrder(orderId);

        _setOrderAsCompleted(order, true);

        if (order.getType() == Order.TYPE_INTERNAL) {
            removeExternalOrderRequests(orderId);
        }

//        else if (order.getType() == Order.TYPE_EXTERNAL) {
//            List<ExternalOrderRequest> eors = daoAdapter.listExternalOrderRequestForExternalOrder(order.getId());
//
//            //TODO create Inventory Reservation Request
//
//            daoAdapter.deleteExternalOrderRequestForExternalOrder(order.getId());
//            daoAdapter.deleteExternalAvailableQuantityForExternalOrder(order.getId());
//        }

        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();
        return true;
    }

    private boolean _setOrderAsCompleted(Order order, boolean isRemoved) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        TimerManager.getInstance().removeOrder(order.getId());
        ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                .getInterventionManager()
                .removeOrder(order.getId());


        if (order == null) {
            return false;
        }

        if (order.getStatus() == Order.ORDER_DELETED || order.getStatus() == Order.ORDER_COMPLETED) {
            return true;
        }

        if (isRemoved) {
            order.setStatus(Order.ORDER_DELETED);
            daoAdapter.putBackIngredientRequirementToInventory(order.getId());
        } else {
            order.setStatus(Order.ORDER_COMPLETED);
            if (order.getIsInventory()) {
                List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());
                for (OrderLine orderLine : orderLines) {
                    Recipe recipe = orderLine.getRecipe();
                    InventoryManagement.moveToInventory(order.getId(), recipe.getId(), orderLine.getQty() * recipe.getOutputQuantity());
                }
            }
        }

        unSetOnCall(order.getId());

        order.setIsUpdated(true);
        daoAdapter.updateOrder(order);


        daoAdapter.removeIngredientRequirementForOrder(order.getId());
        daoAdapter.removeInventoryReservations(order.getId());


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

    public void removeWorksForOrder(String orderId) {

        TaskManger workHelper = context.getTaskManger();
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<Work> list = getQueue().getCloneQueue();

        for (Work t : list) {
            boolean isNotCompletedTasks = t.getStatus() != Work.COMPLETED;
            if (orderId.equals(t.getOrderId())) {
                if ((t.getTransportType() & Work.TRANSPORT_FROM_INVENTORY) > 0) {
                    Recipe recipe = t.getRecipe();
                    InventoryManagement.moveToInventory(orderId, recipe.getId(), t.getQuantity() * recipe.getOutputQuantity());
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
                    workHelper.updateWorkStatus(t, Work.REMOVED);
                    getQueue().remove(t);
                    daoAdapter.updateWork(t);
                }
            }
        }
    }

    public void deleteOrderForEdit(String orderId) {


        removeExternalOrderRequests(orderId);

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        Queue queue = getQueue();
        List<Work> list = queue.getCloneQueue();
        for (Work work : list) {
            if (orderId.equals(work.getOrderId())) {
                queue.remove(work);
            }
        }

        daoAdapter.deleteWorksForOrder(orderId);
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
