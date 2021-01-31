package com.smarttoni.assignment;

import android.content.Context;

import com.smarttoni.assignment.delay.DelayHelper;
import com.smarttoni.assignment.machine.TimerManager;
import com.smarttoni.assignment.order.OrderProcessor;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.utils.Strings;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.DaoNotFoundException;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.User;
import com.smarttoni.entities.Work;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventCallback;
import com.smarttoni.grenade.EventManager;
import com.smarttoni.pegion.PushMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssignmentFactory {

    private DaoAdapter daoAdapter;

    private AssignmentFactory() {
    }

    private static AssignmentFactory INSTANCE = new AssignmentFactory();

    public static AssignmentFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Must init before use
     *
     * @param context
     * @param forceInit
     */
    public synchronized void init(final Context context, boolean forceInit) {
        if (getQueue().size() > 0) {
            return;
        }
        daoAdapter = new GreenDaoAdapter(context);
        DelayHelper.getInstance().init(daoAdapter);
        OrderProcessor.getInstance().init(daoAdapter, ServiceLocator.getInstance().getQueue());
        assign();


        EventManager.getInstance().addEventListner(Event.ORDER_RECEIVED, new EventCallback() {
            @Override
            public void onEvent(Object data) {
                PushMessage message = new PushMessage(PushMessage.TYPE_UPDATE_QUEUE);
                SmarttoniContext context = ServiceLocator.getInstance().getSmarttoniContext();
                context.pushAll(message, null);
            }
        });
    }

    /**
     * Must init before use
     */
    public synchronized void processOrder(Context context, Order order) {
        if (order.getProcessed() ||
                order.getStatus() == Order.ORDER_COMPLETED ||
                order.getStatus() == Order.ORDER_DELETED ||
                order.getType() == Order.TYPE_EXTERNAL) {
            return;
        }
        order.setProcessed(true);

        //TODO Quick Fix
        if (daoAdapter == null) {
            daoAdapter = new GreenDaoAdapter(context);
        }
        daoAdapter.updateOrderWithNoModification(order);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderProcessor.getInstance().processOrder(context, getQueue(), order);
                DelayHelper delayHelper = (DelayHelper) ServiceLocator
                        .getInstance()
                        .getService(ServiceLocator.DELAY_CALCULATOR_SERVICE);
                try {
                    delayHelper.start(getQueue());
                } catch (DaoNotFoundException e) {
                    //e.printStackTrace();
                }
                assign();
                EventManager.getInstance().emit(Event.ORDER_RECEIVED);
            }
        }).start();
        //TODO calculateDelay();
        // new AssignTasks(context).doInBackground(null);
        //new ProcessOrderTasks(context).doInBackground(order);
    }


    public void eventLoop(Context context) {
        DelayHelper delayHelper = (DelayHelper) ServiceLocator
                .getInstance()
                .getService(ServiceLocator.DELAY_CALCULATOR_SERVICE);
        try {
            delayHelper.startAsync(getQueue());
        } catch (DaoNotFoundException e) {
            //e.printStackTrace();
        }
    }


    public void timer(Context context) {
        TimerManager.getInstance().loop(context, daoAdapter);
        ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                .getInterventionManager()
                .loop(daoAdapter);
    }

    public synchronized void assign() {
        List<Work> tasks = getQueue().getCloneQueue();
        if (tasks.isEmpty()) {
            return;
        }

        List<String> userIds = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                .getUserManager()!=null?((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                .getUserManager()
                .getIdleUsers():new ArrayList<>();
        for (Work t : tasks) {
            userIds.iterator();
            if (t.getStatus() != Work.QUEUED) {
                continue;
            }
            if (t.getTask() != null && t.isMachineTask()) {
                if (t.getStatus() != Work.QUEUED || t.isTransportTask()) {
                    continue;
                }
                Machine machine = daoAdapter.getMachineById(t.getTask().getMachineId());
                startTask(t, null, machine);
            } else {
                Iterator<String> userIdIterator = userIds.iterator();
                while (userIdIterator.hasNext()) {

                    String userId = userIdIterator.next();
                    boolean isPreferredChef = (Strings.isEmpty(t.getMeal().getChefId()) || t.getMeal().getChefId().equals("none"));
                    isPreferredChef = isPreferredChef || userId.equals(t.getMeal().getChefId());

                    if((!isPreferredChef && Strings.isNotEmpty(t.getMeal().getChefId()))) {
                        User user = daoAdapter.getUserById(t.getMeal().getChefId());
                        if(user != null && (!user.getOnline()) || !daoAdapter.isUserAssignedInStation(user.getId(), t.getTask().getStationId())){
                            isPreferredChef = true;
                        }
                    }

                    isPreferredChef = isPreferredChef && daoAdapter.isUserAssignedInStation(userId, t.getTask().getStationId());
                    if (isPreferredChef) {
                        if (startTask(t, userId, t.getMachine())) {
                            userIdIterator.remove();
                        }
                    }
                }
            }
        }
    }

    private boolean startTask(Work work, String userId, Machine machine) {
        return ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger().assign(work, userId, machine, false);
    }

    private Queue getQueue() {
        return ServiceLocator.getInstance().getQueue();
    }
}
