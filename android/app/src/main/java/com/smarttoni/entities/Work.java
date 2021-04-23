package com.smarttoni.entities;


import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.utils.UnitHelper;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity(nameInDb = "task_queue")
public class Work {

    public static final int QUEUED = 1;
    public static final int STARTED = 2;
    public static final int COMPLETED = 4;
    public static final int SCHEDULED = 32;
    public static final int SYNERGY = 64;
    public static final int REMOVED = 128;
    public static final int BUNDLED = 256;

    public static final int NO_DELAY = 1;
    public static final int DELAYED = 2;
    public static final int WARNING = 3;

    public static final int TRANSPORT_DELIVERABLES = 4;
    public static final int TRANSPORT_FROM_INVENTORY = 8;
    public static final int TRANSPORT_TO_INVENTORY = 16;
    public static final int TRANSPORT_TO_LOCATION = 32;

    private static final String TEXT_TRANSPORT_FROM_INVENTORY = "Take Items from inventory";
    private static final String TEXT_TRANSPORT_TO_INVENTORY = "Put Items to inventory";
    
    @Id(autoincrement = true)
    private Long id;

    @Transient
    private List<Work> prevTasks;

    @Transient
    private List<Work> nextTasks;

    @Transient
    private Station station;

    @Transient
    private String stationName;

    @Transient
    private long startTime;

    //For Extended Machine Task
    @Transient
    private long extraTime;

    @Transient
    private long extraTimeSetDate;

    @Transient
    private long queueTime = 1;

    @Transient
    private long priority;

    @Transient
    private boolean sheduleSeparately;

    @ToMany(referencedJoinProperty = "workId")
    public List<InterventionJob> interventionJobs;

    @Property
    private float quantity = 1;

    @Property
    private long timeRemaining;


    @Transient
    private boolean showingCheckingTask;

    @Transient
    private String stationId;

    @Transient
    private String output;

    @Transient
    private String timeLeft;

    @Transient
    private Recipe recipe;

    @Transient
    private Work synergyParent;

    @Transient
    private List<Work> synergyList;

    @Transient
    private List<Work> deliveryList;

    @Transient
    private String outputUnit;

    @Transient
    private float actualQty;

    @Transient
    private String name;

    @Transient
    private long timePassed;

    @Transient
    private List<RecipeTag> tags;

    @Transient
    private List<String> wishes;

    //For  Transport Task Only
    @Transient
    private Station previousStation;

    @Transient
    private transient Work clonedWork;

    @Transient
    private int delayStatus;

    @Property(nameInDb = "recipeId")
    private String recipeId;

    @Property(nameInDb = "status")
    private int status;

    @ToOne(joinProperty = "taskId")
    public Task task;

    @Property(nameInDb = "taskId")
    private String taskId;

    @ToOne(joinProperty = "userId")
    private User user;

    @Property(nameInDb = "userId")
    private String userId;

    @ToOne(joinProperty = "machineId")
    private Machine machine;

    @Property(nameInDb = "machineId")
    private String machineId;

    @ToOne(joinProperty = "orderId")
    private Order order;

    @Property(nameInDb = "orderId")
    private String orderId;

    @Property(nameInDb = "courseId")
    private String courseId;

    @ToOne(joinProperty = "courseId")
    private Course course;

    @Property(nameInDb = "transportType")
    private int transportType;

    @Property
    private String title;

    @ToOne(joinProperty = "mealsId")
    private Meal meal;

    @Property(nameInDb = "mealsId")
    private String mealsId;

    @ToOne(joinProperty = "orderLineId")
    private OrderLine orderLine;

    @Property(nameInDb = "orderLineId")
    private String orderLineId;

    @Property(nameInDb = "isEndNode")
    private boolean isEndNode;

    @Property(nameInDb = "subRecipes")
    private String subRecipes;

    @Property(nameInDb = "isUsed")
    private boolean isUsed;

    @Property
    private int interventionsCompleted;

    @Property
    private long lastIntervention;

