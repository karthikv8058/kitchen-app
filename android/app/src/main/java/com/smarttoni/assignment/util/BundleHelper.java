package com.smarttoni.assignment.util;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BundleHelper {

    private boolean checkSameStation(Work work, String stationId) {
        return stationId != null && (stationId.equals(work.getTask().getStationId()));
    }


    public void createDeliverable(Work work) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Map<Long, Work> foundedWorks = new HashMap<>();

        boolean isNotAvailable = !(work.getStatus() == Work.QUEUED || work.getStatus() == Work.SCHEDULED);
        if (isNotAvailable || !work.canStart()) {
            return;
        }
        int transportType = work.getTransportType();
        if ((transportType & Work.TRANSPORT_DELIVERABLES) > 0) {
            List<Work> works = work.getDeliveryList();
            if (works != null) {
                for (Work w :
                        works) {
                    updateWorkStatus(w, Work.QUEUED);
                }
            }
            work.setDeliveryList(null);
        }
        String stationId = work.getTask().getStationId();
        foundedWorks.put(work.getId(), work);
        Set<Work> list = new HashSet<>();
        list.add(work);
        if (stationId != null && !stationId.equals("")) {
            Set<Work> downList = lookDownward(foundedWorks, work, stationId);
            if (downList == null) {
                return;
            }
            list.addAll(downList);
            Set<Work> upList = lookUpward(foundedWorks, work, stationId);
            if (upList != null) {
                list.addAll(upList);
            }
        }


        Work lastTask = findLastTask(work, list);
        String output = (lastTask.getQuantity() * lastTask.getTask().getOutputQuantity()) + lastTask.getTask().getUnit().getName();
        work.setOutput(output);

        List<Work> works = new ArrayList<>();
        works.addAll(list);


//        Comparator comparator = (Comparator<Work>) (lhs, rhs) -> Long.compare(lhs.getTaskId(), rhs.getTaskId());
//        Collections.sort(works, comparator);

        //work.setTransportType(transportType + Work.TRANSPORT_DELIVERABLES);


        //String name = "";
        if (works.size() > 1) {
            for (Work w : works) {
                if (!w.getId().equals(work.getId())) {
                    updateWorkStatus(w, Work.BUNDLED);
                    work.addDeliveryList(w);
                }
                //if (w != work) {
                //name += (w.isTransportTask() ? w.getName() : w.getTask().getName()) + "\n";
                //}
            }
            //name += (work.isTransportTask() ? work.getName() : work.getTask().getName()) + "\n";
            work.setTransportType(transportType + Work.TRANSPORT_DELIVERABLES);
            updateWorkStatus(work, Work.STARTED);
            String title = "[*] " + (lastTask.isTransportTask() ? lastTask.getName() : lastTask.getTask().getName());
            work.setTitle(title);
            daoAdapter.updateWork(work);
            //work.setTitle(name);
        }

    }

    private Work findLastTask(Work work, Set<Work> list) {
        List<Work> works = work.getNextTasks();
        if (works != null && list != null && works.size() > 0) {
            Work t = works.get(0);
            if (list.contains(t)) {
                return findLastTask(works.get(0), list);
            } else {
                return work;
            }
        } else {
            return work;
        }
    }

    private Set<Work> lookUpward(Map<Long, Work> foundedWorks, Work work, String stationId) {
        Set<Work> list = new HashSet<>();
        if (work == null) {
            return list;
        }
        List<Work> nextTasks = work.getNextTasks();
        if (nextTasks != null) {
            for (Work w : nextTasks) {
                if (w.getStatus() == Work.QUEUED || w.getStatus() == Work.SCHEDULED) {
                    if (!checkSameStation(w, stationId)) {
                        return null;//continue;
                    }
                    if (!foundedWorks.containsKey(w.getId())) {
                        foundedWorks.put(w.getId(), w);
                        Set<Work> downList = lookDownward(foundedWorks, w, stationId);
                        if (downList == null) {
                            return null;
                        }
                        list.addAll(downList);
                        Set<Work> upList = lookUpward(foundedWorks, w, stationId);
                        if (upList != null) {
                            list.addAll(upList);
                        }
                    }
                    list.add(w);
                }
            }
        }
        return list;
    }

    private Set<Work> lookDownward(Map<Long, Work> foundedWorks, Work work, String stationId) {
        Set<Work> list = new HashSet<>();
        if (work == null) {
            return list;
        }
        List<Work> prevTasks = work.getPrevTasks();
        if (prevTasks != null) {
            for (Work w : prevTasks) {
                if (w.getStatus() == Work.BUNDLED) {
                    return null;
                } else if (w.getStatus() == Work.QUEUED || w.getStatus() == Work.SCHEDULED) {
                    if (!checkSameStation(w, stationId)) {
                        return null;
                    }
                    if (!foundedWorks.containsKey(w.getId())) {
                        list.add(w);
                        foundedWorks.put(w.getId(), w);
                        Set<Work> downList = lookDownward(foundedWorks, w, stationId);
                        if (downList == null) {
                            return null;
                        }
                        list.addAll(downList);
                        Set<Work> upList = lookUpward(foundedWorks, w, stationId);
                        if (upList != null) {
                            list.addAll(upList);
                        }
                    }
                    list.add(w);
                } else if (w.getStatus() != Work.COMPLETED) {
                    if (!checkSameStation(w, stationId)) {
                        return null;
                    }
                }
            }
        }
        return list;
    }

    public void updateWorkStatus(Work work, int status) {
        TaskManger workHelper = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getTaskManger();
        workHelper.updateWorkStatus(work, status);
    }
}
