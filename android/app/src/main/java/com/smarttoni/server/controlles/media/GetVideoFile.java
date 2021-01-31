package com.smarttoni.server.controlles.media;

import android.content.Context;
import android.os.Environment;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.server.RequestCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GetVideoFile extends RequestCallback {
    private Context context;

    public GetVideoFile(Context context) {
        this.context = context;
    }

    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);
        String filename = request.getQuery().getString("filename");
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File vidFile = new File(path + "/" + filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(vidFile);
            response.sendStream(fileInputStream, vidFile.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
