package com.smarttoni.server.controlles.assignment;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;


public class UnassignTask extends HttpSecurityRequest {

    private Context context;

    public UnassignTask(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        Boolean assigned = false;
        String userId = getUser().getId();
        TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();
        assigned = workHelper.revoke(userId);
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<Boolean>() {
        }.getType();
        response.send(gson.toJson(assigned, type));
    }

}
