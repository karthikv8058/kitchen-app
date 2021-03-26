package com.smarttoni.assignment.interventions;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * Planning
 */
public abstract class TaskSegement {


    // |------5------|---------5-----------|------5---------|
    // -------5------|---------5-----------|------5---------| /// Assign Ok, Otherwise ???
    // |-------5-----|---------5-----------|------5---------  /// On Complete


    //Update Task / Add or Remove Intervention ?

    //Work

    //interface
    abstract Segment getCurrentSegment();

    abstract List<Segment> listSegments(Task task);


    void createSegments(Task task) {
        List<Intervention> interventions = task.getInterventions();
        List<Intervention> interventionTree = new ArrayList<>();
        //find first intervention
        for (Intervention intervention : interventions) {
            if (intervention.getParent() == null || intervention.getParent().equals("")) {
                interventionTree.add(intervention);
                break;
            }
        }

    }

    void updateSegment(DaoAdapter daoAdapter, Work work, int duration) {
        Segment segment = null; // Get current working segment
        segment.segmentDuration = (segment.segmentDuration + duration) / 2;
        //updateSegment()
        //TODO increment  work's segment completed
        //TODO Refactor : move on recalculateTaskWorkDuration after complete Task
        recalculateTaskWorkDuration(daoAdapter, work.getTask());
    }

    long recalculateTaskWorkDuration(DaoAdapter daoAdapter, Task task) {
        List<Segment> segments = listSegments(task); //list
        int taskDuration = 0;
        for (Segment segment : segments) {
            taskDuration += segment.segmentDuration;
        }
        task.setAutoWorkDuration(taskDuration);
        daoAdapter.updateTask(task);
        return 0;
    }

    class Segment {
        int segmentDuration = 2;
    }
}
