package com.smarttoni.server.controlles.assignment;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class UpdateIntervention extends HttpSecurityRequest {

    private Context context;

    public UpdateIntervention(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            long intervention = jsonObject.getLong("intervention");
            int status = jsonObject.getInt("status");
            int time = jsonObject.getInt("time");
            boolean reduceValue = jsonObject.getBoolean("reduceValue");
            InterventionManager interventionManager = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getInterventionManager();
            interventionManager.updateIntervention(context, getUser().getId(), intervention, status, time, reduceValue);

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        } catch (Exception e) {
            response.send("false");
        }
    }
}
