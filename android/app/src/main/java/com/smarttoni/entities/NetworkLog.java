package com.smarttoni.entities;


import io.realm.RealmObject;

public class NetworkLog extends RealmObject {

    private String name;
    private long time;

    public NetworkLog() {

    }

    public NetworkLog(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
