package com.smarttoni.assignment.util;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.ChefActivityLog;
import com.smarttoni.entities.Work;

public class ActivityLogger {

    public static void log(DaoAdapter daoAdapter, Work task) {
        long timeDifference = 0;
        ChefActivityLog log = daoAdapter.getLastActivityOfWorkStarted(task);
        if (log != null && task.getStatus() == Work.COMPLETED) {
            timeDifference = (System.currentTimeMillis() - log.getCreatedAt());

        }
        ChefActivityLog chefActivityLog = new ChefActivityLog();
        chefActivityLog.setCreatedAt(System.currentTimeMillis());
        chefActivityLog.setUser(task.getUser());
        chefActivityLog.setWork(task);
        chefActivityLog.setOrderId(task.getOrderId());
        chefActivityLog.setWorkDuration(timeDifference);
        chefActivityLog.setTaskId(task.getTaskId());
        chefActivityLog.setStatus(task.getStatus());
        daoAdapter.saveActivityLog(chefActivityLog);
    }
}
