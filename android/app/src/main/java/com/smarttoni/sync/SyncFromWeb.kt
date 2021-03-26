package com.smarttoni.sync

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import com.smarttoni.MainActivity
import com.smarttoni.assignment.AssignmentFactory
import com.smarttoni.database.GreenDaoAdapter
import com.smarttoni.grenade.Event
import com.smarttoni.grenade.EventManager
import com.smarttoni.SmartTONiService
import com.smarttoni.utils.LocalStorage
import com.smarttoni.utils.PushUserSettings

class SyncFromWeb {

    interface SyncFinishCallback {
        fun onFinish()
    }

    companion object {

        var isSyncing = false

        fun syncCloudDb(context: Context) {
            syncCloudDb(context, false, null)
        }

        @SuppressLint("CheckResult")
        fun syncCloudDb(context: Context, startServerAfterSync: Boolean, syncFinishCallback: SyncFinishCallback?) {
            if (!startServerAfterSync && isSyncing) {
                return
            }
            isSyncing = true
            var session = LocalStorage(context)
            var daoAdapter = GreenDaoAdapter(context)
            var restaurantId = session.restaurantId.toString()
            SyncObservable.sync(context, daoAdapter, restaurantId, UserSyncAdapter())
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, StationSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, UnitSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, SupplierSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, SettingsSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, StorageSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, RoomSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, LabelSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, ServiceSetSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, MachineSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, RecipeSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, InventorySyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, PrinterConfigSyncAdapter()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, SyncTranportSheet()) }
                    .concatMap { SyncObservable.sync(context, daoAdapter, restaurantId, TagSyncAdapter()) }

                    .subscribe({

                        if (startServerAfterSync) {
                            val i = Intent(context, SmartTONiService::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(i)
                            } else {
                                context.startService(i)
                            }
                        }else{
                            val orders = daoAdapter.loadUnprocessedOrders()
                            for (order in orders) {
                                AssignmentFactory.getInstance().processOrder(context, order)
                            }
                        }
                        isSyncing = false;
                        syncFinishCallback?.onFinish()
                        PushUserSettings.pushSettings(daoAdapter)
                    },
                            { error ->
                                if (startServerAfterSync) {
                                    context.sendBroadcast(Intent(MainActivity.ACTION_SERVER_START_FAILED))
                                    EventManager.getInstance().emit(Event.SERVER_START, false)
                                }
                                isSyncing = false;
                            });
        }
    }

}


