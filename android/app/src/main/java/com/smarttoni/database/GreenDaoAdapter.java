package com.smarttoni.database;

import android.content.Context;

import com.smarttoni.assignment.InventoryManagement;
import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.entities.AppLog;
import com.smarttoni.entities.ArchivedOrder;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.ArchivedOrdersDao;
import com.smarttoni.entities.ChefActivityLog;
import com.smarttoni.entities.ChefActivityLogDao;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.CourseDao;
import com.smarttoni.entities.DaoSession;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalAvailableQuantityDao;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.ExternalOrderRequestDao;
import com.smarttoni.entities.IngredientExtras;
import com.smarttoni.entities.IngredientExtrasDao;
import com.smarttoni.entities.IngredientRequirement;
import com.smarttoni.entities.IngredientRequirementDao;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionDao;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.InterventionJobDao;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryDao;
import com.smarttoni.entities.InventoryMovement;
import com.smarttoni.entities.InventoryRequest;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.entities.InventoryReservationDao;
import com.smarttoni.entities.Label;
import com.smarttoni.entities.LabelDao;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.MachineDao;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.MealDao;
import com.smarttoni.entities.Modifier;
import com.smarttoni.entities.ModifierDao;
import com.smarttoni.entities.Options;
import com.smarttoni.entities.OptionsDao;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderDao;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.OrderLineDao;
import com.smarttoni.entities.Place;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.PrinterDao;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.entities.PrinterDataDao;
import com.smarttoni.entities.Rack;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.RecipeDao;
import com.smarttoni.entities.RecipeIngredients;
import com.smarttoni.entities.RecipeIngredientsDao;
import com.smarttoni.entities.RestaurantSettings;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Segment;
import com.smarttoni.entities.SegmentDao;
import com.smarttoni.entities.ServiceSet;
import com.smarttoni.entities.ServiceSetDao;
import com.smarttoni.entities.ServiceSetRecipes;
import com.smarttoni.entities.ServiceSetRecipesDao;
import com.smarttoni.entities.ServiceSetTimings;
import com.smarttoni.entities.ServiceSetTimingsDao;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.StationDao;
import com.smarttoni.entities.StationTask;
import com.smarttoni.entities.StepIngrediant;
import com.smarttoni.entities.StepIngrediantDao;
import com.smarttoni.entities.Storage;
import com.smarttoni.entities.StorageDao;
import com.smarttoni.entities.Supplier;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskDao;
import com.smarttoni.entities.TaskIngredient;
import com.smarttoni.entities.TaskIngredientDao;
import com.smarttoni.entities.TaskStep;
import com.smarttoni.entities.TaskStepDao;
import com.smarttoni.entities.TransportRoute;
import com.smarttoni.entities.TransportRouteDao;
import com.smarttoni.entities.UnitConversion;
import com.smarttoni.entities.UnitConversionDao;
import com.smarttoni.entities.Units;
import com.smarttoni.entities.User;
import com.smarttoni.entities.UserDao;
import com.smarttoni.entities.UserRights;
import com.smarttoni.entities.UserRightsDao;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.entities.UserStationAssignmentDao;
import com.smarttoni.entities.WebAppData;
import com.smarttoni.entities.WebAppDataDao;
import com.smarttoni.entities.Work;
import com.smarttoni.entities.WorkDao;
import com.smarttoni.sync.orders.SyncCourse;
import com.smarttoni.sync.orders.SyncMeal;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.sync.orders.SyncOrderLine;
import com.smarttoni.utils.Strings;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.Sort;


public class GreenDaoAdapter implements DaoAdapter {

    private Context context;

    public GreenDaoAdapter(Context context) {
        this.context = context;
    }

    private DaoSession getDaoSession() {
        return DbOpenHelper.Companion.getDaoSession(context);
    }

    @Override
    public void clear() {

        for (AbstractDao abstractDao : getDaoSession().getAllDaos()){
            abstractDao.deleteAll();
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        //deleteAllRecipe();
        // getDaoSession().getRecipeDao().deleteAll();
//        getDaoSession().getStationDao().deleteAll();
//        getDaoSession().getUserStationAssignmentDao().deleteAll();
//        getDaoSession().getOrderDao().deleteAll();
//        getDaoSession().getTaskDao().deleteAll();
//        getDaoSession().getTaskStepDao().deleteAll();
//        getDaoSession().getOrderLineDao().deleteAll();
//        getDaoSession().getStepIngrediantDao().deleteAll();
//        getDaoSession().getWorkDao().deleteAll();
//        getDaoSession().getInterventionJobDao().deleteAll();
//        getDaoSession().getUserDao().deleteAll();
//        getDaoSession().getOptionsDao().deleteAll();
//        getDaoSession().getLabelDao().deleteAll();
//        getDaoSession().getOrderLineDao().deleteAll();
//        getDaoSession().getInventoryDao().deleteAll();
    }

    @Override
    public User getUserById(String id) {
        return getDaoSession().getUserDao().load(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(email)).unique();
    }

    @Override
    public User getUserByToken(String token) {
        return getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Token.eq(token)).unique();
    }

    @Override
    public List<UserRights> getUserRightsById(String id) {
        return getDaoSession().getUserRightsDao().queryBuilder()
                .where(UserRightsDao.Properties.UserId.eq(id)).list();
    }

    @Override
    public Inventory getInventoryById(String id) {
        return getDaoSession().getInventoryDao().queryBuilder()
                .where(InventoryDao.Properties.RecipeUuid.eq((id)))
                .unique();
    }

    @Override
    public RestaurantSettings getRestaurantSettings() {
        return getDaoSession().getRestaurantSettingsDao().queryBuilder()
                .build().unique();
    }

    @Override
    public WebAppData getWebAppCredentials(String id) {
        return getDaoSession().getWebAppDataDao().queryBuilder()
                .where(WebAppDataDao.Properties.UserId.eq(id)).build().unique();
    }

    @Override
    public List<UserStationAssignment> loadUserStationAssignmentById(String id) {
        return getDaoSession().getUserStationAssignmentDao()
                .queryBuilder()
                .where(UserStationAssignmentDao.Properties.Userid.eq(id))
                .list();
    }

    @Override
    public void deleteuserStationAssignment(String id) {
        getDaoSession().getUserStationAssignmentDao().queryBuilder()
                .where(UserStationAssignmentDao.Properties.Userid.eq(id))
                .buildDelete().
                executeDeleteWithoutDetachingEntities();
    }

    @Override
    public UserStationAssignment loadUserStationAssignmentByIds(String stationId, String id) {
        return DbOpenHelper.Companion.getDaoSession(context).getUserStationAssignmentDao().queryBuilder()
                .where(UserStationAssignmentDao.Properties.Stationid.eq(stationId))
                .where(UserStationAssignmentDao.Properties.Userid.eq(id)).unique();
    }

    @Override
    public TaskStep loadStepsById(String id) {
        return getDaoSession().getTaskStepDao().queryBuilder()
                .where(TaskStepDao.Properties.Id.eq(id)).unique();
    }

    @Override
    public void updateSteps(TaskStep taskStep) {
        getDaoSession().getTaskStepDao().update(taskStep);
    }


    @Override
    public void saveWebAppData(WebAppData webAppData) {
        getDaoSession().getWebAppDataDao().insert(webAppData);
    }

    @Override
    public void deleteWebAppDatUserDataByUserId(String id) {
        getDaoSession().getWebAppDataDao().queryBuilder()
                .where(WebAppDataDao.Properties.UserId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<Modifier> getModifierList() {
        return getDaoSession().getModifierDao().queryBuilder().list();

    }

    @Override
    public void saveUser(User user) {
        User u = getDaoSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Username.eq(user.getUsername()))
                .unique();
        if (u != null) {
            user.setIpAddress(u.getIpAddress());
            user.setToken(u.getToken());
            u.copySettings(user);
            updateUser(u);
        } else {
            getDaoSession().getUserDao().insertOrReplace(user);
            //UserWork userWork = new UserWork();
            //userWork.setUserId(user.getId());
            //saveUserWork(userWork);
        }
    }

    @Override
    public void saveStorage(Storage storage) {
        getDaoSession().getStorageDao().insert(storage);
    }

    @Override
    public void saveRack(Rack rack) {
        getDaoSession().getRackDao().insert(rack);
    }

    @Override
    public void savePlaces(Place place) {
        getDaoSession().getPlaceDao().insert(place);
    }

    @Override
    public void saveUser(List<User> user) {
        getDaoSession().getUserDao().saveInTx(user);
    }

    @Override
    public void updateUser(User user) {
        getDaoSession().getUserDao().update(user);
    }

    @Override
    public void deleteUser(User user) {
        getDaoSession().getUserDao().delete(user);
    }

    @Override
    public List<User> loadUsers() {
        return getDaoSession().getUserDao().loadAll();
    }

    @Override
    public List<User> loadChefs() {
        return getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.UserType.eq(User.TYPE_USER)).list();
    }

