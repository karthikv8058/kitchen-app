package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SyncWarpper implements Serializable {

    @SerializedName("recipes")
    public RecipeSyncWrapper recipes;

    @SerializedName("tasks")
    public TaskSyncWrapper tasks;

    @SerializedName("taskSegments")
    public TaskSegmentsSyncWrapper taskSegments;

    @SerializedName("taskSteps")
    public TaskStepsSyncWrapper taskSteps;

    @SerializedName("storages")
    List<StorageSynWrapper> storages;

    public List<StorageSynWrapper> getStorages() {
        return storages;
    }

    public void setStorages(List<StorageSynWrapper> storages) {
        this.storages = storages;
    }

    public TaskStepsSyncWrapper getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(TaskStepsSyncWrapper taskSteps) {
        this.taskSteps = taskSteps;
    }

    public RecipeSyncWrapper getRecipes() {
        return recipes;
    }

    public void setRecipes(RecipeSyncWrapper recipes) {
        this.recipes = recipes;
    }

    public TaskSyncWrapper getTasks() {
        return tasks;
    }

    public void setTasks(TaskSyncWrapper tasks) {
        this.tasks = tasks;
    }

    public TaskSegmentsSyncWrapper getTaskSegments() {
        return taskSegments;
    }

    public void setTaskSegments(TaskSegmentsSyncWrapper taskSegments) {
        this.taskSegments = taskSegments;
    }
}
