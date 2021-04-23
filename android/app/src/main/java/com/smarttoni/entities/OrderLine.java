package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.assignment.service.ServiceLocator;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "order_line")
public class OrderLine {

    @Id
    private String id;

    @ToOne(joinProperty = "mealId")
    private transient Meal meal;

    @Property(nameInDb = "mealId")
    private String mealId;

    @Transient
    private Recipe recipe;

    @Property(nameInDb = "recipeId")
    private String recipeId;

    @Property(nameInDb = "modifiers")
    private String modifiers;

    @Property(nameInDb = "orderId")
    private String orderId;

    @Property(nameInDb = "courseId")
    private String courseId;

    @Property(nameInDb = "qty")
    private float qty;

    @Generated(hash = 628505749)
    public OrderLine(String id, String mealId, String recipeId, String modifiers, String orderId,
            String courseId, float qty) {
        this.id = id;
        this.mealId = mealId;
        this.recipeId = recipeId;
        this.modifiers = modifiers;
        this.orderId = orderId;
        this.courseId = courseId;
        this.qty = qty;
    }

    @Generated(hash = 1925626461)
    public OrderLine() {
    }

    public String getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }


    public float getQty() {
        return this.qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public Recipe getRecipe() {
        if (recipe == null) {
            recipe = ServiceLocator.getInstance().getDatabaseAdapter().getRecipeById(recipeId);
        }
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.recipeId = recipe.getId();
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getMealId() {
        return this.mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
