package com.smarttoni.assignment.synergy;

import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.chef.UserManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynergyHelper {

    public static void synergy(Work task, String userId, Machine machine) {
        if (task.getTimePassed() > 0 ) {
            return;
        }
        if (task.getTransportType() == Work.TRANSPORT_TO_LOCATION && task.getTransportMode() == 2) {
            return;
        }
        //TODO not synegy sheduled
        if (checkNotScheduledAndAlreadySynergy(task)) {
            return;
        }
        UserManager userManager = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getUserManager();
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Queue queue = ServiceLocator.getInstance().getQueue();
        List<Work> list = task.getTask() != null && task.isMachineTask() ?
                queue.getNonStartedMachineQueue() : userManager.getUserQueue(daoAdapter, queue, userId);

        if (list == null) {
            return;
        }

        for (Work t : list) {
            if (!t.canStart()) {
                continue;
            }
            if (t.getTimePassed() > 0 ) {
                continue;
            }
            if (checkNotScheduledAndAlreadySynergy(t)) {
                continue;
            }
            if (t.getStatus() == Work.STARTED && t.getUserId() != null && !t.getUserId().equals(userId)) {
                continue;
            }
            boolean checkTransportRoute = task.getTransportRoute() != null && t.getTransportRoute() != null;
            if (t.getTransportType() == Work.TRANSPORT_TO_LOCATION && (checkTransportRoute && t.getTransportMode() == 2 || (task.getTransportRoute() != null && !task.getTransportRoute().equals(t.getTransportRoute())))) {
                continue;
            }

            boolean checkTransport = task.getTransportType() == t.getTransportType() && (task.getTransportType() != Work.TRANSPORT_FROM_INVENTORY || task.getRecipeId().equals(t.getRecipeId()));
            boolean notSameTaskInQueue = !task.getId().equals(t.getId());
            boolean sameTask = task.getTask().getId().equals(t.getTask().getId());
            //TODO boolean sameModifier = task.getOrderLine().getModifiers() != null && task.getOrderLine().getModifiers().equals(t.getOrderLine().getModifiers());
            //boolean notConstantTime = !task.getTask().getOutputTimeFactor();

            if (checkTransport && notSameTaskInQueue && sameTask) {

                updateSynergyTask(task, t);

            }

        }
    }

    private static void updateSynergyTask(Work source, Work target) {
        source.addSynergyList(target);
        TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();
        workHelper.updateWorkStatus(target, Work.SYNERGY);
        target.setSynergyParent(source);
        target.setStartTime(System.currentTimeMillis());
        target.setUser(source.getUser());
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        daoAdapter.updateWork(target);
    }

    @Deprecated
    private static void updateDependentSynergy(Work source, Work target) {
        List<Work> tasks = source.getNextTasks();
        if (tasks == null) {
            return;
        }
        for (Work t : tasks) {
            boolean notConstantTime = !t.getTask().getOutputTimeFactor();
            if (notConstantTime) {
                continue;
            }
            List<Work> targetTasks = target.getNextTasks();
            if (targetTasks == null) {
                continue;
            }
            for (Work t2 : targetTasks) {
                if (t.getTask().getId().equals(t2.getTask().getId())) {
                    updateSynergyTask(t, t2);
                    updateDependentSynergy(t, t2);
                }
            }
        }
    }

    private static boolean checkNotScheduledAndAlreadySynergy(Work task) {
        if (task != null) {
            if (task.getStatus() == Work.SCHEDULED || task.getStatus() == Work.SYNERGY) {
                return true;
            }
            return task.getSynergyList() != null && task.getSynergyList().size() > 0;
        }
        return false;
    }
}
