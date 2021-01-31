package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "interventions_step_ingredient")
public class InterventionStepIngredient {
    @SerializedName("recipe_uuid")
    @Property(nameInDb = "id")
    private String id;

    @SerializedName("quantity")
    @Property(nameInDb = "quantity")
    private float quantity;

    @Property(nameInDb = "stepId")
    private String stepId;

    @Generated(hash = 1932871084)
    public InterventionStepIngredient(String id, float quantity, String stepId) {
        this.id = id;
        this.quantity = quantity;
        this.stepId = stepId;
    }

    @Generated(hash = 1297008033)
    public InterventionStepIngredient() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getStepId() {
        return this.stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

}