    @Override
    public Order getOrderById(String id) {
        return getDaoSession().getOrderDao().queryBuilder().where(OrderDao.Properties.Id.eq(id)).unique();
    }

    @Override
    public void saveOrder(Order order, boolean fromWeb) {
        if (fromWeb) {
            order.setModification(Order.MODIFICATION_NO);
        } else {
            order.setModification(Order.MODIFICATION_CREATED);
            order.setUpdatedAt(System.currentTimeMillis());
        }
        getDaoSession().getOrderDao().insert(order);
    }

    @Override
    public void saveArchivedOrders(ArchivedOrders order) {
        getDaoSession().getArchivedOrdersDao().insert(order);
    }

    @Override
    public void saveArchivedOrder(ArchivedOrder order) {
        getDaoSession().getArchivedOrderDao().insert(order);
    }

    @Override
    public List<ArchivedOrder> listArchivedOrder() {
        return getDaoSession().getArchivedOrderDao().loadAll();
    }

    @Override
    public void saveOrder(Order order) {
        saveOrder(order, false);
    }

    @Override
    public void updateOrder(Order order) {
        order.setModification(Order.MODIFICATION_UPDATED);
        order.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getOrderDao().update(order);
    }

    @Override
    public void updateOrderWithNoModification(Order order) {
        getDaoSession().getOrderDao().update(order);
    }

    @Override
    public List<Order> loadChildOrders(String parentOrderId) {
        List<ExternalOrderRequest> list = getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ParentOrder.eq(parentOrderId))
                .list();

