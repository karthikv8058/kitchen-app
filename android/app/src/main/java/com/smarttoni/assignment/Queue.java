package com.smarttoni.assignment;

import com.smarttoni.assignment.chef.UserManager;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Queue {

    private List<Work> mQueue = new Vector<>();
    private Map<Long, Work> map = new HashMap<>();

    public List<Work> getCloneQueue() {
        return new ArrayList<>(mQueue);
    }

    public Work getTask(Long id) {
        return map.get(id);
    }

    public boolean add(Work work) {
        map.put(work.getId(), work);
        return mQueue.add(work);
    }

    public boolean remove(Work work) {
        map.remove(work.getId());
        return mQueue.remove(work);
    }

    public int size() {
        return mQueue.size();
    }

    public boolean addAll(List<Work> list) {
        for (Work t : list) {
            map.put(t.getId(), t);
        }
        return mQueue.addAll(list);
    }

    public void sort() {
        Comparator comparator = (Comparator<Work>) (lhs, rhs) -> Long.compare(rhs.getPriority(), lhs.getPriority());
        Collections.sort(mQueue, comparator);
    }

    public void clear() {
        map.clear();
        mQueue.clear();
    }

    public List<Work> getNonStartedMachineQueue() {
        List<Work> works = getCloneQueue();
        List<Work> list = new ArrayList<>();
        for (Work work : works) {
            if (work.isMachineTask() && work.getStatus() == Work.QUEUED) {
                list.add(work);
            }
        }
        return list;
    }

    public List<Work> getMachineQueue() {
        List<Work> works = getCloneQueue();
        List<Work> list = new ArrayList<>();
        for (Work work : works) {
            if (work.isMachineTask() && work.getStatus() == Work.STARTED) {
                list.add(work);
            }
        }
        return list;
    }

//    public List<Work> queryTaskQueue(DaoAdapter daoAdapter, String userId, boolean checkUser, int filter, int limit) {
//
//        List<Work> list = new ArrayList<>();
//        List<Work> queue = new ArrayList<>(mQueue);
//        Iterator<Work> iterator = queue.iterator();
//        while (iterator.hasNext()) {
//            Work t = iterator.next();
//            //check user
//            if (userId != null) {
//                if (checkUser) {
//                    if (!UserManager.getInstance().isUserAssignedInStation(daoAdapter, t, userId)) {
//                        continue;
//                    }
//                } else {
//                    if (t.getUser() == null || !t.getUser().getId().equals(userId)) {
//                        continue;
//                    }
//                }
//            }
//            //check filter
//            if (!((t.getStatus() & filter) > 0)) {
//                continue;
//            }
//            t.canStart();
//            list.add(t);
//            if (list.size() > 0 && list.size() == limit) {
//                break;
//            }
//        }
//        return list;
//    }

    public void clear(String orderUUID) {
        List<Work> works = getCloneQueue();
        for (Work work : works) {
            if (work.getOrder() != null && work.getOrder().getId() != null && work.getOrder().getId().equals(orderUUID)) {
                remove(work);
            }
        }
    }

    public interface QueueChangeListener {
        void onQueueChange();
    }
}
