package com.smarttoni.assignment;

import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.List;

public class RecipeList {

    private long duration;
    private List<Work> tasks;

    public void addTask(Work t) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(t);
    }

    public List<Work> getTasks() {
        return tasks;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
