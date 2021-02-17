package com.smarttoni.assignment.order;

import android.content.Intent;
import android.text.TextUtils;

import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.transport.TransportHelper;
import com.smarttoni.assignment.util.RecipeHelper;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderRequest;
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

    private TaskTreeBuilder() {

    }

    public static TaskTreeBuilder getInstance() {
        return INSTANCE;
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



    public static Work buildWorkTree(Recipe recipe, NumberOfItemsAndActualQuantity quantity, Order order,OrderLine orderLine, Work next, Queue queue, boolean isMainRecipe) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Task> tasks = daoAdapter.loadTasks(recipe.getId());
        List<Work> works = new ArrayList<>();
        Map<String, Work> workMap = new HashMap<>();

        List<String> ids = new ArrayList<>();

        boolean recipeCompleted = false;

        float actualQty = quantity.numberOfItems;


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
            int qty = quantity.numberOfItems;// (int) orderLine.getQty();
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

            daoAdapter.saveInventoryRequirement(order.getId(),recipe.getId(),quantity.actualQuantity);


            if (qty == quantity.numberOfItems) {
                recipeCompleted = true;
            }

            //TODO Why????
//            if (qty == quantity.numberOfItems) {
//                recipeCompleted = true;
//            } else if (next == null) {
//                // if its main recipe
//                actualQty = orderLine.getQty() - qty;
//                //orderLine.setQty(actualQty);
//                //actualQty = recipe.getOutputQuantity() * orderLine.getQty();
//            } else {
//                actualQty = orderLine.getQty() - qty;
//            }
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

                        float actualQuantity = UnitHelper.convertToRecipeUnit(_recipe.getOutputUnitId(), _recipe, r.getQuantity());
                        float extra = (qty * _recipe.getOutputQuantity()) - actualQuantity;

                        NumberOfItemsAndActualQuantity q = new NumberOfItemsAndActualQuantity();
                        q.numberOfItems = qty;
                        q.actualQuantity = actualQuantity;
                        q.extra = extra;

                        Work end = buildWorkTree(_recipe, q, order, orderLine, work, queue, false);
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

                            daoAdapter.saveIngredientExtras(end.getId(),_recipe.getId(),extra);
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
}
