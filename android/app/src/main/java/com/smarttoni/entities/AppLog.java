package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "app_log")
public class AppLog {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "tag")
    private String tag;

    @Property(nameInDb = "log")
    private String log;

    @Property(nameInDb = "date")
    private long date;

    @Generated(hash = 1241587189)
    public AppLog(Long id, String tag, String log, long date) {
        this.id = id;
        this.tag = tag;
        this.log = log;
        this.date = date;
    }

    @Generated(hash = 365541855)
    public AppLog() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLog() {
        return this.log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
