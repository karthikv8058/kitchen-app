package com.smarttoni.server.controlles.inventory;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Recipe;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.http.HttpClient;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddNewIngredient extends HttpSecurityRequest {
    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public AddNewIngredient(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse respons) {
        try {
            JSONObject jsonObject = HttpHelper.postDataToJson(request);
            JsonObject jo = new JsonObject();
            jo.addProperty("name", jsonObject.getString("recipeName"));
            jo.addProperty("managed", jsonObject.getInt("inventoryType"));
            jo.addProperty("image_uuid", jsonObject.getString("imageUuid"));
            jo.addProperty("output_unit_uuid", jsonObject.getString("unitUuid"));
            jo.addProperty("product_barcode", jsonObject.getString("barcodeData"));
            jo.addProperty("output_quantity", "1");

            LocalStorage ls = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);

            new HttpClient(context).getHttpClient().addNewIngredient(ls.getRestaurantId(), jo).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        try {
                            String status = String.valueOf(response.body().get("status"));
                            if (status.equals("true") ) {
                                JsonObject object = response.body().getAsJsonObject("data");
                                JSONObject jo2 = new JSONObject(object.toString());
                                Recipe recipe = new Recipe();
                                recipe.setId(jo2.optString("uuid"));
                                recipe.setName(jsonObject.optString("recipeName"));
                                recipe.setInventoryType(jsonObject.optInt("inventoryType"));
                                recipe.setOutputQuantity(0);
                                recipe.setOutputUnitId(jsonObject.optString("unitUuid"));
                                String url = (jo2.optString("image_url"));
                                recipe.setColor(jo2.optString("color"));
                                recipe.setProductBarcode(jo2.optString("product_barcode"));
                                recipe.setVersion(Integer.parseInt(jo2.optString("version")));
                                recipe.setStatus(Integer.parseInt(jo2.optString("status")));
                                recipe.setType(Integer.parseInt(jo2.optString("type")));
                                String recipeImage = HttpHelper.downloadFile(context, url, jsonObject.optString("recipeName"), "");
                                recipe.setImageUrl(recipeImage);
                                List<Recipe> recipes = new ArrayList<>();
                                recipes.add(recipe);
                                greenDaoAdapter.saveRecipe(recipes);
                                Gson gson = GSONBuilder.createGSON();
                                Type type = new TypeToken<Boolean>() {
                                }.getType();
                                respons.send(gson.toJson(true, type));
                            }else{
                                Gson gson = GSONBuilder.createGSON();
                                Type type = new TypeToken<Boolean>() {
                                }.getType();
                                respons.send(gson.toJson(false, type));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Gson gson = GSONBuilder.createGSON();
                        Type type = new TypeToken<Boolean>() {
                        }.getType();
                        respons.send(gson.toJson(false, type));
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    respons.send(gson.toJson(false, type));
                }
            });
//        }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}