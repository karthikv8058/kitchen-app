package com.smarttoni.assignment.machine;

import android.content.Context;

import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;
import com.smarttoni.SmartTONiService;

public class TimerMachine extends IMachine {

    private long remaining = 0;

    TimerMachine(Work work, long initialTime) {
        super(work);
        remaining = initialTime;
    }

    @Override
    public void stop(Context context) {
        TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();
        workHelper.queueUpdateTask(null, getWork().getId(), Work.COMPLETED, 0, TaskManger.MACHINE_CLOSED);
    }

    long getRemaining() {
        return remaining;
    }

    @Override
    void setReaming(DaoAdapter daoAdapter, long duration) {
        remaining = duration;
        getWork().setTimeRemaining(remaining);
        daoAdapter.updateWork(getWork());
    }

    boolean tick(Context context, DaoAdapter daoAdapter) {
        if (isPaused()) {
            return false;
        }
        remaining -= SmartTONiService.TIMER_INTERVAL;
        getWork().setTimeRemaining(remaining);
        daoAdapter.updateWork(getWork());
        if (remaining <= 0) {
            //TODO
            InterventionManager interventionManager = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getInterventionManager();
            if (interventionManager.popInterventions(getWork(),"")) {
                pause();
                return false;
            }
            stop(context);
            return true;
        }
        return false;
    }

}
