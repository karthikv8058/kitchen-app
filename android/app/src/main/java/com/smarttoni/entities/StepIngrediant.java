package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;


@Entity(nameInDb = "stepingrediants")
public class StepIngrediant {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("recipe_uuid")
    @Property(nameInDb = "id")
    private String recipeUuid;

//    @Property(nameInDb = "recipe_uuid")
//    private String recipeUuid;

    @Property(nameInDb = "stepId")
    private String stepId;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "description")
    private String description;

    @Property(nameInDb = "amount")
    private int amount;

    @Property(nameInDb = "quantity")
    private int quantity;

    @Property(nameInDb = "createdat")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    @Property(nameInDb = "image_url")
    @SerializedName("image_url")
    private String imageUrl;

    @Generated(hash = 753271715)
    public StepIngrediant() {
    }




    @Generated(hash = 1414503203)
    public StepIngrediant(Long id, String recipeUuid, String stepId, String name,
            String description, int amount, int quantity, long createdAt,
            long updatedAt, String imageUrl) {
        this.id = id;
        this.recipeUuid = recipeUuid;
        this.stepId = stepId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrl = imageUrl;
    }




    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String getRecipeUuid() {
//        return this.recipeUuid;
//    }
//
//    public void setRecipeUuid(String recipeUuid) {
//        this.recipeUuid = recipeUuid;
//    }

    public String getStepId() {
        return this.stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getRecipeUuid() {
        return this.recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


}
