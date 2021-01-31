package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "service_set_recipes")
public class ServiceSetRecipes {
    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @SerializedName("recipe_uuid")
    @Property(nameInDb = "recipe_uuid")
    private String recipeId;

    @SerializedName("currency")
    @Property(nameInDb = "currency")
    private String currency;

    @SerializedName("price")
    @Property(nameInDb = "price")
    private Float price;

    @Property(nameInDb = "serviceSetId")
    private String serviceSetId;

    @Generated(hash = 385405413)
    public ServiceSetRecipes(@NotNull String id, String recipeId, String currency,
            Float price, String serviceSetId) {
        this.id = id;
        this.recipeId = recipeId;
        this.currency = currency;
        this.price = price;
        this.serviceSetId = serviceSetId;
    }

    @Generated(hash = 1190736709)
    public ServiceSetRecipes() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getServiceSetId() {
        return this.serviceSetId;
    }

    public void setServiceSetId(String serviceSetId) {
        this.serviceSetId = serviceSetId;
    }


}
