package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.models.Locales;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "service_set")
public class ServiceSet {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @SerializedName("name")
    @Property(nameInDb = "name")
    private String name;

    @SerializedName("description")
    @Property(nameInDb = "description")
    private String description;



    @Transient
    @SerializedName("times")
    private List<ServiceSetTimings> times;

    @Transient
    @SerializedName("recipes")
    private List<ServiceSetRecipes> recipes;

    @ToMany(referencedJoinProperty = "serviceSetId")
    private List<ServiceSetTimings> serviceSetTiming;

    @ToMany(referencedJoinProperty = "serviceSetId")
    private List<ServiceSetRecipes> serviceSetRecipes;



    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 228620893)
    private transient ServiceSetDao myDao;



    @Generated(hash = 155289050)
    public ServiceSet() {
    }

    @Generated(hash = 1524321410)
    public ServiceSet(@NotNull String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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



    public List<ServiceSetTimings> getTimes() {
        return times;
    }

    public void setTimes(List<ServiceSetTimings> times) {
        this.times = times;
    }

    public List<ServiceSetRecipes> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ServiceSetRecipes> recipes) {
        this.recipes = recipes;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1169878777)
    public List<ServiceSetTimings> getServiceSetTiming() {
        if (serviceSetTiming == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceSetTimingsDao targetDao = daoSession.getServiceSetTimingsDao();
            List<ServiceSetTimings> serviceSetTimingNew = targetDao
                    ._queryServiceSet_ServiceSetTiming(id);
            synchronized (this) {
                if (serviceSetTiming == null) {
                    serviceSetTiming = serviceSetTimingNew;
                }
            }
        }
        return serviceSetTiming;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 859356911)
    public synchronized void resetServiceSetTiming() {
        serviceSetTiming = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1966542848)
    public List<ServiceSetRecipes> getServiceSetRecipes() {
        if (serviceSetRecipes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceSetRecipesDao targetDao = daoSession.getServiceSetRecipesDao();
            List<ServiceSetRecipes> serviceSetRecipesNew = targetDao
                    ._queryServiceSet_ServiceSetRecipes(id);
            synchronized (this) {
                if (serviceSetRecipes == null) {
                    serviceSetRecipes = serviceSetRecipesNew;
                }
            }
        }
        return serviceSetRecipes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 392225551)
    public synchronized void resetServiceSetRecipes() {
        serviceSetRecipes = null;
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
    @Generated(hash = 605181504)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceSetDao() : null;
    }


}
