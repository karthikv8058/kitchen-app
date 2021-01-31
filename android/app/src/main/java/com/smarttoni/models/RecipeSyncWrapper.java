package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.entities.Recipe;

import java.io.Serializable;
import java.util.List;

public class RecipeSyncWrapper implements Serializable {
    @SerializedName("created")
    List<Recipe> created;
    @SerializedName("deleted")
    List<Recipe> deleted;
    @SerializedName("updated")
    List<Recipe> updated;

    public List<Recipe> getCreated() {
        return created;
    }

    public void setCreated(List<Recipe> created) {
        this.created = created;
    }

    public List<Recipe> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<Recipe> deleted) {
        this.deleted = deleted;
    }

    public List<Recipe> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Recipe> updated) {
        this.updated = updated;
    }
}
