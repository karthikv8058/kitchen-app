package com.smarttoni.assignment.task;

import com.smarttoni.entities.Work;

import java.util.HashMap;
import java.util.Map;

public class TaskRegister {

    private Map<String, Work> register = new HashMap<>();
    private Map<Long, String> reverseRegister = new HashMap<>();

    public void register(String userId, Work work) {
        register.put(userId, work);
        reverseRegister.put(work.getId(), userId);
    }

    public Work getTask(String userId) {
        return register.get(userId);
    }

    public void unregister(Long workId) {
        if (workId == null) {
            return;
        }
        String userId = getUser(workId);
        if(userId == null){
            return;
        }
        Work w = register.get(userId);
        if (w != null && workId.equals(w.getId())) {
            reverseRegister.remove(workId);
            register.put(userId, null);
        }
    }

//    public boolean hasIntervention(String userId) {
//        return register.get(userId) != null && register.get(userId) > 0;
//    }

    public String getUser(Long workId) {
        return reverseRegister.get(workId);
    }

}
