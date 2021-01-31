package com.smarttoni.models;

import java.util.List;

public class SyncData<T> {

    private List<T> created;
    private List<T> updated;
    private List<T> deleted;

    public List<T> getCreated() {
        return created;
    }

    public void setCreated(List<T> created) {
        this.created = created;
    }

    public List<T> getUpdated() {
        return updated;
    }

    public void setUpdated(List<T> updated) {
        this.updated = updated;
    }

    public List<T> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<T> deleted) {
        this.deleted = deleted;
    }
}
