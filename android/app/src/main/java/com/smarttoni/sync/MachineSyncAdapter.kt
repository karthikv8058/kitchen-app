package com.smarttoni.sync


import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Machine
import com.smarttoni.models.SyncData
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.HttpHelper
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MachineSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {

        val machine = daoAdapter.machineByDescOrder
        var lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        if (machine != null && machine.createdAt != null) {
            lastCreatedAt = HttpHelper.convertMillisecondsToMysqlDateTimeString(machine.createdAt.time)
        }
        HttpClient(context).httpClient.syncMachines(restaurantId, lastCreatedAt).enqueue(object : Callback<SyncData<Machine>> {

            override fun onResponse(call: Call<SyncData<Machine>>, response: Response<SyncData<Machine>>) {
                val data = response.body() ?: return
                Thread(Runnable {
                    val created = data.created
                    val updated = data.updated
                    val deleted = data.deleted
                    val toCreate = ArrayList<Machine>()
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

                    val toDeletee = ArrayList<String>()
                    if (data != null) {
                        for (station in created) {
                            toDeletee.add(station.id)
                        }
                    }
                    if (data != null) {
                        deleteMachiness(context, toDeletee, daoAdapter)
                    }

                    if (deleted != null && deleted.size > 0) {
                        for (r in deleted) {
                            toDelete.add(r.id)
                        }
                    }
                    deleteMachines(context, toDelete, daoAdapter)
                    saveMachinesToDb(context, toCreate, daoAdapter)
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<SyncData<Machine>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun deleteMachines(context: Context, ids: List<String>, daoAdapter: DaoAdapter) {
        for (id in ids) {
            var machine = daoAdapter.getMachineById(id)
            if (machine != null) {
                daoAdapter.deleteMachine(machine)
            }
        }

    }

    private fun saveMachinesToDb(context: Context, machines: List<Machine>, daoAdapter: DaoAdapter) {
        val list: ArrayList<Machine> = ArrayList<Machine>()
        for (m in machines) {
            m.name = m.locales[0].name
            m.capacity = m.capacity
            m.id = m.id
            m.room = m.room
            list.add(m)
        }
        daoAdapter.saveMachineList(list)
    }

    private fun deleteMachiness(context: Context, ids: List<String>, daoAdapter: DaoAdapter) {
        for (id in ids) {
            var machine = daoAdapter.getMachineById(id)
            if (machine != null) {
                daoAdapter.deleteMachine(machine)
            }
        }

    }
}
