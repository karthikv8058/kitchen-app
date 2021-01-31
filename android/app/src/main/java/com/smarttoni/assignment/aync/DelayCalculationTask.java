package com.smarttoni.assignment.aync;

import android.os.AsyncTask;

import com.smarttoni.assignment.Queue;
import com.smarttoni.entities.Order;

public class DelayCalculationTask extends AsyncTask<Order, Void, Void> {

    private Queue queue;

    public DelayCalculationTask(Queue queue) {
        this.queue = queue;
    }

    @Override
    protected Void doInBackground(Order... orders) {
        //DelayHelper.getInstance().start( queue);
        return null;
    }
}
