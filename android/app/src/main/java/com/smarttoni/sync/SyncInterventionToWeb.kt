package com.smarttoni.sync

import android.content.Context
import com.smarttoni.assignment.service.ServiceLocator
import com.smarttoni.database.DaoAdapter
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.LocalStorage
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SyncInterventionToWeb : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        val interventions = daoAdapter.loadInterventions()
        val storage: LocalStorage = ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE) as LocalStorage
        val lastUpdated = DateUtil.formatDate(storage.getLong(LocalStorage.LAST_SYNC_INTERVENTION))
        HttpClient(context).httpClient.syncInterventionsToWeb(restaurantId, lastUpdated, interventions).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                storage.setLong(LocalStorage.LAST_SYNC_INTERVENTION, Date().time)
                successListener.onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                failListener.onFail()
            }
        })

    }
}
