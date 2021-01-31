package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.models.Locales;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
import java.util.List;

@Entity(nameInDb = "machine")
public class Machine {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "capacity")
    @SerializedName("capacity")
    private long capacity;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Property(nameInDb = "createdAt")
    @SerializedName("created_at")
    private Date createdAt;

    @Property(nameInDb = "room")
    @SerializedName("room_uuid")
    private String room;

    @Generated(hash = 1796908865)
    public Machine() {
    }

    @Generated(hash = 1406182603)
    public Machine(@NotNull String id, String name, long capacity, Date createdAt,
            String room) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.room = room;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}
