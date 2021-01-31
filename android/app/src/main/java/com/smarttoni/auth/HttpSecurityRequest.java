package com.smarttoni.auth;


import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.smarttoni.database.RealmAdapter;
import com.smarttoni.entities.NetworkLog;
import com.smarttoni.entities.User;

import org.json.JSONException;

import java.util.Date;

public abstract class HttpSecurityRequest implements HttpServerRequestCallback {

    private final String KEY_AUTHENTICATION = "authentication";

    private User user;


    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        long time = new Date().getTime();
        if (getUser(request) == null) {
            response.send("{\"error\":403,\"message\":\"unauthenticated\"}");
        } else {
            try {
                processRequest(request, response);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
        long difference = new Date().getTime() - time;
        RealmAdapter.getInstance().logNetwork(new NetworkLog(request.getPath(), difference));

    }

    private User getUser(AsyncHttpServerRequest request) {
        String token = request.getHeaders().get(KEY_AUTHENTICATION);
        if (token == null || token.isEmpty()) {
            return null;
        }
        user = AuthManager.getInstance().findUserByToken(token);
        return user;
    }

    protected User getUser() {
        return user;
    }

    abstract public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException;

}
