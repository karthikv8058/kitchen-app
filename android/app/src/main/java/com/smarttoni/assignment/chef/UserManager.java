package com.smarttoni.assignment.chef;

import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.User;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private SmarttoniContext context;

    public UserManager(SmarttoniContext context) {
        this.context = context;
    }

    public List<Work> getUserQueue(DaoAdapter daoAdapter, Queue queue, String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Work> works = queue.getCloneQueue();
        List<Work> result = new ArrayList<>();
        for (Work work : works) {
            if (!work.getTaskId().equals("1")) {
                if (!isUserAssignedInStation(daoAdapter, work, userId)) {
                    continue;
                }
            }
            boolean isDeliverableTask = (work.getTransportType() & Work.TRANSPORT_DELIVERABLES) > 0;

            boolean isMachineTask = work.isMachineTask();
            boolean isQueued = work.getStatus() == Work.QUEUED;
            boolean isAssignedToUser = work.getStatus() == Work.STARTED && ((userId.equals(work.getUserId()) || isDeliverableTask));

            if (!isMachineTask && (isQueued || isAssignedToUser)) {
                result.add(work);
            } else if (work.getStatus() == Work.SCHEDULED) {
                Station station = daoAdapter.getStationById(work.getTask().getStationId());
                if (station != null) {
                    if (station.getIsDeliverable()) {
                        result.add(work);
                    }
                }
            }
        }
        return result;
    }

    public Work getActiveTaskByUser(String userId) {
        TaskManger taskManger = context.getTaskManger();
        return taskManger.getTask(userId);
    }

    private List<String> getUsers() {
        List<String> userIds = new ArrayList<>();
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        List<UserStationAssignment> userStationAssignment = daoAdapter.loadUserStationAssignmentDao();
        for (UserStationAssignment user : userStationAssignment) {
            for (String userId : userIds) {
                if (userId.equals(user.getUserid())) {
                    continue;
                }
            }
            userIds.add(user.getUserid());
        }

        ArrayList<String> list = new ArrayList<String>();
        for (String id : userIds) {
            if (!list.contains(id)) {
                list.add(id);
            }
        }
        return list;
    }

    public User getUserById(String userId) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        return daoAdapter.getUserById(userId);
    }

    public List<String> getIdleUsers() {
        List<String> userIds = getUsers();
        List<String> idleUsers = new ArrayList<>();
        if (userIds == null) {
            userIds = new ArrayList<>();
        }

        for (String userId : userIds) {
            Work w = getActiveTaskByUser(userId);
            if (w != null){
                continue;
            }
            User user = getUserById(userId);
            if (user != null) {
                if (user.getAutoAssign()) {
                    idleUsers.add(userId);
                }
            }
        }
        return idleUsers;
    }

    public boolean isUserAssignedInStation(DaoAdapter daoAdapter, Work work, String userId) {
        if (userId == null || work.getTask().getStationId() == null) {
            return false;
        }
        return daoAdapter.isUserAssignedInStation(userId, (work.getTask().getStationId()));
    }

    public void logout(String userId) {
        Work t = getActiveTaskByUser(userId);
        if (t != null) {
            TaskManger workHelper = context.getTaskManger();
            workHelper.updateWorkStatus(t, Work.QUEUED);
        }
        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();
    }

}
