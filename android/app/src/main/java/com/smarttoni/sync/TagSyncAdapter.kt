package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Tag
import com.smarttoni.entities.UnitConversion
import com.smarttoni.entities.Units
import com.smarttoni.http.HttpClient
import com.smarttoni.models.SyncData
import com.smarttoni.models.UnitSync
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TagSyncAdapter : AbstractSyncAdapter  {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {

        var lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        HttpClient(context).httpClient.syncTags(restaurantId, lastCreatedAt).enqueue(object : Callback<SyncData<Tag>> {
            override fun onResponse(call: Call<SyncData<Tag>>, response: Response<SyncData<Tag>>) {
                val data = response.body() ?: return
                Thread(Runnable {
                    if(data  != null) {
                        val created = data.created
                        val updated = data.updated
                        val deleted = data.deleted
                        val toCreate = ArrayList<Tag>()
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
                        if (data != null) {
                            deleteUnits(daoAdapter, toDeletee)
                        }
                        deleteUnits(daoAdapter, toDelete)
                        saveUnitsToDb(daoAdapter, toCreate)
                    }
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<SyncData<Tag>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun deleteUnits(daoAdapter: DaoAdapter, ids: List<String>) {
        daoAdapter.deleteTags(ids)
    }

    private fun saveUnitsToDb(daoAdapter: DaoAdapter, tags: List<Tag>) {
        val list: ArrayList<Tag> = ArrayList<Tag>()
        for (tag in tags) {

            var name : String = "";
            if(tag!!.locales!= null && tag.locales!!.get(0).name != null ){
                name = tag.locales!!.get(0).name;
            }
            tag.name = name;
            list.add(tag)
        }
        daoAdapter.saveTags(list)
    }
}