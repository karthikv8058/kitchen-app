package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.PrinterManager;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;


public class PingIp extends HttpSecurityRequest {

    private Context context;

    public PingIp(Context context) {
        this.context = context;

    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            Boolean issuccess=false;
            String taskId = jsonObject.getString("taskId");
            Task task=ServiceLocator.getInstance().getDatabaseAdapter().getTaskById(taskId);
            if (task.getStationId() != null) {
                Station station = ServiceLocator.getInstance().getDatabaseAdapter().getStationById(task.getStationId());
                Printer printerObject = ServiceLocator.getInstance().getDatabaseAdapter().getPrinterDataById(station.getPrinterUuid());
                InetAddress geek = InetAddress.getByName(printerObject.getIpAddress());
                if (geek.isReachable(5000)){
                    issuccess=true;
                }else{
                    issuccess=false;
                }
            }
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(issuccess, type));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
