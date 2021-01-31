package com.smarttoni.assignment.service;

import android.os.Bundle;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.TaskStep;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

public class PusherService {

    public void sendInterventionPush(InterventionJob interventionJob) {
        //Load Data
        if (interventionJob.getIntervention() != null) {
            //TODO Why getIntervention null
            if (interventionJob.getIntervention().getTaskSteps() != null) {
                for (TaskStep interventionSteps : interventionJob.getIntervention().getTaskSteps()) {
                    interventionSteps.getIngredients();
                }
            }
        }
        if (interventionJob.getWork() != null) {
            if (interventionJob.getWork().getTask() != null) {
                interventionJob.getWork().getTask().getStationId();
            }
        }

        Bundle bundle=new Bundle();
        bundle.putString(InterventionJob.USER_ID,interventionJob.getUserId());
        bundle.putLong(InterventionJob.INTERVENTION_ID,interventionJob.getId());
        EventManager.getInstance().emit(Event.SEND_PEGION,bundle);
    }

}
