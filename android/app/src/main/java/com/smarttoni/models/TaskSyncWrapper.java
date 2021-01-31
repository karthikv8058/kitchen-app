package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.entities.Task;

import java.util.List;

public class TaskSyncWrapper {
    @SerializedName("created")
    List<Task> created;
    @SerializedName("deleted")
    List<Task> deleted;
    @SerializedName("updated")
    List<Task> updated;

    public List<Task> getCreated() {
        return created;
    }

    public void setCreated(List<Task> created) {
        this.created = created;
    }

    public List<Task> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<Task> deleted) {
        this.deleted = deleted;
    }

    public List<Task> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Task> updated) {
        this.updated = updated;
    }
}
