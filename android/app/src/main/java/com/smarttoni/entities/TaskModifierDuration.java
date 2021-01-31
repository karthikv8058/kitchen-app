package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

@Entity(nameInDb = "task_modifier_durations")
public class TaskModifierDuration {

    @Property(nameInDb = "created_at")
    @SerializedName("created_at")
    private Date createdAt;

    @Id()
    @SerializedName("id")
    private Long id;

    @Property(nameInDb = "modifier_id")
    @SerializedName("modifier_id")
    private long modifierId;

    @Property(nameInDb = "task_id")
    @SerializedName("task_id")
    private Long taskId;

    @Property(nameInDb = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    @Property(nameInDb = "working_time")
    @SerializedName("working_time")
    private Long workingTime;

    @Generated(hash = 1883251281)
    public TaskModifierDuration(Date createdAt, Long id, long modifierId,
                                Long taskId, Date updatedAt, Long workingTime) {
        this.createdAt = createdAt;
        this.id = id;
        this.modifierId = modifierId;
        this.taskId = taskId;
        this.updatedAt = updatedAt;
        this.workingTime = workingTime;
    }

    @Generated(hash = 69954279)
    public TaskModifierDuration() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getModifierId() {
        return modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Long workingTime) {
        this.workingTime = workingTime;
    }

    public Date getMCreatedAt() {
        return this.createdAt;
    }

    public void setMCreatedAt(Date mCreatedAt) {
        this.createdAt = mCreatedAt;
    }

    public Long getMId() {
        return this.id;
    }

    public void setMId(Long mId) {
        this.id = mId;
    }

    public long getMModifierId() {
        return this.modifierId;
    }

    public void setMModifierId(long mModifierId) {
        this.modifierId = mModifierId;
    }

    public Long getMTaskId() {
        return this.taskId;
    }

    public void setMTaskId(Long mTaskId) {
        this.taskId = mTaskId;
    }

    public Date getMUpdatedAt() {
        return this.updatedAt;
    }

    public void setMUpdatedAt(Date mUpdatedAt) {
        this.updatedAt = mUpdatedAt;
    }

    public Long getMWorkingTime() {
        return this.workingTime;
    }

    public void setMWorkingTime(Long mWorkingTime) {
        this.workingTime = mWorkingTime;
    }

}
