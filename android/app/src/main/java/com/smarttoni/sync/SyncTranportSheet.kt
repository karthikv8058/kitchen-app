package com.smarttoni.sync

import android.content.Context
import com.smarttoni.assignment.transport.TransportHelper
import com.smarttoni.database.DaoAdapter
import com.smarttoni.models.TransportSheet
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncTranportSheet : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        HttpClient(context).httpClient.syncTransportSheet(restaurantId).enqueue(object : Callback<List<TransportSheet>> {
            override fun onResponse(call: Call<List<TransportSheet>>, response: Response<List<TransportSheet>>) {
                val data = response.body() ?: return
                val a = 10
                val b = 10
                TransportHelper.getInstance().generateTable(daoAdapter, response.body())
                successListener.onSuccess()
            }

            override fun onFailure(call: Call<List<TransportSheet>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

}