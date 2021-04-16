package com.smarttoni.react.modules.server;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

public class PrinterMessageReceived implements HttpServerRequestCallback {
    private Context context;

    public PrinterMessageReceived(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        response.send("true");

    }
}
