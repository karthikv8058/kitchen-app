package com.smarttoni.assignment.task;

import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.util.ActivityLogger;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.delay.DelayHelper;
import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.assignment.order.OrderManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.synergy.SynergyHelper;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.utils.Strings;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.DaoNotFoundException;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Segment;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.User;
import com.smarttoni.entities.Work;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

import java.util.Iterator;
import java.util.List;

public class TaskManger {

    public static final int CHEF_CLOSED = 0;
    public static final int MACHINE_CLOSED = 1;
    public static final int INTERVENTION_CLOSED = 2;

    TaskRegister taskRegister = new TaskRegister();

    private SmarttoniContext context;

    public TaskManger(SmarttoniContext context) {
        this.context = context;
    }

    public synchronized void queueUpdateTask(String userId, Long taskQueueId, int status, int time, int completedFrom) {

        Work t = getQueue().getTask(taskQueueId);
        if (t == null) {
            return;
        }

        //New if same status as current  just return
        if (t.getStatus() == status) {
            return;
        }

        DaoAdapter daoAdapter = getDaoAdapter();

        //Database Sync
        Work _work = daoAdapter.getWorkById(t.getId());
        if (_work.getStatus() == Work.COMPLETED || _work.getStatus() == Work.REMOVED) {
            return;
        }
        t.setInterventionsCompleted(_work.getInterventionsCompleted());

        if (Strings.isNotEmpty(userId)) {
            t.setUserId(userId);
        }

        //Handle Deliverable Task
        if ((t.getTransportType() & Work.TRANSPORT_DELIVERABLES) > 0) {
            updateWorkStatus(t, status);
            if (status == Work.COMPLETED) {
                List<Work> works = t.getDeliveryList();
                if (works != null) {
                    for (Work w : works) {
                        updateWorkStatus(w, Work.COMPLETED);
                        w.setUserId(userId);
                        daoAdapter.updateWork(w);
                        ActivityLogger.log(daoAdapter, w);
                    }
                }
                t.setUserId(userId);
                ActivityLogger.log(daoAdapter, t);
                daoAdapter.updateWork(t);
                OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
                orderManager.checkOrderCompleted(t.getOrder(), t.getMeal());
                //TODO checkOrderCompleted(context, t.getOrder(), t.getMeal());
            }
            return;
        }

        if (status == Work.STARTED) {

            if (t.getUserId() != null || t.isMachineTask()) {
                updateWorkStatus(t, Work.STARTED);
            } else {
                updateWorkStatus(t, Work.QUEUED);
            }

            if (t.getMachine() != null) {
                t.setExtraTime(time);
                t.setTimeRemaining(time);
                t.setExtraTimeSetDate(System.currentTimeMillis());
            }
            if (t.getSynergyList() != null) {
                for (Work work : t.getSynergyList()) {
                    updateWorkStatus(work, t.getStatus());
                    work.setUserId(userId);
                    ActivityLogger.log(daoAdapter, work);
                    updateWorkStatus(work, Work.SYNERGY);
                }
            }
            daoAdapter.updateWork(t);
            EventManager.getInstance().emit(Event.ORDER_RECEIVED);
            return;
        }

        ActivityLogger.log(daoAdapter, t);


        //pop pending interventions && if there is pending intervention just return
        if (status == Work.COMPLETED) {
            InterventionManager interventionManager = context.getInterventionManager();
            if (interventionManager.popInterventions(t, userId)) {
                return;
            }
        }

        updateWorkStatus(t, status);

        if (status == Work.COMPLETED) {

            //If it End of SubTree , mark its used , to handle inventory or to move inventory

            if (t.getIsEndNode()) {
                if (t.getSubRecipes() != null) {
                    String[] ids = t.getSubRecipes().split(",");
                    for (String id : ids) {
                        if (id.equals("")) {
                            continue;
                        }
                        daoAdapter.setAsUsed(t.getOrderLineId(), id);
                    }
                }
                if (Strings.isNotEmpty(t.getExtraQuantity())) {
                    String[] recipes = t.getExtraQuantity().split("&");
                    for (String r : recipes) {
                        if (Strings.isEmpty(r)) {
                            continue;
                        }
                        String[] recipeQty = r.split(":");
                        if (recipeQty.length == 2) {
                            String recipe = recipeQty[0];
                            float qty = Float.parseFloat(recipeQty[1]);
                            InventoryManagement.moveToInventory(t.getOrderId(),recipe, qty, daoAdapter);
                        }
                    }

                }
            }

            int additionalInterventionCompleted = 0;
            if (completedFrom != INTERVENTION_CLOSED) {
                additionalInterventionCompleted++;
            }
            updateSegment(t.getId(), additionalInterventionCompleted);

            //update task time
            List<Segment> segments = daoAdapter.loadSegments(t.getTaskId());
            int workDuration = 0;
            for (Segment segment : segments) {
                workDuration += segment.getDuration();
            }
            Task task = daoAdapter.getTaskById(t.getTaskId());
            task.setWorkDuration(workDuration);
            daoAdapter.updateTask(task);

            getQueue().remove(t);

            OrderManager orderManager = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getOrderManager();
            if (orderManager.checkOrderCompleted(t.getOrder(), t.getMeal())) {
                if (t.getSynergyList() != null) {
                    for (Work w : t.getSynergyList()) {
                        orderManager.checkOrderCompleted(w.getOrder(), w.getMeal());
                    }
                }
            }
        }

        if (t.getStatus() == Work.COMPLETED) {
            t.setUserId(userId);
        }

        daoAdapter.updateWork(t);
        ActivityLogger.log(daoAdapter, t);

        DelayHelper delayHelper = (DelayHelper) ServiceLocator
                .getInstance()
                .getService(ServiceLocator.DELAY_CALCULATOR_SERVICE);
        try {
            delayHelper.startAsync(getQueue());
        } catch (DaoNotFoundException e) {
            //e.printStackTrace();
        }
        AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
        assignmentFactory.assign();
        //TODO  new AssignTasks(context).doInBackground();
    }


