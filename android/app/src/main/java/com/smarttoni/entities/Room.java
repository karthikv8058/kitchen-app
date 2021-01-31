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


@Entity(nameInDb = "Room")
public class Room {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @SerializedName("name")
    @Property(nameInDb = "name")
    private String name;

    @SerializedName("station_count")
    @Property(nameInDb = "station_count")
    private String statioCconunt;

    @SerializedName("machine_count")
    @Property(nameInDb = "machine_count")
    private String machineCount;

    @SerializedName("storage_count")
    @Property(nameInDb = "storage_count")
    private String storageCcount;

    @ToMany(referencedJoinProperty = "roomId")
    private List<Storage> storage;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 740313876)
    private transient RoomDao myDao;


    @Generated(hash = 1916092257)
    public Room(@NotNull String id, String name, String statioCconunt,
                String machineCount, String storageCcount) {
        this.id = id;
        this.name = name;
        this.statioCconunt = statioCconunt;
        this.machineCount = machineCount;
        this.storageCcount = storageCcount;
    }

    @Generated(hash = 703125385)
    public Room() {
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

    public String getStatioCconunt() {
        return this.statioCconunt;
    }

    public void setStatioCconunt(String statioCconunt) {
        this.statioCconunt = statioCconunt;
    }

    public String getMachineCount() {
        return this.machineCount;
    }

    public void setMachineCount(String machineCount) {
        this.machineCount = machineCount;
    }

    public String getStorageCcount() {
        return this.storageCcount;
    }

    public void setStorageCcount(String storageCcount) {
        this.storageCcount = storageCcount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 72032049)
    public List<Storage> getStorage() {
        if (storage == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StorageDao targetDao = daoSession.getStorageDao();
            List<Storage> storageNew = targetDao._queryRoom_Storage(id);
            synchronized (this) {
                if (storage == null) {
                    storage = storageNew;
                }
            }
        }
        return storage;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1176736088)
    public synchronized void resetStorage() {
        storage = null;
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
    @Generated(hash = 1185512297)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoomDao() : null;
    }


}
