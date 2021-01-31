package com.smarttoni.assignment.order;

import android.content.Intent;
import android.text.TextUtils;

import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.transport.TransportHelper;
import com.smarttoni.assignment.util.RecipeHelper;
import com.smarttoni.utils.Strings;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskIngredient;
import com.smarttoni.entities.Work;
import com.smarttoni.utils.Collections;
import com.smarttoni.utils.UnitHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskTreeBuilder {

    private static final TaskTreeBuilder INSTANCE = new TaskTreeBuilder();

    private TaskTreeBuilder(){

    }

    public static TaskTreeBuilder getInstance() {
        return INSTANCE;
    }


    enum CreateExternalOrder {
        CREATE_EXTERNAL_ORDER,
        INVENTORY_AVAILABLE,
        NO_EXTERNAL_ORDER
    }

    public static Work findLastTask(Work work) {
        if (work == null) {
            return null;
        }
        if (work.getNextTasks() == null || work.getNextTasks().size() == 0) {
            return work;
        } else {
            return findLastTask(work.getNextTasks().get(0));
        }
    }

    public static Work createWork(OrderLine orderLine, Task task) {
        Work t = new Work();
        t.setId(null);
        t.setQueueTime(System.currentTimeMillis());
        t.setTask(task);
        t.setOrderLine(orderLine);
        return t;
    }

    private static void createExternalOrders(Order order, Map<String, Quantity> qty) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        if (qty.isEmpty()) {
            //order.setStatus(Order.ORDER_READY_TO_START);
        } else {
            Map<String, List<OrderLine>> supplierOrderMap = new HashMap<>();
            //Group By Supplier & create Orders
            for (String recipeId : qty.keySet()) {

                if(qty.get(recipeId) == null){
                    continue;
                }

                int quantity = qty.get(recipeId).numberOfUnits;

                Recipe r = daoAdapter.getRecipeById(recipeId);
                String supplier = r.getSupplier();

                List<OrderLine> orderLines = supplierOrderMap.get(supplier);
                if (orderLines == null) {
                    orderLines = new ArrayList<>();
                    supplierOrderMap.put(supplier, orderLines);
                }

                OrderLine orderLine = new OrderLine();
                orderLine.setUuid(UUID.randomUUID().toString());
                orderLine.setRecipeId(recipeId);
                orderLine.setQty(quantity);
                daoAdapter.saveOrderLine(orderLine);


                orderLines.add(orderLine);

            }
            for (String supplier : supplierOrderMap.keySet()) {

                OrderBuilder orderBuilder = new OrderBuilder();


//                orderBuilder
//                        .createCourse()
//                        .setDeliveryTime(1313L)
//                        .createMeal()
//                        .addOrderLine("123", 1)
//                        .addOrderLine("qwe", 1)
//                        .saveMeal()
//                        .saveCourse()
//                        .build();


                List<OrderLine> orderLines = supplierOrderMap.get(supplier);
                if (orderLines == null || orderLines.isEmpty()) {
                    continue;
                }
                Order o = new Order();
                o.setId(UUID.randomUUID().toString());

                daoAdapter.addChildOrder(order.getId(), o.getId());

                o.setIsUpdated(true);
                long createAt = new Date().getTime();
                o.setUpdatedAt(createAt);
                o.setCreatedAt(createAt);
                order.setUpdatedAt(new Date().getTime());

                daoAdapter.saveOrder(o);

                Course c = new Course();
                c.setId(UUID.randomUUID().toString());
                c.setDeliveryTime(new Date().getTime());
                c.setOrderId(o.getId());
                //c.setIsOnCall(false);
                c.setIsOnCall(false);
                daoAdapter.saveCourse(c);

                Meal m = new Meal();
                m.setCourseId(c.getId());
                m.setUuid(UUID.randomUUID().toString());
                daoAdapter.saveMeal(m);

                for (OrderLine orderLine : orderLines) {
                    orderLine.setMealId(m.getId());
                    orderLine.setOrderId(o.getId());
                    orderLine.setCourseId(c.getId());
                    daoAdapter.saveOrderLine(orderLine);
                }
                o.setIsUpdated(true);
                daoAdapter.updateOrder(order);
            }
        }
    }

    private static CreateExternalOrder checkForExternalOrder(DaoAdapter daoAdapter,
                                                             String orderId,
                                                             Recipe recipe,
                                                             float required,
                                                             boolean reserveInventory,
                                                             boolean isInventoryOrder) {


        if (Strings.isNotEmpty(recipe.getSupplier()) && isInventoryOrder) {
            return CreateExternalOrder.CREATE_EXTERNAL_ORDER;
        }

        if (!isInventoryOrder && recipe.getInventoryType() == Recipe.INVENTORY_MANAGED) {
//            if (!reserveInventory) { // Only When Tree building
//                return  CreateExternalOrder.NO_EXTERNAL_ORDER;
//            }
//            if(!reserveInventory){
//                return  CreateExternalOrder.INVENTORY_AVAILABLE;
//            }
            Inventory inventory = InventoryManagement.checkInInventory(recipe.getId(), "", daoAdapter);
            if (inventory != null) {
                float _required = required * recipe.getOutputQuantity();
                float inventoryQty = inventory.getQuantity();
                if (inventoryQty > 0) {
                    if (_required <= inventoryQty) {
                        if (reserveInventory) {
                            //Take from inventory
                            InventoryManagement.takeFromInventory(orderId, inventory, required * recipe.getOutputQuantity(), daoAdapter);
                            InventoryReservation ir = new InventoryReservation();
                            ir.setOrderId(orderId);
                            ir.setRecipeId(recipe.getId());
                            ir.setQty(_required);
                            daoAdapter.saveInventoryReservation(ir);
                        }
                        return CreateExternalOrder.INVENTORY_AVAILABLE;
                    }
                }
            }
            if (Strings.isNotEmpty(recipe.getSupplier()) || (recipe.getType() == Recipe.TYPE_INGREDIENT)) {
                return CreateExternalOrder.CREATE_EXTERNAL_ORDER;
            }
        }

        if (recipe.getType() == Recipe.TYPE_INGREDIENT) {
            if (recipe.getInventoryType() == Recipe.INVENTORY_INFINITY) {
                return CreateExternalOrder.NO_EXTERNAL_ORDER;
            }
            return CreateExternalOrder.CREATE_EXTERNAL_ORDER;
        }

        if (Strings.isNotEmpty(recipe.getSupplier())) {
            return CreateExternalOrder.CREATE_EXTERNAL_ORDER;
        }

        return CreateExternalOrder.NO_EXTERNAL_ORDER;
        //return recipe.getType() == Recipe.TYPE_INGREDIENT &&
        //        recipe.getInventoryType() == Recipe.INVENTORY_NO_STOCK ? CreateExternalOrder.CREATE_EXTERNAL_ORDER : CreateExternalOrder.NO_EXTERNAL_ORDER;
    }

    private boolean recursivelyCreateExternalOrder(DaoAdapter daoAdapter, String orderId, Recipe recipe, Map<String, Quantity> qty, float required, boolean isInventoryOrder) {
        CreateExternalOrder cre = checkForExternalOrder(daoAdapter, orderId, recipe, required, true, isInventoryOrder);
        if (Strings.isNotEmpty(recipe.getId()) && cre != CreateExternalOrder.NO_EXTERNAL_ORDER) {
            if (cre == CreateExternalOrder.CREATE_EXTERNAL_ORDER) {
                return true;
            }
        }
        List<Task> tasks = daoAdapter.loadTasks(recipe.getId());
        for (Task task : tasks) {
            List<TaskIngredient> recipes = RecipeHelper.getPreviousRecipes(daoAdapter, task);
            if (recipes != null) {
                for (TaskIngredient taskIngredient : recipes) {
                    Recipe r = daoAdapter.getRecipeById(taskIngredient.getRecipeId());
                    int actualQty = UnitHelper.getRecipeQty(r.getOutputUnitId(), r, taskIngredient.getQuantity());

                    if (recursivelyCreateExternalOrder(daoAdapter, orderId, r, qty, required * actualQty, false)) {
                        Quantity currentQty = qty.get(r.getId());
                        if (currentQty == null) {
                            currentQty = new Quantity();
                            currentQty.numberOfUnits = 0;
                            currentQty.required = 0;
                        }
//                        currentQty.required =  (required * actualQty) + currentQty;
//                        currentQty.numberOfUnits =  (required * actualQty) + currentQty;
//                        if (currentQty != null) {
//                            qty.put(r.getId(), (required * actualQty) + currentQty);
//                        } else {
//                            qty.put(r.getId(), (required * actualQty));
//                        }
                    }

                }
            }
        }
        return false;
    }

    public  boolean parseAndCheckExternalOrders(Order order) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());

        Map<String, Quantity> qty = new HashMap<>();

        for (OrderLine orderLine : orderLines) {
            Recipe recipe = orderLine.getRecipe();
            recursivelyCreateExternalOrder(daoAdapter, order.getId(), recipe, qty, orderLine.getQty(), order.getIsInventory());
        }

        if (qty.isEmpty()) {
            return false;
        }
        createExternalOrders(order, qty);
        return true;
    }

    public static Work buildWorkTree(Recipe recipe, float required, Order order, OrderLine orderLine, Work next, Queue queue, boolean isMainRecipe) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Task> tasks = daoAdapter.loadTasks(recipe.getId());
        List<Work> works = new ArrayList<>();
        Map<String, Work> workMap = new HashMap<>();

        List<String> ids = new ArrayList<>();

        boolean recipeCompleted = false;

        float actualQty = required;


        boolean hasInventoryQty = recipe.getInventoryType() == Recipe.INVENTORY_INFINITY;
        if (!hasInventoryQty && !order.getIsInventory()) {
            if (Collections.isNotEmpty(daoAdapter.listInventoryReservations(order.getId(), recipe.getId()))) {
                hasInventoryQty = true;
            }
            //TODO Check
            if (recipe.getType() == Recipe.TYPE_INGREDIENT || Strings.isNotEmpty(recipe.getSupplier())) {
                hasInventoryQty = true;
            }
        }
        //if(!order.getIsInventory() && checkForExternalOrder(daoAdapter,order.getId(),recipe,0,false,(isMainRecipe && order.getIsInventory())) != CreateExternalOrder.NO_EXTERNAL_ORDER){
        if (!order.getIsInventory() && hasInventoryQty) {
            int qty = (int) orderLine.getQty();
            float inventoryTakeQty = qty * recipe.getOutputQuantity();

            Work t = null;
            if (next == null) {
                t = TransportHelper.createTransportationTask(null, null, recipe, Work.TRANSPORT_FROM_INVENTORY, orderLine, inventoryTakeQty);
                if (t != null) {
                    t.setIsEndNode(true);
                }
            } else {
                t = TransportHelper.createTransportationTask(null, next, recipe, Work.TRANSPORT_TO_LOCATION, orderLine, inventoryTakeQty);
            }
            if (t != null) {
                daoAdapter.saveWork(t);
                works.add(t);
            }

            //TODO Why????
            if (qty == orderLine.getQty()) {
                recipeCompleted = true;
            } else if (next == null) {
                // if its main recipe
                actualQty = orderLine.getQty() - qty;
                orderLine.setQty(actualQty);
                //actualQty = recipe.getOutputQuantity() * orderLine.getQty();
            } else {
                actualQty = orderLine.getQty() - qty;
            }
        }

        // Old Inventory Logic, Now Only for manged and no supplier recipe
        /*boolean haveInfinityQty = recipe.getInventoryType() == Recipe.INVENTORY_INFINITY;
        if (!order.getIsInventory() && (haveInfinityQty || (recipe.getInventoryType() == Recipe.INVENTORY_MANAGED && Strings.isEmpty(recipe.getSupplier())))) {
            Inventory inventory = null;
            if(!haveInfinityQty){
                inventory = InventoryManagement.checkInInventory(recipe.getId(), orderLine.getModifiers(), daoAdapter);
            }
            if (haveInfinityQty || inventory != null) {
                int inventoryQty = 0;
                if(inventory != null){
                    inventoryQty= (int) (inventory.getQuantity() / recipe.getOutputQuantity());
                }

                if (haveInfinityQty || inventoryQty > 0) {
                    int qty =  0;
                    float inventoryTakeQty = 0;
                    if(!haveInfinityQty){
                        qty = orderLine.getQty() <= inventoryQty ? (int) orderLine.getQty() : inventoryQty;
                        inventoryTakeQty = qty * recipe.getOutputQuantity();
                        InventoryManagement.takeFromInventory(inventory, inventoryTakeQty, daoAdapter);
                    }else{
                        qty = (int) orderLine.getQty();
                        inventoryTakeQty = qty * recipe.getOutputQuantity();
                    }
                    //TODO SyncUpdater.Companion.getInstance().syncInventory(context);
                    //TransportHelper.createInventoryTransportationTask(daoAdapter, Work.TRANSPORT_FROM_INVENTORY, recipe, orderLine, processedTasks, null);
                    //Work t =TransportHelper.createTransportationTask(null,next,recipe,Work.TRANSPORT_TO_LOCATION,orderLine);
                    Work t = null;
                    if (next == null) {
                        t = TransportHelper.createTransportationTask(null, null, recipe, Work.TRANSPORT_FROM_INVENTORY, orderLine, inventoryTakeQty);
                        if (t != null) {
                            t.setIsEndNode(true);
                        }
                    } else {
                        t = TransportHelper.createTransportationTask(null, next, recipe, Work.TRANSPORT_TO_LOCATION, orderLine, inventoryTakeQty);
                    }
                    if (t != null) {
                        daoAdapter.saveWork(t);
                        works.add(t);
                    }

                    if (qty == orderLine.getQty()) {
                        recipeCompleted = true;
                    } else if (next == null) {
                        // if its main recipe
                        actualQty = orderLine.getQty() - qty;
                        orderLine.setQty(actualQty);
                        //actualQty = recipe.getOutputQuantity() * orderLine.getQty();
                    } else {
                        actualQty = orderLine.getQty() - qty;
                    }
                }

            }
        }*/

        if (!recipeCompleted) {
            // List<Work> works = recipeWorks.get(recipe.getId());
            for (Task task : tasks) {
                Work work = createWork(orderLine, task);
                work.setRecipe(recipe);
                work.setOrder(order);
                work.setStatus(Work.SCHEDULED);
                work.setQueueTime(System.currentTimeMillis());
                work.setQuantity(actualQty);
                work.setCourseId(orderLine.getOrderId());
                work.setMealsId(orderLine.getMealId());

                daoAdapter.saveWork(work);

                List<TaskIngredient> recipes = RecipeHelper.getPreviousRecipes(daoAdapter, work.getTask());
                if (recipes != null) {
                    for (TaskIngredient r : recipes) {
                        Recipe _recipe = daoAdapter.getRecipeById(r.getRecipeId());
                        int qty = UnitHelper.getRecipeQty(_recipe.getOutputUnitId(), _recipe, r.getQuantity());

                        float requred = UnitHelper.convertToRecipeUnit(_recipe.getOutputUnitId(), _recipe, r.getQuantity());
                        float extra = (qty * _recipe.getOutputQuantity()) - requred;

                        Work end = buildWorkTree(_recipe, qty, order, orderLine, work, queue, false);
                        if (end != null && work.getTransportType() == 0) {
                            String _currentExtras = Strings.isNotEmpty(end.getExtraQuantity()) ? end.getExtraQuantity() : "";
                            String _newExtras = new StringBuilder(_currentExtras)
                                    .append(_recipe.getId())
                                    .append(":")
                                    .append(extra)
                                    .append("&")
                                    .toString();
                            end.setExtraQuantity(_newExtras);
                            daoAdapter.updateWork(end);
                        }
                        ids.add(_recipe.getId());
                        //subRecipes.add(r.getId());
                        //ids.add(r.getRecipeId());
                        //new TaskTreeBuilder().process(context, daoAdapter, mQueue, work, orderLine, order, r, modifierAndWorks);
                    }
                }

                workMap.put(task.getId(), work);
                works.add(work);
            }

            for (Task task : tasks) {
                Work work = workMap.get(task.getId());
                if (work == null) {
                    continue;
                }
                List<Task> dependentTasks = RecipeHelper.getPreviousTasks(daoAdapter, task);
                if (dependentTasks == null) {
                    continue;
                }


                for (Task t : dependentTasks) {
                    Work w = workMap.get(t.getId());
                    if (w != null) {
                        Work transport = TransportHelper.createTransportationTask(w, work, recipe, Work.TRANSPORT_TO_LOCATION, orderLine, 0);
                        if (transport != null) {
                            daoAdapter.saveWork(transport);
                            works.add(transport);
                        }
                        //work.addPrevTask(w);
                        //w.addNextTask(work);
                    }
                }

            }

            // Update PreviousTaskIds to init after server restart
            for (Work w : works) {
                if (w.getPrevTasks() != null) {
                    for (Work t : w.getPrevTasks()) {
                        w.addPreviousTaskIds(t.getId());
                    }
                }
                daoAdapter.updateWork(w);
            }
        }

        if (works.isEmpty() && order.getIsInventory() && isMainRecipe) {
            Work work = TransportHelper.createTransportationTask(null, null, recipe, Work.TRANSPORT_TO_INVENTORY, orderLine, orderLine.getQty() * recipe.getOutputQuantity());
            daoAdapter.saveWork(work);
            works.add(work);
        }
        Work lastWork = null;
        if (!works.isEmpty()) {
            lastWork = findLastTask(works.get(0));
            lastWork.setSubRecipes(TextUtils.join(",", ids));
            if (lastWork != null) {
                lastWork.setIsEndNode(true);
                lastWork.setIsUsed(false);
                if (next != null && lastWork != next) {
                    lastWork.addNextTask(next);
                    next.addPrevTask(lastWork);
                }
            }
        }
        queue.addAll(works);

        return lastWork;
    }

    public class Quantity {
        float required;
        int numberOfUnits;
    }
}
