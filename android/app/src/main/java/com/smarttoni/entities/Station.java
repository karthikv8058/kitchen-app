package com.smarttoni.entities;

import android.content.Context;

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

import java.util.Date;
import java.util.List;

@Entity(nameInDb = "stations")

public class Station {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Transient
    Context context;

    @ToMany(referencedJoinProperty = "stationid")
    private List<UserStationAssignment> userStationAssignments;

    @Property(nameInDb = "name")
    private String name;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Property(nameInDb = "color")
    @SerializedName("color")
    private String color;

    @Property(nameInDb = "printerUuid")
    @SerializedName("printer_uuid")
    private String printerUuid;

    @SerializedName("operation_mode")
    private int operationMode;

    @Property(nameInDb = "description")
    private String description;

    @Property(nameInDb = "createdat")
    @SerializedName("created_at")
    private Date createdat;

    @Property(nameInDb = "updatedat")
    @SerializedName("updated_at")
    private Date updatedat;

    @Property(nameInDb = "isDeliveryMode")
    private boolean isDeliverable;

    @Property(nameInDb = "room")
    @SerializedName("room_uuid")
    private String room;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 407760209)
    private transient StationDao myDao;


    @Generated(hash = 833410198)
    public Station() {
    }

    @Generated(hash = 550466386)
    public Station(@NotNull String id, String name, String color, String printerUuid, int operationMode,
            String description, Date createdat, Date updatedat, boolean isDeliverable, String room) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.printerUuid = printerUuid;
        this.operationMode = operationMode;
        this.description = description;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.isDeliverable = isDeliverable;
        this.room = room;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedat() {
        return this.createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return this.updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public boolean getIsDeliverable() {
        return this.isDeliverable;
    }

    public void setIsDeliverable(boolean isDeliverable) {
        this.isDeliverable = isDeliverable;
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
    @Generated(hash = 820348332)
    public List<UserStationAssignment> getUserStationAssignments() {
        if (userStationAssignments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserStationAssignmentDao targetDao = daoSession
                    .getUserStationAssignmentDao();
            List<UserStationAssignment> userStationAssignmentsNew = targetDao
                    ._queryStation_UserStationAssignments(id);
            synchronized (this) {
                if (userStationAssignments == null) {
                    userStationAssignments = userStationAssignmentsNew;
                }
            }
        }
        return userStationAssignments;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1557983704)
    public synchronized void resetUserStationAssignments() {
        userStationAssignments = null;
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

    public int getOperationMode() {
        return this.operationMode;
    }

    public void setOperationMode(int operationMode) {
        this.operationMode = operationMode;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPrinterUuid() {
        return this.printerUuid;
    }

    public void setPrinterUuid(String printerUuid) {
        this.printerUuid = printerUuid;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1184240734)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStationDao() : null;
    }

}