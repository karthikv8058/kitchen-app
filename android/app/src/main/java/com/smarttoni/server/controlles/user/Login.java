package com.smarttoni.server.controlles.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.AuthManager;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.DaoMaster;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.User;
import com.smarttoni.entities.UserRights;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.entities.WebAppData;
import com.smarttoni.models.WebLoginModel;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.RequestCallback;
import com.smarttoni.http.HttpClient;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends RequestCallback {

    private Context context;
    private static DaoSession daoSession;
    GreenDaoAdapter greenDaoAdapter;

    public Login(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        super.onRequest(request, response);
        AsyncHttpRequestBody requestBody = request.getBody();
        daoSession = new DaoMaster(new DbOpenHelper(context, DbOpenHelper.Companion.getDB_NAME()).getWritableDb()).newSession();
        try {
            response.code(200);
            JSONObject json = new JSONObject(requestBody.get().toString());
            String email = json.getString("username");
            String password = json.getString("password");
            //request.getSocket().getServer().+
            String ip = json.getString("ip");
            User userList = greenDaoAdapter.getUserByEmail(email.trim().toLowerCase());
//            final OptionsDao optionsDao = DbOpenHelper.Companion.getDaoSession(context).getOptionsDao();
//
//            List<Options> optionsList = optionsDao.queryBuilder()
//                    .where(OptionsDao.Properties.Key.eq("likelyHood_limit"))
//                    .list();
            JSONObject jsonauth = new JSONObject();
            if (userList != null) {
                User user = userList;
                String pw = user.getPassword();

                if (BCrypt.checkpw(password.trim(), pw)) {
                    try {
                        user.setIpAddress(ip);
                        user.setOnline(true);
                        AuthManager.getInstance().generateToken(user);
                        daoSession.getUserDao().update(userList);
                        jsonauth.put("userid", user.getId());
                        jsonauth.put("authToken", user.getToken());
                        jsonauth.put("userType", user.getUserType());
                        jsonauth.put("stationId", user.getStationId());
                        JSONObject settings = new JSONObject();
                        settings.put("taskAutoOpen", user.getTaskAutoOpen());
                        settings.put("stepAutoOpen", user.getStepAutoOpen());
                        settings.put("autoAssign", user.getAutoAssign());
                        settings.put("isVROn", user.getIsVROn());
                        settings.put("isTTSOn", user.getIsTTSOn());
                        settings.put("readOnAssign", user.getReadOnAssign());
                        settings.put("readDescriptionInDetail", user.getReadDescriptionInDetail());
                        settings.put("readDescriptionInSteps", user.getReadDescriptionInSteps());
                        settings.put("readIngredientInDetail", user.getReadIngredientInDetail());
                        settings.put("readIngredientInSteps", user.getReadIngredientInSteps());
                        jsonauth.put("settings", settings);
                        jsonauth.put("LikelyHoodLimit", "10");
                        jsonauth.put("name", user.getName());


                        List<UserRights> userRights = greenDaoAdapter.getUserRightsById(user.getId());
                        Gson gson = GSONBuilder.createGSON();
                        Type type = new TypeToken<List<UserRights>>() {
                        }.getType();

                        jsonauth.put("UserRights", gson.toJson(userRights, type));
                        List<Station> stations = greenDaoAdapter.loadStations();
                        Random rand = new Random();
                        jsonauth.put("stationCount", stations.size());
                        if (stations.size() > 0) {
                            Station randomStation = stations.get(rand.nextInt(stations.size()));
                            List<UserStationAssignment> userStationAssignments = greenDaoAdapter.loadUserStationAssignmentById(userList.getId());
                            if (userStationAssignments.size() == 0) {
                                if (userList.getUserType() == User.TYPE_USER) {
                                    UserStationAssignment userStationAssignment = new UserStationAssignment();
                                    userStationAssignment.setStationid(randomStation.getId());
                                    userStationAssignment.setUserid(userList.getId());
                                    userStationAssignment.setCreatedAt(System.currentTimeMillis());
                                    greenDaoAdapter.saveUserStationAssignmentDao(userStationAssignment);
                                }
                            }
                        }
                        getWebAppCredentials(email.toLowerCase(), password, user.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    jsonauth.put("authenticated", 0);
                }
            } else {
                jsonauth.put("authenticated", 2);
            }
//            AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
//            assignmentFactory.assign();
            response.send(jsonauth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWebAppCredentials(String userName, String password, String userId) {
        JsonObject jo = new JsonObject();
        jo.addProperty("username", userName);
        jo.addProperty("password", password);

        new HttpClient(context).getHttpClient().syncWebData(jo).enqueue(new Callback<WebLoginModel>() {
            @Override
            public void onResponse(Call<WebLoginModel> call, Response<WebLoginModel> response) {
                if (response.body() != null) {
                    greenDaoAdapter.deleteWebAppDatUserDataByUserId(userId);
                    JSONObject jsonObject = new JSONObject();
                    if (response.body().data.getDetails() != null) {
                        try {
                            jsonObject.put("email", response.body().data.getDetails().getEmail());
                            jsonObject.put("enabled", true);
                            jsonObject.put("image_uuid", response.body().data.getDetails().getImageUuid());
                            jsonObject.put("first_name", response.body().data.getDetails().getFirstName());
                            jsonObject.put("last_name", response.body().data.getDetails().getLastName());
                            jsonObject.put("country_code", response.body().data.getDetails().getCountryCode());
                            jsonObject.put("city", response.body().data.getDetails().getCity());
                            jsonObject.put("image_url", response.body().data.getDetails().getImageUrl());
                            jsonObject.put("uuid", response.body().data.getDetails().getUuid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WebAppData webAppData = new WebAppData();
                    LocalStorage localStorage = new LocalStorage(context);
                    webAppData.setAccessToken(response.body().data.getAccessToken());
                    webAppData.setRefreshToken(response.body().data.getRefreshToken());
                    webAppData.setProfileUuid(response.body().data.getDetails().getUuid());
                    webAppData.setDetails(jsonObject.toString());
                    webAppData.setUserId(userId);
                    webAppData.setResturantId(localStorage.getRestaurantId());
                    greenDaoAdapter.saveWebAppData(webAppData);

                }

            }

            @Override
            public void onFailure(Call<WebLoginModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
