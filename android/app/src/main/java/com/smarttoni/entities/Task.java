package com.smarttoni.entities;


import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.assignment.service.ServiceLocator;
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

import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Entity(nameInDb = "task")
public class Task {

    @ToMany(referencedJoinProperty = "taskid")
    @SerializedName("task_step")
    public List<TaskStep> taskSteps;

    @ToMany(referencedJoinProperty = "taskId")
    @SerializedName("intervention")
    public List<Intervention> interventions;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "station")
    @SerializedName("station")
    private String stationId;

    @Property(nameInDb = "parentTasks")
    @SerializedName("preparedItems")
    private String parentTasks;

    @Property(nameInDb = "type")
    @SerializedName("type")
    private int type;

    @Property(nameInDb = "name")
    @SerializedName("name")
    private String name;

    @Property(nameInDb = "description")
    @SerializedName("description")
    private String description;

    @Property(nameInDb = "workduration")
    @SerializedName("workDuration")
    private long workDuration;

    @Property(nameInDb = "interventionTime")
    @SerializedName("interventionTime")
    private long interventionTime;

    @Property(nameInDb = "autoWorkDuration")
    @SerializedName("averageWorkDuration")
    private long autoWorkDuration;

    @Property(nameInDb = "buffer_time")
    @SerializedName("bufferTime")
    private long bufferTime;

    @Property(nameInDb = "imageurl")
    @SerializedName("image")
    private String imageurl;

    @Property(nameInDb = "quantity")
    private int quantity;

    @Property(nameInDb = "createdat")
    @SerializedName("created_at")
    private Date createdat;

    @Property(nameInDb = "updatedat")
    @SerializedName("updated_at")
    private Date updatedat;

    @Property(nameInDb = "start_before_delivery")
    @SerializedName("startBeforeDelivery")
    private boolean startBeforeDelivery;

    @Property(nameInDb = "printLabel")
    @SerializedName("printLabel")
    private boolean printLabel;

    @Property(nameInDb = "output_time_factor")
    @SerializedName("outputTimeFactor")
    private boolean outputTimeFactor;

    @Property(nameInDb = "outputQuantity")
    @SerializedName("outputQuantity")
    private int outputQuantity;

    @Property(nameInDb = "interventionPosition")
    @SerializedName("interventionPosition")
    private int interventionPosition;

    @Property(nameInDb = "outputUnit")
    @SerializedName("outputUnit")
    private String outputUnit;



    @Transient
    @SerializedName("modifier_durations")
    private List<TaskModifierDuration> taskModifierDurations;

    @Transient
    @SerializedName("taskIngredients")
    private String taskIngredientComaSperatad;


    @ToMany(referencedJoinProperty = "taskId")
    private List<TaskIngredient> taskIngredient;

    @ToOne(joinProperty = "outputUnit")
    private Units unit;

    @SerializedName("delayableIntervention")
    @Property(nameInDb = "delayable_intervention")
    private boolean isDelayable;


    @Property(nameInDb = "machineId")
    @SerializedName("machine")
    private String machineId;

    @ToOne(joinProperty = "machineId")
    private Machine machines;


    @Property(nameInDb = "chef_involved")
    @SerializedName("chefInvolved")
    private boolean needExternalChef;

    @Transient
    public Recipe recipes;

    @Property(nameInDb = "recipeId")
    @SerializedName("recipe")
    private String recipeId;

    @Property(nameInDb = "outputUnitName")
    @SerializedName("outputUnitName")
    private String outputUnitName;

    @Transient
    @SerializedName("previousTasks")
    private List<Long> previousTasks;

    @Property(nameInDb = "previousTasks")
    @SerializedName("parentTasks")
    private String previous;

    @Transient
    @SerializedName("taskUuid")
    private String taskUuid;

    @Transient
    @SerializedName("interventionParent")
    private String interventionParent;

    @Property(nameInDb = "numberOfSegments")
    private int numberOfSegments;

    @Transient
    @SerializedName("previousRecipes")
    private List<Long> previousRecipes;

    @Property(nameInDb = "previousRecipes")
    private String txtPreviousRecipes;

    @Transient
    @SerializedName("dependent_tasks")
    private List<DependentTask> dependentTasks;

    @ToMany(referencedJoinProperty = "taskId")
    private List<Segment> segments;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    public String getTaskIngredientComaSperatad() {
        return taskIngredientComaSperatad;
    }

    public void setTaskIngredientComaSperatad(String taskIngredientComaSperatad) {
        this.taskIngredientComaSperatad = taskIngredientComaSperatad;
    }

    @Generated(hash = 1032066549)
    public Task(@NotNull String id, String stationId, String parentTasks, int type, String name, String description,
            long workDuration, long interventionTime, long autoWorkDuration, long bufferTime, String imageurl, int quantity,
            Date createdat, Date updatedat, boolean startBeforeDelivery, boolean printLabel, boolean outputTimeFactor,
            int outputQuantity, int interventionPosition, String outputUnit, boolean isDelayable, String machineId,
            boolean needExternalChef, String recipeId, String outputUnitName, String previous, int numberOfSegments,
            String txtPreviousRecipes) {
        this.id = id;
        this.stationId = stationId;
        this.parentTasks = parentTasks;
        this.type = type;
        this.name = name;
        this.description = description;
        this.workDuration = workDuration;
        this.interventionTime = interventionTime;
        this.autoWorkDuration = autoWorkDuration;
        this.bufferTime = bufferTime;
        this.imageurl = imageurl;
        this.quantity = quantity;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.startBeforeDelivery = startBeforeDelivery;
        this.printLabel = printLabel;
        this.outputTimeFactor = outputTimeFactor;
        this.outputQuantity = outputQuantity;
        this.interventionPosition = interventionPosition;
        this.outputUnit = outputUnit;
        this.isDelayable = isDelayable;
        this.machineId = machineId;
        this.needExternalChef = needExternalChef;
        this.recipeId = recipeId;
        this.outputUnitName = outputUnitName;
        this.previous = previous;
        this.numberOfSegments = numberOfSegments;
        this.txtPreviousRecipes = txtPreviousRecipes;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    @Generated(hash = 1373466704)
    private transient String unit__resolvedKey;

    @Generated(hash = 112856231)
    private transient String machines__resolvedKey;

    public List<TaskModifierDuration> getTaskModifierDurations() {
        return taskModifierDurations;
    }

    public void setTaskModifierDurations(List<TaskModifierDuration> taskModifierDurations) {
        this.taskModifierDurations = taskModifierDurations;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStartBeforeDelivery() {
        return startBeforeDelivery;
    }

    public void setStartBeforeDelivery(boolean startBeforeDelivery) {
        this.startBeforeDelivery = startBeforeDelivery;
    }

    public boolean getStartBeforeDelivery() {
        return this.startBeforeDelivery;
    }

    public long getBufferTime() {
        return this.bufferTime;
    }

    public void setBufferTime(int bufferTime) {
        this.bufferTime = bufferTime;
    }

    public void setAutoWorkDuration(int autoWorkDuration) {
        this.autoWorkDuration = autoWorkDuration;
    }

    public int getOutputQuantity() {
        return this.outputQuantity;
    }

    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }


    public List<DependentTask> getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(List<DependentTask> dependentTasks) {
        this.dependentTasks = dependentTasks;
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


    public String getMachineId() {
        return this.machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public boolean getNeedExternalChef() {
        return this.needExternalChef;
    }

    public void setNeedExternalChef(boolean needExternalChef) {
        this.needExternalChef = needExternalChef;
    }

    public boolean isMachineTask() {
        return getMachineId() != null && !getNeedExternalChef();
    }

    public void generatePrevious() {
        if (previousTasks != null) {
            previous = TextUtils.join(",", previousTasks);
            txtPreviousRecipes = TextUtils.join(",", previousRecipes);
        }
    }

    public String getPrevious() {
        return this.previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getPreviousRecipes() {
        return this.txtPreviousRecipes;
    }

    public void setPreviousRecipes(String txtPreviousRecipes) {
        this.txtPreviousRecipes = txtPreviousRecipes;
    }

    public String getTxtPreviousRecipes() {
        return this.txtPreviousRecipes;
    }

    public void setTxtPreviousRecipes(String txtPreviousRecipes) {
        this.txtPreviousRecipes = txtPreviousRecipes;
    }

    public void setOutputUnit(String outputUnit) {
        this.outputUnit = outputUnit;
    }

    public String getOutputUnit() {
        return this.outputUnit;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
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

    public String getStationId() {
        return this.stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }


    class DependentTask {
        @SerializedName("task_uuid")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
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
        DependentTask m = (DependentTask) it.next();
        sb.append(m.getId());
        while (it.hasNext()) {
            DependentTask dependentTask = (DependentTask) it.next();
            sb.append(",");
            sb.append(dependentTask.getId());
        }
        return sb.toString();
    }

    public boolean getOutputTimeFactor() {
        return this.outputTimeFactor;
    }


    public void setOutputTimeFactor(boolean outputTimeFactor) {
        this.outputTimeFactor = outputTimeFactor;
    }


    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }


    public String getOutputUnitName() {
        return this.outputUnitName;
    }

    public void setOutputUnitName(String outputUnitName) {
        this.outputUnitName = outputUnitName;
    }

    public void setNumberOfSegments(int numberOfSegments) {
        this.numberOfSegments = numberOfSegments;
    }

    public int getNumberOfSegments() {
        return this.numberOfSegments;
    }

    public long getWorkDuration() {
        return this.workDuration;
    }

    public void setWorkDuration(long workDuration) {
        this.workDuration = workDuration;
    }

    public long getAutoWorkDuration() {
        return this.autoWorkDuration;
    }

    public void setAutoWorkDuration(long autoWorkDuration) {
        this.autoWorkDuration = autoWorkDuration;
    }

    public void setBufferTime(long bufferTime) {
        this.bufferTime = bufferTime;
    }

    public long getWorkDuration(Work work) {
        return getOutputTimeFactor() ? (long) (this.workDuration * work.getQuantity()) : this.workDuration;
    }

    public long getAutoWorkDuration(Work work) {
        return getOutputTimeFactor() ? (long) (this.autoWorkDuration * work.getQuantity()) : this.autoWorkDuration;
    }

    public Recipe getRecipe() {
        if (recipes == null) {
            recipes = ServiceLocator
                    .getInstance()
                    .getDatabaseAdapter()
                    .getRecipeById(recipeId);
        }
        return recipes;
    }

    public void setRecipe(Recipe recipe) {
        this.recipes = recipe;
        this.recipeId = recipe.getId();
    }


    public int getInterventionPosition() {
        return this.interventionPosition;
    }

    public void setInterventionPosition(int interventionPosition) {
        this.interventionPosition = interventionPosition;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsDelayable() {
        return this.isDelayable;
    }

    public void setIsDelayable(boolean isDelayable) {
        this.isDelayable = isDelayable;
    }

    public long getInterventionTime() {
        return this.interventionTime;
    }


    public void setInterventionTime(long interventionTime) {
        this.interventionTime = interventionTime;
    }

    public String getParentTasks() {
        return this.parentTasks;
    }

    public void setParentTasks(String parentTasks) {
        this.parentTasks = parentTasks;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1823395121)
    public Units getUnit() {
        String __key = this.outputUnit;
        if (unit__resolvedKey == null || unit__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitsDao targetDao = daoSession.getUnitsDao();
            Units unitNew = targetDao.load(__key);
            synchronized (this) {
                unit = unitNew;
                unit__resolvedKey = __key;
            }
        }
        return unit;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 378076227)
    public void setUnit(Units unit) {
        synchronized (this) {
            this.unit = unit;
            outputUnit = unit == null ? null : unit.getId();
            unit__resolvedKey = outputUnit;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1267288526)
    public Machine getMachines() {
        String __key = this.machineId;
        if (machines__resolvedKey == null || machines__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MachineDao targetDao = daoSession.getMachineDao();
            Machine machinesNew = targetDao.load(__key);
            synchronized (this) {
                machines = machinesNew;
                machines__resolvedKey = __key;
            }
        }
        return machines;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1591204075)
    public void setMachines(Machine machines) {
        synchronized (this) {
            this.machines = machines;
            machineId = machines == null ? null : machines.getId();
            machines__resolvedKey = machineId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1423975086)
    public List<TaskStep> getTaskSteps() {
        if (taskSteps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskStepDao targetDao = daoSession.getTaskStepDao();
            List<TaskStep> taskStepsNew = targetDao._queryTask_TaskSteps(id);
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2044692578)
    public List<Intervention> getInterventions() {
        if (interventions == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InterventionDao targetDao = daoSession.getInterventionDao();
            List<Intervention> interventionsNew = targetDao._queryTask_Interventions(id);
            synchronized (this) {
                if (interventions == null) {
                    interventions = interventionsNew;
                }
            }
        }
        return interventions;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 903205762)
    public synchronized void resetSegments() {
        segments = null;
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

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }

    public String getInterventionParent() {
        return interventionParent;
    }

    public void setInterventionParent(String interventionParent) {
        this.interventionParent = interventionParent;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1020783501)
    public synchronized void resetInterventions() {
        interventions = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 39254474)
    public List<TaskIngredient> getTaskIngredient() {
        if (taskIngredient == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskIngredientDao targetDao = daoSession.getTaskIngredientDao();
            List<TaskIngredient> taskIngredientNew = targetDao._queryTask_TaskIngredient(id);
            synchronized (this) {
                if (taskIngredient == null) {
                    taskIngredient = taskIngredientNew;
                }
            }
        }
        return taskIngredient;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 513179239)
    public synchronized void resetTaskIngredient() {
        taskIngredient = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 664459498)
    public List<Segment> getSegments() {
        if (segments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SegmentDao targetDao = daoSession.getSegmentDao();
            List<Segment> segmentsNew = targetDao._queryTask_Segments(id);
            synchronized (this) {
                if (segments == null) {
                    segments = segmentsNew;
                }
            }
        }
        return segments;
    }

    public boolean getPrintLabel() {
        return this.printLabel;
    }

    public void setPrintLabel(boolean printLabel) {
        this.printLabel = printLabel;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }
}
