package com.smarttoni.assignment.order;

import android.content.Context;

import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Work;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessor {

    private OrderProcessor() {
    }

    private static OrderProcessor INSTANCE = new OrderProcessor();

    public static OrderProcessor getInstance() {
        return INSTANCE;
    }

    private boolean inited = false;

    private ModifierAndWorks sameOrderSynergy(DaoAdapter daoAdapter, Recipe recipe, String modifierSeparated, Map<String, List<ModifierAndWorks>> modifierAndWorksMap) {
        List<ModifierAndWorks> listModifierAndWorks = null;
        if (recipe != null) {
            listModifierAndWorks = modifierAndWorksMap.get(recipe.getId());
        }
        ModifierAndWorks modifierAndWorks = null;
        if (listModifierAndWorks == null) {
            listModifierAndWorks = new ArrayList<>();
            modifierAndWorks = new ModifierAndWorks();
            modifierAndWorks.setModifier(modifierSeparated);
            listModifierAndWorks.add(modifierAndWorks);
            if (recipe != null)
                modifierAndWorksMap.put(recipe.getId(), listModifierAndWorks);
        } else {
            for (ModifierAndWorks works : listModifierAndWorks) {
                if (works.getModifier() != null && works.getModifier().equals(modifierSeparated)) {

                    List<Work> listWorks = works.getWorks();
                    if (listWorks != null) {
                        for (Work w : listWorks) {
                            w.setQuantity(w.getQuantity() + 1);
                            daoAdapter.updateWork(w);
                        }
                    }
                    //TODO Skip Doing Rest
                    //continue mainLoop;
                    return null;
                }
            }
            modifierAndWorks = new ModifierAndWorks();
            modifierAndWorks.setModifier(modifierSeparated);
            listModifierAndWorks.add(modifierAndWorks);
        }
        return modifierAndWorks;
    }

    /**
     * @param context
     * @param mQueue  Assignment Queue
     * @param order   Order to process
     *                <p>
     *                This Method will create Work object from the order
     *                a Work object contain (Task, Order , Modifier , Course , Status , Next & Prev Tasks etc.)
     *                <p>
     *                This Method create a data structure like this
     *                [1]\
     *                [2]-[4]->[5]
     *                [3]/
     *                <p>
     *                (Here 1,2,3,4 & 5 are Work object)
     *                After this method we add Work object to Assignment Queue
     */
    public synchronized void processOrder(Context context, Queue mQueue, Order order) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());
        Map<String, Float> recipeQty = new HashMap<>();

        if (order.getStatus() == Order.ORDER_OPEN && order.getChildOrderStatus() == Order.EXTERNAL_ORDER_COMPLETED) {

            if(daoAdapter.hasWaitingInventoryRequest(order.getId())){
                order.setProcessed(false);
               return;
            }

            boolean completedAllRecipes = true;

            for (OrderLine orderLine : orderLines) {
                Recipe recipe = orderLine.getRecipe();
                if(recipe == null){
                    order.setProcessed(false);
                    daoAdapter.updateOrderWithNoModification(order);
                    return;
                }
                String recipeId = recipe.getId();
                Float qty = (recipeQty.get(recipeId) != null ? recipeQty.get(recipeId) : 0)+orderLine.getQty();
                recipeQty.put(recipeId,qty);
            }


            for (OrderLine orderLine : orderLines) {
                Float qty = recipeQty.get(orderLine.getRecipe().getId());
                if (qty != null && qty != 0) {
                    orderLine.setQty(qty);

                    NumberOfItemsAndActualQuantity numberOfItemsAndActualQuantity = new NumberOfItemsAndActualQuantity();
                    numberOfItemsAndActualQuantity.numberOfItems = (int) orderLine.getQty();
                    numberOfItemsAndActualQuantity.actualQuantity = orderLine.getQty() * orderLine.getRecipe().getOutputQuantity();

                    if (TaskTreeBuilder.buildWorkTree(orderLine.getRecipe(),numberOfItemsAndActualQuantity, order, orderLine, null, mQueue,true)!= null) {
                        completedAllRecipes = false;
                    }
                }
                recipeQty.put(orderLine.getRecipe().getId(), null);
            }

            //Remove All Reservations
            daoAdapter.removeInventoryReservations(order.getId());

            if (completedAllRecipes) {
                OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
                orderManager.completeOrder(context, "", order.getId());
            }

        } else if(order.getChildOrderStatus()==Order.EXTERNAL_ORDER_NOT_CREATED){

            for (OrderLine orderLine : orderLines) {
                Recipe recipe = orderLine.getRecipe();
                if (recipe == null) {
                    order.setProcessed(false);
                    daoAdapter.updateOrderWithNoModification(order);
                    return;
                }
                String recipeId = recipe.getId();
                Float qty = (recipeQty.get(recipeId) != null ? recipeQty.get(recipeId) : 0) + orderLine.getQty();
                recipeQty.put(recipeId, qty);
            }

            boolean isOrderCreated = ExternalOrderManager.getInstance().parseAndCheckExternalOrders(order);
            if(isOrderCreated){
                order.setChildOrderStatus(Order.EXTERNAL_ORDER_CREATED);
                daoAdapter.updateOrder(order);
            }else{
                order.setChildOrderStatus(Order.EXTERNAL_ORDER_COMPLETED);
                daoAdapter.updateOrder(order);
                processOrder(context,mQueue,order);
               return;
            }
        }
        EventManager.getInstance().emit(Event.ORDER_PROCESSED, order.getId());

    }


    /**
     * Same As processOrder, but its create Work objects from DB
     *
     * @param mQueue
     */
    public void init(DaoAdapter daoAdapter, Queue mQueue) {
        if (inited) {
            return;
        }
        List<Work> queue = daoAdapter.loadNotCompletedWorks();
        Map<Long, Work> workMap = new HashMap<>();
        for (Work work : queue) {
            if (work.getOrder() == null) {
                daoAdapter.deleteWork(work);
                continue;
            } else if (work.getOrder().getStatus() == Order.ORDER_COMPLETED
                    || work.getOrder().getStatus() == Order.ORDER_DELETED) {
                work.setStatus(Work.COMPLETED);
                daoAdapter.saveWork(work);
                continue;
            }
            if (!work.isInventoryTransport()) {
                work.setTransportType(0);
            }
            workMap.put(work.getId(), work);
            work.setQueueTime(System.currentTimeMillis());
            if (work.getStatus() == Work.STARTED || work.getStatus() == Work.SYNERGY) {
                work.setUserId(null);
                work.setUser(null);
                work.setStatus(Work.QUEUED);
            }
        }

        for (Work work : queue) {
            String previousTaskIds = work.getPreviousTaskIds();
            if (previousTaskIds != null) {
                String[] ids = previousTaskIds.split(",");
                for (String id :
                        ids) {
                    try {
                        long workId = Long.parseLong(id);
                        Work previous = workMap.get(workId);
                        if (previous != null) {
                            work.addPrevTask(previous);
                            previous.addNextTask(work);
                        }
                    } catch (Exception e) {
                        continue;
                    }

                }
            }
            mQueue.add(work);
        }
        inited = true;
    }

    class ModifierAndWorks {
        private String modifier;
        private List<Work> works;

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public List<Work> getWorks() {
            return works;
        }

        public void setWorks(List<Work> works) {
            this.works = works;
        }
    }
}

