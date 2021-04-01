package com.smarttoni.entities;

import com.smarttoni.assignment.service.ServiceLocator;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

@Entity(nameInDb = "intervention_jobs")
public class InterventionJob {

    public static final String USER_ID = "USER_ID";
    public static final String INTERVENTION_ID = "INTERVENTION_ID";

    public static final int QUEUED = 0;
    public static final int STARTED = 1;
    public static final int COMPLETED = 2;
    public static final int WAITING = 4;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "order_id")
    private String orderId;

    @Property(nameInDb = "work_id")
    private long workId;

    @Property(nameInDb = "intervention_id")
    private String interventionId;

    @Property(nameInDb = "user_id")
    private String userId;

    @Property(nameInDb = "status")
    private int status;

    @Property(nameInDb = "waiting_from")
    private long waitingFrom;

    @Property(nameInDb = "extraTime")
    private int extraTime;

    @Property(nameInDb = "schedule_next")
    private boolean scheduleNext;

    @Property(nameInDb = "no_start")
    private boolean noStart;

    @Property(nameInDb = "started_at")
    private Date startedAt;

    @ToOne(joinProperty = "interventionId")
    private Intervention intervention;

    @ToOne(joinProperty = "workId")
    private Work work;

    @ToOne(joinProperty = "userId")
    private User user;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 989751545)
    private transient InterventionJobDao myDao;

    @Generated(hash = 382552866)
    public InterventionJob() {
    }

    @Generated(hash = 1589955316)
    public InterventionJob(Long id, String orderId, long workId, String interventionId, String userId,
            int status, long waitingFrom, int extraTime, boolean scheduleNext, boolean noStart,
            Date startedAt) {
        this.id = id;
        this.orderId = orderId;
        this.workId = workId;
        this.interventionId = interventionId;
        this.userId = userId;
        this.status = status;
        this.waitingFrom = waitingFrom;
        this.extraTime = extraTime;
        this.scheduleNext = scheduleNext;
        this.noStart = noStart;
        this.startedAt = startedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getWorkId() {
        return this.workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public String getInterventionId() {
        return this.interventionId;
    }

    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }

    @Generated(hash = 1032588215)
    private transient String intervention__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1440469598)
    public Intervention getIntervention() {
        String __key = this.interventionId;
        if (intervention__resolvedKey == null
                || intervention__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InterventionDao targetDao = daoSession.getInterventionDao();
            Intervention interventionNew = targetDao.load(__key);
            synchronized (this) {
                intervention = interventionNew;
                intervention__resolvedKey = __key;
            }
        }
        return intervention;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1814905838)
    public void setIntervention(Intervention intervention) {
        synchronized (this) {
            this.intervention = intervention;
            interventionId = intervention == null ? null : intervention.getId();
            intervention__resolvedKey = interventionId;
        }
    }

    @Generated(hash = 359473681)
    private transient Long work__resolvedKey;
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 352054099)
    public Work getWork() {
        long __key = this.workId;
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
    @Generated(hash = 29818956)
    public void setWork(@NotNull Work work) {
        if (work == null) {
            throw new DaoException(
                    "To-one property 'workId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.work = work;
            workId = work.getId();
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

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public InterventionJob clone() {
        InterventionJob interventionJob = new InterventionJob();
        interventionJob.setId(id);
        interventionJob.setStatus(status);
        interventionJob.setUserId(userId);
        interventionJob.setStartedAt(startedAt);
        if (work == null) {
            Work w = ServiceLocator.getInstance().getDatabaseAdapter().getWorkById(workId);
            if (w != null) {
                interventionJob.setWork(w);
            } else {
                ServiceLocator.getInstance().getDatabaseAdapter().deleteInterventionJobById(id);
            }
        }
        interventionJob.setWorkId(workId);
        if (getIntervention() != null) {
            getIntervention().getTaskSteps();
            interventionJob.setIntervention(getIntervention());
        }
        Work work = ServiceLocator.getInstance().getQueue().getTask(workId);
        if (work != null) {
            //DaoAdapter daoAdapter = ServiceLocator.getInstance().getQueue().getTask(workId)
            interventionJob.setWork(work.cloneWithoutNextTask());
        }
        return interventionJob;
    }

    public long getWaitingFrom() {
        return this.waitingFrom;
    }

    public void setWaitingFrom(long waitingFrom) {
        this.waitingFrom = waitingFrom;
    }

    public Date getStartedAt() {
        return this.startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public int getExtraTime() {
        return this.extraTime;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public boolean getScheduleNext() {
        return this.scheduleNext;
    }

    public void setScheduleNext(boolean scheduleNext) {
        this.scheduleNext = scheduleNext;
    }

    public boolean getNoStart() {
        return this.noStart;
    }

    public void setNoStart(boolean noStart) {
        this.noStart = noStart;
    }


    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1309606452)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInterventionJobDao() : null;
    }
}
