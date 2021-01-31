package com.smarttoni.server.controlles.recipe;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Label;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.ServiceSet;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetRecipesAndLabels extends HttpSecurityRequest {

    private Context context;

    public GetRecipesAndLabels(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<Recipe> recipes = daoAdapter.loadRecipes();
        List<Recipe> recipeList=new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getStatus()==2) {
                recipeList.add(r);
            }
        }
        List<Label> labels = daoAdapter.loadLabels();
        List<ServiceSet> serviceSets = daoAdapter.loadServiceSets();
        List<ServiceSet> serviceSetList=new ArrayList<>();
        for (ServiceSet serviceSet:serviceSets) {
            ServiceSet serviceSet1 =new ServiceSet();
            serviceSet1.setId(serviceSet.getId());
            serviceSet1.setDescription(serviceSet.getDescription());
            serviceSet1.setName(serviceSet.getName());
            serviceSet1.setRecipes( serviceSet.getServiceSetRecipes());
            serviceSet1.setTimes( serviceSet.getServiceSetTiming());
            serviceSetList.add(serviceSet1);
        }
        RecipesAndLabels recipesAndLabels = new RecipesAndLabels();
        recipesAndLabels.setRecipes(recipeList);
        recipesAndLabels.setLabels(labels);
        recipesAndLabels.setServiceSets(serviceSetList);
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<RecipesAndLabels>() {
        }.getType();
        response.send(gson.toJson(recipesAndLabels, type));
    }


    class RecipesAndLabels {
        List<Recipe> recipes;
        List<Label> labels;
        List<ServiceSet> serviceSets;
        public void setRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
        }
        public void setLabels(List<Label> labels) {
            this.labels = labels;
        }
        public void setServiceSets(List<ServiceSet> serviceSets) {
            this.serviceSets = serviceSets;
        }
    }
}
