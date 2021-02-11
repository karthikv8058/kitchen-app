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
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.PrinterManager;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class UpdateIntervention extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public UpdateIntervention(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
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

            InterventionJob interventionJob = greenDaoAdapter.getInterventionJobById(intervention);
            if (interventionJob != null && interventionJob.getStatus() == InterventionJob.COMPLETED ) {
                Work work = ServiceLocator.getInstance().getDatabaseAdapter().getWorkById(interventionJob.getWorkId());
                Task task = ServiceLocator.getInstance().getDatabaseAdapter().getTaskById(work.getTaskId());

                if(status ==InterventionJob.COMPLETED && interventionJob.getIntervention().getPrintLabel()==true){
                    try {
                        PrinterManager.getInstance().printTask(work,interventionJob.getIntervention().getDescription(),task);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        } catch (Exception e) {
            response.send("false");
        }
    }
}
