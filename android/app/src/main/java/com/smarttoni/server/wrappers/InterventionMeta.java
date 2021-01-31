package com.smarttoni.server.wrappers;

public class InterventionMeta {

    private int position;
    private long startedAt;
    private long time;
    private boolean enabledRecipeEdit;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isEnabledRecipeEdit() {
        return enabledRecipeEdit;
    }

    public void setEnabledRecipeEdit(boolean enabledRecipeEdit) {
        this.enabledRecipeEdit = enabledRecipeEdit;
    }
}
