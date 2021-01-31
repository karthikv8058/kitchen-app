package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.entities.TaskStep;

import java.util.List;

public class TaskStepsSyncWrapper {
    @SerializedName("created")
    List<TaskStep> created;
    @SerializedName("deleted")
    List<TaskStep> deleted;
    @SerializedName("updated")
    List<TaskStep> updated;

    public List<TaskStep> getCreated() {
        return created;
    }

    public void setCreated(List<TaskStep> created) {
        this.created = created;
    }

    public List<TaskStep> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<TaskStep> deleted) {
        this.deleted = deleted;
    }

    public List<TaskStep> getUpdated() {
        return updated;
    }

    public void setUpdated(List<TaskStep> updated) {
        this.updated = updated;
    }
}
