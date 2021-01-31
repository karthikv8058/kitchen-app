package com.smarttoni.sync

import android.content.Context
import com.smarttoni.assignment.InventoryManagement
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Inventory
import com.smarttoni.http.HttpClient
import com.smarttoni.server_app.models.UsersResponse.InventoryList
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventorySyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        val inventories = daoAdapter.loadAllInventories()
        val inventoryList: ArrayList<InventoryList> = ArrayList()

        for (i in inventories) {
            val inventory = InventoryList()
            inventory.quantity = i.quantity
            inventory.recipeId = i.recipeUuid
            inventoryList.add(inventory)
        }

        HttpClient(context).httpClient.updateInventory(restaurantId.toString(), inventoryList).enqueue(object : Callback<List<InventoryList>> {
            override fun onResponse(call: Call<List<InventoryList>>, response: Response<List<InventoryList>>) {
                val inventoryResponses = response.body() ?: return
                Thread(Runnable {
                    val inventories = ArrayList<Inventory>()
                    for (inventoryResponse in inventoryResponses) {
                        val inventory = Inventory()
                        inventory.id = inventoryResponse.id
                        inventory.recipeUuid = inventoryResponse.recipeId
                        inventory.quantity = inventoryResponse.quantity
                        inventory.isDeleted = inventoryResponse.isDeleted
                        inventory.isUpdated="0"
                        try {
                            if (inventoryResponse.isDeleted) {
                                val recipeData = JSONObject(inventoryResponse.deletedDetails)
                                inventory.image = recipeData.getString("image_url")
                                val title = recipeData.getJSONObject("en")
                                inventory.recipeName = title.getString("name")
                            }
                        } catch (e: JSONException) {
                        }
                        inventories.add(inventory)
                    }

                    InventoryManagement.saveInventory(context,daoAdapter, inventories)
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<InventoryList>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }
}
