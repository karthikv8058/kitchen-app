package com.smarttoni.server.controlles.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskStep;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class ImageUpload extends HttpSecurityRequest {

    private Context context;
    private GreenDaoAdapter daoAdapter;

    public ImageUpload(Context context) {
        this.context = context;
        this.daoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);

        Boolean isIntervention = jsonObject.getBoolean("isIntervention");
        Boolean isSteps = jsonObject.getBoolean("isSteps");
        Boolean isFromRecipe = jsonObject.getBoolean("isFromRecipe");


        int imageType = jsonObject.getInt("imageType"); //RECIPE = 1,TASK = 2,INTERVENTION = 3,STEPS = 4
        switch (imageType) {
            case 1:

        }


        String uuid = jsonObject.getString("uuid");
        String uri = jsonObject.getString("uri");
        String responseUrl = "";

        if (isFromRecipe) {
            Recipe recipe = ServiceLocator.getInstance().getDatabaseAdapter().getRecipeById(uuid);
            responseUrl = HttpHelper.downloadFile(context, uri, recipe.getName(), recipe.getDescription());
            recipe.setImageUrl(responseUrl);
            ServiceLocator.getInstance().getDatabaseAdapter().updateRecipe(recipe);
        } else if (isIntervention) {
            if (isSteps) {
                TaskStep interventionSteps = daoAdapter.loadStepsById(uuid);
                responseUrl = HttpHelper.downloadFile(context, uri, interventionSteps.getName(), interventionSteps.getDescription());
                interventionSteps.setImageurl(responseUrl);
                daoAdapter.updateSteps(interventionSteps);
            } else {
                Intervention intervention = daoAdapter.getInterventionByUuid(uuid);
                responseUrl = HttpHelper.downloadFile(context, uri, intervention.getName(), intervention.getDescription());
                intervention.setImageUrl(responseUrl);
                daoAdapter.updateIntervention(intervention);
            }
        } else {
            if (isSteps) {
                TaskStep taskStep = daoAdapter.loadStepsById(uuid);
                responseUrl = HttpHelper.downloadFile(context, uri, taskStep.getName(), taskStep.getDescription());
                taskStep.setImageurl(responseUrl);
                daoAdapter.updateSteps(taskStep);
            } else {
                Task task = daoAdapter.getTaskById(uuid);
                responseUrl = HttpHelper.downloadFile(context, uri, task.getName(), task.getDescription());
                task.setImageurl(responseUrl);
                daoAdapter.updateTask(task);

            }
        }
        JSONObject json = new JSONObject();
        json.put("status", true);
        json.put("data", responseUrl);

        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<JSONObject>() {
        }.getType();
        response.send(gson.toJson(json, type));
    }

}
