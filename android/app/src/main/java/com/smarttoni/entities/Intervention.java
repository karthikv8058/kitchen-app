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

import java.util.Iterator;
import java.util.List;

@Entity(nameInDb = "interventions")
public class Intervention {

    public static final int POSITION_START = 1;
    public static final int POSITION_END = 3;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "task_id")
    private String taskId;

    @ToOne(joinProperty = "taskId")
    public Task task;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "description")
    private String description;

    @Property(nameInDb = "check_in_criteria")
    private String checkInCriteria;

    @SerializedName("intervention_parent_uuid")
    @Property(nameInDb = "parent")
    private String parent;

    @SerializedName("delayableIntervention")
    @Property(nameInDb = "delayable_intervention")
    private boolean isDelayable;

    @SerializedName("intervention_time")
    @Property(nameInDb = "intervention_time")
    private long time;

    @Property(nameInDb = "imageUrl")
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("intervention_position")
    @Property(nameInDb = "intervention_position")
    private int interventionPosition;

    @SerializedName("ingredients")
    @ToMany(referencedJoinProperty = "taskId")
    private List<TaskIngredient> taskIngredients;

    @Property(nameInDb = "station_color")
    private String stationColor;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;


    @ToMany(referencedJoinProperty = "taskid")
    @SerializedName("task_step")
    public List<TaskStep> taskSteps;

    @Transient
    @SerializedName("dependent_tasks")
    private List<Task.DependentTask> dependentTasks;

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
    @Generated(hash = 1382822740)
    private transient InterventionDao myDao;


    @Generated(hash = 768626443)
    public Intervention() {
    }

    @Generated(hash = 1673800824)
    public Intervention(@NotNull String id, String taskId, String name, String description,
                        String checkInCriteria, String parent, boolean isDelayable, long time, String imageUrl,
                        int interventionPosition, String stationColor, String previous) {
        this.id = id;
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.checkInCriteria = checkInCriteria;
        this.parent = parent;
        this.isDelayable = isDelayable;
        this.time = time;
        this.imageUrl = imageUrl;
        this.interventionPosition = interventionPosition;
        this.stationColor = stationColor;
        this.previous = previous;
    }


    @Generated(hash = 1106107757)
    private transient String task__resolvedKey;


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

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckInCriteria() {
        return this.checkInCriteria;
    }

    public void setCheckInCriteria(String checkInCriteria) {
        this.checkInCriteria = checkInCriteria;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterventionPosition() {
        return this.interventionPosition;
    }

    public void setInterventionPosition(int interventionPosition) {
        this.interventionPosition = interventionPosition;
    }

    public String getStationColor() {
        return this.stationColor;
    }


    public void setStationColor(String stationColor) {
        this.stationColor = stationColor;
    }


    public String getDependentTaskCommaSeparated() {
        if (dependentTasks == null) {
            return "";
        }
        final Iterator<?> it = dependentTasks.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        Task.DependentTask m = (Task.DependentTask) it.next();
        sb.append(m.getId());
        while (it.hasNext()) {
            Task.DependentTask dependentTask = (Task.DependentTask) it.next();
            sb.append(",");
            sb.append(dependentTask.getId());
        }
        return sb.toString();
    }


    public String getImageUrl() {
        return this.imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public long getTime() {
        return this.time;
    }


    public void setTime(long time) {
        this.time = time;
    }


    public String getPrevious() {
        return this.previous;
    }


    public void setPrevious(String previous) {
        this.previous = previous;
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1796616442)
    public List<TaskIngredient> getTaskIngredients() {
        if (taskIngredients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskIngredientDao targetDao = daoSession.getTaskIngredientDao();
            List<TaskIngredient> taskIngredientsNew = targetDao
                    ._queryIntervention_TaskIngredients(id);
            synchronized (this) {
                if (taskIngredients == null) {
                    taskIngredients = taskIngredientsNew;
                }
            }
        }
        return taskIngredients;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 823836124)
    public synchronized void resetTaskIngredients() {
        taskIngredients = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 66244130)
    public List<TaskStep> getTaskSteps() {
        if (taskSteps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskStepDao targetDao = daoSession.getTaskStepDao();
            List<TaskStep> taskStepsNew = targetDao
                    ._queryIntervention_TaskSteps(id);
            synchronized (this) {
                if (taskSteps == null) {
                    taskSteps = taskStepsNew;
                }
            }
        }
        return taskSteps;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1963609033)
    public synchronized void resetTaskSteps() {
        taskSteps = null;
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

    public boolean getIsDelayable() {
        return this.isDelayable;
    }

    public void setIsDelayable(boolean isDelayable) {
        this.isDelayable = isDelayable;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1347965172)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInterventionDao() : null;
    }


}
