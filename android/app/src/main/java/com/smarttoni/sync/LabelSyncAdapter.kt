package com.smarttoni.sync

import android.content.Context
import com.smarttoni.database.DaoAdapter
import com.smarttoni.entities.Label
import com.smarttoni.http.HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabelSyncAdapter : AbstractSyncAdapter {

    override fun onSync(context: Context, daoAdapter: DaoAdapter, restaurantId: String, successListener: AbstractSyncAdapter.SyncSuccessListener, failListener: AbstractSyncAdapter.SyncFailListener) {
        HttpClient(context).httpClient.syncLabels(restaurantId).enqueue(object : Callback<List<Label>> {
            override fun onResponse(call: Call<List<Label>>, response: Response<List<Label>>) {
                val labelResponses = response.body() ?: return
                Thread(Runnable {
                    daoAdapter.deleteLabelByRestuarantId(restaurantId)
                    for (labels in labelResponses) {
                        labels.restaurantId = restaurantId
                        labels.name = labels.locales.get(0).name
                        labels.description = labels.locales.get(0).description
                        var parentLabel: String = ""
                        var childLabels: String =getChildLabel( labelResponses, labels.id)
                        var i = 0
                        for (labelItem in labels.parentLabels) {
                            if (i != 0) {
                                parentLabel = parentLabel + "," + labelItem.label_uuid
                            } else {
                                parentLabel = parentLabel + labelItem.label_uuid
                            }
                            i++
                        }
                        labels.parentLabel = parentLabel
                        labels.childLabels=childLabels
                        daoAdapter.saveRecipeCategoryDao(labels)
                    }
                    successListener.onSuccess()
                }).start()
            }

            override fun onFailure(call: Call<List<Label>>, t: Throwable) {
                failListener.onFail()
            }
        })
    }

    private fun getChildLabel(labelResponses: List<Label>, id: String?): String {
        var child = ""
        for (label in labelResponses) {
                for (parentLabel in label.parentLabels) {
                    if (parentLabel.label_uuid == id) {
                        if (child == "") {
                            child = label.id
                        } else {
                            child=child+","+ label.id
                        }
                    }
                }

        }
        return child
    }
}
