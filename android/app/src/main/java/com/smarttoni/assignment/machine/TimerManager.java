package com.smarttoni.assignment.machine;

import android.content.Context;

import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerManager {

    private TimerManager() {
    }

    private static TimerManager INSTANCE = new TimerManager();

    public static TimerManager getInstance() {
        return INSTANCE;
    }


    private List<TimerMachine> timerMachines = new ArrayList<>();
    private Map<Long, TimerMachine> map = new HashMap<>();


    public void loop(Context context, DaoAdapter daoAdapter) {
        List<TimerMachine> t = new ArrayList<>(timerMachines);
        for (TimerMachine machine : t) {
            if (machine.tick(context, daoAdapter)) {
                timerMachines.remove(machine);
                map.remove(machine.getWork().getId());
            }
        }
    }

    public void add(TimerMachine timerMachine) {
        timerMachines.add(timerMachine);
        map.put(timerMachine.getWork().getId(), timerMachine);
    }

    public void remove(Work work) {
        for (TimerMachine machine : timerMachines) {
            if (machine.getWork().getId().equals(work.getId())) {
                timerMachines.remove(machine);
                break;
            }
        }
        map.remove(work.getId());
    }

    public void removeOrder(String orderId) {
        for (TimerMachine machine : timerMachines) {
            if (machine.getWork().getOrderId()!= null && machine.getWork().getOrderId().equals(orderId)) {
                timerMachines.remove(machine);
                map.remove(machine.getWork().getId());
                break;
            }
        }
    }

    public void start(Work work) {
        if (work != null) {
            TimerMachine machine = map.get(work.getId());
            if (machine == null) {
                if (work.isMachineTask()) {
                    long workingTime = work.getTimeRemaining();
                    machine = new TimerMachine(work, workingTime);
                    add(machine);
                } else {
                    return;
                }
            }
            machine.start();
        }

    }

    public void restart(Work work) {
        if (work == null) {
            return;
        }
        TimerMachine machine = map.get(work.getId());
        if (machine != null) {
            machine.start();
        }
    }

    public void setReaming(DaoAdapter daoAdapter, Work work, long duration) {
        if (work == null) {
            return;
        }
        TimerMachine machine = map.get(work.getId());
        if (machine != null) {
            machine.setReaming(daoAdapter, duration);
        }
    }

    public void forceStop(Context context, Work work) {
        TimerMachine machine = map.get(work.getId());
        if (machine != null) {
            machine.stop(context);
            timerMachines.remove(machine);
            map.remove(machine.getWork().getId());
        }
    }

    public long getTimeRemainingForMachine(Work work) {
        TimerMachine machine = map.get(work.getId());
        if (machine == null) {
            return 0;
        }
        return machine.getRemaining();
    }

    public void pause(Work work) {
        if (work != null) {
            TimerMachine machine = map.get(work.getId());
            if (machine == null) {
                return;
            }
            machine.pause();
        }
    }
}
