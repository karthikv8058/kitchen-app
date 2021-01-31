package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.models.Locales;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

@Entity(nameInDb = "units")
public class Units {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "symbol")
    private String symbol;

    @Property(nameInDb = "recipeUuid")
    @SerializedName("recipe_uuid")
    private String recipeUuid;

//    @SerializedName("uuid")
//    @Property(nameInDb = "uuid")
//    private String uuId;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;


    @Generated(hash = 384173611)
    public Units() {
    }

    @Generated(hash = 1931734867)
    public Units(@NotNull String id, String name, String symbol,
            String recipeUuid) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.recipeUuid = recipeUuid;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeUuid() {
        return this.recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }



}
