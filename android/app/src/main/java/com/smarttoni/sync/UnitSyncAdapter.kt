package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.UnitConversion
import com.smarttoni.entities.Units
import com.smarttoni.models.UnitSync
import com.smarttoni.http.HttpClient
import com.smarttoni.utils.DateUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UnitSyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {

        var lastCreatedAt = DateUtil.formatStandardDate(Date(0))
        HttpClient(context).httpClient.syncUnits(restaurantId, lastCreatedAt).enqueue(object : Callback<UnitSync> {
            override fun onResponse(call: Call<UnitSync>, response: Response<UnitSync>) {
                val data = response.body() ?: return
                Thread(Runnable {
                    if(data.units != null) {
                        val created = data.units.created
                        val updated = data.units.updated
                        val deleted = data.units.deleted
                        val toCreate = ArrayList<Units>()
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
                    if(data.conversions != null){
                        val conversionMap = HashMap<String,UnitConversion>();
                        for(conversion in data.conversions){
                            conversionMap.put(conversion.from+"_"+conversion.to,conversion);
                        }
                        val result = ArrayList<UnitConversion>();
                        for(conversion in data.conversions){
                           val c = conversionMap.get(conversion.from+"_"+conversion.to)
                            if(c != null){
                                val reverse = conversionMap.get(conversion.to+"_"+conversion.from)
                                if(reverse == null){
                                    val unitConversion = UnitConversion()
                                    unitConversion.from= c.to;
                                    unitConversion.to= c.from;
                                    unitConversion.factor= 1f/c.factor;
                                    result.add(unitConversion)
                                }
                            }
                            result.add(conversion)
                        }
                        daoAdapter.saveUnitConversions(result)
                    }
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<UnitSync>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun deleteUnits(daoAdapter: DaoAdapter, ids: List<String>) {
        daoAdapter.deleteUnits(ids)
    }

    private fun saveUnitsToDb(daoAdapter: DaoAdapter, units: List<Units>) {
        val list: ArrayList<Units> = ArrayList<Units>()
        for (Unit in units) {
            Unit.name = Unit.locales[0].name
            Unit.symbol = Unit.locales[0].symbol
            list.add(Unit)
        }
        daoAdapter.saveUnit(list)

    }

}
