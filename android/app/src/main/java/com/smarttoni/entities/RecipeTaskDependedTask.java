package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by sujith.ps on 7/30/2018.
 */
@Entity(nameInDb = "recipietaskdependedntask")
public class RecipeTaskDependedTask {


    @Id(autoincrement = true)
    private Long Id;

    @Property(nameInDb = "recipe_task_id")
    private Long recipeTaskId;


    @Property(nameInDb = "dependent_task_id")
    private Long dependentTaskId;


    @Generated(hash = 1778775051)
    public RecipeTaskDependedTask(Long Id, Long recipeTaskId,
                                  Long dependentTaskId) {
        this.Id = Id;
        this.recipeTaskId = recipeTaskId;
        this.dependentTaskId = dependentTaskId;
    }


    @Generated(hash = 1990525377)
    public RecipeTaskDependedTask() {
    }


    public Long getId() {
        return this.Id;
    }


    public void setId(Long Id) {
        this.Id = Id;
    }


    public Long getRecipeTaskId() {
        return this.recipeTaskId;
    }


    public void setRecipeTaskId(Long recipeTaskId) {
        this.recipeTaskId = recipeTaskId;
    }


    public Long getDependentTaskId() {
        return this.dependentTaskId;
    }


    public void setDependentTaskId(Long dependentTaskId) {
        this.dependentTaskId = dependentTaskId;
    }


}
