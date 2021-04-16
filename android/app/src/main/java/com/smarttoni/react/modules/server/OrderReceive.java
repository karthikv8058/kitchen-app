package com.smarttoni.react.modules.server;

import android.content.Context;

import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

import org.json.JSONObject;

public class OrderReceive implements HttpServerRequestCallback {
    
    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        JSONObjectBody body = (JSONObjectBody) request.getBody();

        JSONObject jsonObject = body.get();

        EventManager.getInstance().emit(Event.REACT_PUSH, jsonObject.toString());

        response.send("true");
    }
}
