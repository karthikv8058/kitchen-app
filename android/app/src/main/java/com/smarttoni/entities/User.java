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


@Entity(nameInDb = "users")

public class User {

    public static final int TYPE_USER = 0;
    public static final int TYPE_STATION = 1;

    @ToMany(referencedJoinProperty = "userid")
    private List<UserStationAssignment> userStationAssignments;

    @ToMany(referencedJoinProperty = "userId")
    private List<UserRights> userRights;

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "realname")
    private String realName;

    @Property(nameInDb = "username")
    private String username;

    @Property(nameInDb = "password")
    private String password;

    @Property(nameInDb = "is_mainchef")
    private Boolean ismainchef;

    @Property(nameInDb = "created_at")
    private long createdat;

    @Property(nameInDb = "updated_at")
    private long updatedat;

    @Property(nameInDb = "user_type")
    private int userType;

    @Property(nameInDb = "station_id")
    private String stationId;

    @Property(nameInDb = "ip_address")
    private String ipAddress;

    @Property(nameInDb = "isAutoOpen")
    private boolean taskAutoOpen;

    @Property(nameInDb = "isStepAutoOpen")
    private boolean stepAutoOpen;

    @Property(nameInDb = "auto_assign")
    private boolean autoAssign;

    @Property(nameInDb = "is_vr_on")
    private boolean isVROn;

    @Property(nameInDb = "is_tts_on")
    private boolean isTTSOn;

    @Property(nameInDb = "read_on_assign")
    private boolean readOnAssign;

    @Property(nameInDb = "read_description_in_detail")
    private boolean readDescriptionInDetail;

    @Property(nameInDb = "read_description_in_steps")
    private boolean readDescriptionInSteps;

    @Property(nameInDb = "read_ingredient_in_detail")
    private boolean readIngredientInDetail;

    @Property(nameInDb = "read_ingredient_in_steps")
    private boolean readIngredientInSteps;

    @Property(nameInDb = "token")
    private String token;

    @Property(nameInDb = "isSupervisor")
    @SerializedName("is_supervisor")
    private boolean isSupervisor;

    @Property(nameInDb = "isOnline")
    private boolean online;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;


    @Generated(hash = 586692638)
    public User() {
    }


    @Generated(hash = 1439480680)
    public User(@NotNull String id, String name, String realName, String username, String password,
                Boolean ismainchef, long createdat, long updatedat, int userType, String stationId,
                String ipAddress, boolean taskAutoOpen, boolean stepAutoOpen, boolean autoAssign,
                boolean isVROn, boolean isTTSOn, boolean readOnAssign, boolean readDescriptionInDetail,
                boolean readDescriptionInSteps, boolean readIngredientInDetail,
                boolean readIngredientInSteps, String token, boolean isSupervisor, boolean online) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.username = username;
        this.password = password;
        this.ismainchef = ismainchef;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.userType = userType;
        this.stationId = stationId;
        this.ipAddress = ipAddress;
        this.taskAutoOpen = taskAutoOpen;
        this.stepAutoOpen = stepAutoOpen;
        this.autoAssign = autoAssign;
        this.isVROn = isVROn;
        this.isTTSOn = isTTSOn;
        this.readOnAssign = readOnAssign;
        this.readDescriptionInDetail = readDescriptionInDetail;
        this.readDescriptionInSteps = readDescriptionInSteps;
        this.readIngredientInDetail = readIngredientInDetail;
        this.readIngredientInSteps = readIngredientInSteps;
        this.token = token;
        this.isSupervisor = isSupervisor;
        this.online = online;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsmainchef() {
        return this.ismainchef;
    }

    public void setIsmainchef(Boolean ismainchef) {
        this.ismainchef = ismainchef;
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

    public String getStationId() {
        return this.stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsSupervisor() {
        return this.isSupervisor;
    }

    public void setIsSupervisor(boolean isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 769570809)
    public List<UserStationAssignment> getUserStationAssignments() {
        if (userStationAssignments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserStationAssignmentDao targetDao = daoSession
                    .getUserStationAssignmentDao();
            List<UserStationAssignment> userStationAssignmentsNew = targetDao
                    ._queryUser_UserStationAssignments(id);
            synchronized (this) {
                if (userStationAssignments == null) {
                    userStationAssignments = userStationAssignmentsNew;
                }
            }
        }
        return userStationAssignments;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 949754411)
    public synchronized void resetUserRights() {
        userRights = null;
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


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 509066506)
    public List<UserRights> getUserRights() {
        if (userRights == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserRightsDao targetDao = daoSession.getUserRightsDao();
            List<UserRights> userRightsNew = targetDao._queryUser_UserRights(id);
            synchronized (this) {
                if (userRights == null) {
                    userRights = userRightsNew;
                }
            }
        }
        return userRights;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1557983704)
    public synchronized void resetUserStationAssignments() {
        userStationAssignments = null;
    }

    public boolean getOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean getTaskAutoOpen() {
        return this.taskAutoOpen;
    }


    public void setTaskAutoOpen(boolean taskAutoOpen) {
        this.taskAutoOpen = taskAutoOpen;
    }


    public boolean getStepAutoOpen() {
        return this.stepAutoOpen;
    }


    public void setStepAutoOpen(boolean stepAutoOpen) {
        this.stepAutoOpen = stepAutoOpen;
    }


    public boolean getAutoAssign() {
        return this.autoAssign;
    }


    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
    }


    public boolean getIsVROn() {
        return this.isVROn;
    }


    public void setIsVROn(boolean isVROn) {
        this.isVROn = isVROn;
    }


    public boolean getIsTTSOn() {
        return this.isTTSOn;
    }


    public void setIsTTSOn(boolean isTTSOn) {
        this.isTTSOn = isTTSOn;
    }


    public boolean getReadOnAssign() {
        return this.readOnAssign;
    }


    public void setReadOnAssign(boolean readOnAssign) {
        this.readOnAssign = readOnAssign;
    }


    public boolean getReadDescriptionInDetail() {
        return this.readDescriptionInDetail;
    }


    public void setReadDescriptionInDetail(boolean readDescriptionInDetail) {
        this.readDescriptionInDetail = readDescriptionInDetail;
    }


    public boolean getReadDescriptionInSteps() {
        return this.readDescriptionInSteps;
    }


    public void setReadDescriptionInSteps(boolean readDescriptionInSteps) {
        this.readDescriptionInSteps = readDescriptionInSteps;
    }


    public boolean getReadIngredientInDetail() {
        return this.readIngredientInDetail;
    }


    public void setReadIngredientInDetail(boolean readIngredientInDetail) {
        this.readIngredientInDetail = readIngredientInDetail;
    }


    public boolean getReadIngredientInSteps() {
        return this.readIngredientInSteps;
    }


    public void setReadIngredientInSteps(boolean readIngredientInSteps) {
        this.readIngredientInSteps = readIngredientInSteps;
    }

    public void copySettings(User user) {
        taskAutoOpen = user.getTaskAutoOpen();
        stepAutoOpen = user.getStepAutoOpen();
        autoAssign = user.getAutoAssign();
        isVROn = user.getIsVROn();
        isTTSOn = user.getIsTTSOn();
        readOnAssign = user.getReadOnAssign();
        readDescriptionInDetail = user.getReadDescriptionInDetail();
        readDescriptionInSteps = user.getReadDescriptionInSteps();
        readIngredientInDetail = user.getReadIngredientInDetail();
        readIngredientInSteps = user.getReadIngredientInSteps();
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
