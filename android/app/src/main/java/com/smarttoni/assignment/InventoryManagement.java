package com.smarttoni.assignment;

import android.content.Context;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryMovement;
import com.smarttoni.entities.InventoryRequest;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.utils.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class InventoryManagement {

    public static Inventory checkInInventory(String recipeId, String modifierSeparated, DaoAdapter daoAdapter) {
        Inventory inventory = daoAdapter.getFilteredInventory(recipeId);
        return inventory;
    }

    public static void moveToInventory(String orderId,String recipeId, float qty) {


        if(qty == 0){
            return;
        }

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();

        Inventory inventory = daoAdapter.getFilteredInventory(recipeId);

        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + qty);
            daoAdapter.updateInventoryDao(inventory);

        } else {
            inventory= new Inventory();
            inventory.setId(UUID.randomUUID().toString());
            inventory.setRecipeUuid(recipeId);
            inventory.setModifier("");
            inventory.setQuantity(qty);
            daoAdapter.saveInventoryDao(inventory);
        }

        InventoryMovement im = new InventoryMovement( UUID.randomUUID().toString(),
                InventoryMovement.INVENTORY_IN,
                orderId,
                inventory.getId(),
                qty,
                DateUtil.formatStandardDate(new Date()));

        daoAdapter.insertInventoryMovement(im);

    }

    public static void saveInventory(Context context, DaoAdapter daoAdapter, List<Inventory> inventories) {
        List<InventoryRequest> list = daoAdapter.loadAllInventoryRequests();
        for (InventoryRequest inventoryRequest : list) {
            for (Inventory inventory : inventories) {
                if (inventoryRequest.getRecipeId() != null && inventoryRequest.getRecipeId().equals(inventory.getRecipeUuid())) {

                    if (inventory.getQuantity() >= inventoryRequest.getQty()) {
                        inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getQty());
                        daoAdapter.deleteInventoryRequests(inventoryRequest);
                        Order order = daoAdapter.getOrderById(inventoryRequest.getOrderId());
                        if(order != null){
                            List<ExternalOrderRequest> _eors = daoAdapter
                                    .listExternalOrderRequestForOrder(order.getId());
                            if (_eors.isEmpty()) {
                                order.setChildOrderStatus(Order.EXTERNAL_ORDER_COMPLETED);
                                order.setProcessed(false);
                                daoAdapter.updateOrder(order);
                                AssignmentFactory.getInstance().processOrder(context, order);
                            }
                        }
                    } else if (inventory.getQuantity() > 0) {
                        inventoryRequest.setQty(inventoryRequest.getQty() - inventory.getQuantity());
                        inventory.setQuantity(0);
                        daoAdapter.deleteInventoryRequests(inventoryRequest);
                        daoAdapter.saveInventoryRequest(inventoryRequest);
                    }
                }
            }
        }
        daoAdapter.deleteAllAndSave(inventories);
    }

    public synchronized static void takeFromInventory(String orderId,Inventory inventory, float qty, DaoAdapter daoAdapter) {

        if(qty == 0){
            return;
        }

        InventoryMovement im = new InventoryMovement( UUID.randomUUID().toString(),
                InventoryMovement.INVENTORY_OUT,
                orderId,
                inventory.getId(),
                qty,
                DateUtil.formatStandardDate(new Date()));

        daoAdapter.insertInventoryMovement(im);

        inventory.setQuantity(inventory.getQuantity() - qty);
        daoAdapter.updateInventoryDao(inventory);
    }
}
