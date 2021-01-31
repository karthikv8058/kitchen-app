package com.smarttoni.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;


@Entity(nameInDb = "chef_activity_log")
public class ChefActivityLog {

    @ToOne(joinProperty = "userId")
    private User user;

    @Property(nameInDb = "user_id")
    private String userId;

    @ToOne(joinProperty = "stationId")
    private Station station;

    @Property(nameInDb = "station_id")
    private String stationId;

    @ToOne(joinProperty = "taskId")
    private Task task;

    @Property(nameInDb = "task_id")
    private String taskId;

    @ToOne(joinProperty = "orderId")
    private Order order;

    @Property(nameInDb = "order_id")
    private String orderId;
    
    @Property(nameInDb = "isUpdated")
    private String isUpdated;

    @Property(nameInDb = "status")
    private int status;

    @Property(nameInDb = "created_at")
    private long createdAt;

    @ToOne(joinProperty = "workId")
    private Work work;

    @Property(nameInDb = "work")
    private Long workId;

    @Property(nameInDb = "work_duration")
    private Long workDuration;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 48990797)
    private transient ChefActivityLogDao myDao;



    @Generated(hash = 1916344537)
    public ChefActivityLog() {
    }

    @Generated(hash = 34623033)
    public ChefActivityLog(String userId, String stationId, String taskId, String orderId,
            String isUpdated, int status, long createdAt, Long workId, Long workDuration) {
        this.userId = userId;
        this.stationId = stationId;
        this.taskId = taskId;
        this.orderId = orderId;
        this.isUpdated = isUpdated;
        this.status = status;
        this.createdAt = createdAt;
        this.workId = workId;
        this.workDuration = workDuration;
    }

    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    @Generated(hash = 836891722)
    private transient String station__resolvedKey;

    @Generated(hash = 1106107757)
    private transient String task__resolvedKey;

    @Generated(hash = 1063247591)
    private transient String order__resolvedKey;

    @Generated(hash = 359473681)
    private transient Long work__resolvedKey;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStationId() {
        return this.stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getWorkId() {
        return this.workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getWorkDuration() {
        return this.workDuration;
    }

    public void setWorkDuration(Long workDuration) {
        this.workDuration = workDuration;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 538271798)
    public User getUser() {
        String __key = this.userId;
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
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1392187007)
    public Station getStation() {
        String __key = this.stationId;
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
    @Generated(hash = 978766149)
    public void setStation(Station station) {
        synchronized (this) {
            this.station = station;
            stationId = station == null ? null : station.getId();
            station__resolvedKey = stationId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1528694183)
    public Task getTask() {
        String __key = this.taskId;
        if (task__resolvedKey == null || task__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            Task taskNew = targetDao.load(__key);
            synchronized (this) {
                task = taskNew;
                task__resolvedKey = __key;
            }
        }
        return task;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 711336367)
    public void setTask(Task task) {
        synchronized (this) {
            this.task = task;
            taskId = task == null ? null : task.getId();
            task__resolvedKey = taskId;
        }
    }


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 563743264)
    public Work getWork() {
        Long __key = this.workId;
        if (work__resolvedKey == null || !work__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkDao targetDao = daoSession.getWorkDao();
            Work workNew = targetDao.load(__key);
            synchronized (this) {
                work = workNew;
                work__resolvedKey = __key;
            }
        }
        return work;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 781388248)
    public void setWork(Work work) {
        synchronized (this) {
            this.work = work;
            workId = work == null ? null : work.getId();
            work__resolvedKey = workId;
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

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1955925347)
    public Order getOrder() {
        String __key = this.orderId;
        if (order__resolvedKey == null || order__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDao targetDao = daoSession.getOrderDao();
            Order orderNew = targetDao.load(__key);
            synchronized (this) {
                order = orderNew;
                order__resolvedKey = __key;
            }
        }
        return order;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1275275852)
    public void setOrder(Order order) {
        synchronized (this) {
            this.order = order;
            orderId = order == null ? null : order.getId();
            order__resolvedKey = orderId;
        }
    }

    public String getIsUpdated() {
        return this.isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1038435209)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChefActivityLogDao() : null;
    }


}
