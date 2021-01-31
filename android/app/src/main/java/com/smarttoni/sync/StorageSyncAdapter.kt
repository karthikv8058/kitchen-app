package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Storage
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StorageSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        daoAdapter.deleteStorage()
        daoAdapter.deleteRack()
        daoAdapter.deletePlace()
        HttpClient(context).httpClient.syncStorage(restaurantId).enqueue(object : Callback<List<Storage>> {
            override fun onResponse(call: Call<List<Storage>>, response: Response<List<Storage>>) {
                val storageResponses = response.body() ?: return
                Thread(Runnable {
                    for (storage in storageResponses) {


                        if (storage.locales.size > 0) {
                            storage.name = storage.locales[0].name
                        }
                        if (storage.racks != null)
                            for (racks in storage.racks) {
                                if (racks.locales.size > 0) {
                                    racks.name = racks.locales[0].name
                                }
                                racks.storageId = storage.id
                                if (racks.places != null)
                                    for (places in racks.places) {
                                        places.id = places.id
                                        if (places.locales.size > 0) {
                                            places.name = places.locales[0].name
                                        }
                                        places.rackId = racks.id
                                        daoAdapter.savePlaces(places)
                                    }
                                daoAdapter.saveRack(racks)
                            }
                        daoAdapter.saveStorage(storage)
                    }

                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<Storage>>, t: Throwable) {
                failListener.onFail() 
            }
        })
    }


}