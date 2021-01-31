package com.smarttoni.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

@Entity(nameInDb = "user_station_assignment")
public class UserStationAssignment {

    @ToOne(joinProperty = "userid")
    private User user;

    @ToOne(joinProperty = "stationid")
    private Station station;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "userid")
    private String userid;

    @Property(nameInDb = "stationid")
    private String stationid;

    @Property(nameInDb = "createdAt")
    long createdAt;

    @Property(nameInDb = "isUpdated")
    private String isUpdated;

    @Property(nameInDb = "updatedAt")
    long updatedAt;

    @Generated(hash = 12239814)
    public UserStationAssignment() {
    }

    @Generated(hash = 806722002)
    public UserStationAssignment(Long id, String userid, String stationid, long createdAt,
            String isUpdated, long updatedAt) {
        this.id = id;
        this.userid = userid;
        this.stationid = stationid;
        this.createdAt = createdAt;
        this.isUpdated = isUpdated;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    @Generated(hash = 836891722)
    private transient String station__resolvedKey;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 118334115)
    private transient UserStationAssignmentDao myDao;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStationid() {
        return this.stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 300366389)
    public User getUser() {
        String __key = this.userid;
        if (user__resolvedKey == null || user__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 509120315)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userid = user == null ? null : user.getId();
            user__resolvedKey = userid;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1563863820)
    public Station getStation() {
        String __key = this.stationid;
        if (station__resolvedKey == null || station__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StationDao targetDao = daoSession.getStationDao();
            Station stationNew = targetDao.load(__key);
            synchronized (this) {
                station = stationNew;
                station__resolvedKey = __key;
            }
        }
        return station;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 54860561)
    public void setStation(Station station) {
        synchronized (this) {
            this.station = station;
            stationid = station == null ? null : station.getId();
            station__resolvedKey = stationid;
        }
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

    public String getIsUpdated() {
        return this.isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 239638119)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserStationAssignmentDao() : null;
    }


}
