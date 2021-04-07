package com.smarttoni.http;

import com.google.gson.JsonObject;
import com.smarttoni.entities.ChefActivityLog;
import com.smarttoni.entities.Intervention;
import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.InventoryMovement;
import com.smarttoni.entities.Label;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.entities.Room;
import com.smarttoni.entities.Segment;
import com.smarttoni.entities.ServiceSetSyncWrapper;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Storage;
import com.smarttoni.entities.StoreRequest;
import com.smarttoni.entities.Supplier;
import com.smarttoni.entities.Tag;
import com.smarttoni.http.models.QRRequest;
import com.smarttoni.http.models.QRResponse;
import com.smarttoni.http.models.ServerResponse;
import com.smarttoni.models.SyncData;
import com.smarttoni.models.SyncWarpper;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.UserStationAssignment;
import com.smarttoni.models.WebLoginModel;
import com.smarttoni.entities.Work;
import com.smarttoni.models.ServerStartRequest;
import com.smarttoni.models.ServerStartResponse;
import com.smarttoni.models.TransportSheet;
import com.smarttoni.models.UnitSync;
import com.smarttoni.models.Chat;
import com.smarttoni.server_app.models.UsersResponse.InventoryList;
import com.smarttoni.server_app.models.UsersResponse.UsersResponse;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.sync.orders.SyncOrderWrapper;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpService {

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/recipes")
    Call<SyncWarpper> syncRecipes(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/units")
    Call<UnitSync> syncUnits(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/tags")
    Call<SyncData<Tag>> syncTags(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/storage")
    Call<List<Storage>> syncStorage(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/rooms")
    Call<List<Room>> syncRoom(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/stations")
    Call<List<Station>> syncStations(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/users")
    Call<List<UsersResponse>> syncUsers(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/printers")
    Call<List<Printer>> getPrinterConfig(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/transport-tasks")
    Call<List<TransportSheet>> syncTransportSheet(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @FormUrlEncoded
    @POST("api/1.0/mobile/{restaurant_uuid}/save-printer-message")
    Call<JsonObject> storePrinterMessage(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body JsonObject messages);

    @POST("api/1.0/mobile/{restaurant_uuid}/sync/inventory")
    Call<List<InventoryList>> updateInventory(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body List<InventoryList> inventories);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/machines")
    Call<SyncData<Machine>> syncMachines(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpsdated);


    @GET("api/1.0/mobile/{restaurant_uuid}/sync/settings")
    Call<JsonObject> synSettings(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/labels")
    Call<List<Label>> syncLabels(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/service-sets")
    Call<ServiceSetSyncWrapper> syncServiceSet(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpsdated);

    @POST("api/1.0/mobile/{restaurant_uuid}/incremental-upload")
    Call<ResponseBody> uploadIncrementalDb(
            @Path(value = "restaurant_uuid", encoded = true) String restaurantId,@Query("printer_data") List<PrinterData> printerData,
            @Query("inventory") List<Inventory> inventories,  @Query("task_queue") List<Work> works,@Query("user_station_assignment") List<UserStationAssignment> userStationAssignments,
            @Query("chef_activity_log") List<ChefActivityLog> chefActivityLogs
    );

    @POST("api/1.0/mobile/{restaurant_uuid}/sync/orders")
    Call<SyncOrderWrapper> syncOrders(@Path(value = "restaurant_uuid", encoded = true) String restaurantId,  @Query("initialSync") boolean initialSync, @Body SyncOrderWrapper orders);


    @FormUrlEncoded
    @POST("api/1.0/chat/relay")
    Call<Chat> chat(@Field("query") String query, @Field("session") String session);

    @GET("api/1.0/mobile/{restaurant_uuid}/orders/load")
    Call<List<SyncOrder>> loadOrders(@Path(value = "restaurant_uuid", encoded = true) String restaurantId,  @Query("reference") String uuid, @Query("pageSize") int pageSize, @Query("externalOrder") boolean externalOrder);

    @POST("public/user/login")
    Call<WebLoginModel> syncWebData(
            @Body JsonObject data
    );
    @POST("api/1.0/mobile/{restaurant_uuid}/add/ingredient")
    Call<JsonObject> addNewIngredient(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body JsonObject data);

    @GET("public/update/check")
    Call<StoreRequest> checkUpdate();

    @POST("api/1.0/mobile/{restaurant_uuid}/sync/interventions")
    Call<Void> syncInterventionsToWeb(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated, @Body List<Intervention> interventions);

    @POST("api/1.0/mobile/{restaurant_uuid}/sync/segments")
    Call<Void> syncSegmentsToWeb(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated, @Body List<Segment> segments);

    @POST("api/1.0/mobile/{restaurant_uuid}/server/start")
    Call<ServerStartResponse> startServer(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body ServerStartRequest serverStartRequest);

    @POST("api/1.0/mobile/{restaurant_uuid}/server/stop")
    Call<ServerStartResponse> stopServer(@Path(value = "restaurant_uuid", encoded = true) String restaurantId);

    @GET("api/1.0/mobile/{restaurant_uuid}/sync/suppliers")
    Call<SyncData<Supplier>> syncSuppliers(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Query("lastUpdated") String lastUpdated);

    @POST("api/1.0/mobile/{restaurant_uuid}/sync/inventory-movement")
    Call<String> syncInventoryMovement(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body List<InventoryMovement> inventoryMovements);

    @POST("api/1.0/mobile/{restaurant_uuid}/generate-qr-code")
    Call<ServerResponse<List<QRResponse>>> generateQR(@Path(value = "restaurant_uuid", encoded = true) String restaurantId, @Body QRRequest request);
}