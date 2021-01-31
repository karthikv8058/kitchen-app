package com.smarttoni.server;


import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

public abstract class RequestCallbacks implements HttpServerRequestCallback {

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
    }

}
