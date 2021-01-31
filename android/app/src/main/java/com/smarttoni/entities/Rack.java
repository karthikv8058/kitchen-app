package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.models.Locales;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

@Entity(nameInDb = "Rack")

public class Rack {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "storage_id")
    private String storageId;

    @Property(nameInDb = "name")
    private String name;

    @ToMany(referencedJoinProperty = "rackId")
    @SerializedName("places")
    private List<Place> places;

    public List<Locales> getLocales() {
        return locales;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1762058020)
    private transient RackDao myDao;

    @Generated(hash = 779128176)
    public Rack(@NotNull String id, String storageId, String name) {
        this.id = id;
        this.storageId = storageId;
        this.name = name;
    }

    @Generated(hash = 477618782)
    public Rack() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStorageId() {
        return this.storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1004336316)
    public List<Place> getPlaces() {
        if (places == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlaceDao targetDao = daoSession.getPlaceDao();
            List<Place> placesNew = targetDao._queryRack_Places(id);
            synchronized (this) {
                if (places == null) {
                    places = placesNew;
                }
            }
        }
        return places;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 203716876)
    public synchronized void resetPlaces() {
        places = null;
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
    @Generated(hash = 363516018)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRackDao() : null;
    }

}

