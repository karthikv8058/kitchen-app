package com.smarttoni.server.controlles.media;

import android.content.Context;
import android.os.Environment;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.server.RequestCallback;

import java.io.File;

public class GetImageFile extends RequestCallback {
    private Context context;

    public GetImageFile(Context context) {
        this.context = context;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);
        String filename = request.getQuery().getString("filename");
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File imageFile = new File(path + "/" + filename);
        response.sendFile(imageFile);
    }

}
