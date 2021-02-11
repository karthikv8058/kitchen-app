package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.List;

public class getPrinters extends HttpSecurityRequest {

    private Context context;
    private GreenDaoAdapter greenDaoAdapter;

    public getPrinters(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);

    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        try {
           List<Printer> printer=greenDaoAdapter.getPrinterList();
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<List<Printer>>() {
            }.getType();
            response.send(gson.toJson(printer, type));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
