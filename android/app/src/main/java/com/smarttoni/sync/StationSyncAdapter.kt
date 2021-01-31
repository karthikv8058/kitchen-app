package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Options
import com.smarttoni.entities.Station
import com.smarttoni.entities.User
import com.smarttoni.http.HttpClient
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StationSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        HttpClient(context).httpClient.syncStations(restaurantId).enqueue(object : Callback<List<Station>> {
            override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
                Thread(Runnable {
                    setOptionsData(daoAdapter)
                    val data = response.body()
                    val toDelete = ArrayList<String>()
                    if (data != null) {
                        for (station in data) {
                            toDelete.add(station.id)
                        }
                    }
                    if (data != null) {
                        deleteStations(toDelete, daoAdapter)
                    }

                    if (data != null) {
                        saveStationsToDb(data, daoAdapter)
                    }
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                setOptionsData(daoAdapter)
                failListener.onFail()
            }
        })
    }

    private fun deleteStations(ids: List<String>, daoAdapter: DaoAdapter) {
        daoAdapter.deleteStations(ids)
        daoAdapter.deleteUserByIdList(ids)
    }

    private fun setOptionsData(daoAdapter: DaoAdapter) {
        val optionsList = daoAdapter.getOptionLitem("userList")
        if (optionsList.size != 0) {
            val options1 = optionsList[0]
            options1.value = "1"
            daoAdapter.saveOptionsDao(options1)
        } else {
            val options = Options()
            options.key = "userList"
            options.value = "1"
            daoAdapter.saveOptionsDao(options)
        }
    }

    private fun saveStationsToDb(stationApiResponses: List<Station>, daoAdapter: DaoAdapter) {
        daoAdapter.deleteAllStations()
        for (station in stationApiResponses) {
            if (station == null) {
                break
            }

            if (station.locales != null) {
                station.name = station.locales[0].name
            }
            station.isDeliverable = station.operationMode != 1

            var stationItm = daoAdapter.getStationById(station.id)
            if (stationItm != null) {
                daoAdapter.updateStation(station)
            } else {
                daoAdapter.saveStationDao(station)
            }
            insertStationAsUser(daoAdapter, station)
        }
    }


    private fun insertStationAsUser(daoAdapter: DaoAdapter, station: Station) {
        val user = User()
        user.name = station.locales[0].name
        user.id = station.id
        user.username = station.locales[0].name.toLowerCase().replace("\\s+".toRegex(), "")
        user.password = BCrypt.hashpw(station.locales[0].name.toLowerCase().replace("\\s+".toRegex(), ""), BCrypt.gensalt())
        user.ismainchef = false
        user.createdat = 0
        user.updatedat = 0
        user.userType = User.TYPE_STATION
        user.stationId = station.id
        daoAdapter.saveUser(user)
    }
}
