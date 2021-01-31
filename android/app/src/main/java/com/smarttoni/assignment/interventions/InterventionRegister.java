package com.smarttoni.assignment.interventions;

import java.util.HashMap;
import java.util.Map;

public class InterventionRegister {

    Map<String, Long> register = new HashMap<>();
    Map<Long, String> reverseRegister = new HashMap<>();

    public void register(String userId, Long interventionId) {
        register.put(userId, interventionId);
        reverseRegister.put(interventionId, userId);
    }

    public void unregister(String userId) {
        reverseRegister.remove(register.get(userId));
        register.put(userId, 0L);
    }

    public boolean hasIntervention(String userId) {
        return register.get(userId) != null && register.get(userId) > 0;
    }

    public String getUser(Long id) {
        return reverseRegister.get(id);
    }
}
