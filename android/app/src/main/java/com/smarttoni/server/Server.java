package com.smarttoni.server;

import android.content.Context;

import com.smarttoni.models.ServerStartRequest;
import com.smarttoni.http.HttpClient;

public class Server {

    private Context context;


    public Server(Context context) {
        this.context = context;
    }

    public void start(){

     new HttpClient(context).getHttpClient().startServer("",new ServerStartRequest(""));
    }

    public void onIpChanged(){

    }


    public void stop(){

    }


}
