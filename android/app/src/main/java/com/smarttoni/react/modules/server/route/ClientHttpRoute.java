package com.smarttoni.react.modules.server.route;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.smarttoni.react.modules.server.OrderReceive;
import com.smarttoni.react.modules.server.Ping;
import com.smarttoni.react.modules.server.PrinterMessageReceived;

public class ClientHttpRoute {
    public static void setRoutes(AsyncHttpServer mHttpServer, Context context) {
        mHttpServer.post("/push", new OrderReceive(context));
        mHttpServer.get("/ping", new Ping(context));
        mHttpServer.post("/printer-message-received", new PrinterMessageReceived(context));
    }
}
