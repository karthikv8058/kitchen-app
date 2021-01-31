package com.smarttoni.server.controlles.assignment;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.delay.DelayHelper;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.DaoNotFoundException;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class PostOnCall extends HttpSecurityRequest {

    private Context context;

    public PostOnCall(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String courseId = jsonObject.getString("courseId");
            DaoAdapter daoAdapter = new GreenDaoAdapter(context);
            Course course = daoAdapter.getCourseById(courseId);
            if (course != null) {
                Order order = daoAdapter.getOrderById(course.getOrderId());
                order.setUpdatedAt(System.currentTimeMillis());
                order.setModification(Order.MODIFICATION_UPDATED);
                order.setIsUpdated(true);
                daoAdapter.updateOrder(order);

                course.setDeliveryTime(System.currentTimeMillis());
                course.setIsOnCall(false);
                daoAdapter.updateCourse(course);
                Queue queue = ServiceLocator.getInstance().getQueue();
                List<Work> works = queue.getCloneQueue();
                for (Work work : works) {
                    if (work.getCourse() != null && work.getCourse().getId().equals(course.getId())) {
                        work.getCourse().setIsOnCall(false);
                    }
                }
            }
            DelayHelper delayHelper = (DelayHelper) ServiceLocator.getInstance().getService(ServiceLocator.DELAY_CALCULATOR_SERVICE);
            try {
                delayHelper.startAsync(ServiceLocator.getInstance().getQueue());
            } catch (DaoNotFoundException e) {
            }
            AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
            assignmentFactory.eventLoop(context);
            assignmentFactory.assign();
        } catch (JSONException e) {
        }
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<Boolean>() {
        }.getType();
        response.send(gson.toJson(true, type));
    }

}
