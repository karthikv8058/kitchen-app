package com.smarttoni.sync

import android.annotation.SuppressLint
import android.content.Context
import com.smarttoni.assignment.service.ServiceLocator
import com.smarttoni.database.DaoAdapter
import com.smarttoni.utils.LocalStorage

class UpdateManager {


    fun syncAll(context: Context) {
        this.syncAll(context, null)
    }

    fun syncAll(context: Context, syncFinishCallback: SyncFromWeb.SyncFinishCallback?) {
        val daoAdapter: DaoAdapter = ServiceLocator.getInstance().databaseAdapter
        val restaurantId = (ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE) as LocalStorage).restaurantId.toString()
        this.syncAll(context, daoAdapter, restaurantId, syncFinishCallback)
    }

    @SuppressLint("CheckResult")
    fun syncAll(context: Context, daoAdapter: DaoAdapter, restaurantId: String, syncFinishCallback: SyncFromWeb.SyncFinishCallback?) {
        //SyncObservable//.//sync(context, daoAdapter, restaurantId, UserSyncAdapter())
                SyncObservable.sync(context, daoAdapter, restaurantId, OrderSyncToWeb())
                .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, SyncInterventionToWeb()) }
                .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, SyncSegmentToWeb()) }
                .subscribe ({
                    syncFinishCallback?.onFinish()
                },{})

    }
}
