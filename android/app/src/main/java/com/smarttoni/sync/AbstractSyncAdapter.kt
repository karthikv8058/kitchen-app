package com.smarttoni.sync

import android.content.Context

import com.smarttoni.database.DaoAdapter

interface AbstractSyncAdapter {

    fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: SyncSuccessListener, failListener: SyncFailListener)

    interface SyncSuccessListener {
        fun onSuccess()
    }

    interface SyncFailListener {
        fun onFail()
    }
}
