package com.smarttoni.server.controlles.devtools;

import android.content.Context;
import android.os.Environment;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.server.RequestCallback;

import java.io.File;

public class DownloadDatabase extends RequestCallback {

    private Context context;

    public DownloadDatabase(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);
        String apath= context.getDatabasePath("smartoni.db").getAbsolutePath();
        String path= context.getDatabasePath("smartoni.db").getPath();
        response.sendFile(context.getDatabasePath("smartoni.db"));
    }

}
