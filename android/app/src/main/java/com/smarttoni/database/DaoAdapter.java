package com.smarttoni.database;

import com.smarttoni.entities.ArchivedOrder;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.ChefActivityLog;
import com.smarttoni.entities.Course;
import com.smarttoni.entities.ExternalAvailableQuantity;
import com.smarttoni.entities.ExternalOrderRequest;
import com.smarttoni.entities.IngredientExtras;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryMovement;
import com.smarttoni.entities.InventoryRequest;
import com.smarttoni.entities.InventoryReservation;
import com.smarttoni.entities.Label;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Meal;
import com.smarttoni.entities.Modifier;
import com.smarttoni.entities.Options;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderLine;
import com.smarttoni.entities.Place;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.entities.Rack;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.RecipeIngredients;
import com.smarttoni.entities.RestaurantSettings;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Segment;
import com.smarttoni.entities.ServiceSet;
import com.smarttoni.entities.ServiceSetRecipes;
import com.smarttoni.entities.ServiceSetTimings;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.StationTask;
import com.smarttoni.entities.StepIngrediant;
import com.smarttoni.entities.Storage;
import com.smarttoni.entities.Supplier;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.TaskIngredient;
import com.smarttoni.entities.TaskStep;
import com.smarttoni.entities.TransportRoute;
import com.smarttoni.entities.UnitConversion;
import com.smarttoni.entities.Units;
import com.smarttoni.entities.User;
import com.smarttoni.entities.UserRights;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.entities.WebAppData;
import com.smarttoni.entities.Work;
import com.smarttoni.sync.orders.SyncOrder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DaoAdapter {


    void clear();

    //User
    User getUserById(String id);

    Label loadLabelById(String id);

    User getUserByEmail(String email);

    User getUserByToken(String token);

    void saveUser(User user);

    void saveStorage(Storage storage);

    void saveUser(List<User> user);

    void updateUser(User user);

    void deleteUser(User user);

    List<User> loadUsers();

    List<User> loadChefs();

    //Order
    Order getOrderById(String id);

    List<Work> getWorkByStatus();

    void deleteWorksForOrder(String id);

    List<UserRights> getUserRightsById(String id);

    List<UserStationAssignment> loadUserStationAssignmentById(String id);

    void deleteWebAppDatUserDataByUserId(String id);

    void saveOrder(Order order);

    List<ArchivedOrders> loadArchivedOrderById(String uuid);

    void deletArchivedOrder(ArchivedOrders archivedOrders);

    void saveWebAppData(WebAppData webAppData);

    void saveOrder(Order order, boolean fromWeb);

    void updateOrder(Order order);

    void deleteOrder(Order order);

    boolean deleteOrder(SyncOrder order);

    List<Order> loadOrders();

    List<Order> loadnonArchivedOrders();

    List<ArchivedOrders> loadArchivedOrders();

    List<PrinterData> loadUnUpdatedPrinterData();

    List<Inventory> loadUnUpdatedInventory();

    List<ChefActivityLog> loadUnUpdatedChefActivity();

    List<UserStationAssignment> loadUnUpdatedUserStation();

    List<Work> loadUnUpdatedWorks();


    void setOrderStarted(String orderId);

    List<Modifier> getModifierList();

    List<Storage> loadStorageById(String id);

    List<Work> getworkByorderLine(Long id);

    List<ChefActivityLog> getChefActivityByOrderId(String id);

    //Course
    Course getCourseById(String id);

    void saveCourse(Course course);

    void updateCourse(Course course);

    void deleteCourse(Course course);

    List<Course> loadCourses();

    List<Course> loadCourses(String orderId);

    void deleteuserStationAssignment(String id);

    List<Order> listOrdersWithOpenStatus();

    Inventory getInventoryById(String id);

    List<Station> stationByName();

    RestaurantSettings getRestaurantSettings();

    WebAppData getWebAppCredentials(String id);

    UserStationAssignment loadUserStationAssignmentByIds(String stationId, String id);

    TaskStep loadStepsById(String id);

    void updateSteps(TaskStep taskStep);

    Intervention getInterventionByUuid(String id);

    //Meal
    Meal getMealById(Long id);

    void saveMeal(Meal meal);

    void updateMeal(Meal meal);

    void deleteMeal(Meal meal);

    List<Meal> loadMeals();

    Printer getPrinterDataById(String id);

    //Work
    Work getWorkById(Long id);

    void saveWork(Work work);

    void updateWork(Work work);

    void deleteWork(Work work);

    List<Work> loadWorks();

    List<Work> loadNotCompletedWorks();

    //Modifier
    List<Modifier> geModifierByIds(List<Long> ids);

    //Stations
    Station getStationById(String id);

    void savStation(Station station);

    void updateStation(Station station);

    void deleteStation(Station station);

    void deleteStations(List<String> ids);

    void deleteUserByIdList(List<String> ids);

    List<Station> loadStations();

    //Machines
    Machine getMachineById(String id);

    List<Options> getOptionLitem(String Key);

    void saveMachine(Machine machine);

    void saveMachineList(List<Machine> machines);

    void savePlaces(Place place);

    void saveRack(Rack rack);


    void updateMachine(Machine machine);

    void deleteMachine(Machine machine);

    void deleteStorage();

    void deleteRack();

    void deletePlace();

    void deleteAllOptions();

    void deleteAllRights();

    void deleteAllUser();


    List<User> getAllUser();

    boolean getUserRights(String id, String right);

    List<Machine> loadMachines();

    //Task
    Task getTaskById(String id);

    Task getTaskByRecipeId(String id);

    void saveTask(Task task);

    void saveUserRight(UserRights userRights);

    void updateTask(Task task);

    void deleteTask(Task task);

    List<Task> loadTasks();

    List<Task> loadTasks(String recipeId);

    //Task
    Segment getSegmentById(String id);

    Segment getSegment(String taskId, int position);

    void saveSegment(Segment segment);

    void updateSegment(Segment segment);

    List<Segment> loadSegments();

    List<Segment> loadSegments(String taskId);

    void deleteSegmentByTask(String taskId);

    void deleteSegmentById(String id);

    void deleteAllSegment();

    //Recipe
    Recipe getRecipeById(String id);

    Recipe getRecipeByPrinterName(String printerName);

    RecipeIngredients getRecipeIngredientByRecipeId(String printerName);

    void updateRecipe(Recipe recipe);

    void saveRecipe(List<Recipe> recipes);

    void deleteInterventionById(String Id);

    void saveTasks(List<Task> tasks);

    void saveServiceSets(List<ServiceSet> list);

    void deleteTaskIngredient(String id);

    void deletetaskById(String id);

    void deleteRecipeIngredientById(String id);

    void deleteTaskStepById(String id);

    void deleteStepIngredientById(String id);

    void deleteRecipe(List<String> ids);

    void deleteServiceSet(List<String> ids);

    void deleteTasks(List<String> ids);

    void deleteSegments(List<String> ids);

    void deleteAllRecipe();

    void deleteRecipeIngredient(List<String> ids);

    void deleteInterventionStepIng(List<String> ids);

    void saveRecipeIngredient(List<RecipeIngredients> recipeIngredients);

    void saveTaskSteps(List<TaskStep> taskStep);

    void saveServiceSetTiming(ServiceSetTimings serviceSetTimings);

    void saveServiceSetRecipes(ServiceSetRecipes serviceSetRecipes);

    void saveStepsIngredient(List<StepIngrediant> stepIngrediant);

    void saveInterventions(List<Intervention> interventions);

    void saveTaskIngredients(List<TaskIngredient> taskIngredients);


    void deleteInterventions(List<String> ids);

    void deleteTaskSteps(List<String> ids);

    //void saveInterventionSteps(InterventionSteps interventionSteps);

    List<Recipe> loadRecipes();

    List<Supplier> loadSuppliers();

    List<TaskIngredient> getIngredientsForTask(Task task);

    void saveUserStationAssignmentDao(UserStationAssignment userstationassignment);

    void updateUserStationAssignmentDao(UserStationAssignment userstationassignment);

    List<UserStationAssignment> loadUserStationAssignmentDao();

    UserStationAssignment getUserStationAssignmentDao(Long id);

    //Printer

    void savePrinter(Printer printer);

    //RecipeCategoryDao

    void saveRecipeCategoryDao(Label recipecategory);

    //OptionsDao

    void saveOptionsDao(Options options);

    //StationDao

    void saveStationDao(Station station);

    List<StationTask> loadStationTaskDao();

    //OrderDao
    void saveOrderDao(Order order);

    void saveArchivedOrders(ArchivedOrders order);

    void updateOrderDao(Order order);

    //InventoryDao
    void saveInventoryDao(Inventory inventory);

    void updateInventoryDao(Inventory inventory);

    void updatePrinterDao(PrinterData printerData);

    void updateChefActivityDao(ChefActivityLog chefActivityLog);

    Inventory getFilteredInventory(String recipeId);

    //Unit
    Units getUnitById(String id);

    void saveUnit(List<Units> unit);

    void deleteUnits(List<String> ids);

    List<Units> loadUnits();

    List<Task> getTaskById(List<String> ids);

    List<OrderLine> getOrderLinesByOrder(String id);

    List<OrderLine> getFilterdOrderLine(long mealId, String recipeId);

    void saveOrderLine(OrderLine orderLine);

    //Label

    List<Label> loadLabels();

    List<ServiceSet> loadServiceSets();
    //Intervention

    List<Intervention> loadInterventions();

    void updateInterventionJob(InterventionJob interventionJob);

    void updateIntervention(Intervention intervention);

    List<InterventionJob> getInterventionJobByUserId(String userId, boolean isStarted);

    List<InterventionJob> getStartedInterventionJobs();

    List<InterventionJob> getInterventionJobChilds(InterventionJob interventionJob);

    List<InterventionJob> getInterventionJobs(Work work);

    List<InterventionJob> getWaitingInterventionJobs();

    InterventionJob getInterventionJobById(long id);

    void deleteInterventionJobById(long id);

    void saveActivityLog(ChefActivityLog log);

    boolean isUserAssignedInStation(String userId, String stationId);

    ChefActivityLog getLastActivityOfWorkStarted(Work work);

    List<Order> getUpdatedOrders();

    void deleteAllStations();

    void deleteAllOrder();

    void deleteAllCourses();

    void deleteAllUserStationAssignment();

    void deleteAllRecipeIngredient();


    void deleteAllMeals();

    void deleteAllOrderLine();

    void deletePrinterData();

    void deleteAllTaskStep();


    void deletAllLabels();

    void deleteAllWebAppData();

    void deleteAllInventory();

    List<User> getUserByASCName();

    List<Inventory> loadAvailableInventories();

    void deleteLabelByRestuarantId(String id);

    void deleteWebAppDataandUserDataByUserId(String id);

    List<Printer> getPrinterData();

    void deleteAllInterventions();

    void deleteAllStepIngredient();

    void deleteAllTasks();

    void deleteAllSteps();

    void setAsUsed(long orderLineId, String recipeId);

    List<Work> getAllUnusedWorkEndNodes(String orderId);

    List<Order> loadOrderById(String orderId);

    List<Course> loadCourseById(String courseId);

    List<Course> loadCourseByOrderId(String orderId);

    List<Meal> loadMealById(String id);

    List<Meal> getmealByCourseId(String courseId);

    void deleteRestaurantSettings();

    void deleteRooms();

    void saveRoom(Room room);

    Machine getMachineByDescOrder();

    void saveRestaurantSettings(RestaurantSettings restaurantSettings);

    OrderLine getOrdeLineById(String orderId, Long mealId, String recipeId);

    List<OrderLine> loadOrderLineListById(String id);

    void deleteOrderLineByObject(OrderLine orderLine);

    List<OrderLine> getordeLineByMealId(Long id);

    List<User> getUsersForStation(String stationId);

    void deleteWorks(String orderUUID);

    void deleteAllAndSave(List<Inventory> inventories);

    void insertTransportRoutes(List<TransportRoute> transportRoutes);

    TransportRoute getTransportRoute(String route);


    List<Place> loadPlaces();

    List<Rack> loadRacks();

    List<Storage> loadStorage();

    List<Room> loadRooms();

    void saveSuppliers(List<Supplier> suppliers);

    Supplier getSupplierById(String id);

    void deleteSupplier(List<String> ids);

    List<Order> loadUnprocessedOrders();

    void updateOrderWithNoModification(Order order);

    //TODO List<Order> loadIncompleteChildOrders(String parentOrderId);

    boolean hasWaitingInventoryRequest(String orderId);

    List<InventoryRequest> loadAllInventoryRequests();

    void deleteInventoryRequests(InventoryRequest inventoryRequest);

    void saveInventoryRequest(InventoryRequest inventoryRequest);

    void addExternalOrderRequest(String parentOrderId, String externalOrderId, String recipeId, float qty);

    void addExternalAvailableQuantity(String externalOrderId, String recipeId, float qty);

    ExternalAvailableQuantity getExternalAvailableQuantity(String externalOrderId);

    List<Order> loadChildOrders(String parentOrderId);

    List<Order> loadParentOrders(String childOrderId);

    void saveInventoryReservation(InventoryReservation inventoryReservation);

    void removeInventoryReservations(String orderId);

    List<InventoryReservation> listInventoryReservations(String orderId);

    List<InventoryReservation> listInventoryReservations(String orderId, String recipeId);

    void writeLog(String tag, String value);

    void saveUnitConversions(List<UnitConversion> conversions);


    List<UnitConversion> listUnitConversions();

    List<UnitConversion> listUnitConversions(String fromUnitId);

    //void deleteServiceSetRecipes(String id);

    //void deleteServiceSetTimings(String id);


    List<Inventory> loadAllInventories();

    String getSupplierNameByOrderId(String orderId);

    List<Printer> getPrinterList();

    List<ExternalAvailableQuantity> listExternalAvailableQuantityForRecipe(String recipeId);

    void updateExternalAvailableQuantity(ExternalAvailableQuantity eaq);

    List<ExternalOrderRequest> listExternalOrderRequestForExternalOrder(String externalOrderId);

    List<ExternalOrderRequest> listExternalOrderRequestForOrder(String id);

    void deleteExternalOrderRequestForExternalOrder(String externalOrderId);

    List<ExternalAvailableQuantity> listExternalAvailableQuantityForOrder(String id);

    void deleteExternalAvailableQuantityForExternalOrder(String id);

    void deleteExternalOrderRequestForParentOrder(String id);

    List<ExternalAvailableQuantity> listExternalAvailableQuantity(String externalOrder, String recipe);


    List<InventoryMovement> listInventoryMovement();

    void deleteInventoryMovements(@NotNull List<InventoryMovement> orders);

    void insertInventoryMovement(InventoryMovement im);

    List<ExternalAvailableQuantity> listExternalAvailableQuantity();

    List<ExternalOrderRequest> listExternalOrderRequest();

    List<InventoryReservation> listInventoryReservationsForRecipe(String id);

    void insertAllExternalAvailableQuantity(List<ExternalAvailableQuantity> eaq);

    void insertAllExternalOrderRequest(List<ExternalOrderRequest> eoq);


    void saveInventoryRequirement(String orderId, String recipeId, float required);

    void useInventoryRequirement(String orderId, String recipeId, float used);

    void removeIngredientRequirementForOrder(String orderId);

    void saveIngredientExtras(Long workId, String recipeId, float extra);

    void moveToInventoryAndDeleteExtras(String orderId,Long workId);

    void putBackIngredientRequirementToInventory(String orderId);

    void saveArchivedOrder(ArchivedOrder order);

    List<ArchivedOrder> listArchivedOrder();
}
