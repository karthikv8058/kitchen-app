package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Supplier
import com.smarttoni.models.SyncData
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.LocalStorage
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SupplierSyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        val storage = LocalStorage(context)
        val time = storage.getLong(LocalStorage.LAST_SYNC_SUPPLIER)
        var lastCreatedAt = ""
        if (time > 0) {
            lastCreatedAt = DateUtil.formatStandardDate(Date(time))
        } else {
            lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        }

        HttpClient(context).httpClient.syncSuppliers(restaurantId,lastCreatedAt).enqueue(object : Callback<SyncData<Supplier>> {

            override fun onFailure(call: Call<SyncData<Supplier>>, t: Throwable) {
                failListener.onFail()
            }

            override fun onResponse(call: Call<SyncData<Supplier>>, response: Response<SyncData<Supplier>>) {

                val data = response.body() ?: return

                storage.setLong(LocalStorage.LAST_SYNC_SUPPLIER, Date().time);

                Thread(Runnable {
                    val created = data.created
                    val updated = data.updated
                    val deleted = data.deleted
                    val toCreate = ArrayList<Supplier>()
                    val toDelete = ArrayList<String>()

                    if (created != null && created.size > 0) {
                        toCreate.addAll(created)
                        for (r in created) {
                            toDelete.add(r.id)
                        }
                    }
                    if (updated != null && updated.size > 0) {
                        toCreate.addAll(updated)
                        for (r in updated) {
                            toDelete.add(r.id)
                        }
                    }
                    if (deleted != null && deleted.size > 0) {
                        for (r in deleted) {
                            toDelete.add(r.id)
                        }
                    }
                    val toDeletee = ArrayList<String>()
                    if (data != null) {
                        for (unit in created) {
                            toDeletee.add(unit.id)
                        }
                    }
//                    if (data != null) {
//                        deleteSupplier(daoAdapter, toDeletee)
//                    }
                    deleteSupplier(daoAdapter, toDelete)
                    saveSupplier(daoAdapter, toCreate)
                    successListener.onSuccess()
                }).start()

            }
        })
    }


    private fun deleteSupplier(daoAdapter: DaoAdapter, ids: List<String>) {
        daoAdapter.deleteSupplier(ids)
    }

    private fun saveSupplier(daoAdapter: DaoAdapter, suppliers: List<Supplier>) {
        val list: ArrayList<Supplier> = ArrayList<Supplier>()
        for (supplier in suppliers) {
            supplier.name = supplier.locales[0].name
            list.add(supplier)
        }
        daoAdapter.saveSuppliers(list)
    }

}
