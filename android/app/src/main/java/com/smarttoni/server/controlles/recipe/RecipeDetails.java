package com.smarttoni.server.controlles.recipe;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.RecipeDetailData;
import com.smarttoni.entities.RecipeIngredientsDao;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskDao;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeDetails extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public RecipeDetails(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws JSONException {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);

        String recipeId = jsonObject.getString("recipeId");
        RecipeDetailData detailData = new RecipeDetailData();
        detailData.recipeIngredients = DbOpenHelper.Companion.getDaoSession(context).getRecipeIngredientsDao().queryBuilder()
                .where(RecipeIngredientsDao.Properties.RecipeId.eq(recipeId)).list();
        List<Task> tasks = DbOpenHelper.Companion.getDaoSession(context).getTaskDao().queryBuilder()
                .where(TaskDao.Properties.RecipeId.eq(recipeId)).list();
        for (Task task : tasks) {
            task.getTaskIngredient();
            task.getInterventions();
        }
        detailData.tasks = tasks;
        Gson gson = GSONBuilder.createGSON();

        Type type = new TypeToken<RecipeDetailData>() {
        }.getType();
        response.send(gson.toJson(detailData, type));
    }


}