    public synchronized void autoAssign() {
        List<Work> tasks = getQueue().getCloneQueue();
        if (tasks.isEmpty()) {
            return;
        }
        List<String> userIds = context
                .getUserManager()
                .getIdleUsers();
        for (Work t : tasks) {
            userIds.iterator();
            if (t.getStatus() != Work.QUEUED) {
                continue;
            }
            //Assign for Machine
            if (t.getTask() != null && t.isMachineTask()) {
                if (t.getStatus() != Work.QUEUED || t.isTransportTask()) {
                    continue;
                }
                Machine machine = getDaoAdapter().getMachineById(t.getTask().getMachineId());
                assign(t, null, machine);
            } else {
                //Assign for User
                //TODO get user from registery
                Iterator<String> userIdIterator = userIds.iterator();
                while (userIdIterator.hasNext()) {
                    String userId = userIdIterator.next();
                    if (getDaoAdapter().isUserAssignedInStation(userId, t.getTask().getStationId())) {
                        if (assign(t, userId, t.getMachine())) {
                            userIdIterator.remove();
                        }
                    }
                }
            }
        }
    }

    public boolean assign(Work work, String userId, Machine machine) {
        return assign(work, userId, machine, false);
    }

    public synchronized boolean assign(Work work, String userId, Machine machine, boolean force) {
        if (work == null) {
            return false;
        }
        if (!work.canStart()) {
            return false;
        }

        DaoAdapter daoAdapter = getDaoAdapter();

        //If deliverable, Do not assign, skip
        Station station = daoAdapter.getStationById(work.getTask().getStationId());
        if (station != null) {
            if (station.getIsDeliverable()) {
                return false;
            }
        }

        //Mark order as Started
        daoAdapter.setOrderStarted(work.getOrderId());

        SynergyHelper.synergy(work, userId, machine);

        //Intervention logic  pop all interventions

        work.resetInterventionJobs();
        List<InterventionJob> interventionJobs = work.getInterventionJobs();
        boolean foundUncompletedInterventionTask = false;

        for (InterventionJob interventionJob : interventionJobs) {
            //Check Intervention At Start is Completed or not
            if (interventionJob.getIntervention().getInterventionPosition() == Intervention.POSITION_START &&
                    interventionJob.getIntervention().getTime() == 0) {

                if (interventionJob.getStatus() != InterventionJob.COMPLETED) {
                    if (force || !context.getInterventionManager().hasIntervention(interventionJob.getId())) {
                        if (interventionJob.getStatus() == InterventionJob.QUEUED) {
                            interventionJob.setUserId(userId);
                            updateInterventionStatus(interventionJob, InterventionJob.WAITING);
                            daoAdapter.updateInterventionJob(interventionJob);
                        } else if (interventionJob.getStatus() == InterventionJob.STARTED) {
                            context.getInterventionManager().sendInterventionPush(interventionJob);
                        }
                    }
                    foundUncompletedInterventionTask = true;
                } else {
                    break;
                }
            }
        }

        if (foundUncompletedInterventionTask) {
            context.getInterventionManager().showAssignedInterventions(daoAdapter, userId);
            return true;
        }

        //Start other interventions
        List<InterventionJob> jobs = daoAdapter.getInterventionJobs(work);
        for (InterventionJob job : jobs) {
            context.getInterventionManager().startTimer(daoAdapter, job);
        }


        long now = System.currentTimeMillis();
        work.setLastIntervention(now);
        work.setStartTime(now);
        User user = daoAdapter.getUserById(userId);
        work.setUserId(userId);
        if (Strings.isNotEmpty(userId)) {
            Meal m = work.getMeal();
            if (Strings.isEmpty(m.getChefId())) {
                boolean assignMealToSameChef = daoAdapter.getRestaurantSettings().getAssignMealToSameChef();
                if (assignMealToSameChef) {
                    m.setChefId(userId);
                } else {
                    m.setChefId("none");
                }
                daoAdapter.updateMeal(m);
            }
        }
        work.setUser(user);
        work.setMachine(machine);
        updateWorkStatus(work, Work.STARTED);

        daoAdapter.updateWork(work);

        //Synergy is here

        if (work.getSynergyList() != null) {
            for (Work t : work.getSynergyList()) {
                updateWorkStatus(t, Work.STARTED);
                t.setUserId(userId);
                ActivityLogger.log(daoAdapter, t);
                updateWorkStatus(t, Work.SYNERGY);
            }
        }
        work.setTimeRemaining(TaskUtil.getWorkingForTaskInMinutes(work));

        if (machine != null && work.isMachineTask()) {
            work.setTimeRemaining(TaskUtil.getWorkingForTaskInMillis(work));
            TimerManager.getInstance().start(work);
            //new MachineWorker().startTask(context, machine, work);
        } else if (Strings.isNotEmpty(userId)) {
            taskRegister.register(userId, work);
        }

        //Push to notify
        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
        return true;
    }


//    public boolean revokeTask(String userId) {
//        DaoAdapter daoAdapter = (DaoAdapter) ServiceLocator.getInstance().getService(ServiceLocator.DAO_ADAPTER_SERVICE);
//        Work current = UserManager.getInstance().getActiveTaskByUser(getQueue(), userId);
////        User user = UserManager.getInstance().getUserById(userId);
////        if (user != null && user.getAutoAssign()) {
////            return false;
////        }
//        if (current != null) {
//            updateWorkStatus(daoAdapter, current, Work.QUEUED);
////            if (current.getMachine() != null) {
////                current.getMachine().completeTask(current);
////            }
//        }
//        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
//        return true;
//    }


