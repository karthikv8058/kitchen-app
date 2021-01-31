package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "task_ingredient")
public class TaskIngredient {
    @Id(autoincrement = true)
    private Long id;

    @SerializedName("recipe_uuid")
    @Property(nameInDb = "recipe_id")
    private String recipeId;

    @SerializedName("quantity")
    @Property(nameInDb = "quantity")
    private float quantity;

    @Property(nameInDb = "taskId")
    private String taskId;


    @Generated(hash = 666235188)
    public TaskIngredient(Long id, String recipeId, float quantity, String taskId) {
        this.id = id;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.taskId = taskId;
    }

    @Generated(hash = 888979427)
    public TaskIngredient() {
    }


    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
