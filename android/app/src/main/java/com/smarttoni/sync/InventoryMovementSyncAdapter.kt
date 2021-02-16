package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryMovementSyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {

        val orders = daoAdapter.listInventoryMovement();

        HttpClient(context).httpClient.syncInventoryMovement(restaurantId,orders).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                daoAdapter.deleteInventoryMovements(orders)
                successListener?.onSuccess()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                failListener?.onFail()
            }

        })
    }
}