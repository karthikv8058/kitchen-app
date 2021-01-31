package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.models.Locales;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Supplier  extends RealmObject {

    @SerializedName("uuid")
    @PrimaryKey
    private String id;

    private String name;

    private String email;

    @Ignore
    private List<Locales> locales;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }
}
