package com.smarttoni.assignment.interventions;

import android.content.Context;

import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.chef.UserManager;
import com.smarttoni.assignment.machine.TimerMachine;
import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.assignment.service.PusherService;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Segment;
import com.smarttoni.entities.User;
import com.smarttoni.entities.Work;
import com.smarttoni.utils.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InterventionManager {

    private SmarttoniContext context;

    public InterventionManager(SmarttoniContext context) {
        this.context = context;
    }


    InterventionRegister register = new InterventionRegister();

    public static final String INTERVENTION_JOB = "INTERVENTION_JOB";

    List<InterventionTimer> interventionTimers = new ArrayList<>();
    private Map<Long, InterventionTimer> map = new HashMap<>();

    public void loop(DaoAdapter daoAdapter) {
//        List<InterventionTimer> t = new ArrayList<>(interventionTimers);
        Iterator<InterventionTimer> timers = interventionTimers.iterator();
        while (timers.hasNext()) {
            InterventionTimer timer = timers.next();
            Work work = ServiceLocator.getInstance().getDatabaseAdapter().getWorkById(timer.getWorkId());
            if (work != null && (work.getStatus() == Work.COMPLETED || work.getStatus() == Work.REMOVED)) {
                timers.remove();
            }
            timer.tick(daoAdapter);
        }
//        for (InterventionTimer timers : t) {
//            timers.tick(daoAdapter);
//        }
        pushAllInterventions(daoAdapter);
        checkAllWaitingInterventions(daoAdapter);
    }

    public void removeOrder(String orderId) {
        Iterator<InterventionTimer> timers = interventionTimers.iterator();
        while (timers.hasNext()) {
            InterventionTimer timer = timers.next();
            if (timer.getInterventionJob().getOrderId() != null && timer.getInterventionJob().getOrderId().equals(orderId)) {
                timers.remove();
                map.remove(timer.getInterventionJob().getId());
                break;
            }
        }
    }

    private void pushAllInterventions(DaoAdapter daoAdapter) {
        List<InterventionJob> jobs = daoAdapter.getStartedInterventionJobs();
        for (InterventionJob job : jobs) {
            sendInterventionPush(job);
        }
    }

    private void checkAllWaitingInterventions(DaoAdapter daoAdapter) {
        List<InterventionJob> jobs = daoAdapter.getWaitingInterventionJobs();
        for (InterventionJob job : jobs) {
            assignUserAndShowIntervention(daoAdapter, job);
        }
    }

    public void showAssignedInterventions(DaoAdapter daoAdapter, String userId) {
        if (userId == null) {
            List<InterventionJob> jobs = daoAdapter.getWaitingInterventionJobs();
            for (InterventionJob job :
                    jobs) {
                assignUserAndShowIntervention(daoAdapter, job);
            }
            return;
        }
        List<InterventionJob> jobs = daoAdapter.getInterventionJobByUserId(userId, false);
        if (jobs == null || jobs.size() == 0) {
            return;
        }

        for (InterventionJob job : jobs) {
            if (job.getStatus() == InterventionJob.STARTED) {
                return;
            }
        }
        InterventionJob interventionJob = jobs.get(0);
        updateInterventionStatus(interventionJob, InterventionJob.STARTED);
        daoAdapter.updateInterventionJob(interventionJob);
        sendInterventionPush(interventionJob);
    }


    public void startTimer(DaoAdapter daoAdapter, InterventionJob interventionJob) {
        if (interventionJob.getStartedAt() == null) {
            interventionJob.setStartedAt(new Date());
        }
        long timeInMills = interventionJob.getIntervention().getTime();
        if (interventionJob.getExtraTime() > 0) {
            timeInMills = interventionJob.getExtraTime();
        }
        daoAdapter.updateInterventionJob(interventionJob);

        if (timeInMills == 0) {
            interventionJob.setStatus(InterventionJob.WAITING);
            daoAdapter.updateInterventionJob(interventionJob);
            return;
        }
        addOrGetInterventionTimer(interventionJob, timeInMills);
    }

    private InterventionTimer addOrGetInterventionTimer(InterventionJob interventionJob, long timeInMills) {
        InterventionTimer timer = map.get(interventionJob.getId());
        if (timer == null) {
            timer = new InterventionTimer(interventionJob, interventionJob.getWorkId(), timeInMills);
            interventionTimers.add(timer);
            map.put(interventionJob.getId(), timer);
        }
        return timer;
    }

    private void pauseOtherInterventionsFor(long workId) {
        List<InterventionTimer> t = new ArrayList<>(interventionTimers);
        for (InterventionTimer timer : t) {
            if (timer.getWorkId() == workId) {
                timer.pause();
            }
        }
    }

    public boolean hasIntervention(Long interventionId) {
        InterventionTimer timer = map.get(interventionId);
        return timer != null && timer.getRemaining() > 0;
    }

    public void finishInterventionJob(InterventionJob interventionJob) {
        InterventionTimer timer = map.get(interventionJob.getId());
        interventionTimers.remove(timer);
        map.remove(interventionJob.getId());
        startAllInterventionsFor(interventionJob.getWorkId());
    }

    private void startAllInterventionsFor(long workId) {
        List<InterventionTimer> t = new ArrayList<>(interventionTimers);
        for (InterventionTimer timer : t) {
            if (timer.getWorkId() == workId) {
                timer.start();
            }
        }
    }

    public void resumeAllInterventions(long workId) {
        List<InterventionTimer> t = new ArrayList<>(interventionTimers);
        for (InterventionTimer timer : t) {
            if (timer.getWorkId() == workId) {
                timer.start();
            }
        }
    }

    public void extend(InterventionJob interventionJob, long timeInMills) {
        InterventionTimer timer = addOrGetInterventionTimer(interventionJob, timeInMills);
        timer.extend(timeInMills);
    }


    public InterventionJob getNearest(List<InterventionJob> interventionJobs) {
        InterventionJob nearest = null;
        long timeRemainsForNearest = 0;
        for (InterventionJob job : interventionJobs) {
            InterventionTimer timer = map.get(job.getId());
            if (timer == null) {
                continue;
            }
            if (nearest == null) {
                nearest = job;
                timeRemainsForNearest = timer.getRemaining();
            } else {
                if (timer.getRemaining() < timeRemainsForNearest) {
                    nearest = job;
                    timeRemainsForNearest = timer.getRemaining();
                }
            }
        }
        return nearest;
    }

    public void assignUserAndShowIntervention(DaoAdapter daoAdapter, InterventionJob interventionJob) {
        User user = interventionJob.getUser();
        String userId = interventionJob.getUserId();
        if (user == null || !user.getOnline()) {
            userId = getUserToNotify(interventionJob);
        }
        if (userId == null) {
            return;
        }
        if (daoAdapter.getInterventionJobByUserId(userId, true).size() > 0) {
            return;
        }
        interventionJob.setUserId(userId);
        updateInterventionStatus(interventionJob, InterventionJob.STARTED);
        daoAdapter.updateInterventionJob(interventionJob);
        sendInterventionPush(interventionJob);
    }

    private boolean isUserIdle(String userId, Work work, boolean checkTaskAssignment) {
        if (register.hasIntervention(userId)) {
            return false;
        }
        if (!checkTaskAssignment) {
            return true;
        }
        TaskManger taskManger = context.getTaskManger();
        Work w = taskManger.getTask(userId);
        return w == null || w.getId().equals(work.getId());
    }


    private String getUserToNotify(InterventionJob interventionJob) {

        Work work = interventionJob.getWork();

        String uid = register.getUser(interventionJob.getId());

        if (uid != null && uid != "") {
            return uid;
        }


        DaoAdapter daoAdapter = ServiceLocator
                .getInstance()
                .getDatabaseAdapter();


        if (work.getMeal() != null && Strings.isNotEmpty(work.getMeal().getChefId())) {
            User user = daoAdapter.getUserById(work.getMeal().getChefId());
            if (user != null && user.getOnline()) {
                if (isUserIdle(user.getId(), work, false)) {
                    return user.getId();
                } else {
                    return null;
                }
            }
        }


        //Map<String, Long> userAndWorks = UserManager.getInstance().getUsersAndWorks(ServiceLocator.getInstance().getQueue());

        boolean isAssignedUserInOnline = false;
        if (!work.isMachineTask() && work.getStatus() == Work.STARTED && work.getUserId() != null) {
            isAssignedUserInOnline = daoAdapter.getUserById(work.getUserId()).getOnline();
            if (isAssignedUserInOnline && isUserIdle(work.getUserId(), work, true)) {
                return work.getUserId();
            }
        }

        if (work.getTask() != null && work.getTask().getStationId() != null) {
            List<User> users = daoAdapter
                    .getUsersForStation(work.getTask().getStationId());
            for (User user : users) {
                if (user.getOnline() && isUserIdle(user.getId(), work, true)) {
                    return user.getId();
                }
            }
        }

        List<User> users = daoAdapter.loadUsers();

        for (User user : users) {
            if (user.getOnline() && isUserIdle(user.getId(), work, true)) {
                return user.getId();
            }
        }

        //// Force Assign
        if (work.getUserId() != null) {
            if (isAssignedUserInOnline && isUserIdle(work.getUserId(), work, false)) {
                return work.getUserId();
            }
        }

        if (work.getTask() != null && work.getTask().getStationId() != null) {
            List<User> list = daoAdapter
                    .getUsersForStation(work.getTask().getStationId());
            for (User user : list) {
                if (user.getOnline() && isUserIdle(user.getId(), work, false)) {
                    return user.getId();
                }
            }
        }

        for (User user : users) {
            if (user.getOnline() && isUserIdle(user.getId(), work, false)) {
                return user.getId();
            }
        }

        return null;

    }

    public void sendInterventionPush(InterventionJob interventionJob) {
        if (interventionJob.getStatus() != InterventionJob.STARTED) {
            return;
        }
        //pause this work
        TimerManager.getInstance().pause(interventionJob.getWork());


        //pause all interventions for this work
        pauseOtherInterventionsFor(interventionJob.getWorkId());

        register.register(interventionJob.getUserId(), interventionJob.getId());

        PusherService pusherService = (PusherService) ServiceLocator.getInstance().getService(ServiceLocator.PUSH_SERVICE);
        pusherService.sendInterventionPush(interventionJob);
    }

    public List<InterventionJob> createAllInterventionJob(DaoAdapter daoAdapter, Work work) {
        List<InterventionJob> interventionJobs = new ArrayList<>();
        if (work.getTask() != null) {
            List<Intervention> interventions = work.getTask().getInterventions();
            for (Intervention intervention : interventions) {
                InterventionJob interventionJob = new InterventionJob();
                interventionJob.setWork(work);
                interventionJob.setOrderId(work.getOrderId());
                interventionJob.setIntervention(intervention);
                interventionJob.setStatus(InterventionJob.QUEUED);
                interventionJobs.add(interventionJob);
            }
        }
        return interventionJobs;
    }


    public boolean popInterventions(Work work, String userId) {
        if (work == null) {
            return false;
        }
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        work.resetInterventionJobs();
        List<InterventionJob> interventionJobs = work.getInterventionJobs();
        int interventionToBeComplete = 0;
        InterventionJob scheduleFirst = null;
        List<InterventionJob> queuedInterventions = new ArrayList<>();
        for (InterventionJob interventionJob : interventionJobs) {
            if (interventionJob.getStatus() == InterventionJob.COMPLETED) {
                continue;
            }
            interventionToBeComplete++;
            if (interventionJob.getStatus() == InterventionJob.STARTED) {
                this.sendInterventionPush(interventionJob);
                return true;
            } else if (interventionJob.getStatus() == InterventionJob.QUEUED) {
                interventionJob.setUserId(userId);
                queuedInterventions.add(interventionJob);
                Intervention intervention = interventionJob.getIntervention();
                int position = intervention.getInterventionPosition();
                if (position == Intervention.POSITION_START && intervention.getTime() == 0) {
                    scheduleFirst = interventionJob;
                }
            }
        }
        if (scheduleFirst != null) {
            scheduleFirst.setNoStart(true);
            assignUserAndShowIntervention(daoAdapter, scheduleFirst);
            return true;
        }
        //Loop Queue and Find Nearest;
        InterventionJob job = getNearest(queuedInterventions);
        if (job != null) {
            assignUserAndShowIntervention(daoAdapter, job);
            return true;
        }

        //TODO fix
        for (InterventionJob interventionJob : interventionJobs) {
            if (interventionJob.getStatus() == InterventionJob.QUEUED) {
                //interventionJob.setScheduleNext(true);
                //if there is only one end intervention, issue auto close machine after intervention close
                if (interventionToBeComplete == 1 &&
                        interventionJob.getIntervention().getInterventionPosition() == Intervention.POSITION_END) {
                    interventionJob.setScheduleNext(true);
                }
                assignUserAndShowIntervention(daoAdapter, interventionJob);
                return true;
            } else if (interventionJob.getStatus() == InterventionJob.STARTED) {
                sendInterventionPush(interventionJob);
                return true;
            }
        }
        return false;
    }

    /**
     * Update Intervention Status to Complete or Queue
     *
     * @param context
     * @param userId       requested user Id
     * @param intervention to update
     * @param status       status defined in {@link InterventionJob} use QUEUED or COMPLETED
     * @param time         this intervention again pop-up after this time
     */
    public synchronized void updateIntervention(Context context, String userId, long intervention, int status, int time, boolean reduceValue) {

        register.unregister(userId);

        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        InterventionJob interventionJob = daoAdapter.getInterventionJobById(intervention);
        if (interventionJob == null) {
            return;
        }
        //if completed no need for further process
        if (interventionJob.getStatus() == InterventionJob.COMPLETED) {
            return;
        }
        //update intervention status
        updateInterventionStatus(interventionJob, status);
        if (time > 0) {
            interventionJob.setExtraTime(time);
        }
        daoAdapter.updateInterventionJob(interventionJob);
        Work work = ServiceLocator.getInstance().getQueue().getTask(interventionJob.getWorkId());
        Intervention i = interventionJob.getIntervention();
        if (status == InterventionJob.COMPLETED) {
            //start working on the task since we completed the intervention
            int interventionsCompleted = work.getInterventionsCompleted() + 1;
            long now = System.currentTimeMillis();
            long lastTime = work.getLastIntervention();

            if (i.getInterventionPosition() != Intervention.POSITION_END) {
                work.setInterventionsCompleted(interventionsCompleted);
                if (lastTime > 0) {
                    TaskManger workHelper = this.context.getTaskManger();
                    workHelper.updateSegment(work.getId(), 0);
                }
                work.setLastIntervention(now);
                int duration = 0;
                List<Segment> segments = daoAdapter.loadSegments(work.getTaskId());
                for (Segment segment : segments) {
                    if (segment.getPosition() > interventionsCompleted) {
                        duration += segment.getDuration();
                    }
                }
                TimerManager.getInstance().setReaming(daoAdapter, work, duration);
            }
            work.setUserId(userId);
            daoAdapter.updateWork(work);
            TimerManager.getInstance().restart(work);
            //start other interventions same task
            resumeAllInterventions(work.getId());
            //get child interventions and show/start timer
            List<InterventionJob> jobs = daoAdapter.getInterventionJobChilds(interventionJob);
            for (InterventionJob job : jobs) {
                if (job.getIntervention().getTime() == 0) {
                    job.setUserId(userId);
                    job.setStatus(InterventionJob.WAITING);
                    daoAdapter.updateInterventionJob(job);
                } else {
                    startTimer(daoAdapter, job);
                }
            }
            //if its an starting intervention, then start the task
            if (interventionJob.getIntervention().getTime() == 0
                    && interventionJob.getIntervention().getInterventionPosition() == Intervention.POSITION_START) {
                if (work.isMachineTask()) {
                    AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator
                            .getInstance()
                            .getService(ServiceLocator.ASSIGNMENT_SERVICE);
                    assignmentFactory.assign();
                } else {
                    if (!interventionJob.getNoStart()) {
                        TaskManger workHelper = this.context.getTaskManger();
                        workHelper.reAssignTask(userId, work.getId(), false);
                    }
                }
            }
            //if this intervention is triggered by machine stop, then close the machine
            if (interventionJob.getScheduleNext()) {
                TimerManager.getInstance().forceStop(context, work);
            }
            if (reduceValue) {
                if (interventionJob.getStartedAt() != null) {
                    int currentTime = (int) ((System.currentTimeMillis() - interventionJob.getStartedAt().getTime()));
                    if (currentTime < i.getTime()) {
                        i.setTime(currentTime);
                        daoAdapter.updateIntervention(i);
                    }
                }
            }
            if (!interventionJob.getScheduleNext() && i.getInterventionPosition() == Intervention.POSITION_END) {
                TaskManger workHelper = this.context.getTaskManger();
                workHelper.queueUpdateTask(userId, interventionJob.getWorkId(), Work.COMPLETED, 0, TaskManger.INTERVENTION_CLOSED);
            }
        } else if (status == InterventionJob.QUEUED && time > 0) {
            extend(interventionJob, time);
        }
        showAssignedInterventions(daoAdapter, userId);
    }

    public void updateInterventionStatus(InterventionJob interventionJob, int status) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        daoAdapter.setOrderStarted(interventionJob.getOrderId());
        if (status == InterventionJob.WAITING) {
            interventionJob.setWaitingFrom(System.currentTimeMillis());
        } else if (status == InterventionJob.COMPLETED) {
            finishInterventionJob(interventionJob);
        }
        interventionJob.setStatus(status);
    }

    public void onInterventionPushFailed(String userId) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        //Find intervention for user and Reset Intervention user Id
        List<InterventionJob> interventionJobs = daoAdapter.getInterventionJobByUserId(userId, true);
        if (interventionJobs != null) {
            for (InterventionJob job : interventionJobs) {
                job.setUserId("");
                job.setStatus(InterventionJob.WAITING);
                daoAdapter.updateInterventionJob(job);
            }
        }
        //push intervention
        loop(daoAdapter);
    }
}
