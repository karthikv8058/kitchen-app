package com.smarttoni.sync

import android.content.Context

import com.smarttoni.database.DaoAdapter

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object SyncObservable {

    fun sync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, listener: AbstractSyncAdapter): Observable<Boolean> {
        val subject = PublishSubject.create<Boolean>()
        listener.onSync(context, daoAdapter, restaurantId, object : AbstractSyncAdapter.SyncSuccessListener {
            override fun onSuccess() {
                subject.onNext(true)
            }
        }, object : AbstractSyncAdapter.SyncFailListener {
            override fun onFail() {
                subject.onError(Throwable("Failed to Sync"))
                subject.onNext(false)
            }
        })
        return subject
    }
}
