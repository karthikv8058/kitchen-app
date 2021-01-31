package com.smarttoni.server.controlles.recipe;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Recipe;
import com.smarttoni.models.wrappers.RecipeWrapper;
import com.smarttoni.models.wrappers.RecipelookupWrapper;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetRecipe extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public GetRecipe(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        List<Recipe> recipes = ServiceLocator.getInstance().getDatabaseAdapter().loadRecipes();
        List<Recipe> recipeList=new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getStatus() == 2 && r.getType()==1) {
                recipeList.add(r);
            }
        }
        List<RecipeWrapper> recipeWrappers = new ArrayList<>();

            for (Recipe recipe : recipeList) {
                RecipeWrapper recipeWrapper=new RecipeWrapper();
                recipeWrapper.setRecipeName(recipe.getName());
                recipeWrapper.setRecipeName(recipe.getName());
                recipeWrapper.setColors(recipe.getColor());
                recipeWrapper.setUuid(recipe.getId());
                recipeWrapper.setDescription(recipe.getDescription());
                recipeWrapper.setRecipeLabels(recipe.getParentLabel());
                if (recipe.getOutputUnit()!=null){
                    recipeWrapper.setOutPutUnit(recipe.getOutputUnit().getSymbol());
                }
                recipeWrapper.setOutPutQuantity(recipe.getOutputQuantity());
                recipeWrapper.setImage(recipe.getImageUrl());
                recipeWrappers.add(recipeWrapper);
        }
        RecipelookupWrapper recipelookupWrapper=new RecipelookupWrapper();
        recipelookupWrapper.recipes=recipeWrappers;
        recipelookupWrapper.labels=greenDaoAdapter.loadLabels();
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<RecipelookupWrapper>() {
        }.getType();
        response.send(gson.toJson(recipelookupWrapper, type));
    }

}
