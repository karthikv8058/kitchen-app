package com.smarttoni.assignment.order;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    DaoAdapter daoAdapter;
    List<Work> processedTasks;

    public TaskList(DaoAdapter daoAdapter) {
        this.daoAdapter = daoAdapter;
        processedTasks = new ArrayList<>();
    }

    public void add(Work work) {
        //daoAdapter.saveWork(work);
        processedTasks.add(work);
    }

    public List<Work> getList() {
        return processedTasks;
    }

    public List<Work> getClonedList() {
        return new ArrayList<>(processedTasks);
    }
}
