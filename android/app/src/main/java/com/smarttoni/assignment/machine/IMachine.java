package com.smarttoni.assignment.machine;

import android.content.Context;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;

public abstract class IMachine {

    private Work work;

    IMachine(Work work) {
        this.work = work;
    }

    public Work getWork() {
        return work;
    }

    private boolean paused = false;

    void start() {
        paused = false;
    }

    void pause() {
        paused = true;
    }

    boolean isPaused() {
        return paused;
    }

    abstract void stop(Context context);

    abstract void setReaming(DaoAdapter daoAdapter, long duration);
}
