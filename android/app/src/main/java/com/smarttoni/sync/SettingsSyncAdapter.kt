package com.smarttoni.sync

import android.content.Context
import com.google.gson.JsonObject
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.RestaurantSettings
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback

class SettingsSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String,
                        successListener: AbstractSyncAdapter.SyncSuccessListener,
                        failListener: AbstractSyncAdapter.SyncFailListener) {
        daoAdapter.deleteRestaurantSettings()

        HttpClient(context).httpClient.synSettings(restaurantId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                if (response.body() != null) {
                    val settings = response.body()!!.getAsJsonObject("settings")
                    val restaurantSettings = RestaurantSettings()
                    restaurantSettings.enabledRecipeEdit = "\"1\"" == settings.get("recipe_edit_mode").toString()
                    if (settings.get("meals_to_same_chef") != null) {
                        restaurantSettings.assignMealToSameChef = "\"1\"" == settings.get("meals_to_same_chef").toString()
                    }
                    daoAdapter.saveRestaurantSettings(restaurantSettings)
                }

                successListener.onSuccess()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failListener.onFail()
            }
        })

    }


}