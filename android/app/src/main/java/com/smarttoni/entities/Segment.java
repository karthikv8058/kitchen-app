package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;


@Entity
public class Segment {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @SerializedName("averageWorkDuration")
    private long duration;

    @SerializedName("position")
    private int position;

    @SerializedName("totalSegmentDuration")
    private long total;

    @SerializedName("totalSegmentsCompleted")
    private int completed;

    @SerializedName("task")
    private String taskId;

    @Generated(hash = 94717829)
    public Segment(@NotNull String id, long duration, int position, long total,
                   int completed, String taskId) {
        this.id = id;
        this.duration = duration;
        this.position = position;
        this.total = total;
        this.completed = completed;
        this.taskId = taskId;
    }

    @Generated(hash = 191266783)
    public Segment() {
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCompleted() {
        return this.completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
