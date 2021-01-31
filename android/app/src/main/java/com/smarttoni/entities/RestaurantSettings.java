package com.smarttoni.entities;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "restaurant_settings")
public class RestaurantSettings {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("recipe_edit_mode")
    @Property(nameInDb = "recipe_edit_mode")
    private boolean enabledRecipeEdit;


    @SerializedName("meals_to_same_chef")
    @Property(nameInDb = "meals_to_same_chef")
    private boolean assignMealToSameChef;

    @Generated(hash = 1077827145)
    public RestaurantSettings(Long id, boolean enabledRecipeEdit,
            boolean assignMealToSameChef) {
        this.id = id;
        this.enabledRecipeEdit = enabledRecipeEdit;
        this.assignMealToSameChef = assignMealToSameChef;
    }


    @Generated(hash = 1603689247)
    public RestaurantSettings() {
    }
    
    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public boolean getEnabledRecipeEdit() {
        return this.enabledRecipeEdit;
    }


    public void setEnabledRecipeEdit(boolean enabledRecipeEdit) {
        this.enabledRecipeEdit = enabledRecipeEdit;
    }


    public boolean getAssignMealToSameChef() {
        return this.assignMealToSameChef;
    }


    public void setAssignMealToSameChef(boolean assignMealToSameChef) {
        this.assignMealToSameChef = assignMealToSameChef;
    }

}