    public synchronized boolean revoke(String userId) {

        //If user has Auto-Assign , prevent revoke
        User user = context.getUserManager().getUserById(userId);
        if (user != null && user.getAutoAssign()) {
            return false;
        }

        Work w = taskRegister.getTask(userId);
        if (w == null) {
            return false;
        }

        //Why w.getId
        Work work = getQueue().getTask(w.getId());
        if (work == null) {
            return false;
        }
        //Work current = UserManager.getInstance().getActiveTaskByUser(getQueue(), userId);
        updateWorkStatus(w, Work.QUEUED);
        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
        return true;
    }

    public void updateWorkStatus(Work work, int status) {

        //TODO SynergyList call it


        if (work.getStatus() == Work.COMPLETED || work.getStatus() == Work.REMOVED) {
            return;
        }

        if (status != Work.STARTED && !work.isMachineTask()) {
            taskRegister.unregister(work.getId());
        }

        DaoAdapter daoAdapter = getDaoAdapter();
        work.setStatus(status);
        ActivityLogger.log(daoAdapter, work);

        if (status == Work.QUEUED) {
            work.setUserId(null);
        } else if (status == Work.COMPLETED) {
            if (work.getSynergyList() != null) {
                for (Work w : work.getSynergyList()) {
                    w.setStatus(Work.COMPLETED);
                    daoAdapter.updateWork(w);
                    List<InterventionJob> jobs = w.getInterventionJobs();
                    if (jobs != null) {
                        for (InterventionJob interventionJob : jobs) {
                            if (interventionJob.getStatus() != InterventionJob.COMPLETED) {
                                updateInterventionStatus(interventionJob, InterventionJob.COMPLETED);
                                daoAdapter.updateInterventionJob(interventionJob);
                            }
                        }
                    }
                }
            }
            TimerManager.getInstance().remove(work);
            List<InterventionJob> jobs = work.getInterventionJobs();
            if (jobs != null) {
                for (InterventionJob interventionJob : jobs) {
                    if (interventionJob.getStatus() != InterventionJob.COMPLETED) {
                        updateInterventionStatus(interventionJob, InterventionJob.COMPLETED);
                        daoAdapter.updateInterventionJob(interventionJob);
                    }
                }
            }
        } else if (status == Work.REMOVED) {
            TimerManager.getInstance().remove(work);
            List<InterventionJob> jobs = work.getInterventionJobs();
            if (jobs != null) {
                for (InterventionJob interventionJob : jobs) {
                    if (interventionJob.getStatus() != InterventionJob.COMPLETED) {
                        updateInterventionStatus(interventionJob, InterventionJob.COMPLETED);
                        daoAdapter.updateInterventionJob(interventionJob);
                    }
                }
            }
        }
    }

