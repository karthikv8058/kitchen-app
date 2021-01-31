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

import java.util.List;

@Entity(nameInDb = "Place")

public class Place {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "rack_id")
    private String rackId;

    @Property(nameInDb = "name")
    private String name;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Generated(hash = 272336180)
    public Place(@NotNull String id, String rackId, String name) {
        this.id = id;
        this.rackId = rackId;
        this.name = name;
    }

    @Generated(hash = 1170019414)
    public Place() {
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRackId() {
        return this.rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
