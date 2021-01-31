package com.smarttoni.server.controlles.inventory;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Inventory;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.sync.SyncUpdater;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.UnitHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class UpdateInventory extends HttpSecurityRequest {

    private Context context;
    private GreenDaoAdapter greenDaoAdapter;

    public UpdateInventory(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String inventoryId = jsonObject.getString("inventoryId");
            long inventoryQuantity = Long.parseLong(jsonObject.getString("inventoryQuantity"));
            String unit = jsonObject.getString("unit");
            Inventory inventory = greenDaoAdapter.getInventoryById(inventoryId);
            if (inventory != null) {
                float qty = UnitHelper.convertToUnit(unit,inventory.getRecipe().getOutputUnitId(),inventoryQuantity);
                inventory.setQuantity(qty);
                greenDaoAdapter.updateInventoryDao(inventory);
                SyncUpdater.Companion.getInstance().syncInventory(context);
            }
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}