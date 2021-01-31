package com.smarttoni.entities;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;


@Entity(nameInDb = "modifiers")
public class Modifier {

//    @ToOne(joinProperty = "recipeId")
//    private com.smarttoni.entities.SelectedRecipe recipe;

    @Property(nameInDb = "quantity")
    private String quantity;


    @Property(nameInDb = "coursename")
    private String coursename;

    @Property(nameInDb = "mealsname")
    private String mealsname;

    @Property(nameInDb = "imageUrl")
    private String imageUrl;

    @Property(nameInDb = "recipeName")
    private String recipeName;
    @Property(nameInDb = "courseid")
    private Long courseid;

    @Property(nameInDb = "mealsid")
    private Long mealsId;

    //New
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "recipeId")
    @SerializedName("recipe_id")
    private String recipeId;

    @Property(nameInDb = "modifier")
    private String modifier;

    @Property(nameInDb = "printerName")
    @SerializedName("printerName")
    private String printerName;

    //internal
    @Transient
    private boolean isSelected;


    @Generated(hash = 1937670106)
    public Modifier(String quantity, String coursename, String mealsname,
                    String imageUrl, String recipeName, Long courseid, Long mealsId,
                    Long id, String recipeId, String modifier, String printerName) {
        this.quantity = quantity;
        this.coursename = coursename;
        this.mealsname = mealsname;
        this.imageUrl = imageUrl;
        this.recipeName = recipeName;
        this.courseid = courseid;
        this.mealsId = mealsId;
        this.id = id;
        this.recipeId = recipeId;
        this.modifier = modifier;
        this.printerName = printerName;
    }

    @Generated(hash = 385685553)
    public Modifier() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getCoursename() {
        return this.coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getMealsname() {
        return this.mealsname;
    }

    public void setMealsname(String mealsname) {
        this.mealsname = mealsname;
    }

    public Long getCourseid() {
        return this.courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public Long getMealsId() {
        return this.mealsId;
    }

    public void setMealsId(Long mealsId) {
        this.mealsId = mealsId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getPrinterName() {
        return this.printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
