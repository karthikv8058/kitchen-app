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
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

@Entity(nameInDb = "orders")
public class Order {

    public static final int ORDER_OPEN = 0;
    public static final int ORDER_COMPLETED = 1;
    public static final int ORDER_DELETED = 2;
    public static final int ORDER_STARTED = 3;

    public static int TYPE_INTERNAL = 0;
    public static int TYPE_EXTERNAL = 1;
    
    public static final int MODIFICATION_NO = 0;
    public static final int MODIFICATION_CREATED = 1;
    public static final int MODIFICATION_UPDATED = 2;
    public static final int MODIFICATION_DELETED = 3;

    public static final int EXTERNAL_ORDER_NOT_CREATED = 0;
    public static final int EXTERNAL_ORDER_CREATED = 1;
    public static final int EXTERNAL_ORDER_COMPLETED = 2;
    public static final int TREE_BUILD_COMPLETED = 3;

    @ToOne(joinProperty = "printerDataId")
    private PrinterData printerData;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "tableNo")
    private String tableNo;

    @Property(nameInDb = "status")
    private int status;

    @Property(nameInDb = "isInventory")
    private boolean isInventory;

    @Property
    private int type;

    @Property(nameInDb = "printerDataId")
    private long printerDataId;

    @Property(nameInDb = "createdAt")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    @Property(nameInDb = "modification")
    private long modification;

    @Property(nameInDb = "isStarted")
    private boolean isStarted;

    @Property(nameInDb = "isArchive")
    private boolean isArchive;
    
    @Property
    private int sort;

//    @Property
//    private String parentOrderId;
    
    @Property
    private int childOrderStatus;

    @Property
    private boolean processed;


    @ToMany(referencedJoinProperty = "orderId")
    private List<Course> courses;

    @Property(nameInDb = "isUpdated")
    private boolean isUpdated;



    @Generated(hash = 666612333)
    private transient Long printerData__resolvedKey;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 949219203)
    private transient OrderDao myDao;

    @Generated(hash = 241484941)
    public Order(@NotNull String id, String tableNo, int status, boolean isInventory, int type,
            long printerDataId, long createdAt, long updatedAt, long modification, boolean isStarted,
            boolean isArchive, int sort, int childOrderStatus, boolean processed, boolean isUpdated) {
        this.id = id;
        this.tableNo = tableNo;
        this.status = status;
        this.isInventory = isInventory;
        this.type = type;
        this.printerDataId = printerDataId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.modification = modification;
        this.isStarted = isStarted;
        this.isArchive = isArchive;
        this.sort = sort;
        this.childOrderStatus = childOrderStatus;
        this.processed = processed;
        this.isUpdated = isUpdated;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
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

    public boolean getIsInventory() {
        return this.isInventory;
    }

    public void setIsInventory(boolean isInventory) {
        this.isInventory = isInventory;
    }

    public long getPrinterDataId() {
        return this.printerDataId;
    }

    public void setPrinterDataId(long printerDataId) {
        this.printerDataId = printerDataId;
    }

    public boolean getIsStarted() {
        return this.isStarted;
    }


    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }


    public long getModification() {
        return this.modification;
    }


    public void setModification(long modification) {
        this.modification = modification;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getIsUpdated() {
        return this.isUpdated;
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getParentOrderId() {
//        return this.parentOrderId;
//    }
//
//    public void setParentOrderId(String parentOrderId) {
//        this.parentOrderId = parentOrderId;
//    }

    public boolean getProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }



    /** To-one relationship, resolved on first access. */
    @Generated(hash = 985308854)
    public PrinterData getPrinterData() {
        long __key = this.printerDataId;
        if (printerData__resolvedKey == null
                || !printerData__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PrinterDataDao targetDao = daoSession.getPrinterDataDao();
            PrinterData printerDataNew = targetDao.load(__key);
            synchronized (this) {
                printerData = printerDataNew;
                printerData__resolvedKey = __key;
            }
        }
        return printerData;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 453031977)
    public void setPrinterData(@NotNull PrinterData printerData) {
        if (printerData == null) {
            throw new DaoException(
                    "To-one property 'printerDataId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.printerData = printerData;
            printerDataId = printerData.getId();
            printerData__resolvedKey = printerDataId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 183106713)
    public List<Course> getCourses() {
        if (courses == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseDao targetDao = daoSession.getCourseDao();
            List<Course> coursesNew = targetDao._queryOrder_Courses(id);
            synchronized (this) {
                if (courses == null) {
                    courses = coursesNew;
                }
            }
        }
        return courses;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1373990168)
    public synchronized void resetCourses() {
        courses = null;
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

    public boolean getIsArchive() {
        return this.isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;

    }

    public int getChildOrderStatus() {
        return this.childOrderStatus;
    }

    public void setChildOrderStatus(int childOrderStatus) {
        this.childOrderStatus = childOrderStatus;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 965731666)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }
    
}
