package com.smarttoni.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

@Entity(nameInDb = "selected_recipe_modifier")
public class SelectedRecipeModifier {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "selected_recipe_id")
    private String selectedRecipeId;


    @ToOne(joinProperty = "modifierId")
    private RecipeModifier recipeModifier;

    @Property(nameInDb = "modifier_id")
    private long modifierId;

    @Property(nameInDb = "orderId")
    private long orderId;

    @ToMany(referencedJoinProperty = "id")
    private List<RecipeModifier> recipeModifiers;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 417872541)
    private transient SelectedRecipeModifierDao myDao;

    @Generated(hash = 1764821833)
    public SelectedRecipeModifier(Long id, String selectedRecipeId, long modifierId,
                                  long orderId) {
        this.id = id;
        this.selectedRecipeId = selectedRecipeId;
        this.modifierId = modifierId;
        this.orderId = orderId;
    }

    @Generated(hash = 1150613409)
    public SelectedRecipeModifier() {
    }

    @Generated(hash = 946015382)
    private transient Long recipeModifier__resolvedKey;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }


    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getSelectedRecipeId() {
        return this.selectedRecipeId;
    }

    public void setSelectedRecipeId(String selectedRecipeId) {
        this.selectedRecipeId = selectedRecipeId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1791066500)
    public RecipeModifier getRecipeModifier() {
        long __key = this.modifierId;
        if (recipeModifier__resolvedKey == null
                || !recipeModifier__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeModifierDao targetDao = daoSession.getRecipeModifierDao();
            RecipeModifier recipeModifierNew = targetDao.load(__key);
            synchronized (this) {
                recipeModifier = recipeModifierNew;
                recipeModifier__resolvedKey = __key;
            }
        }
        return recipeModifier;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2055132131)
    public void setRecipeModifier(@NotNull RecipeModifier recipeModifier) {
        if (recipeModifier == null) {
            throw new DaoException(
                    "To-one property 'modifierId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.recipeModifier = recipeModifier;
            modifierId = recipeModifier.getId();
            recipeModifier__resolvedKey = modifierId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 969218838)
    public List<RecipeModifier> getRecipeModifiers() {
        if (recipeModifiers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeModifierDao targetDao = daoSession.getRecipeModifierDao();
            List<RecipeModifier> recipeModifiersNew = targetDao
                    ._querySelectedRecipeModifier_RecipeModifiers(id);
            synchronized (this) {
                if (recipeModifiers == null) {
                    recipeModifiers = recipeModifiersNew;
                }
            }
        }
        return recipeModifiers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1406614523)
    public synchronized void resetRecipeModifiers() {
        recipeModifiers = null;
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
    @Generated(hash = 1858628824)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSelectedRecipeModifierDao() : null;
    }
}


