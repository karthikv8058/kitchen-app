package com.smarttoni.server.wrappers;

import java.util.List;

public class TaskStep {
    private String name;
    private String description;
    private String image;
    private String video;
    private String uuid;
    private List<TaskWithQuantity> tasks;
    private List<IngredientWithQuantity> ingredients;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<IngredientWithQuantity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientWithQuantity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<TaskWithQuantity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskWithQuantity> tasks) {
        this.tasks = tasks;
    }
}