    @Property(nameInDb = "readyToStart")
    private boolean readyToStart;

    //1 No transport, 2 immediate,3 with synergy
    @Property(nameInDb = "transportMode")
    private int transportMode;

    //UUID to transport route
    @Property(nameInDb = "transportRoute")
    private String transportRoute;

    /**
     * currently for mapping transport task after restart
     */
    @Property
    private String previousTaskIds;

    @Property
    private String extraQuantity;//For End Node Only, After Completion this will add to inventory

    @Property(nameInDb = "createdAt")
    long createdAt;

    @Property(nameInDb = "updatedAt")
    long updatedAt;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 960630733)
    private transient WorkDao myDao;

    @Generated(hash = 572069219)
    public Work() {
    }

    @Generated(hash = 850250238)
    public Work(Long id, float quantity, long timeRemaining, String recipeId, int status, String taskId, String userId, String machineId, String orderId,
            String courseId, int transportType, String title, String mealsId, String orderLineId, boolean isEndNode, String subRecipes, boolean isUsed,
            int interventionsCompleted, long lastIntervention, boolean readyToStart, int transportMode, String transportRoute, String previousTaskIds,
            String extraQuantity, long createdAt, long updatedAt) {
        this.id = id;
        this.quantity = quantity;
        this.timeRemaining = timeRemaining;
        this.recipeId = recipeId;
        this.status = status;
        this.taskId = taskId;
        this.userId = userId;
        this.machineId = machineId;
        this.orderId = orderId;
        this.courseId = courseId;
        this.transportType = transportType;
        this.title = title;
        this.mealsId = mealsId;
        this.orderLineId = orderLineId;
        this.isEndNode = isEndNode;
        this.subRecipes = subRecipes;
        this.isUsed = isUsed;
        this.interventionsCompleted = interventionsCompleted;
        this.lastIntervention = lastIntervention;
        this.readyToStart = readyToStart;
        this.transportMode = transportMode;
        this.transportRoute = transportRoute;
        this.previousTaskIds = previousTaskIds;
        this.extraQuantity = extraQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 1106107757)
    private transient String task__resolvedKey;
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;
    @Generated(hash = 34963262)
    private transient String machine__resolvedKey;
    @Generated(hash = 1063247591)
    private transient String order__resolvedKey;
    @Generated(hash = 2048268504)
    private transient String course__resolvedKey;
    @Generated(hash = 1661191700)
    private transient String meal__resolvedKey;
    @Generated(hash = 2135920025)
    private transient String orderLine__resolvedKey;

    public boolean canStart() {
        boolean hasDependentTask = false;
        if (prevTasks == null || prevTasks.size() == 0) {
            this.readyToStart = true;
            return true;
        }
        for (Work task : prevTasks) {
            if (task.getStatus() != Work.COMPLETED) {
                hasDependentTask = true;
            }
        }
        this.readyToStart = !hasDependentTask;
        return readyToStart;
    }

    public List<Work> getPrevTasks() {
        return prevTasks;
    }

    public void setPrevTasks(List<Work> prevTasks) {
        this.prevTasks = prevTasks;
    }

    public void addSynergyList(Work task) {
        if (synergyList == null) {
            synergyList = new ArrayList<>();
        }
        synergyList.add(task);
        float qty = getQuantity() * getTask().getOutputQuantity();
        if (synergyList != null) {
            for (Work q : synergyList) {
                qty += q.getQuantity() * q.getTask().getOutputQuantity();
            }
        }
        setActualQty(qty);
        updateTitle();
    }

    public void removePrevTask(Work task) {
        if (prevTasks == null) {
            return;
        }
        prevTasks.remove(task);
    }

    public List<Work> getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(List<Work> nextTasks) {
        this.nextTasks = nextTasks;
    }

    public void addNextTask(Work task) {
        if (nextTasks == null) {
            nextTasks = new ArrayList<>();
        }
        nextTasks.add(task);
    }

    public void removeNextTask(Work task) {
        if (nextTasks == null) {
            return;
        }
        nextTasks.remove(task);
    }

    public boolean isIndependentTask() {
        return prevTasks == null || prevTasks.size() == 0;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        if (this.status == 0) {
            return Work.QUEUED;
        }
        return this.status;
    }

    public void setStatus(int status) {
        if (this.status == STARTED && status == QUEUED) {
            if (getTask() != null) {
               // long passed = getTask().getWorkDuration() - timeRemaining;
                long passed = System.currentTimeMillis() - getStartTime();
                if (getTask().getOutputTimeFactor()) {
                    passed = passed / 2;
                }
                timePassed = passed > 0 ? passed : 0;
            }
        }
        this.status = status;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Task getTaskObj() {
        return task;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


//    public void updateTransport(Work task) {
//        if ((transportType & TRANSPORT_STATION) > 0 || (transportType & TRANSPORT_MACHINE) > 0) {
//            name = "(Transport) ";
//            switch (transportType) {
//                case TRANSPORT_STATION:
//                    if (task != null) {
//                        if (task.getTask() != null) {
//                            name += task.getTask().getName();
//                        }
//                        if (task.getStationName() != null) {
//                            name += " from " + task.getStationName();
//                        }
//                    }
//                    break;
//                case TRANSPORT_MACHINE:
//                    if (nextTasks != null && nextTasks.get(0) != null && nextTasks.get(0).getTask() != null) {
//                        name += "for " + nextTasks.get(0).getTask().getName();
//                    }
//                    break;
//            }
//            title = name;
//        }
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String source) {
        this.title = source;
    }

    @Override
    public Work clone() {
        Work task = cloneWithoutNextTask(false);
        if (task.getNextTasks() != null) {
            for (Work t : task.getNextTasks()) {
                task.addNextTask(t.cloneWithoutNextTask(true));
            }
        }
        task.setStationId(task.getTask().getStationId());
        return task;
    }

    public Work cloneWithoutNextTask() {
        return cloneWithoutNextTask(false);
    }

    public Work cachedWork() {
        if (clonedWork != null) {
            return clonedWork;
        }
        Work task = new Work();
        task.setId(getId());
        if (getTask() != null) {
            task.setTask(getTask());
        }
        if (getRecipe() != null) {
            task.setRecipe(getRecipe());
        }
        if (getOrder() != null) {
            task.setOrder(getOrder());
        }
        task.setMealsId(mealsId);
        if (meal != null) {
            task.setMeal(meal);
        }
        if (course != null) {
            task.setCourse(course);
        }
        clonedWork = task;
        return task;
    }


    /**
     * Basic clone do not copy the synergy
     */
    public Work cloneWithoutNextTask(boolean isBasic) {
        Work task = cachedWork();

        task.setQuantity(getQuantity());
        task.setUserId(userId);
        task.setStatus(status);
        task.setTimeRemaining(timeRemaining);
        task.setStartTime(startTime);
        task.setDelayStatus(delayStatus);
        task.setOrderLineId(orderLineId);
        task.setPriority(priority);
        task.setTransportType(transportType);
        task.setTitle(title);
        task.setPreviousStation(previousStation);
        task.setReadyToStart(readyToStart);
        if (getTask().getUnit() != null) {
            task.setOutputUnit(getTask().getUnit().getName());
        } else {
            task.setOutputUnit("");
        }
        course = daoSession.getCourseDao().load(courseId);
        if (course != null) {
            task.setTimeLeft(String.valueOf((getCourse().getDeliveryTime() - System.currentTimeMillis())));
        }

        updateOutput();
        task.setOutput(output);

        if(task != null){
            Recipe r =task.getRecipe();
            if(r != null){
                DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
                List<RecipeTag> tags = daoAdapter.listTagsForRecipe(r.getId());
                List<String> wishes = new ArrayList();
                for(RecipeTag _recipeTag : tags){
                    Tag tag = daoAdapter.listTagsById(_recipeTag.getTag());

                    String tagValue= "";
                    switch (_recipeTag.getValue()){
                        case "true": tagValue = "Yes";break;
                        case "false": tagValue = "No";break;
                        default:tagValue=_recipeTag.getValue();
                    }
                    wishes.add(tag.getName()+" : "+tagValue);
                }
                task.setWishes(wishes);
            }
        }
        task.setStationName(stationName);
        return task;
    }

    public void updateOutput() {
        float outputQuantity = getActualQty() != 0 ? getActualQty() : getQuantity();
        if (isInventoryTransport()) {
            if (getTitle() != null
                    && (getTitle().equals(TEXT_TRANSPORT_FROM_INVENTORY)
                    || getTransportType() == TRANSPORT_TO_LOCATION
                    || getTitle().equals(TEXT_TRANSPORT_TO_INVENTORY))) {
                setOutput("-");
            } else {
                Recipe r = getRecipe();
                if (getTransportType() == TRANSPORT_FROM_INVENTORY) {
                    this.setOutput(outputQuantity + r.getOutputUnit().getName());
                } else if (r.getOutputUnit() != null) {
                    this.setOutput((outputQuantity * Math.round(r.getOutputQuantity())) + r.getOutputUnit().getName());
                } else {
                    setOutput("-");
                }
            }
        } else {
            if (getTask().getUnit() != null) {
                if (getActualQty() == 0) {
                    outputQuantity = getQuantity() * getTask().getOutputQuantity();
                }
                //String output = outputQuantity + getTask().getUnit().getName();
                //this.setOutput(output);
                this.setOutput(UnitHelper.convertToString(outputQuantity, getTask().getUnit()));
            }
        }
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        long delayInMinutes = priority / (60 * 1000);
        if (delayInMinutes >= 0) {
            setDelayStatus(DELAYED);
        } else if (delayInMinutes > -10) {
            setDelayStatus(WARNING);
        } else {
            setDelayStatus(NO_DELAY);
        }
        this.priority = priority;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(long queueTime) {
        this.queueTime = queueTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void addPrevTask(Work task) {
        if (prevTasks == null) {
            prevTasks = new ArrayList<>();
        }
        prevTasks.add(task);
    }

    public Work getSynergyParent() {
        return synergyParent;
    }

    public void setSynergyParent(Work synergyParent) {
        this.synergyParent = synergyParent;
    }

    public List<Work> getSynergyList() {
        return synergyList;
    }

    public void setSynergyList(List<Work> synergyList) {
        updateTitle();
        this.synergyList = synergyList;
    }

    public String getOutputUnit() {
        return outputUnit;
    }

    public void setOutputUnit(String outputUnit) {
        this.outputUnit = outputUnit;
    }

    public float getActualQty() {
        return actualQty;
    }

    public void setActualQty(float actualQty) {
        this.actualQty = actualQty;
        updateOutput();
    }

    public String getMachineId() {
        return this.machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public boolean isTransportTask() {
        return transportType != 0;
    }

    public void setTransportType(int transportType) {
        this.transportType = transportType;
    }

    public int getTransportType() {
        return transportType;
    }

    public int getDelayStatus() {
        return delayStatus;
    }

    public void setDelayStatus(int delayStatus) {
        this.delayStatus = delayStatus;
    }

    @Override
    public String toString() {
        String status = "";
        switch (getStatus()) {
            case QUEUED:
                status = "QUEUED";
                break;
            case STARTED:
                status = "STARTED";
                break;
            case COMPLETED:
                status = "COMPLETED";
                break;
            case SYNERGY:
                status = "SYNERGY";
                break;
            case SCHEDULED:
                status = "SCHEDULED";
                break;
        }
        String s = getId() + " (";
        if (getTask() != null) {
            s = s + getTask().getName() + ")" + status;
        }
        return s;
    }

    public boolean isSheduleSeparately() {
        return sheduleSeparately;
    }

    public void setSheduleSeparately(boolean sheduleSeparately) {
        this.sheduleSeparately = sheduleSeparately;
    }


    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        long remaining = timeRemaining - getTimePassed();
        this.timeRemaining = remaining > 0 ? remaining : 0;
    }
    
    public boolean isShowingCheckingTask() {
        return showingCheckingTask;
    }

    public void setShowingCheckingTask(boolean showingCheckingTask) {
        this.showingCheckingTask = showingCheckingTask;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public long getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(long extraTime) {
        this.extraTime = extraTime;
    }

    public long getExtraTimeSetDate() {
        return extraTimeSetDate;
    }

    public void setExtraTimeSetDate(long extraTimeSetDate) {
        this.extraTimeSetDate = extraTimeSetDate;
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

    public List<Work> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(List<Work> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public void addDeliveryList(Work work) {
        if (deliveryList == null) {
            deliveryList = new ArrayList<>();
        }
        deliveryList.add(work);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }


    public Station getPreviousStation() {
        return previousStation;
    }

    public void setPreviousStation(Station previousStation) {
        this.previousStation = previousStation;
    }

    public boolean isInventoryTransport() {
        return transportType == Work.TRANSPORT_FROM_INVENTORY || transportType == Work.TRANSPORT_TO_INVENTORY || transportType == Work.TRANSPORT_TO_LOCATION;
    }

    public boolean getIsEndNode() {
        return this.isEndNode;
    }

    public void setIsEndNode(boolean isEndNode) {
        this.isEndNode = isEndNode;
    }

    public String getSubRecipes() {
        return this.subRecipes;
    }

    public void setSubRecipes(String subRecipes) {
        this.subRecipes = subRecipes;
    }

    public boolean getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Recipe getRecipe() {
        if (recipe == null) {
            recipe = ServiceLocator
                    .getInstance()
                    .getDatabaseAdapter()
                    .getRecipeById(recipeId);
        }
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.recipeId = recipe.getId();
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
    @Generated(hash = 795561316)
    public Machine getMachine() {
        String __key = this.machineId;
        if (machine__resolvedKey == null || machine__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MachineDao targetDao = daoSession.getMachineDao();
            Machine machineNew = targetDao.load(__key);
            synchronized (this) {
                machine = machineNew;
                machine__resolvedKey = __key;
            }
        }
        return machine;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1865273958)
    public void setMachine(Machine machine) {
        synchronized (this) {
            this.machine = machine;
            machineId = machine == null ? null : machine.getId();
            machine__resolvedKey = machineId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1275275852)
    public void setOrder(Order order) {
        synchronized (this) {
            this.order = order;
            orderId = order == null ? null : order.getId();
            order__resolvedKey = orderId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     * <p>
     * /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */

    public long getTimePassed() {
        if (getTask() != null && getTask().getOutputTimeFactor()) {
            return timePassed * 2;
        }
        return timePassed;
    }

    public void setTimePassed(long timePassed) {
        this.timePassed = timePassed;
    }

    public int getInterventionsCompleted() {
        return this.interventionsCompleted;
    }

    public void setInterventionsCompleted(int interventionsCompleted) {
        this.interventionsCompleted = interventionsCompleted;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1231463247)
    public List<InterventionJob> getInterventionJobs() {
        if (interventionJobs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InterventionJobDao targetDao = daoSession.getInterventionJobDao();
            List<InterventionJob> interventionJobsNew = targetDao._queryWork_InterventionJobs(id);
            synchronized (this) {
                if (interventionJobs == null) {
                    interventionJobs = interventionJobsNew;
                }
            }
        }
        return interventionJobs;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1607617679)
    public synchronized void resetInterventionJobs() {
        interventionJobs = null;
    }

    public long getLastIntervention() {
        return this.lastIntervention;
    }

    public void setLastIntervention(long lastIntervention) {
        this.lastIntervention = lastIntervention;
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

    public boolean getReadyToStart() {
        return this.readyToStart;
    }

    public void setReadyToStart(boolean readyToStart) {
        this.readyToStart = readyToStart;
    }

    public void updateTitle() {
        if (!isTransportTask()) {
            return;
        }
        List<Work> works = getSynergyList();
        if (works != null) {
            List<String> recipeids = new ArrayList<>();
            recipeids.add(getRecipeId());
            for (Work w : works) {
                if (!recipeids.contains(w.getRecipeId())) {
                    recipeids.add(w.getRecipeId());
                }
                if (recipeids.size() > 1) {
                    break;
                }
            }
            if (recipeids.size() > 1) {
                setOutput("-");
                if (getTransportType() == Work.TRANSPORT_FROM_INVENTORY) {
                    setTitle(TEXT_TRANSPORT_FROM_INVENTORY);
                }
            }
        }
    }

    public boolean isMachineTask() {
        return task != null && task.isMachineTask();
    }

    public int getTransportMode() {
        return this.transportMode;
    }

    public void setTransportMode(int transportMode) {
        this.transportMode = transportMode;
    }

    public String getTransportRoute() {
        return this.transportRoute;
    }

    public void setTransportRoute(String transportRoute) {
        this.transportRoute = transportRoute;
    }

    public String getPreviousTaskIds() {
        return this.previousTaskIds;
    }

    public void addPreviousTaskIds(long taskId) {
        if (previousTaskIds == null || previousTaskIds.equals("")) {
            previousTaskIds = String.valueOf(taskId);
        } else {
            previousTaskIds += "," + taskId;
        }
    }

    public void setPreviousTaskIds(String previousTaskIds) {
        this.previousTaskIds = previousTaskIds;
    }

    public Work getClonedWork() {
        return clonedWork;
    }

    public void setClonedWork(Work clonedWork) {
        this.clonedWork = clonedWork;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1243393428)
    public Course getCourse() {
        String __key = this.courseId;
        if (course__resolvedKey == null || course__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseDao targetDao = daoSession.getCourseDao();
            Course courseNew = targetDao.load(__key);
            synchronized (this) {
                course = courseNew;
                course__resolvedKey = __key;
            }
        }
        return course;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 147477936)
    public void setCourse(Course course) {
        synchronized (this) {
            this.course = course;
            courseId = course == null ? null : course.getId();
            course__resolvedKey = courseId;
        }
    }

    public String getCourseId() {
        return this.courseId;
    }


    public String getExtraQuantity() {
        return this.extraQuantity;
    }

    public void setExtraQuantity(String extraQuantity) {
        this.extraQuantity = extraQuantity;
    }

    public List<String> getWishes() {
        return wishes;
    }

    public void setWishes(List<String> wishes) {
        this.wishes = wishes;
    }

    public void setMealsId(String mealsId) {
        this.mealsId = mealsId;
    }

    public String getMealsId() {
        return this.mealsId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 286316545)
    public Meal getMeal() {
        String __key = this.mealsId;
        if (meal__resolvedKey == null || meal__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MealDao targetDao = daoSession.getMealDao();
            Meal mealNew = targetDao.load(__key);
            synchronized (this) {
                meal = mealNew;
                meal__resolvedKey = __key;
            }
        }
        return meal;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1048215057)
    public void setMeal(Meal meal) {
        synchronized (this) {
            this.meal = meal;
            mealsId = meal == null ? null : meal.getId();
            meal__resolvedKey = mealsId;
        }
    }

    public void setOrderLineId(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    public String getOrderLineId() {
        return this.orderLineId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1369878535)
    public OrderLine getOrderLine() {
        String __key = this.orderLineId;
        if (orderLine__resolvedKey == null || orderLine__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderLineDao targetDao = daoSession.getOrderLineDao();
            OrderLine orderLineNew = targetDao.load(__key);
            synchronized (this) {
                orderLine = orderLineNew;
                orderLine__resolvedKey = __key;
            }
        }
        return orderLine;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1946645347)
    public void setOrderLine(OrderLine orderLine) {
        synchronized (this) {
            this.orderLine = orderLine;
            orderLineId = orderLine == null ? null : orderLine.getId();
            orderLine__resolvedKey = orderLineId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1470493720)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWorkDao() : null;
    }
}
