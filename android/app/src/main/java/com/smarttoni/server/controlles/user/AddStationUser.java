package com.smarttoni.server.controlles.user;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class AddStationUser extends HttpSecurityRequest {

    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public AddStationUser(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }


    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            JSONArray stationData = (jsonObject.getJSONArray("stationId"));
            String userId = getUser().getId();

            greenDaoAdapter.deleteuserStationAssignment(userId);


            for (int i = 0; i < stationData.length(); i++) {
                UserStationAssignment userStationAssignmentOld = greenDaoAdapter.loadUserStationAssignmentByIds(String.valueOf(stationData.get(i)), userId);

                if ((!(stationData.get(i)).equals(null)) && (!(stationData.get(i)).equals(""))) {
                    if (userStationAssignmentOld == null) {
                        UserStationAssignment userStationAssignment = new UserStationAssignment();
                        userStationAssignment.setUserid(userId);
                        userStationAssignment.setStationid(String.valueOf(stationData.get(i)));
                        userStationAssignment.setCreatedAt(System.currentTimeMillis());
                        greenDaoAdapter.saveUserStationAssignmentDao(userStationAssignment);
                    }
                }
            }
            AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
            assignmentFactory.assign();
            EventManager.getInstance().emit(Event.ORDER_RECEIVED);

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
