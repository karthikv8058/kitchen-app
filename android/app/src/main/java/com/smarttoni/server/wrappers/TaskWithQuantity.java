package com.smarttoni.server.wrappers;

import com.smarttoni.entities.Task;

public class TaskWithQuantity {

    private Task task;
    private float quantity;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