        List<Order> orders = new ArrayList<>();
        for (ExternalOrderRequest eor : list) {
            Order order = getOrderById(eor.getExternalOrder());
            orders.add(order);
        }
        return orders;
    }

    @Override
    public List<Order> loadParentOrders(String childOrderId) {
        List<ExternalOrderRequest> list = getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ExternalOrder.eq(childOrderId))
                .list();

        List<Order> orders = new ArrayList<>();
        for (ExternalOrderRequest eor : list) {
            Order order = getOrderById(eor.getParentOrder());
            orders.add(order);
        }
        return orders;
    }

    @Override
    public void saveInventoryReservation(InventoryReservation inventoryReservation) {
        getDaoSession()
                .getInventoryReservationDao()
                .insert(inventoryReservation);
    }

    @Override
    public void removeInventoryReservations(String orderId) {
        getDaoSession()
                .getInventoryReservationDao()
                .queryBuilder()
                .where(InventoryReservationDao.Properties.OrderId.eq(orderId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<InventoryReservation> listInventoryReservations(String orderId) {
        return getDaoSession()
                .getInventoryReservationDao()
                .queryBuilder()
                .where(InventoryReservationDao.Properties.OrderId.eq(orderId))
                .list();
    }

    @Override
    public List<InventoryReservation> listInventoryReservations(String orderId, String recipeId) {

        return getDaoSession()
                .getInventoryReservationDao()
                .queryBuilder()
                .where(InventoryReservationDao.Properties.OrderId.eq(orderId))
                .where(InventoryReservationDao.Properties.RecipeId.eq(recipeId))
                .list();
    }

    @Override
    public void writeLog(String tag, String value) {
        AppLog appLog = new AppLog();
        appLog.setTag(tag);
        appLog.setLog(value);
        appLog.setDate(new Date().getTime());
        getDaoSession().getAppLogDao().insert(appLog);
    }

    @Override
    public void saveUnitConversions(List<UnitConversion> conversions) {
        getDaoSession().getUnitConversionDao().deleteAll();
        getDaoSession().getUnitConversionDao().insertInTx(conversions);
    }

    @Override
    public List<UnitConversion> listUnitConversions() {
        return getDaoSession().getUnitConversionDao().queryBuilder()
                .list();
    }

    @Override
    public List<UnitConversion> listUnitConversions(String fromUnitId) {
        return getDaoSession().getUnitConversionDao().queryBuilder()
                .where(UnitConversionDao.Properties.From.eq(fromUnitId))
                .list();
    }

    @Override
    public boolean hasWaitingInventoryRequest(String orderId) {
        Realm realm = Realm.getDefaultInstance();
        List<InventoryRequest> inventoryRequests = realm
                .copyFromRealm(realm.where(InventoryRequest.class).equalTo("orderId", orderId).limit(1).findAll());

        realm.close();
        return !inventoryRequests.isEmpty();
    }

    @Override
    public void deleteInventoryRequests(InventoryRequest inventoryRequest) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(InventoryRequest.class)
                .equalTo("orderId", inventoryRequest.getOrderId())
                .equalTo("recipeId", inventoryRequest.getRecipeId())
                .findAll()
                .deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public List<InventoryRequest> loadAllInventoryRequests() {
        Realm realm = Realm.getDefaultInstance();
        List<InventoryRequest> inventoryRequests = realm
                .copyFromRealm(realm.where(InventoryRequest.class).findAll());
        realm.close();
        return inventoryRequests;
    }

    @Override
    public void saveInventoryRequest(InventoryRequest inventoryRequest) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(inventoryRequest);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void addExternalOrderRequest(String parentOrderId, String externalOrderId, String recipeId, float qty) {
        ExternalOrderRequest eor = new ExternalOrderRequest();
        eor.setId(UUID.randomUUID().toString());
        eor.setParentOrder(parentOrderId);
        eor.setExternalOrder(externalOrderId);
        eor.setRecipe(recipeId);
        eor.setQuantity(qty);
        getDaoSession().getExternalOrderRequestDao().insert(eor);
    }

    @Override
    public void addExternalAvailableQuantity(String externalOrderId, String recipeId, float qty) {
        ExternalAvailableQuantity eaq = new ExternalAvailableQuantity();
        eaq.setId(UUID.randomUUID().toString());
        eaq.setOrder(externalOrderId);
        eaq.setRecipe(recipeId);
        eaq.setQuantity(qty);
        getDaoSession().getExternalAvailableQuantityDao().insert(eaq);
    }

    @Override
    public ExternalAvailableQuantity getExternalAvailableQuantity(String externalOrderId) {
        return getDaoSession()
                .getExternalAvailableQuantityDao()
                .queryBuilder()
                .where(ExternalAvailableQuantityDao.Properties.Order.eq(externalOrderId))
                .unique();
    }

    @Override
    public void deleteOrder(Order order) {
        order.setModification(Order.MODIFICATION_DELETED);
        order.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getOrderDao().delete(order);
    }

    @Override
    public boolean deleteOrder(SyncOrder order) {
        Order o = getDaoSession()
                .getOrderDao()
                .queryBuilder()
                .where(OrderDao.Properties.Id.eq(order.uuid)).unique();

        if (o == null) {
            return true;
        }

        if (o.getStatus() == Order.ORDER_STARTED) {
            return false;
        }

        getDaoSession().getOrderDao().delete(o);


        if (order.courses == null) {
            return true;
        }

        for (SyncCourse course : order.courses) {
            List<Course> courses = getDaoSession()
                    .getCourseDao()
                    .queryBuilder()
                    .where(CourseDao.Properties.Id.eq(course.uuid)).list();

            if (courses == null) {
                continue;
            }

            for (Course c : courses) {
                getDaoSession().getCourseDao().delete(c);

                for (SyncMeal meal : course.meals) {

                    Meal m = getDaoSession()
                            .getMealDao()
                            .queryBuilder()
                            .where(MealDao.Properties.Uuid.eq(meal.uuid)).unique();
                    if (m == null) {
                        continue;
                    }
                    getDaoSession().getMealDao().delete(m);
                    for (SyncOrderLine orderLine : meal.orderLines) {

                        OrderLine ol = getDaoSession()
                                .getOrderLineDao()
                                .queryBuilder()
                                .where(OrderLineDao.Properties.Uuid.eq(orderLine.uuid)).unique();
                        if (ol == null) {
                            continue;
                        }
                        getDaoSession().getOrderLineDao().delete(ol);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<ArchivedOrders> loadArchivedOrders() {
        return getDaoSession().getArchivedOrdersDao().loadAll();
    }

    @Override
    public List<Order> loadnonArchivedOrders() {
        return getDaoSession()
                .getOrderDao()
                .queryBuilder().orderDesc(OrderDao.Properties.CreatedAt)
                .where(OrderDao.Properties.IsArchive.notEq(1)).list();
    }

    @Override
    public List<Order> listOrdersByType(int type) {
        return getDaoSession().getOrderDao().queryBuilder().where(OrderDao.Properties.Type.eq(type)).list();
    }
    
    @Override
    public List<Order> loadOrders() {
        return getDaoSession().getOrderDao().loadAll();
    }

    @Override
    public void setOrderStarted(String orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setIsStarted(true);
            getDaoSession().getOrderDao().update(order);
        }
    }

    @Override
    public Course getCourseById(String id) {
        return getDaoSession().getCourseDao().load(id);
    }

    @Override
    public void saveCourse(Course course) {
        if(getCourseById(course.getId()) == null){
            if (course.getActualDeliveryTime() == 0) {
                course.setActualDeliveryTime(System.currentTimeMillis());
            }
            getDaoSession().getCourseDao().insert(course);
        }
    }

    @Override
    public void updateCourse(Course course) {
        if (course.getActualDeliveryTime() == 0) {
            course.setActualDeliveryTime(System.currentTimeMillis());
        }
        getDaoSession().getCourseDao().update(course);
    }

    @Override
    public void deleteCourse(Course course) {
        getDaoSession().getCourseDao().delete(course);
    }

    @Override
    public List<Course> loadCourses() {
        return getDaoSession().getCourseDao().loadAll();
    }

    @Override
    public List<Course> loadCourses(String orderId) {
        return getDaoSession().
                getCourseDao()
                .queryBuilder()
                .where(CourseDao.Properties.OrderId.eq(orderId))
                .list();
    }

    @Override
    public List<PrinterData> loadUnUpdatedPrinterData() {
        return getDaoSession().
                getPrinterDataDao()
                .queryBuilder()
                .where(PrinterDataDao.Properties.IsUpdated.notEq("1"))
                .list();
    }

    @Override
    public List<Inventory> loadUnUpdatedInventory() {
        return getDaoSession().
                getInventoryDao()
                .queryBuilder()
                .where(InventoryDao.Properties.IsUpdated.notEq("1"))
                .list();
    }

    @Override
    public List<Printer> getPrinterList() {
        return  getDaoSession().getPrinterDao().loadAll();
    }

    @Override
    public List<ChefActivityLog> loadUnUpdatedChefActivity() {
        return getDaoSession().
                getChefActivityLogDao()
                .queryBuilder()
                .where(ChefActivityLogDao.Properties.IsUpdated.notEq("1"))
                .list();
    }

    @Override
    public List<UserStationAssignment> loadUnUpdatedUserStation() {
        return getDaoSession().
                getUserStationAssignmentDao()
                .queryBuilder()
                .where(UserStationAssignmentDao.Properties.IsUpdated.notEq("1"))
                .list();
    }

    @Override
    public List<Work> loadUnUpdatedWorks() {
        return getDaoSession().
                getWorkDao()
                .queryBuilder()
                .where(WorkDao.Properties.IsUpdated.notEq("1"))
                .list();
    }


    @Override
    public List<Meal> loadMealById(String mealId) {
        return getDaoSession().getMealDao().queryBuilder()
                .where(MealDao.Properties.Uuid.eq(mealId)).list();
    }

    @Override
    public List<Meal> getmealByCourseId(String CourseId) {
        return getDaoSession().getMealDao().queryBuilder()
                .where(MealDao.Properties.CourseId.eq(CourseId)).list();
    }

    @Override
    public List<OrderLine> loadOrderLineListById(String id) {
        return getDaoSession().getOrderLineDao().queryBuilder()
                .where(OrderLineDao.Properties.Uuid.eq(id)).list();
    }

    @Override
    public OrderLine getOrdeLineById(String orderId, Long mealId, String recipeId) {
        return getDaoSession().getOrderLineDao().queryBuilder()
                .where(OrderLineDao.Properties.OrderId.eq(orderId), OrderLineDao.Properties.MealId.eq(mealId),
                        OrderLineDao.Properties.RecipeId.eq(recipeId)).unique();
    }

    @Override
    public List<OrderLine> getordeLineByMealId(Long id) {
        return getDaoSession().getOrderLineDao().queryBuilder().where(OrderLineDao.Properties.MealId.eq(id)).list();
    }

    @Override
    public Meal getMealById(Long id) {
        return getDaoSession().getMealDao().load(id);
    }

    @Override
    public void deleteOrderLineByObject(OrderLine orderLine) {
        getDaoSession().getOrderLineDao().delete(orderLine);

    }


    @Override
    public void saveMeal(Meal meal) {
        getDaoSession().getMealDao().save(meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        getDaoSession().getMealDao().update(meal);
    }

    @Override
    public void deleteMeal(Meal meal) {
        getDaoSession().getMealDao().delete(meal);
    }

    @Override
    public List<Meal> loadMeals() {
        return getDaoSession().getMealDao().loadAll();
    }

    @Override
    public Work getWorkById(Long id) {
        return getDaoSession().getWorkDao().load(id);
    }

    @Override
    public void saveWork(Work work) {
        work.setCreatedAt(System.currentTimeMillis());
        getDaoSession().getWorkDao().save(work);
        List<InterventionJob> interventions = ((SmarttoniContext) ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT)).getInterventionManager().createAllInterventionJob(this, work);
        getDaoSession()
                .getInterventionJobDao()
                .insertInTx(interventions);
    }

    @Override
    public void updateWork(Work work) {
        work.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getWorkDao().update(work);
    }

    @Override
    public void deleteWork(Work work) {
        getDaoSession().getWorkDao().delete(work);
        getDaoSession().getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.WorkId.eq(work.getId()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<Work> loadWorks() {
        return getDaoSession().getWorkDao().loadAll();
    }

    @Override
    public List<Work> loadNotCompletedWorks() {
        return getDaoSession()
                .getWorkDao()
                .queryBuilder()
                .where(WorkDao.Properties.Status.notEq(Work.COMPLETED))
                .list();
    }

    @Override
    public List<Modifier> geModifierByIds(List<Long> ids) {
        return getDaoSession()
                .getModifierDao()
                .queryBuilder()
                .where(ModifierDao.Properties.Id.in(ids)).list();
    }

    @Override
    public Station getStationById(String id) {
        return getDaoSession().getStationDao().load(id);
    }

    @Override
    public void savStation(Station station) {
        getDaoSession().getStationDao().insert(station);
    }

    @Override
    public void updateStation(Station station) {
        getDaoSession().getStationDao().update(station);
    }

    @Override
    public void deleteStation(Station station) {
        getDaoSession().getStationDao().delete(station);
    }

    @Override
    public void deleteStations(List<String> ids) {
        getDaoSession().getStationDao().deleteByKeyInTx();
    }

    @Override
    public void deleteTaskIngredient(String id) {
        getDaoSession()
                .getTaskIngredientDao()
                .queryBuilder()
                .where(TaskIngredientDao.Properties.TaskId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteInterventionById(String id) {
        getDaoSession()
                .getInterventionDao()
                .queryBuilder()
                .where(InterventionDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteStepIngredientById(String id) {
        getDaoSession()
                .getStepIngrediantDao()
                .queryBuilder()
                .where(StepIngrediantDao.Properties.StepId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deletetaskById(String id) {
        getDaoSession()
                .getTaskDao()
                .queryBuilder()
                .where(TaskDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteTaskStepById(String id) {
        getDaoSession()
                .getTaskStepDao()
                .queryBuilder()
                .where(TaskStepDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }


    @Override
    public void deleteRecipeIngredientById(String id) {
        getDaoSession()
                .getRecipeIngredientsDao()
                .queryBuilder()
                .where(RecipeIngredientsDao.Properties.RecipeId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteUserByIdList(List<String> ids) {
        getDaoSession().getUserDao().deleteByKeyInTx();
    }

    @Override
    public void deleteLabelByRestuarantId(String id) {
        getDaoSession().getLabelDao().queryBuilder()
                .where(LabelDao.Properties.RestaurantId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteWebAppDataandUserDataByUserId(String id) {
        getDaoSession().getWebAppDataDao().queryBuilder()
                .where(WebAppDataDao.Properties.UserId.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
        getDaoSession().getUserStationAssignmentDao()
                .queryBuilder()
                .where(UserStationAssignmentDao.Properties.Userid.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<User> getUserByASCName() {
        return getDaoSession().getUserDao().queryBuilder()
                .orderAsc(UserDao.Properties.Name)
                .list();
    }

    @Override
    public List<Work> getWorkByStatus() {
        return getDaoSession().getWorkDao().queryBuilder().where(WorkDao.Properties.Status.in(Work.COMPLETED, Work.STARTED)).list();
    }

    @Override
    public List<Work> getworkByorderLine(Long id) {
        return getDaoSession().getWorkDao().queryBuilder()
                .where(WorkDao.Properties.OrderLineId.eq(id))
                .list();
    }

    public List<ChefActivityLog> getChefActivityByOrderId(String id) {
        return getDaoSession().getChefActivityLogDao().queryBuilder()
                .where(ChefActivityLogDao.Properties.OrderId.eq(id))
                .list();
    }


    @Override
    public void deleteWorksForOrder(String id) {
        getDaoSession().
                getWorkDao()
                .queryBuilder()
                .where(WorkDao.Properties.OrderId.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

        getDaoSession().
                getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.OrderId.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<Station> loadStations() {
        return getDaoSession().getStationDao().loadAll();
    }

    @Override
    public Machine getMachineById(String id) {
        return getDaoSession().getMachineDao().load(id);
    }

    @Override
    public List<Options> getOptionLitem(String key) {
        return getDaoSession()
                .getOptionsDao()
                .queryBuilder()
                .where(OptionsDao.Properties.Key.in(key)).list();

    }

    @Override
    public void saveMachine(Machine machine) {
        getDaoSession().getMachineDao().save(machine);
    }

    @Override
    public void updateMachine(Machine machine) {
        getDaoSession().getMachineDao().update(machine);
    }

    @Override
    public void deleteMachine(Machine machine) {
        getDaoSession().getMachineDao().delete(machine);
    }

    @Override
    public List<Machine> loadMachines() {
        return getDaoSession().getMachineDao().loadAll();
    }

    @Override
    public Task getTaskById(String id) {
        return getDaoSession().getTaskDao().load(id);
    }

    @Override
    public void saveTask(Task task) {
        getDaoSession().getTaskDao().save(task);
    }

    @Override
    public void updateTask(Task task) {
        getDaoSession().getTaskDao().update(task);
    }

    @Override
    public void deleteTask(Task task) {
        getDaoSession().getTaskDao().delete(task);
    }

    @Override
    public void deleteRestaurantSettings() {
        getDaoSession().getRestaurantSettingsDao().deleteAll();
    }

    @Override
    public void deleteStorage() {
        getDaoSession().getStorageDao().deleteAll();
    }

    @Override
    public void deleteAllOptions() {
        getDaoSession().getOptionsDao().deleteAll();
    }

    @Override
    public void deleteAllRights() {
        getDaoSession().getUserRightsDao().deleteAll();
    }

    @Override
    public void deleteAllUser() {
        getDaoSession().getUserDao().deleteAll();
    }

    @Override
    public List<User> getAllUser() {
        return getDaoSession().getUserDao().loadAll();
    }

    @Override
    public boolean getUserRights(String id, String right) {
        return getDaoSession()
                .getUserRightsDao()
                .queryBuilder()
                .where(UserRightsDao.Properties.UserId.in(id))
                .where(UserRightsDao.Properties.Right.in(right))
                .list().size() > 0;
    }

    @Override
    public void deleteRack() {
        getDaoSession().getRackDao().deleteAll();

    }

    @Override
    public void deletePlace() {
        getDaoSession().getPlaceDao().deleteAll();

    }

    @Override
    public void saveUserRight(UserRights userRights) {
        getDaoSession().getUserRightsDao().insert(userRights);

    }

    @Override
    public void saveRestaurantSettings(RestaurantSettings restaurantSettings) {
        getDaoSession().getRestaurantSettingsDao().insert(restaurantSettings);

    }


    @Override
    public List<Task> loadTasks() {
        return getDaoSession().getTaskDao().loadAll();
    }

    @Override
    public List<Task> loadTasks(String recipeId) {
        return getDaoSession()
                .getTaskDao()
                .queryBuilder()
                .where(TaskDao.Properties.RecipeId.eq(recipeId))
                .list();
    }

    @Override
    public Segment getSegmentById(String id) {
        return getDaoSession().getSegmentDao().load(id);
    }

    @Override
    public Segment getSegment(String taskId, int position) {
        return getDaoSession()
                .getSegmentDao()
                .queryBuilder()
                .where(SegmentDao.Properties.TaskId.eq(taskId))
                .where(SegmentDao.Properties.Position.eq(position))
                .unique();
    }

    @Override
    public void saveSegment(Segment segment) {
        getDaoSession().getSegmentDao().insert(segment);
    }

    @Override
    public void updateSegment(Segment segment) {
        getDaoSession().getSegmentDao().update(segment);
    }

    @Override
    public List<Segment> loadSegments() {
        return getDaoSession().getSegmentDao().loadAll();
    }

    @Override
    public List<Segment> loadSegments(String taskId) {
        return getDaoSession()
                .getSegmentDao()
                .queryBuilder()
                .where(SegmentDao.Properties.TaskId.eq(taskId))
                .list();
    }

    @Override
    public void deleteSegmentByTask(String taskId) {
        getDaoSession()
                .getSegmentDao()
                .queryBuilder()
                .where(SegmentDao.Properties.TaskId.eq(taskId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteSegmentById(String id) {
        getDaoSession()
                .getSegmentDao()
                .queryBuilder()
                .where(SegmentDao.Properties.Id.eq(id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void saveRoom(Room room) {
        getDaoSession().getRoomDao().insert(room);
    }

    @Override
    public void deleteAllSegment() {
        getDaoSession().getSegmentDao().deleteAll();
    }

    @Override
    public void deleteRooms() {
        getDaoSession().getRoomDao().deleteAll();
    }

    @Override
    public Task getTaskByRecipeId(String id) {
        return getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.RecipeId.eq(id)).unique();
    }

    @Override
    public Machine getMachineByDescOrder() {
        return getDaoSession().getMachineDao().queryBuilder().orderDesc(MachineDao.Properties.CreatedAt).limit(1).unique();
    }

    @Override
    public RecipeIngredients getRecipeIngredientByRecipeId(String id) {
        return getDaoSession().getRecipeIngredientsDao().queryBuilder().where(RecipeIngredientsDao.Properties.RecipeId.eq(id)).unique();
    }

    @Override
    public Recipe getRecipeByPrinterName(String printerName) {

        List<Recipe> recipeList = getDaoSession().getRecipeDao().queryBuilder().where(RecipeDao.Properties.PrinterName.eq(printerName)).limit(1).list();
        if (recipeList.size() > 0) {
            return recipeList.get(0);
        }
        return null;
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        getDaoSession().getRecipeDao().update(recipe);
    }

    @Override
    public void saveRecipe(List<Recipe> recipes) {
        List<Long> ids = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        if (getDaoSession().
                getTaskDao().load("1") == null) {
            Task t = new Task();
            t.setId("1");
            t.setWorkDuration(1);
            t.setOutputQuantity(1);
            t.setStartBeforeDelivery(true);
            tasks.add(t);
        }
        List<String> taskIds = new ArrayList<>();
        for (Recipe recipe : recipes) {
            recipe.setName(recipe.getName());
            recipe.setDescription(recipe.getDescription());
            recipe.setPrinterName(recipe.getPrinterName());
            recipe.setParentLabel(recipe.getParentLabel());

            if (recipe.getTasks() != null)
                for (Task task : recipe.getTasks()) {
                    task.setName(task.getLocales().get(0).getName());
                    task.setDescription(task.getLocales().get(0).getDescription());
                    task.setRecipeId(recipe.getId());
                    task.setPrevious(task.getDependentTaskCommaSeparated());
                    task.setOutputUnitName(getUnitById(task.getOutputUnit()).getName());
                    tasks.add(task);
                    taskIds.add(task.getId());
                    deleteSegmentByTask(task.getId());
                    if (task.getSegments() != null) {
                        task.setNumberOfSegments(task.getSegments().size());
                        for (Segment segment : task.getSegments()) {
                            segment.setTaskId(task.getId());
                            segment.setTotal(segment.getDuration());
                            segment.setCompleted(1);
                            saveSegment(segment);
                        }
                    }
                }
        }

        getDaoSession().getRecipeDao().insertInTx(recipes);

        getDaoSession().getTaskDao().deleteByKeyInTx(taskIds);
        getDaoSession().
                getTaskDao().
                insertInTx(tasks);

        List<Modifier> modifiers = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<Modifier> m = recipe.getModifiers();
            if (m != null) {
                for (Modifier modifier : m) {
                    ids.add(modifier.getId());
                    modifiers.add(modifier);
                }
            }
        }
        getDaoSession()
                .getModifierDao()
                .queryBuilder()
                .where(ModifierDao.Properties.Id.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
//        getDaoSession().
//                getModifierDao().
//                insertInTx(modifiers);
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        if (getDaoSession().
                getTaskDao().load("1") == null) {
            Task t = new Task();
            t.setId("1");
            t.setWorkDuration(1);
            t.setOutputQuantity(1);
            t.setStartBeforeDelivery(true);
            tasks.add(t);
        }
        getDaoSession()
                .getTaskDao()
                .insertInTx(tasks);
    }

    @Override
    public void saveServiceSets(List<ServiceSet> list) {
        getDaoSession()
                .getServiceSetDao()
                .insertInTx(list);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return  getDaoSession().getRecipeDao().load(id);
    }

    @Override
    public void deleteRecipe(List<String> ids) {
        getDaoSession().getRecipeDao().deleteByKeyInTx(ids);
    }

    @Override
    public void deleteServiceSet(List<String> ids) {
        getDaoSession()
                .getServiceSetDao()
                .queryBuilder()
                .where(ServiceSetDao.Properties.Id.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();


        for(String id : ids){
            getDaoSession()
                    .getServiceSetRecipesDao()
                    .queryBuilder()
                    .where(ServiceSetRecipesDao.Properties.ServiceSetId.eq(id))
                    .buildDelete().executeDeleteWithoutDetachingEntities();

            getDaoSession()
                    .getServiceSetTimingsDao()
                    .queryBuilder()
                    .where(ServiceSetTimingsDao.Properties.ServiceSetId.eq(id))
                    .buildDelete().executeDeleteWithoutDetachingEntities();

        }




    }

    @Override
    public void deleteTasks(List<String> ids) {
        getDaoSession()
                .getTaskDao()
                .queryBuilder()
                .where(TaskDao.Properties.Id.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteSegments(List<String> ids) {
        getDaoSession()
                .getSegmentDao()
                .queryBuilder()
                .where(SegmentDao.Properties.Id.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }


    @Override
    public void deleteAllRecipe() {
        getDaoSession().getRecipeDao().deleteAll();
    }

    @Override
    public List<Recipe> loadRecipes() {
        return getDaoSession().getRecipeDao().queryBuilder().orderAsc(RecipeDao.Properties.Name).list();
    }

    @Override
    public List<Supplier> loadSuppliers() {
        Realm realm = Realm.getDefaultInstance();
        List<Supplier> Supplier = realm.copyFromRealm(realm.where(Supplier.class).sort("name", Sort.ASCENDING).findAll());
        realm.close();
        return Supplier;
    }

    @Override
    public void deleteRecipeIngredient(List<String> ids) {
        getDaoSession()
                .getRecipeIngredientsDao()
                .queryBuilder()
                .where(RecipeIngredientsDao.Properties.Id.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteInterventionStepIng(List<String> ids) {
        getDaoSession()
                .getInterventionStepIngredientDao()
                .queryBuilder()
                .where(StepIngrediantDao.Properties.RecipeUuid.in(ids))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void saveRecipeIngredient(List<RecipeIngredients> recipeIngredients) {
        getDaoSession().getRecipeIngredientsDao().insertInTx(recipeIngredients);
    }

    @Override
    public void saveMachineList(List<Machine> machines) {
        getDaoSession().getMachineDao().insertInTx(machines);
    }


    @Override
    public void saveTaskSteps(List<TaskStep> taskSteps) {
        getDaoSession()
                .getTaskStepDao()
                .insertInTx(taskSteps);
    }

    @Override
    public void saveServiceSetTiming(ServiceSetTimings serviceSetTimings) {
        getDaoSession().getServiceSetTimingsDao().insertInTx(serviceSetTimings);
    }

    @Override
    public void saveServiceSetRecipes(ServiceSetRecipes serviceSetRecipes) {
        getDaoSession().getServiceSetRecipesDao().insertInTx(serviceSetRecipes);
    }

    @Override
    public void saveStepsIngredient(List<StepIngrediant> stepIngrediant) {
        getDaoSession().getStepIngrediantDao().insertInTx(stepIngrediant);
    }

    @Override
    public void saveInterventions(List<Intervention> interventions) {
        getDaoSession()
                .getInterventionDao()
                .insertInTx(interventions);
    }


    @Override
    public void saveTaskIngredients(List<TaskIngredient> taskIngredients) {
        getDaoSession()
                .getTaskIngredientDao()
                .insertInTx(taskIngredients);
    }


    @Override
    public void deleteInterventions(List<String> ids) {
        getDaoSession()
                .getInterventionDao()
                .deleteByKeyInTx(ids);
    }

    @Override
    public void deleteTaskSteps(List<String> ids) {
        getDaoSession()
                .getTaskStepDao()
                .deleteByKeyInTx(ids);
    }


    @Override
    public void deleteAllStations() {
        getDaoSession().getStationDao().deleteAll();
    }

    @Override
    public void deletAllLabels() {
        getDaoSession().getLabelDao().deleteAll();
    }

    @Override
    public void deleteAllWebAppData() {
        getDaoSession().getWebAppDataDao().deleteAll();
    }

    @Override
    public void deleteAllInventory() {
        getDaoSession().getInventoryDao().deleteAll();
    }

    @Override
    public void deleteAllMeals() {
        getDaoSession().getMealDao().deleteAll();
    }

    @Override
    public void deleteAllOrderLine() {
        getDaoSession().getMealDao().deleteAll();
    }

    @Override
    public void deleteAllTaskStep() {
        getDaoSession().getTaskStepDao().deleteAll();
    }

    @Override
    public void deleteAllOrder() {
        getDaoSession().getOrderDao().deleteAll();
    }

    @Override
    public void deleteAllCourses() {
        getDaoSession().getCourseDao().deleteAll();
    }


    @Override
    public void deleteAllUserStationAssignment() {
        getDaoSession().getUserStationAssignmentDao().deleteAll();
    }

    @Override
    public void deleteAllRecipeIngredient() {
        getDaoSession().getRecipeIngredientsDao().deleteAll();
    }

    @Override
    public void deleteAllStepIngredient() {
        getDaoSession().getStepIngrediantDao().deleteAll();
    }

    @Override
    public void deleteAllInterventions() {
        getDaoSession().getInterventionDao().deleteAll();
    }

    @Override
    public void deleteAllTasks() {
        getDaoSession().getTaskDao().deleteAll();
    }

    @Override
    public void deleteAllSteps() {
        getDaoSession().getTaskStepDao().deleteAll();
    }

    @Override
    public void deletePrinterData() {
        getDaoSession().getPrinterDao().deleteAll();
    }

    @Override
    public List<Label> loadLabels() {
        return getDaoSession().getLabelDao().loadAll();
    }

    @Override
    public List<ServiceSet> loadServiceSets() {
        return getDaoSession().getServiceSetDao().loadAll();
    }

    @Override
    public List<Printer> getPrinterData() {
        return getDaoSession().getPrinterDao().loadAll();
    }


    @Override
    public List<Inventory> loadAllInventories() {
        return getDaoSession()
                .getInventoryDao()
                .loadAll();
    }

    @Override
    public List<Inventory> loadAvailableInventories() {
        return getDaoSession()
                .getInventoryDao()
                .queryBuilder()
                .where(InventoryDao.Properties.Quantity.notEq(0.0))
                .list();
    }

    @Override
    public List<Intervention> loadInterventions() {
        return getDaoSession()
                .getInterventionDao()
                .loadAll();
    }

    @Override
    public List<TaskIngredient> getIngredientsForTask(Task task) {
        return getDaoSession().getTaskIngredientDao().queryBuilder().where(TaskIngredientDao.Properties.TaskId.eq(task.getId())).list();
//        List<String> list = new ArrayList<>();
//        for (TaskIngredient taskIngredient : ingredients) {
//            list.add(taskIngredient.getId());
//        }
//        return getRecipeById(list);
    }

    @Override
    public Units getUnitById(String id) {
        return getDaoSession().getUnitsDao().load(id);
    }

    @Override
    public Label loadLabelById(String id) {
        return getDaoSession().getLabelDao().load(id);
    }

    @Override
    public void saveUnit(List<Units> unit) {
        getDaoSession().getUnitsDao().insertInTx(unit);
    }

    @Override
    public void deleteUnits(List<String> ids) {
        getDaoSession().getUnitsDao().deleteByKeyInTx(ids);
    }

    @Override
    public List<Order> loadOrderById(String uuid) {
        return getDaoSession().getOrderDao().queryBuilder()
                .where(OrderDao.Properties.Id.eq(uuid)).list();
    }

    @Override
    public List<ArchivedOrders> loadArchivedOrderById(String uuid) {
        return getDaoSession().getArchivedOrdersDao().queryBuilder()
                .where(ArchivedOrdersDao.Properties.Id.eq(uuid)).list();
    }

    @Override
    public void deletArchivedOrder(ArchivedOrders archivedOrders) {
        getDaoSession().getArchivedOrdersDao().delete(archivedOrders);
    }

    @Override
    public List<Course> loadCourseById(String uuid) {
        return getDaoSession().getCourseDao().queryBuilder()
                .where(CourseDao.Properties.Id.eq(uuid))
                .list();
    }

    @Override
    public List<Course> loadCourseByOrderId(String orderId) {
        return getDaoSession().getCourseDao().queryBuilder().where(CourseDao.Properties.OrderId.eq(orderId)).list();
    }

    @Override
    public List<Units> loadUnits() {
        return getDaoSession().getUnitsDao().loadAll();
    }

    @Override
    public List<Task> getTaskById(List<String> ids) {
        return getDaoSession()
                .getTaskDao()
                .queryBuilder()
                .where(TaskDao.Properties.Id.in(ids))
                .list();
    }

    ///////////////////////////////////////////////////////////////

    @Override
    public void saveOrderLine(OrderLine orderLine) {
        getDaoSession().getOrderLineDao().saveInTx(orderLine);
    }

    @Override
    public List<Order> listOrdersWithOpenStatus() {
        return getDaoSession().getOrderDao().queryBuilder()
                .where(OrderDao.Properties.Status.eq(Order.ORDER_OPEN)).build().list();
    }

    @Override
    public List<Station> stationByName() {
        return getDaoSession().getStationDao().queryBuilder()
                .orderAsc(StationDao.Properties.Name)
                .list();
    }

    @Override
    public List<OrderLine> getFilterdOrderLine(long mealId, String recipeId) {
        return getDaoSession()
                .getOrderLineDao()
                .queryBuilder()
                .where(OrderLineDao.Properties.MealId.eq(mealId))
                .where(OrderLineDao.Properties.RecipeId.eq(recipeId))
                .list();
    }

    @Override
    public String getSupplierNameByOrderId(String orderId) {
        List<OrderLine> orderLines = getDaoSession().getOrderLineDao().queryBuilder().where(OrderLineDao.Properties.OrderId.eq(orderId)).limit(1).list();
        if (orderLines != null && orderLines.size() > 0) {
            String supplierId = orderLines.get(0).getRecipe().getSupplier();
            if (Strings.isNotEmpty(supplierId)) {
                Supplier s = getSupplierById(supplierId);
                if (s != null) {
                    return s.getName();
                }
            }
        }
        return "";
    }

    @Override
    public List<ExternalAvailableQuantity> listExternalAvailableQuantityForRecipe(String recipeId) {
        return getDaoSession()
                .getExternalAvailableQuantityDao()
                .queryBuilder()
                .where(ExternalAvailableQuantityDao.Properties.Recipe.eq(recipeId))
                .list();
    }

    @Override
    public void updateExternalAvailableQuantity(ExternalAvailableQuantity eaq) {
        getDaoSession()
                .getExternalAvailableQuantityDao()
                .update(eaq);
    }

    @Override
    public List<ExternalOrderRequest> listExternalOrderRequestForExternalOrder(String externalOrderId) {
        return getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ExternalOrder.eq(externalOrderId))
                .list();
    }

    @Override
    public List<ExternalOrderRequest> listExternalOrderRequestForOrder(String id) {
        return getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ParentOrder.eq(id))
                .list();
    }

    @Override
    public void deleteExternalOrderRequestForExternalOrder(String externalOrderId) {
        getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ExternalOrder.eq(externalOrderId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<ExternalAvailableQuantity> listExternalAvailableQuantityForOrder(String id) {
        return getDaoSession()
                .getExternalAvailableQuantityDao()
                .queryBuilder()
                .where(ExternalAvailableQuantityDao.Properties.Order.eq(id))
                .list();
    }

    @Override
    public void deleteExternalAvailableQuantityForExternalOrder(String id) {
        getDaoSession()
                .getExternalAvailableQuantityDao()
                .queryBuilder()
                .where(ExternalAvailableQuantityDao.Properties.Order.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteExternalOrderRequestForParentOrder(String id) {
        getDaoSession()
                .getExternalOrderRequestDao()
                .queryBuilder()
                .where(ExternalOrderRequestDao.Properties.ParentOrder.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public List<ExternalAvailableQuantity> listExternalAvailableQuantity(String externalOrder, String recipe) {
        return getDaoSession()
                .getExternalAvailableQuantityDao()
                .queryBuilder()
                .where(ExternalAvailableQuantityDao.Properties.Recipe.eq(recipe))
                .where(ExternalAvailableQuantityDao.Properties.Order.eq(externalOrder))
                .list();
    }

    @Override
    public List<InventoryMovement> listInventoryMovement() {
        return getDaoSession()
                .getInventoryMovementDao()
                .loadAll();
    }

    @Override
    public void deleteInventoryMovements(@NotNull List<InventoryMovement> orders) {
        getDaoSession()
                .getInventoryMovementDao()
                .deleteInTx(orders);
    }

    @Override
    public void insertInventoryMovement(InventoryMovement im) {
        getDaoSession()
                .getInventoryMovementDao()
                .insert(im);
    }

    @Override
    public List<ExternalAvailableQuantity> listExternalAvailableQuantity() {
            return getDaoSession()
                    .getExternalAvailableQuantityDao()
                    .loadAll();

    }

    @Override
    public List<ExternalOrderRequest> listExternalOrderRequest() {
        return getDaoSession()
                .getExternalOrderRequestDao()
                .loadAll();
    }

    @Override
    public List<InventoryReservation> listInventoryReservationsForRecipe(String id) {
        return getDaoSession()
                .getInventoryReservationDao()
                .queryBuilder()
                .where(InventoryReservationDao.Properties.RecipeId.eq(id))
                .list();

    }

    @Override
    public void insertAllExternalAvailableQuantity(List<ExternalAvailableQuantity> eaq) {
        getDaoSession().getExternalAvailableQuantityDao().deleteAll();
        getDaoSession().getExternalAvailableQuantityDao().insertInTx(eaq);
    }

    @Override
    public void insertAllExternalOrderRequest(List<ExternalOrderRequest> eoq) {
        getDaoSession().getExternalOrderRequestDao().deleteAll();
        getDaoSession().getExternalOrderRequestDao().insertInTx(eoq);
    }

    @Override
    public List<OrderLine> getOrderLinesByOrder(String orderId) {
        return getDaoSession().getOrderLineDao().queryBuilder().where(OrderLineDao.Properties.OrderId.eq(orderId)).list();
    }

    @Override
    public void updateInterventionJob(InterventionJob interventionJob) {
        getDaoSession().getInterventionJobDao().update(interventionJob);
    }

    @Override
    public void updateIntervention(Intervention intervention) {
        getDaoSession().getInterventionDao().update(intervention);
    }

    @Override
    public List<InterventionJob> getInterventionJobByUserId(String userId, boolean isStarted) {
        if (isStarted) {
            return getDaoSession()
                    .getInterventionJobDao()
                    .queryBuilder()
                    .where(InterventionJobDao.Properties.UserId.eq(userId), InterventionJobDao.Properties.Status.eq(InterventionJob.STARTED))
                    .list();
        }
        return getDaoSession()
                .getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.UserId.eq(userId), InterventionJobDao.Properties.Status.notIn(InterventionJob.COMPLETED, InterventionJob.QUEUED))
                .orderAsc(InterventionJobDao.Properties.WaitingFrom)
                .list();
    }

    @Override
    public List<InterventionJob> getStartedInterventionJobs() {

        List<InterventionJob> interventions = new ArrayList<>();

        try {
            List<InterventionJob> interventionJobs = getDaoSession().getInterventionJobDao().loadAll();
            for (InterventionJob i : interventionJobs) {
                if (i.getStatus() == InterventionJob.STARTED) {
                    interventions.add(i);
                }
            }
        } catch (Exception e) {
        }
        return interventions;
//        return getDaoSession()
//                .getInterventionJobDao()
//                .queryBuilder()
//                .where(InterventionJobDao.Properties.Status.eq(InterventionJob.STARTED))
//                .list();
    }

    @Override
    public List<InterventionJob> getInterventionJobChilds(InterventionJob interventionJob) {
        List<Intervention> interventions = getDaoSession()
                .getInterventionDao()
                .queryBuilder()
                .where(InterventionDao.Properties.Parent.eq(interventionJob.getInterventionId()))
                .list();
        List<String> interventionIds = new ArrayList<>();
        for (Intervention i : interventions) {
            interventionIds.add(i.getId());
        }
        return getDaoSession()
                .getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.InterventionId.in(interventionIds), InterventionJobDao.Properties.Status.notEq(InterventionJob.COMPLETED))
                .orderAsc(InterventionJobDao.Properties.WaitingFrom)
                .list();
    }

    @Override
    public List<InterventionJob> getInterventionJobs(Work work) {
        List<Intervention> interventions = getDaoSession()
                .getInterventionDao()
                .queryBuilder()
                .where(InterventionDao.Properties.Parent.isNull(),
                        InterventionDao.Properties.TaskId.eq(work.getTaskId()),
                        InterventionDao.Properties.InterventionPosition.eq(1))
                .list();
        List<String> interventionIds = new ArrayList<>();
        for (Intervention i : interventions) {
            interventionIds.add(i.getId());
        }

        List<InterventionJob> interventionJobs = getDaoSession()
                .getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.WorkId.eq(work.getId()),
                        InterventionJobDao.Properties.InterventionId.in(interventionIds),
                        InterventionJobDao.Properties.Status.notEq(InterventionJob.COMPLETED))
                .orderAsc(InterventionJobDao.Properties.WaitingFrom)
                .list();

        List<Long> jobIds = new ArrayList<>();
        for (InterventionJob i : interventionJobs) {
            jobIds.add(i.getId());
        }

        interventionJobs.addAll(getDaoSession()
                .getInterventionJobDao()
                .queryBuilder()
                .where(InterventionJobDao.Properties.WorkId.eq(work.getId()),
                        InterventionJobDao.Properties.Id.notIn(jobIds),
                        InterventionJobDao.Properties.StartedAt.isNotNull(),
                        InterventionJobDao.Properties.Status.eq(InterventionJob.QUEUED))
                .list());

        return interventionJobs;
    }

    @Override
    public List<InterventionJob> getWaitingInterventionJobs() {
        List<InterventionJob> interventions = new ArrayList<>();

        try {
            List<InterventionJob> interventionJobs = getDaoSession().getInterventionJobDao().loadAll();
            for (InterventionJob i : interventionJobs) {
                if (i.getStatus() == InterventionJob.WAITING) {
                    interventions.add(i);
                }
            }
        } catch (Exception e) {
        }
        return interventions;
//
//
//        return getDaoSession()
//                .getInterventionJobDao()
//                .queryBuilder()
//                .where(InterventionJobDao.Properties.Status.eq(InterventionJob.WAITING))
//                .list();
    }

    @Override
    public InterventionJob getInterventionJobById(long id) {
        return getDaoSession().getInterventionJobDao().load(id);
    }

    @Override
    public Intervention getInterventionByUuid(String id) {
        return getDaoSession().getInterventionDao().queryBuilder()
                .where(InterventionDao.Properties.Id.eq(id)).unique();
    }

    @Override
    public void deleteInterventionJobById(long id) {
        getDaoSession().getInterventionJobDao().deleteByKey(id);
    }

    @Override
    public void saveActivityLog(ChefActivityLog log) {
        log.setCreatedAt(System.currentTimeMillis());
        getDaoSession()
                .getChefActivityLogDao()
                .insert(log);
    }

    @Override
    public boolean isUserAssignedInStation(String userId, String stationId) {
        if (stationId == null || stationId.equals("")) {
            return true;
        }
        UserStationAssignment userStationAssignment = getDaoSession()
                .getUserStationAssignmentDao().queryBuilder()
                .where(UserStationAssignmentDao.Properties.Userid.eq(userId))
                .where(UserStationAssignmentDao.Properties.Stationid.eq(stationId))
                .unique();
        return userStationAssignment != null;
    }

    @Override
    public void saveUserStationAssignmentDao(UserStationAssignment userstationassignment) {
        userstationassignment.setCreatedAt(System.currentTimeMillis());
        getDaoSession().getUserStationAssignmentDao().save(userstationassignment);
    }

    @Override
    public void updateUserStationAssignmentDao(UserStationAssignment userstationassignment) {
        userstationassignment.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getUserStationAssignmentDao().update(userstationassignment);
    }

    @Override
    public List<UserStationAssignment> loadUserStationAssignmentDao() {
        return getDaoSession().getUserStationAssignmentDao().loadAll();
    }

    @Override
    public UserStationAssignment getUserStationAssignmentDao(Long id) {
        return getDaoSession().getUserStationAssignmentDao().load(id);
    }

    @Override
    public void savePrinter(Printer printer) {
        getDaoSession().getPrinterDao().insert(printer);

    }

    @Override
    public void saveRecipeCategoryDao(Label recipecategory) {
        getDaoSession().getLabelDao().insert(recipecategory);
    }

    @Override
    public void saveOptionsDao(Options options) {
        getDaoSession().getOptionsDao().save(options);
    }

    @Override
    public void saveStationDao(Station station) {
        getDaoSession().getStationDao().insert(station);
    }

    @Override
    public List<StationTask> loadStationTaskDao() {
        return getDaoSession().getStationTaskDao().loadAll();
    }

    @Override
    public void saveOrderDao(Order order) {
        getDaoSession().getOrderDao().save(order);
    }

    @Override
    public void updateOrderDao(Order order) {
        getDaoSession().getOrderDao().update(order);
    }

    @Override
    public void saveInventoryDao(Inventory inventory) {
        inventory.setCreatedAt(System.currentTimeMillis());
        getDaoSession().getInventoryDao().insert(inventory);
    }

    @Override
    public void updateInventoryDao(Inventory inventory) {
        inventory.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getInventoryDao().update(inventory);
    }

    @Override
    public void updatePrinterDao(PrinterData printerData) {
        printerData.setUpdatedAt(System.currentTimeMillis());
        getDaoSession().getPrinterDataDao().update(printerData);
    }

    @Override
    public void updateChefActivityDao(ChefActivityLog chefActivityLog) {
        getDaoSession().getChefActivityLogDao().update(chefActivityLog);
    }


    @Override
    public Inventory getFilteredInventory(String recipeId) {
        return getDaoSession()
                .getInventoryDao()
                .queryBuilder()
                .where(InventoryDao.Properties.RecipeUuid.eq(recipeId))
                .unique();
    }

    @Override
    public ChefActivityLog getLastActivityOfWorkStarted(Work work) {
        QueryBuilder<ChefActivityLog> qb = getDaoSession().getChefActivityLogDao().queryBuilder();
        ChefActivityLog log = qb
                .where(ChefActivityLogDao.Properties.WorkId.eq(work.getId()), ChefActivityLogDao.Properties.Status.eq(Work.STARTED))
                .orderAsc(ChefActivityLogDao.Properties.CreatedAt).limit(1).unique();
        return log;
    }

    @Override
    public List<Order> getUpdatedOrders() {
        return getDaoSession().getOrderDao().queryBuilder()
                .where(OrderDao.Properties.Modification.gt(0)).list();
    }

    @Override
    public void setAsUsed(long orderLineId, String recipeId) {
        List<Work> works =
                getDaoSession()
                        .getWorkDao()
                        .queryBuilder()
                        .where(WorkDao.Properties.OrderLineId.eq(orderLineId))
                        .where(WorkDao.Properties.RecipeId.eq(recipeId))
                        .where(WorkDao.Properties.IsEndNode.eq(true))
                        .where(WorkDao.Properties.IsUsed.eq(false))
                        .list();
        if (works != null && works.size() > 0) {
            Work work = works.get(0);
            work.setIsUsed(true);
            updateWork(work);
        }
    }

    @Override
    public List<Work> getAllUnusedWorkEndNodes(String orderId) {
        return getDaoSession()
                .getWorkDao()
                .queryBuilder()
                .where(WorkDao.Properties.OrderId.eq(orderId))
                .list();
    }

    @Override
    public List<User> getUsersForStation(String stationId) {
        List<UserStationAssignment> userStationAssignments = getDaoSession()
                .getUserStationAssignmentDao()
                .queryBuilder()
                .where(UserStationAssignmentDao.Properties.Stationid.eq(stationId))
                .list();

        List<User> list = new ArrayList<>();
        for (UserStationAssignment stationAssignment : userStationAssignments) {
            list.add(stationAssignment.getUser());
        }
        return list;
    }

    @Override
    public void deleteWorks(String orderUUID) {
        try {
            Order order = getDaoSession()
                    .getOrderDao()
                    .queryBuilder()
                    .where(OrderDao.Properties.Id.eq(orderUUID))
                    .unique();
            if (order != null) {
                getDaoSession()
                        .getWorkDao()
                        .queryBuilder()
                        .where(WorkDao.Properties.OrderId.eq(order.getId()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();

                getDaoSession()
                        .getInterventionJobDao()
                        .queryBuilder()
                        .where(InterventionJobDao.Properties.OrderId.eq(order.getId()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void deleteAllAndSave(List<Inventory> inventories) {
        getDaoSession().getInventoryDao().deleteAll();
        getDaoSession().getInventoryDao().insertOrReplaceInTx(inventories);
        //TODO
//        if(inventories != null){
//            Map<String,String> s = new HashMap<>();
//            for(Inventory inventory:inventories){
//                if(s.get(inventory.getId()) == null){
//                    s.put(inventory.getId(),inventory.getRecipeName());
//                }else{
//                    String ss = s.get(inventory.getId());
//                    int a =10;
//                    int b = 20;
//                    int c = a+b;
//                }
//                getDaoSession().getInventoryDao().insert(inventory);
//            }
//        }
    }

    @Override
    public void insertTransportRoutes(List<TransportRoute> transportRoutes) {
        getDaoSession().getTransportRouteDao().deleteAll();
        getDaoSession().getTransportRouteDao().insertInTx(transportRoutes);
    }

    @Override
    public TransportRoute getTransportRoute(String route) {
        List<TransportRoute> list = getDaoSession().getTransportRouteDao().queryBuilder()
                .where(TransportRouteDao.Properties.Key.eq(route))
                .limit(1)
                .list();
        return list.size() > 0 ? list.get(0) : null;

    }

    @Override
    public List<Place> loadPlaces() {
        return getDaoSession().getPlaceDao().loadAll();
    }

    @Override
    public List<Rack> loadRacks() {
        return getDaoSession().getRackDao().loadAll();
    }

    @Override
    public List<Storage> loadStorage() {
        return getDaoSession().getStorageDao().loadAll();
    }

    @Override
    public List<Room> loadRooms() {
        return getDaoSession().getRoomDao().loadAll();
    }

    @Override
    public List<Order> loadUnprocessedOrders() {
        return getDaoSession().getOrderDao().queryBuilder()
                .where(OrderDao.Properties.Status.eq(Order.ORDER_OPEN))
                .where(OrderDao.Properties.Processed.eq(false))
                .list();
    }

    @Override
    public List<Storage> loadStorageById(String id) {
        return getDaoSession().getStorageDao().queryBuilder()
                .where(StorageDao.Properties.Id.eq(id)).list();
    }

    @Override
    public void saveSuppliers(List<Supplier> suppliers) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(suppliers);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Supplier getSupplierById(String id) {
        Realm realm = Realm.getDefaultInstance();
        Supplier supplier = realm.where(Supplier.class).equalTo("id", id).findFirst();
        if (supplier != null) {
            supplier =  realm.copyFromRealm(supplier);
        }
        realm.close();
        return supplier;
    }

    @Override
    public void deleteSupplier(List<String> ids) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(Supplier.class).in("id", ids.toArray(new String[0])).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public Printer getPrinterDataById(String printerUuid) {
        return getDaoSession().getPrinterDao().queryBuilder()
                .where(PrinterDao.Properties.UuId.eq(printerUuid)).unique();
    }

    @Override
    public void saveInventoryRequirement(String orderId, String recipeId, float required) {

        QueryBuilder<IngredientRequirement> qb = getDaoSession()
                .getIngredientRequirementDao()
                .queryBuilder()
                .where(IngredientRequirementDao.Properties.OrderId.eq(orderId))
                .where(IngredientRequirementDao.Properties.RecipeId.eq(recipeId));

        List<IngredientRequirement> list =  qb.list();

        if (list.size() == 0 ) {
            IngredientRequirement ir = new IngredientRequirement();
            ir.setOrderId(orderId);
            ir.setRecipeId(recipeId);
            ir.setQuantity(required);
            getDaoSession()
                    .getIngredientRequirementDao()
                    .insert(ir);
        }else{
            IngredientRequirement ir = list.get(0);
            ir.setQuantity(ir.getQuantity()+required);
            getDaoSession()
                    .getIngredientRequirementDao()
                    .update(ir);
        }
    }


    @Override
    public void useInventoryRequirement(String orderId, String recipeId, float used) {

        QueryBuilder<IngredientRequirement> qb = getDaoSession()
                .getIngredientRequirementDao()
                .queryBuilder()
                .where(IngredientRequirementDao.Properties.OrderId.eq(orderId))
                .where(IngredientRequirementDao.Properties.RecipeId.eq(recipeId));


        //qb.and(IngredientRequirementDao.Properties.OrderId.eq(orderId),IngredientRequirementDao.Properties.RecipeId.eq(recipeId));

        List<IngredientRequirement> list =  qb.list();

        if(list.size() > 0 ){
            IngredientRequirement ir = list.get(0);
            float newQty = ir.getQuantity() - used ;
            ir.setQuantity(newQty);
            getDaoSession()
                    .getIngredientRequirementDao()
                    .update(ir);
        }
    }

    @Override
    public void removeIngredientRequirementForOrder(String orderId){

        getDaoSession()
                .getIngredientRequirementDao()
                .queryBuilder()
                .where(IngredientRequirementDao.Properties.OrderId.eq(orderId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

    }

    @Override
    public void saveIngredientExtras(Long workId, String recipeId, float extra){

        IngredientExtras ie =  new IngredientExtras();
        ie.setWorkId(workId);
        ie.setRecipeId(recipeId);
        ie.setQuantity(extra);

        getDaoSession().getIngredientExtrasDao().insert(ie);

    }

    @Override
    public void moveToInventoryAndDeleteExtras(String orderId,Long workId){

        IngredientExtrasDao ingredientExtrasDao = getDaoSession().getIngredientExtrasDao();

        List<IngredientExtras> extrasList = ingredientExtrasDao
                .queryBuilder()
                .where(IngredientExtrasDao.Properties.WorkId.eq(workId))
                .list();

        for(IngredientExtras extra : extrasList){
            useInventoryRequirement(orderId, extra.getRecipeId(), extra.getQuantity());
            InventoryManagement.moveToInventory(orderId,extra.getRecipeId(), extra.getQuantity());
            ingredientExtrasDao.delete(extra);
        }
    }

    @Override
    public void putBackIngredientRequirementToInventory(String orderId){

        List<IngredientRequirement> list = getDaoSession()
                .getIngredientRequirementDao()
                .queryBuilder()
                .where(IngredientRequirementDao.Properties.OrderId.eq(orderId))
                .list();



        for(IngredientRequirement ir:list){
            InventoryManagement.moveToInventory(orderId,ir.getRecipeId(), ir.getQuantity());
        }

    }
}
