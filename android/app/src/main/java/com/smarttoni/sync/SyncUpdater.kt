package com.smarttoni.sync

import android.content.Context
import com.smarttoni.assignment.InventoryManagement
import com.smarttoni.assignment.service.ServiceLocator
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Inventory
import com.smarttoni.http.HttpClient
import com.smarttoni.server_app.models.UsersResponse.InventoryList
import com.smarttoni.utils.LocalStorage
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncUpdater private constructor() {

    fun syncInventory(context: Context) {
        val daoAdapter: DaoAdapter = ServiceLocator.getInstance().databaseAdapter
        val inventories = daoAdapter.loadAllInventories()
        val inventoryList: ArrayList<InventoryList> = ArrayList()
        for (i in inventories) {
            val inventory = InventoryList()
            inventory.quantity = i.quantity
            inventory.recipeId = i.recipeUuid
            inventoryList.add(inventory)
        }

        HttpClient(context).httpClient.updateInventory(LocalStorage(context).restaurantId, inventoryList).enqueue(object : Callback<List<InventoryList>> {
            override fun onResponse(call: Call<List<InventoryList>>, response: Response<List<InventoryList>>) {
                val inventoryResponses = response.body() ?: return
                Thread(Runnable {
                    val inventories = ArrayList<Inventory>()
                    for (inventoryResponse in inventoryResponses) {
                        val inventory = Inventory()
                        inventory.id = inventoryResponse.id
                        inventory.recipeUuid = inventoryResponse.recipeId
                        inventory.modifier = if (inventoryResponse.modifier != null) inventoryResponse.modifier else ""
                        inventory.quantity = inventoryResponse.quantity
                        inventory.isDeleted = inventoryResponse.isDeleted
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
                    InventoryManagement.saveInventory(context,ServiceLocator.getInstance().databaseAdapter, inventories)
                }).start()
            }

            override fun onFailure(call: Call<List<InventoryList>>, t: Throwable) {

            }
        })
    }

    companion object {

        val instance = SyncUpdater()
    }

}
