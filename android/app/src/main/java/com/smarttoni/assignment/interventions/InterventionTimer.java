package com.smarttoni.assignment.interventions;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.SmartTONiService;

public class InterventionTimer {

    private InterventionJob interventionJob;

    private long workId;

    private long remaining;

    private boolean paused = false;

    public InterventionTimer(InterventionJob interventionJob, long workId, long time) {
        this.interventionJob = interventionJob;
        this.workId = workId;
        remaining = time;
    }

    public InterventionJob geInterventionJob() {
        return interventionJob;
    }

    void start() {
        paused = false;
    }

    void pause() {
        paused = true;
    }

    void extend(long timeInMins) {
        remaining = timeInMins * 1000;
        paused = false;
    }

    boolean tick(DaoAdapter daoAdapter) {
        if (isPaused()) {
            return false;
        }
        remaining -= SmartTONiService.TIMER_INTERVAL;
        if (remaining <= 0) {
            interventionJob.setStatus(InterventionJob.WAITING);
            if (remaining > 0) {
                Intervention intervention = interventionJob.getIntervention();
                long currentTime = intervention.getTime() - remaining;
                interventionJob.getIntervention().setTime(currentTime);
                daoAdapter.updateIntervention(intervention);
            }
            daoAdapter.updateInterventionJob(interventionJob);
//            if (NewAssignmentLogic.getInstance().popInterventions(context,getWork())) {
//                pause();
//                return false;
//            }
            pause();
            return true;
        }
        return false;
    }

    boolean isPaused() {
        return paused;
    }

    long getWorkId() {
        return workId;
    }

    long getRemaining() {
        return remaining;
    }

    public InterventionJob getInterventionJob() {
        return interventionJob;
    }

//abstract void stop(Context context);
}
