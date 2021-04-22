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


@Entity(nameInDb = "Meal")

public class Meal {

    @Id
    private String id;

    @Property(nameInDb = "tableNo")
    private String tableNo;

    @Property(nameInDb = "courseName")
    private String courseName;

    @Property(nameInDb = "courseId")
    private String courseId;

    @Property(nameInDb = "modifier")
    private String modifier;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "status")
    private int status;

    @Property(nameInDb = "createdAt")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    @ToMany(referencedJoinProperty = "mealId")
    private List<OrderLine> orderLine;

    @Property(nameInDb = "chefId")
    private String chefId;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1947976862)
    private transient MealDao myDao;

    @Generated(hash = 178737780)
    public Meal(String id, String tableNo, String courseName, String courseId, String modifier,
            String name, int status, long createdAt, long updatedAt, String chefId) {
        this.id = id;
        this.tableNo = tableNo;
        this.courseName = courseName;
        this.courseId = courseId;
        this.modifier = modifier;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.chefId = chefId;
    }

    @Generated(hash = 167100247)
    public Meal() {
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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
    
    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getChefId() {
        return this.chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
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
    @Generated(hash = 938923247)
    public List<OrderLine> getOrderLine() {
        if (orderLine == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderLineDao targetDao = daoSession.getOrderLineDao();
            List<OrderLine> orderLineNew = targetDao._queryMeal_OrderLine(id);
            synchronized (this) {
                if (orderLine == null) {
                    orderLine = orderLineNew;
                }
            }
        }
        return orderLine;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1816500150)
    public synchronized void resetOrderLine() {
        orderLine = null;
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
    @Generated(hash = 644317336)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMealDao() : null;
    }
}
