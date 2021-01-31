package com.smarttoni.entities;

public class StationQueueTask {
    public Task task;
    public Work work;

    public StationQueueTask(Task task, Work work) {
        this.task = task;
        this.work = work;
    }
}
