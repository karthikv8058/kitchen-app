package com.smarttoni.assignment.order;

import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.util.RecipeHelper;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskIngredient;
import com.smarttoni.utils.Strings;
import com.smarttoni.utils.UnitHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExternalOrderManager {

    public class Quantity {
        float required;
    }

    enum CreateExternalOrder {
        CREATE_EXTERNAL_ORDER,
        INVENTORY_AVAILABLE,
        NO_EXTERNAL_ORDER
    }

    private static final ExternalOrderManager INSTANCE = new ExternalOrderManager();

    private ExternalOrderManager() {

    }

    public static ExternalOrderManager getInstance() {
        return INSTANCE;
    }


    private static void createExternalOrders(Order order, Map<String, Quantity> qty) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        if (qty.isEmpty()) {
            //order.setStatus(Order.ORDER_READY_TO_START);
        } else {
            Map<String, List<OrderLine>> supplierOrderMap = new HashMap<>();
            Map<String, String> orderUUIDs = new HashMap<>();
            //Group By Supplier & create Orders
            outLoop:
            for (String recipeId : qty.keySet()) {

                Quantity q = qty.get(recipeId);
                if (q == null) {
                    continue;
                }


                List<ExternalAvailableQuantity> listExternalAvailableQuantityForRecipe = daoAdapter.listExternalAvailableQuantityForRecipe(recipeId);
                for (ExternalAvailableQuantity eaq : listExternalAvailableQuantityForRecipe) {
                    if (eaq.getQuantity() >= q.required) {
                        daoAdapter.addExternalOrderRequest(order.getId(), eaq.getOrder(), recipeId, q.required);
                        float available = eaq.getQuantity() - q.required;
                        eaq.setQuantity(available);
                        daoAdapter.updateExternalAvailableQuantity(eaq);
                        continue outLoop;
                    }
                }


                Recipe r = daoAdapter.getRecipeById(recipeId);

                int quantity = 0;
                if (r.getOutputQuantity() > 0) {
                    quantity = (int) Math.ceil(q.required / r.getOutputQuantity());
                }

                String supplier = r.getSupplier();

                List<OrderLine> orderLines = supplierOrderMap.get(supplier);
                if (orderLines == null) {
                    orderLines = new ArrayList<>();
                    supplierOrderMap.put(supplier, orderLines);
                    orderUUIDs.put(supplier, UUID.randomUUID().toString());
                }

                OrderLine orderLine = new OrderLine();
                orderLine.setId(UUID.randomUUID().toString());
                orderLine.setRecipeId(recipeId);
                orderLine.setQty(quantity);
                daoAdapter.saveOrderLine(orderLine);


                orderLines.add(orderLine);


                String externalOrderUUID = orderUUIDs.get(supplier);

                daoAdapter.addExternalOrderRequest(order.getId(),externalOrderUUID, orderLine.getRecipeId(), q.required);

                float total = r.getOutputQuantity() * orderLine.getQty();
                float available = total - q.required;

                if (available > 0) {
                    daoAdapter.addExternalAvailableQuantity(externalOrderUUID, orderLine.getRecipeId(), available);
                }

            }


            for (String supplier : supplierOrderMap.keySet()) {

                Order o = new Order();
                o.setId(orderUUIDs.get(supplier));

                List<OrderLine> orderLines = supplierOrderMap.get(supplier);
                if (orderLines == null || orderLines.isEmpty()) {
                    continue;
                }

                o.setType(Order.TYPE_EXTERNAL);
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
                m.setId(UUID.randomUUID().toString());
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
                                                                             float requiredQuantity,
                                                                             boolean reserveInventory,
                                                                             boolean isInventoryOrder) {


        if (Strings.isNotEmpty(recipe.getSupplier()) && isInventoryOrder) {
            return CreateExternalOrder.CREATE_EXTERNAL_ORDER;
        }

        if (!isInventoryOrder && recipe.getInventoryType() == Recipe.INVENTORY_MANAGED) {
            Inventory inventory = InventoryManagement.checkInInventory(recipe.getId(), "", daoAdapter);
            if (inventory != null) {
                float _required = requiredQuantity;
                float inventoryQty = inventory.getQuantity();
                if (inventoryQty > 0) {
                    if (_required <= inventoryQty) {
                        if (reserveInventory) {
                            //Take from inventory
                            InventoryManagement.takeFromInventory(orderId, inventory, requiredQuantity, daoAdapter);
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

    private boolean recursivelyCreateExternalOrder(DaoAdapter daoAdapter, String orderId, Recipe recipe, Map<String, Quantity> qty, float required, float actualQuantity, boolean isInventoryOrder) {
       CreateExternalOrder cre = checkForExternalOrder(daoAdapter, orderId, recipe, actualQuantity, true, isInventoryOrder);
        if (Strings.isNotEmpty(recipe.getId()) && cre == CreateExternalOrder.CREATE_EXTERNAL_ORDER) {
            return true;
        }else if(cre == CreateExternalOrder.INVENTORY_AVAILABLE){
            return false;
        }
        List<Task> tasks = daoAdapter.loadTasks(recipe.getId());
        for (Task task : tasks) {
            List<TaskIngredient> recipes = RecipeHelper.getPreviousRecipes(daoAdapter, task);
            if (recipes != null) {
                for (TaskIngredient taskIngredient : recipes) {
                    Recipe r = daoAdapter.getRecipeById(taskIngredient.getRecipeId());
                    int actualQty = UnitHelper.getRecipeQty(r.getOutputUnitId(), r, taskIngredient.getQuantity());

                    if (recursivelyCreateExternalOrder(daoAdapter, orderId, r, qty, required * actualQty, required * taskIngredient.getQuantity(), false)) {
                        Quantity currentQty = qty.get(r.getId());
                        if (currentQty == null) {
                            currentQty = new Quantity();
                            currentQty.required = 0;
                            qty.put(r.getId(), currentQty);
                        }
                        currentQty.required = (required * taskIngredient.getQuantity()) + currentQty.required;
                    }

                }
            }
        }
        return false;
    }

    public boolean parseAndCheckExternalOrders(Order order) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(order.getId());

        Map<String,Quantity> qty = new HashMap<>();

        for (OrderLine orderLine : orderLines) {
            Recipe recipe = orderLine.getRecipe();
            //TODO redundant code
            if(recursivelyCreateExternalOrder(daoAdapter, order.getId(), recipe, qty, orderLine.getQty(), orderLine.getQty() * recipe.getOutputQuantity(), order.getIsInventory())){
                Quantity currentQty = qty.get(recipe.getId());
                if (currentQty == null) {
                    currentQty = new Quantity();
                    currentQty.required = 0;
                    qty.put(recipe.getId(), currentQty);
                }
                currentQty.required = (orderLine.getQty() * recipe.getOutputQuantity()) + currentQty.required;
            }
        }

        if (qty.isEmpty()) {
            return false;
        }
        createExternalOrders(order, qty);
        return true;
    }
}
