package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.ServiceSet
import com.smarttoni.entities.ServiceSetSyncWrapper
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ServiceSetSyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        var lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        HttpClient(context).httpClient.syncServiceSet(restaurantId,lastCreatedAt).enqueue(object : Callback<ServiceSetSyncWrapper> {
            override fun onResponse(call: Call<ServiceSetSyncWrapper>, response: Response<ServiceSetSyncWrapper>) {
                val data = response.body() ?: return
                Thread(Runnable {
                    val serviceSetCreated = data.created
                    val serviceSetUpdated = data.updated
                    val serviceSetDeleted = data.deleted
                    val serviceSetToCreate = ArrayList<ServiceSet>()
                    val ServiceSettoDelete = ArrayList<String>()

                    if (serviceSetCreated != null && serviceSetCreated.size > 0) {
                        serviceSetToCreate.addAll(serviceSetCreated)
                        for (r in serviceSetCreated) {
                            ServiceSettoDelete.add(r.id)
                        }
                    }
                    if (serviceSetUpdated != null && serviceSetUpdated.size > 0) {
                        serviceSetToCreate.addAll(serviceSetUpdated)
                        for (r in serviceSetUpdated) {
                            ServiceSettoDelete.add(r.id)
                        }
                    }
                    if (serviceSetDeleted != null && serviceSetDeleted.size > 0) {
                        for (r in serviceSetDeleted) {
                            ServiceSettoDelete.add(r.id)
                        }
                    }
                    deleteServiceSets(ServiceSettoDelete, daoAdapter)
                    saveServiceSetToDb(context, serviceSetToCreate, daoAdapter)

                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<ServiceSetSyncWrapper>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun saveServiceSetToDb(context: Context, serviceSet: List<ServiceSet>, daoAdapter: DaoAdapter) {
        val list: ArrayList<ServiceSet> = ArrayList<ServiceSet>()

        for (serviceSet in serviceSet) {
            serviceSet.name = serviceSet.locales[0].name
            serviceSet.description = serviceSet.locales[0].description
            for (serviceSetRecipe in serviceSet.recipes) {
                daoAdapter.deleteServiceSetRecipes(serviceSetRecipe.id)
                serviceSetRecipe.serviceSetId = serviceSet.id
                daoAdapter.saveServiceSetRecipes(serviceSetRecipe)
            }
            for (serviceSetTiming in serviceSet.times) {
                daoAdapter.deleteServiceSetTimings(serviceSetTiming.id)
                var weekdDay = ""
                serviceSetTiming.serviceSetId = serviceSet.id
                for (weekday in serviceSetTiming.weekDay) {
                    if (weekdDay == "") {
                        weekdDay = weekday.toString()
                    } else {
                        weekdDay = weekdDay + "," + weekday
                    }
                }
                serviceSetTiming.weekDays = weekdDay
                daoAdapter.saveServiceSetTiming(serviceSetTiming)
            }
            list.add(serviceSet)
        }
        daoAdapter.saveServiceSets(list)
    }

    private fun deleteServiceSets(ids: List<String>, daoAdapter: DaoAdapter) {
        daoAdapter.deleteServiceSet(ids)
    }

}
