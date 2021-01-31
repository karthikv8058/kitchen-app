package com.smarttoni.entities;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


@Entity(nameInDb = "SelectedRecipe")

public class SelectedRecipe {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "quantity")
    private String quantity;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "modifierData")
    private String modifierData;

    @Property(nameInDb = "recipeId")
    private String recipeId;

    @Property(nameInDb = "imageUri")
    private String imageUri;

    @Property(nameInDb = "courseId")
    private Long courseId;

    @Property(nameInDb = "modifierId")
    private Long modifierId;

    @Property(nameInDb = "orderId")
    private Long orderId;

    @Property(nameInDb = "courseName")
    private String courseName;

    @Property(nameInDb = "mealsId")
    private Long mealsId;

    @Property(nameInDb = "mealsName")

    private String mealsName;

    @ToMany(referencedJoinProperty = "recipeId")
    private List<Modifier> modifiers;

    @ToMany(referencedJoinProperty = "selectedRecipeId")
    private List<SelectedRecipeModifier> selectedRecipeModifiers;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1941598037)
    private transient SelectedRecipeDao myDao;

    @Generated(hash = 822630236)
    public SelectedRecipe(@NotNull String id, String quantity, String name,
                          String modifierData, String recipeId, String imageUri, Long courseId,
                          Long modifierId, Long orderId, String courseName, Long mealsId,
                          String mealsName) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.modifierData = modifierData;
        this.recipeId = recipeId;
        this.imageUri = imageUri;
        this.courseId = courseId;
        this.modifierId = modifierId;
        this.orderId = orderId;
        this.courseName = courseName;
        this.mealsId = mealsId;
        this.mealsName = mealsName;
    }

    @Generated(hash = 1498692327)
    public SelectedRecipe() {
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifierData() {
        return this.modifierData;
    }

    public void setModifierData(String modifierData) {
        this.modifierData = modifierData;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getMealsId() {
        return this.mealsId;
    }

    public void setMealsId(Long mealsId) {
        this.mealsId = mealsId;
    }

    public String getMealsName() {
        return this.mealsName;
    }

    public void setMealsName(String mealsName) {
        this.mealsName = mealsName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 165039288)
    public List<Modifier> getModifiers() {
        if (modifiers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ModifierDao targetDao = daoSession.getModifierDao();
            List<Modifier> modifiersNew = targetDao
                    ._querySelectedRecipe_Modifiers(id);
            synchronized (this) {
                if (modifiers == null) {
                    modifiers = modifiersNew;
                }
            }
        }
        return modifiers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 51124353)
    public synchronized void resetModifiers() {
        modifiers = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1110255404)
    public List<SelectedRecipeModifier> getSelectedRecipeModifiers() {
        if (selectedRecipeModifiers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SelectedRecipeModifierDao targetDao = daoSession
                    .getSelectedRecipeModifierDao();
            List<SelectedRecipeModifier> selectedRecipeModifiersNew = targetDao
                    ._querySelectedRecipe_SelectedRecipeModifiers(id);
            synchronized (this) {
                if (selectedRecipeModifiers == null) {
                    selectedRecipeModifiers = selectedRecipeModifiersNew;
                }
            }
        }
        return selectedRecipeModifiers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1960673533)
    public synchronized void resetSelectedRecipeModifiers() {
        selectedRecipeModifiers = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1566872270)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSelectedRecipeDao() : null;
    }

}
