package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "service_set_timing")
public class ServiceSetTimings {
    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @SerializedName("start_time")
    @Property(nameInDb = "start_time")
    private String startTime;

    @SerializedName("end_time")
    @Property(nameInDb = "end_time")
    private String endTime;

    @Transient
    @SerializedName("week_days")
    private List<String> weekDay;

    @Property(nameInDb = "week_days")
    private String weekDays;

    @Property(nameInDb = "serviceSetId")
    private String serviceSetId;

    @Generated(hash = 1285597788)
    public ServiceSetTimings(@NotNull String id, String startTime, String endTime,
            String weekDays, String serviceSetId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDays = weekDays;
        this.serviceSetId = serviceSetId;
    }

    @Generated(hash = 562838839)
    public ServiceSetTimings() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeekDays() {
        return this.weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getServiceSetId() {
        return this.serviceSetId;
    }

    public void setServiceSetId(String serviceSetId) {
        this.serviceSetId = serviceSetId;
    }

    public List<String> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(List<String> weekDay) {
        this.weekDay = weekDay;
    }
}
