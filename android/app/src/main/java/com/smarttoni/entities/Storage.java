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

@Entity(nameInDb = "Storage")
public class Storage {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Property(nameInDb = "name")
    private String name;

    @SerializedName("room_uuid")
    @Property(nameInDb = "roomId")
    private String roomId;

    @ToMany(referencedJoinProperty = "storageId")
    @SerializedName("storageracks")
    private List<Rack> racks;

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public void setRacks(List<Rack> racks) {
        this.racks = racks;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 363957741)
    private transient StorageDao myDao;

    @Generated(hash = 51253978)
    public Storage(@NotNull String id, String name, String roomId) {
        this.id = id;
        this.name = name;
        this.roomId = roomId;
    }

    @Generated(hash = 2114225574)
    public Storage() {
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
    @Generated(hash = 1718872890)
    public List<Rack> getRacks() {
        if (racks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RackDao targetDao = daoSession.getRackDao();
            List<Rack> racksNew = targetDao._queryStorage_Racks(id);
            synchronized (this) {
                if (racks == null) {
                    racks = racksNew;
                }
            }
        }
        return racks;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1870043007)
    public synchronized void resetRacks() {
        racks = null;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1527546401)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStorageDao() : null;
    }

}
