package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.server.RequestCallback;

public class GetTaskStepDetails extends RequestCallback {
    private Context context;
    private DaoSession daoSession;

    public GetTaskStepDetails(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);

    }
}
