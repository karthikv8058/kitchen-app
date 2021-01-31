package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.entities.Segment;

import java.util.List;

public class TaskSegmentsSyncWrapper {
    @SerializedName("created")
    List<Segment> created;
    @SerializedName("deleted")
    List<Segment> deleted;
    @SerializedName("updated")
    List<Segment> updated;

    public List<Segment> getCreated() {
        return created;
    }

    public void setCreated(List<Segment> created) {
        this.created = created;
    }

    public List<Segment> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<Segment> deleted) {
        this.deleted = deleted;
    }

    public List<Segment> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Segment> updated) {
        this.updated = updated;
    }
}
