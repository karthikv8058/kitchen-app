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

    private Context context;

    public static final String ORDER_RECEIVED = "order received";
    public static final String SERVER_LOGGED_OUT = "SERVER_LOGGED_OUT";

    public OrderReceive(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        JSONObjectBody body = (JSONObjectBody) request.getBody();

        JSONObject jsonObject = body.get();

        //JSONObject jsonObject = postDataToJson(request);


        EventManager.getInstance().emit(Event.REACT_PUSH, jsonObject.toString());

//        context.getReactInstanceManager()
//                .getCurrentReactContext()
//                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//                .emit(Events.SERVER_PUSH, jsonObject.toString());

        response.send("true");
    }

    public JSONObject postDataToJson(AsyncHttpServerRequest request) {

        JSONObject json = new JSONObject();
        try {

            if (request.getBody() instanceof UrlEncodedFormBody) {
                UrlEncodedFormBody body = (UrlEncodedFormBody) request.getBody();
                for (NameValuePair pair : body.get()) {
                    json.put(pair.getName(), pair.getValue());
                }
            } else if (request.getBody() instanceof JSONObjectBody) {
                json = ((JSONObjectBody) request.getBody()).get();
            } else if (request.getBody() instanceof StringBody) {
                json.put("body", ((StringBody) request.getBody()).get());
            } else if (request.getBody() instanceof MultipartFormDataBody) {
                MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
                for (NameValuePair pair : body.get()) {
                    json.put(pair.getName(), pair.getValue());
                }
            }

        } catch (Exception e) {
        }
        return json;
    }
}
