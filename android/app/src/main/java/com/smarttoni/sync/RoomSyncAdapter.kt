package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Room
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomSyncAdapter : AbstractSyncAdapter {
    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        daoAdapter.deleteRooms()
        HttpClient(context).httpClient.syncRoom(restaurantId).enqueue(object : Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                val roomResponse = response.body() ?: return
                Thread(Runnable {
                    for (room in roomResponse) {
                        if (room != null) {
                            daoAdapter.saveRoom(room)
                        }

                    }

                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }
}


