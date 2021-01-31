package com.smarttoni.grenade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * __
 * ____ _________  ____  ____ _____/ /__
 * / __ `/ ___/ _ \/ __ \/ __ `/ __  / _ \
 * / /_/ / /  /  __/ / / / /_/ / /_/ /  __/
 * \__, /_/   \___/_/ /_/\__,_/\__,_/\___/
 * /____/
 * <p>
 * <p>
 * The Event Emitter for SmartTONi
 */
public class EventManager {

    List<EventWrapper> eventListeners = new ArrayList<>();

    public void addEventListner(Event event, EventCallback callback) {

        //Remove call back already exist
        removeEventListner(callback);

        eventListeners.add(new EventWrapper(event, callback));
    }


    public void removeEventListner(EventCallback callback) {
        Iterator iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            EventWrapper wrapper = (EventWrapper) iterator.next();
            if (wrapper.callback == callback) {
                iterator.remove();
            }
        }
    }

    public void emit(Event event) {
        emit(event, null);
    }

    public void emit(Event event, Object data) {
        for (EventWrapper wrapper :
                eventListeners) {
            if (wrapper.event == event) {
                wrapper.callback.onEvent(data);
            }
        }
    }

    private class EventWrapper {
        Event event;
        EventCallback callback;

        public EventWrapper(Event event, EventCallback callback) {
            this.event = event;
            this.callback = callback;
        }
    }

    //Singleton

    private EventManager() {
    }

    private static EventManager INSTANCE = new EventManager();

    public static EventManager getInstance() {
        return INSTANCE;
    }
}
