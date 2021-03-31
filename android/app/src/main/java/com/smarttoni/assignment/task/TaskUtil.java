package com.smarttoni.assignment.task;

import com.smarttoni.entities.Work;

public class TaskUtil {

    public static long getWorkingForTaskInMinutes(Work work) {
        long workDuration = work.getTask().getAutoWorkDuration(work) != 0 ?
                work.getTask().getAutoWorkDuration(work) : work.getTask().getWorkDuration(work);
        return workDuration;
    }

    public static long getWorkingForTaskInMillis(Work work) {
        long workDuration = work.getTask().getAutoWorkDuration(work) != 0 ?
                work.getTask().getAutoWorkDuration(work) : work.getTask().getWorkDuration(work);
        return workDuration;
    }

}
