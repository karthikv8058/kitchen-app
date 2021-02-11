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

@Entity(nameInDb = "label")
public class Label {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "color")
    private String color;

    @Property(nameInDb = "restaurantId")
    private String restaurantId;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Property(nameInDb = "printerUuid")
    @SerializedName("printer_uuid")
    private String printerUuid;

    @Property(nameInDb = "name")
    private String name;

    @Transient
    @SerializedName("parent_labels")
    private List<Locales> parentLabels;

    public List<Locales> getParentLabels() {
        return parentLabels;
    }

    @Property(nameInDb = "parentLabels")
    private String parentLabel;

    @Property(nameInDb = "childLabels")
    private String childLabels;

    public void setParentLabels(List<Locales> parentLabels) {
        this.parentLabels = parentLabels;
    }

    @Property(nameInDb = "description")
    private String description;

    @Generated(hash = 2137109701)
    public Label() {
    }

    @Generated(hash = 1043016626)
    public Label(@NotNull String id, String color, String restaurantId,
            String printerUuid, String name, String parentLabel, String childLabels,
            String description) {
        this.id = id;
        this.color = color;
        this.restaurantId = restaurantId;
        this.printerUuid = printerUuid;
        this.name = name;
        this.parentLabel = parentLabel;
        this.childLabels = childLabels;
        this.description = description;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getParentLabel() {
        return this.parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
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

    public String getChildLabels() {
        return childLabels;
    }

    public void setChildLabels(String childLabels) {
        this.childLabels = childLabels;
    }

    public String getPrinterUuid() {
        return this.printerUuid;
    }

    public void setPrinterUuid(String printerUuid) {
        this.printerUuid = printerUuid;
    }
}
