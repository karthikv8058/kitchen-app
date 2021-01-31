package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.InventoryRequest;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Recipe;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class DeleteExternalOrder extends HttpSecurityRequest {

    private Context context;

    public DeleteExternalOrder(Context context) {
        this.context = context;
    }

    private boolean canDelete(String orderId) {
        DaoAdapter daoAdapter = new GreenDaoAdapter(context);
        List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(orderId);
        for (OrderLine orderLine : orderLines) {
            Recipe recipe = orderLine.getRecipe();
            if (recipe.getInventoryType() == Recipe.INVENTORY_NO_STOCK) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String orderId = jsonObject.getString("orderId");
            DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
            Order order = daoAdapter.getOrderById(orderId);
            boolean isDeleted = order.getStatus() != Order.ORDER_STARTED && canDelete(orderId);
            if (isDeleted) {
                order.setUpdatedAt(System.currentTimeMillis());
                order.setModification(Order.MODIFICATION_DELETED);
                order.setStatus(Order.ORDER_DELETED);
//                List<OrderLine> orderLines = daoAdapter.getOrderLinesByOrder(orderId);
//                for (OrderLine orderLine : orderLines) {
//                    InventoryRequest inventoryRequest = new InventoryRequest();
//                    inventoryRequest.setOrderId(order.getParentOrderId());
//                    inventoryRequest.setRecipeId(orderLine.getRecipeId());
//                    inventoryRequest.setQty(orderLine.getQty());
//                    daoAdapter.saveInventoryRequest(inventoryRequest);
//                }
//                daoAdapter.updateOrder(order);

                List<ExternalOrderRequest> list = daoAdapter.listExternalOrderRequestFor(orderId);
                for(ExternalOrderRequest eor: list){
                    InventoryRequest inventoryRequest = new InventoryRequest();
                    inventoryRequest.setOrderId(eor.getRequestedOrder());
                    inventoryRequest.setRecipeId(eor.getRecipe());
                    inventoryRequest.setQty(eor.getQty());
                    daoAdapter.saveInventoryRequest(inventoryRequest);
                }
            }
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(isDeleted, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}