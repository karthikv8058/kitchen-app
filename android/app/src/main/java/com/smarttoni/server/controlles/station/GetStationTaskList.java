package com.smarttoni.server.controlles.station;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.util.BundleHelper;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Work;
import com.smarttoni.server.GSONBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class GetStationTaskList extends HttpSecurityRequest {

    private Context context;

    public GetStationTaskList(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        //List<Course> courses = ServiceLocator.getInstance().getDatabaseAdapter().loadCourses();
        List<Work> workList = ServiceLocator.getInstance().getQueue().getCloneQueue();
        List<Work> works = new ArrayList<>();
        if (workList != null) {
            for (Work t : workList) {
                if ((!t.canStart() && !t.isMachineTask()) || t.getStatus() == Work.SCHEDULED) {
                    continue;
                }
                t.getUser();
                t.getTask();
                t.setStationId(t.getTask().getStationId());
                Work work = t.clone();
                if (work.getStatus() != Work.COMPLETED &&
                        work.getStatus() != Work.REMOVED &&
                        work.getStatus() != Work.SYNERGY) {
                    works.add(work);
                }

                int transportType = work.getTransportType();
                if (((transportType & Work.TRANSPORT_DELIVERABLES) > 0) || (work.getStatus() == Work.BUNDLED)) {
                    continue;
                }
                Station station = ServiceLocator
                        .getInstance()
                        .getDatabaseAdapter()
                        .getStationById(work.getTask().getStationId());//getStationForTask(work.getTask());
                if (station != null && station.getIsDeliverable()) {
                    //work.checkToStart();
                    new BundleHelper().createDeliverable(t);
                }
            }
        }
        Gson gson = GSONBuilder.createGSON();
        Type type = new TypeToken<List<Work>>() {
        }.getType();
        response.send(gson.toJson(works, type));
    }
}
