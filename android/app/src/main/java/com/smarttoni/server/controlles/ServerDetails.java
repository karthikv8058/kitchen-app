package com.smarttoni.server.controlles;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.utils.LocalStorage;

public class ServerDetails implements HttpServerRequestCallback {

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        LocalStorage localStorage = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);
        response.send("{\"name\":\"" + localStorage.getRestaurantName() + "\"}");
    }
}
