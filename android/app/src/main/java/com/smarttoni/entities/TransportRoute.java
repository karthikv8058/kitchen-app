package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "TransportRoute")
public class TransportRoute {

    private String key;
    private int type;
    private String name;
    private String routeId;

    @Generated(hash = 651805029)
    public TransportRoute() {
    }

    @Generated(hash = 145326348)
    public TransportRoute(String key, int type, String name, String routeId) {
        this.key = key;
        this.type = type;
        this.name = name;
        this.routeId = routeId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}
