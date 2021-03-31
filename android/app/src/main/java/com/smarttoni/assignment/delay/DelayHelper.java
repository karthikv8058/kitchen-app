package com.smarttoni.assignment.delay;

import android.os.AsyncTask;

import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.RecipeList;
import com.smarttoni.assignment.task.TaskTimeHelper;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.DaoNotFoundException;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelayHelper {

    private DaoAdapter daoAdapter;


    private DelayHelper() {
    }

    private static DelayHelper INSTANCE = new DelayHelper();

    public static DelayHelper getInstance() {
        return INSTANCE;
    }


    private void calculateDelay(Queue queue) {
        Map<String, List<Work>> map = new HashMap<String, List<Work>>();
        List<Work> workList = queue.getCloneQueue();
        for (Work task : workList) {
            // if delay status 0
            if (task.getStatus() == Work.QUEUED ||
                    task.getStatus() == Work.STARTED ||
                    task.getStatus() == Work.SCHEDULED) {
                if (task.getCourse() != null) {
                    List<Work> courseTaskList = map.get(task.getCourse().getId());
                    if (map.get(task.getCourse().getId()) == null) {
                        courseTaskList = new ArrayList<>();
                        String courseId = task.getCourse().getId();
                        map.put(courseId, courseTaskList);
                    }
                    courseTaskList.add(task);
                }
            }
        }
        for (String key : map.keySet()) {
            List<Work> list = map.get(key);
            calculate(list);
        }
        queue.sort();
    }

    private void calculate(List<Work> queue) {

        Map<String, RecipeList> times = new HashMap<String, RecipeList>();
        long maxDuration = 0;
        for (Work q : queue) {
            long mealsId = q.getMealsId();
            String recipeId = q.getRecipeId();
            String key = new StringBuilder().append(mealsId)
                    .append("*").append(recipeId).toString();

            RecipeList recipeList = times.get(key);
            if (recipeList == null) {
                recipeList = new RecipeList();
                times.put(key, recipeList);
            }
            recipeList.addTask(q);
        }

        //TodO new logic
        for (Map.Entry<String, RecipeList> entry : times.entrySet()) {
            RecipeList list = times.get(entry.getKey());
            TaskTimeHelper.getRecipeDuration(list);
            if (list.getDuration() > maxDuration) {
                maxDuration = list.getDuration();
            }
        }
        Course course = daoAdapter.getCourseById(queue.get(0).getCourse().getId());
        if(course == null){
            return;
        }
        long deliveryTime = course.getDeliveryTime();
        long mtdt = deliveryTime;
        long timeForLogesTask = System.currentTimeMillis() + maxDuration;
        if (timeForLogesTask > deliveryTime) {
            mtdt = timeForLogesTask;
        }
        //updating actual delivery time
        course.setActualDeliveryTime(mtdt);
        course.setExpectedDeliveryTime(timeForLogesTask);
        daoAdapter.updateCourse(course);
        //updated
        for (String key : times.keySet()) {
            RecipeList recipeList = times.get(key);
            if (recipeList == null) {
                continue;
            }
            long delay;
            if (recipeList.getDuration() == maxDuration) {
                delay = deliveryTime - timeForLogesTask;
            } else {
                delay = mtdt - (System.currentTimeMillis() + recipeList.getDuration());
            }
            for (Work t : recipeList.getTasks()) {
                if (t.getCourse() != null) {
                    t.getCourse().setActualDeliveryTime(mtdt);
                }
                t.setPriority(-1 * delay);
            }
            scheduleTasks(recipeList.getTasks(), mtdt);
        }
    }

    private void scheduleTasks(List<Work> list, long deliveryTime) {
        List<Work> tasks = new ArrayList<>(list);
        outLoop:
        for (Work t : tasks) {
            if (t != null) {
                if (t.getPrevTasks() != null) {
                    for (Work q : t.getPrevTasks()) {
                        if (q.getStatus() == Work.SCHEDULED) {
                            continue outLoop;
                        }
                    }
                }
                if (t.getStatus() != Work.SCHEDULED) {
                    continue;
                }
                long time = TaskTimeHelper.getTaskDurationWithBufferTime(t);
                long expected = deliveryTime - time;
                long cuurentTime = System.currentTimeMillis();
                long expectedWithBuffer = expected - (10 * 1000);
                if (expectedWithBuffer <= cuurentTime) {
                    _scheduleTask(t, true);
                }
            }
        }

    }

    private void _scheduleTask(Work task, boolean isRoot) {
        if (task != null) {
            if (!isRoot && task.isSheduleSeparately()) {
                return;
            }
            if (!isRoot && task.getTask().isStartBeforeDelivery() && task.getStatus() == Work.SCHEDULED) {
                return;
            }
            if (task.getPrevTasks() != null) {
                for (Work t : task.getPrevTasks()) {
                    if (t.getStatus() == Work.SCHEDULED) {
                        return;
                    }
                }
            }
            TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();
            workHelper.updateWorkStatus(task, Work.QUEUED);
            daoAdapter.updateWork(task);
            if (task.getNextTasks() != null) {
                for (Work t : task.getNextTasks()) {
                    _scheduleTask(t, false);
                }
            }
        }
    }

    public void init(DaoAdapter daoAdapter) {
        this.daoAdapter = daoAdapter;
    }

    public void start(Queue queue) throws DaoNotFoundException {
        if (daoAdapter == null) {
            throw new DaoNotFoundException();
        }
        calculateDelay(queue);
    }

    public void startAsync(Queue queue) throws DaoNotFoundException {
        if (daoAdapter == null) {
            throw new DaoNotFoundException();
        }
        new DelayCalculationTask().doInBackground(queue);
    }

    class DelayCalculationTask extends AsyncTask<Queue, Void, Void> {

        public DelayCalculationTask() {

        }

        @Override
        protected Void doInBackground(Queue... queues) {
            calculateDelay(queues[0]);
            return null;
        }
    }
}
