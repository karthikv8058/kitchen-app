package com.smarttoni.assignment.task;

import com.smarttoni.assignment.RecipeList;
import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.entities.Work;

public class TaskTimeHelper {

    public static long getTaskDuration(Work task) {
        if (task == null || task.getTask() == null) {
            return 0;
        }
        long workDuration = 0;
        if (!task.isTransportTask()) {
            workDuration = task.getTask().getAutoWorkDuration(task) != 0 ?
                    task.getTask().getAutoWorkDuration(task) : task.getTask().getWorkDuration(task);
            workDuration += task.getExtraTime();

            if (task.isMachineTask() && task.getStatus() == Work.STARTED) {
                workDuration = TimerManager.getInstance().getTimeRemainingForMachine(task);
            } else if (task.getExtraTimeSetDate() > 0) {
                long time = task.getExtraTime() - (System.currentTimeMillis() - task.getExtraTimeSetDate());
                task.setTimeRemaining(time);
                if (time > 0) {
                    workDuration = time;
                } else {
                    workDuration = 0;
                }
            } else if (task.getStatus() == Work.STARTED) {
                long time = workDuration - (System.currentTimeMillis() - task.getStartTime());
                task.setTimeRemaining(time);
                if (time > 0) {
                    workDuration = (int) time;
                } else {
                    workDuration = 0;
                }
            }
        }
        long time = workDuration;
        long maxChildTime = 0;
        if (task.getNextTasks() != null) {
            for (Work t : task.getNextTasks()) {
                long timeOfT = getTaskDuration(t);
                if (timeOfT > maxChildTime) {
                    maxChildTime = timeOfT;
                }
            }
        }
        return time + maxChildTime;
    }

    public static void getRecipeDuration(RecipeList list) {
        if (list == null || list.getTasks() == null) {
            return;
        }
        long recipeDuration = 0;
        for (Work t : list.getTasks()) {
            long workDuration = 0;
            if (t.getPrevTasks() == null || t.getPrevTasks().size() == 0) {
                workDuration = getTaskDuration(t);
            } else {
                boolean allPrevTasksAreCompleted = true;
                for (Work p : t.getPrevTasks()) {
                    if (p.getStatus() != Work.COMPLETED && p.getStatus() != Work.QUEUED) {
                        allPrevTasksAreCompleted = false;
                        break;
                    }
                }
                if (allPrevTasksAreCompleted) {
                    workDuration = getTaskDuration(t);
                }
            }
            if (workDuration > recipeDuration) {
                recipeDuration = workDuration;
            }
        }
        list.setDuration(recipeDuration);
    }

    public static long getTaskDurationWithBufferTime(Work task) {
        if (task == null) {
            return 0;
        }
        long workDuration = 0;
        if (!task.isTransportTask()) {
            workDuration = task.getTask().getAutoWorkDuration(task) != 0 ?
                    task.getTask().getAutoWorkDuration(task) : task.getTask().getWorkDuration(task);
            workDuration += task.getTask().getBufferTime();
        }
        long time = workDuration;
        long maxChildTime = 0;
        if (task.getNextTasks() != null) {
            for (Work t : task.getNextTasks()) {
                long timeOfT = getTaskDurationWithBufferTime(t);
                if (timeOfT > maxChildTime) {
                    maxChildTime = timeOfT;
                }
            }
        }
        return time + maxChildTime;
    }

}
