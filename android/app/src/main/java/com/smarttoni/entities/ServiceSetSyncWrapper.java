package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceSetSyncWrapper {
    @SerializedName("created")
    List<ServiceSet> created;
    @SerializedName("deleted")
    List<ServiceSet> deleted;
    @SerializedName("updated")
    List<ServiceSet> updated;

    public List<ServiceSet> getCreated() {
        return created;
    }

    public void setCreated(List<ServiceSet> created) {
        this.created = created;
    }

    public List<ServiceSet> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<ServiceSet> deleted) {
        this.deleted = deleted;
    }

    public List<ServiceSet> getUpdated() {
        return updated;
    }

    public void setUpdated(List<ServiceSet> updated) {
        this.updated = updated;
    }
}
