package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "station_task")

public class StationTask {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "station_id")
    @SerializedName("station_id")
    private Long stationId;

    @Property(nameInDb = "task_id")
    @SerializedName("task_id")
    private Long taskId;

    @Generated(hash = 287005479)
    public StationTask(Long id, Long stationId, Long taskId) {
        this.id = id;
        this.stationId = stationId;
        this.taskId = taskId;
    }

    @Generated(hash = 1162221744)
    public StationTask() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return this.stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


}
