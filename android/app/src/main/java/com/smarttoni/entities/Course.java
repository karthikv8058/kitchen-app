package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


@Entity(nameInDb = "Course")

public class Course {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "tableNo")
    private String tableNo;

    @Property(nameInDb = "courseName")
    private String courseName;

    @Property(nameInDb = "deliveryTime")
    private long deliveryTime;

    @Property(nameInDb = "actualDeliveryTime")
    private long actualDeliveryTime;

    @Property(nameInDb = "expectedDeliveryTime")
    private long expectedDeliveryTime;

    @Property(nameInDb = "orderId")
    private String orderId;

    @ToMany(referencedJoinProperty = "courseId")
    private List<Meal> meals;

    @Property(nameInDb = "isOnCall")
    private boolean isOnCall;

    @Property(nameInDb = "createdAt")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2063667503)
    private transient CourseDao myDao;


    @Generated(hash = 51462183)
    public Course(@NotNull String id, String tableNo, String courseName,
            long deliveryTime, long actualDeliveryTime, long expectedDeliveryTime,
            String orderId, boolean isOnCall, long createdAt, long updatedAt) {
        this.id = id;
        this.tableNo = tableNo;
        this.courseName = courseName;
        this.deliveryTime = deliveryTime;
        this.actualDeliveryTime = actualDeliveryTime;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.orderId = orderId;
        this.isOnCall = isOnCall;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 1355838961)
    public Course() {
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

    public long getDeliveryTime() {
        return this.deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getActualDeliveryTime() {
        return this.actualDeliveryTime;
    }

    public void setActualDeliveryTime(long actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public long getExpectedDeliveryTime() {
        return this.expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(long expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public boolean getIsOnCall() {
        return this.isOnCall;
    }

    public void setIsOnCall(boolean isOnCall) {
        this.isOnCall = isOnCall;
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



    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
    @Generated(hash = 1486752237)
    public List<Meal> getMeals() {
        if (meals == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MealDao targetDao = daoSession.getMealDao();
            List<Meal> mealsNew = targetDao._queryCourse_Meals(id);
            synchronized (this) {
                if (meals == null) {
                    meals = mealsNew;
                }
            }
        }
        return meals;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 875075432)
    public synchronized void resetMeals() {
        meals = null;
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
    @Generated(hash = 94420068)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCourseDao() : null;
    }

}