    public void updateInterventionStatus(InterventionJob interventionJob, int status) {
        getDaoAdapter().setOrderStarted(interventionJob.getOrderId());
        if (status == InterventionJob.WAITING) {
            interventionJob.setWaitingFrom(System.currentTimeMillis());
        } else if (status == InterventionJob.COMPLETED) {
            context.getInterventionManager().finishInterventionJob(interventionJob);
        }
        interventionJob.setStatus(status);
    }

    public synchronized void updateSegment(long workId, int additionalInterventionCompleted) {
        DaoAdapter daoAdapter = getDaoAdapter();
        if (!daoAdapter.getRestaurantSettings().getEnabledRecipeEdit()) {
            return;
        }

        Work work = daoAdapter.getWorkById(workId);
        long lastTime = work.getLastIntervention();
        if (lastTime == 0) {
            return;
        }
        long now = System.currentTimeMillis();
        long difference = now - lastTime;
        Segment segment = daoAdapter.getSegment(work.getTaskId(), work.getInterventionsCompleted() + additionalInterventionCompleted);
        if (segment != null) {
            double bound = segment.getDuration() * .1; // 10%
            if (difference > (segment.getDuration() - bound) && difference < (segment.getDuration() + bound)) {
                segment.setTotal(segment.getTotal() + difference);
                segment.setCompleted(segment.getCompleted() + 1);
                segment.setDuration(Math.round(segment.getTotal() / segment.getCompleted()));
                daoAdapter.updateSegment(segment);
            }
        }
    }


    public synchronized boolean reAssignTask(String userId, Long taskId, boolean force) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Queue queue = getQueue();
        Work newTask = queue.getTask(taskId);
        if (newTask == null) {
            return false;
        }
        if (newTask.getStatus() == Work.STARTED) {
            return false;
        }
        if (!newTask.canStart()) {
            return false;
        }
        Work current = getTask(userId);
        Machine m = null;
//        if (current != null && current.getMachine() == null) {
//            if (newTask != null && newTask.getTask() != null && !newTask.isTransportTask() && newTask.getTask().getMachineId() != null) {
//                for (Machine machine : machines) {
//                    //if (machine.gActivityLoggeretCurrentTask() == null) {
//                    if (machine.getId().equals(newTask.getTask().getMachineId())) {
//                        m = machine;
//                    }
//                    //}
//                }
//                if (m == null) {
//                    return false;
//                }
//            }
//        }
        if (newTask.getTask().getMachineId() != null) {
            m = daoAdapter.getMachineById(newTask.getTask().getMachineId());
        }
        boolean isAssigned = assign(newTask, userId, m, force);
        if (isAssigned) {
            if (current != null && current.getStatus() == Work.STARTED) {
                updateWorkStatus(current, Work.QUEUED);
            }
        }
        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
        return isAssigned;
    }

    public boolean canStart(Long taskId) {
        Work work = getQueue().getTask(taskId);
        return work != null && work.canStart();

    }

    public Work getTask(String userId) {
        return taskRegister.getTask(userId);
    }

    private Queue getQueue() {
        return ServiceLocator.getInstance().getQueue();
    }

    private DaoAdapter getDaoAdapter() {
        return ServiceLocator.getInstance().getDatabaseAdapter();
    }
}
