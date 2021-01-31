package com.smarttoni.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServer;

public class WebServer {


    private AsyncHttpServer httpServer;

    public void start(int port){
        httpServer.listen(port);
    }

    public void stop(){
        httpServer.stop();
    }

    public AsyncHttpServer AsyncHttpServer(){
        return httpServer;
    }
}
