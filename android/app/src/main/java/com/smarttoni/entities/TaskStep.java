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
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

@Entity(nameInDb = "task_step")
public class  TaskStep {

    @ToOne(joinProperty = "taskid")
    private Task tasks;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "taskid")
    @SerializedName("task")
    private String taskid;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "description")
    @SerializedName("description")
    private String description;

    @Property(nameInDb = "type")
    @SerializedName("type")
    private String type;

    @Property(nameInDb = "imageurl")
    @SerializedName("image")
    private String imageurl;

    @Property(nameInDb = "position")
    @SerializedName("position")
    private String position;

    @Property(nameInDb = "videourl")
    @SerializedName("video")
    private String videourl;

    @Property(nameInDb = "quantity")
    private int quantity;

    @Transient
    @SerializedName("dependentTasks")
    private String dependentTask;

    @Transient
    @SerializedName("stepIngredients")
    private String taskIngredientComaSperatad;

    public String getTaskIngredientComaSperatad() {
        return taskIngredientComaSperatad;
    }

    public void setTaskIngredientComaSperatad(String taskIngredientComaSperatad) {
        this.taskIngredientComaSperatad = taskIngredientComaSperatad;
    }

    @Property(nameInDb = "createdat")
    private long createdat;

    @Property(nameInDb = "updatedat")
    private long updatedat;

    @Property(nameInDb = "sortorder")
    @SerializedName("sort_order")
    private int sortorder;

    @ToMany(referencedJoinProperty = "stepId")
    @SerializedName("ingredients")
    private List<StepIngrediant> ingredients;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;


    @Property(nameInDb = "previousTasks")
    private String previous;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 7160061)
    private transient TaskStepDao myDao;

    @Generated(hash = 812422596)
    public TaskStep(@NotNull String id, String taskid, String name, String description, String type,
            String imageurl, String position, String videourl, int quantity, long createdat,
            long updatedat, int sortorder, String previous) {
        this.id = id;
        this.taskid = taskid;
        this.name = name;
        this.description = description;
        this.type = type;
        this.imageurl = imageurl;
        this.position = position;
        this.videourl = videourl;
        this.quantity = quantity;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.sortorder = sortorder;
        this.previous = previous;
    }

    @Generated(hash = 903385424)
    public TaskStep() {
    }

    @Generated(hash = 543683049)
    private transient String tasks__resolvedKey;

    public void setIngredients(List<StepIngrediant> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskid() {
        return this.taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return this.imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getVideourl() {
        return this.videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public long getCreatedat() {
        return this.createdat;
    }

    public void setCreatedat(long createdat) {
        this.createdat = createdat;
    }

    public long getUpdatedat() {
        return this.updatedat;
    }

    public void setUpdatedat(long updatedat) {
        this.updatedat = updatedat;
    }

    public int getSortorder() {
        return this.sortorder;
    }

    public void setSortorder(int sortorder) {
        this.sortorder = sortorder;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2071024319)
    public List<StepIngrediant> getIngredients() {
        if (ingredients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StepIngrediantDao targetDao = daoSession.getStepIngrediantDao();
            List<StepIngrediant> ingredientsNew = targetDao
                    ._queryTaskStep_Ingredients(id);
            synchronized (this) {
                if (ingredients == null) {
                    ingredients = ingredientsNew;
                }
            }
        }
        return ingredients;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 183837919)
    public synchronized void resetIngredients() {
        ingredients = null;
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

    public String getPrevious() {
        return this.previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getDependentTask() {
        return dependentTask;
    }

    public void setDependentTask(String dependentTask) {
        this.dependentTask = dependentTask;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 763215425)
    public Task getTasks() {
        String __key = this.taskid;
        if (tasks__resolvedKey == null || tasks__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            Task tasksNew = targetDao.load(__key);
            synchronized (this) {
                tasks = tasksNew;
                tasks__resolvedKey = __key;
            }
        }
        return tasks;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 933194578)
    public void setTasks(Task tasks) {
        synchronized (this) {
            this.tasks = tasks;
            taskid = tasks == null ? null : tasks.getId();
            tasks__resolvedKey = taskid;
        }
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 677787380)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskStepDao() : null;
    }
}
