package com.smarttoni.server;

import android.content.Context;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.socketio.ErrorCallback;
import com.smarttoni.react.modules.server.Ping;
import com.smarttoni.server.controlles.ServerDetails;
import com.smarttoni.server.controlles.StoreToAnalyze;
import com.smarttoni.server.controlles.assignment.AssignTaskToMe;
import com.smarttoni.server.controlles.assignment.CheckTaskToFinish;
import com.smarttoni.server.controlles.assignment.PostOnCall;
import com.smarttoni.server.controlles.assignment.UnassignTask;
import com.smarttoni.server.controlles.assignment.UpdateIntervention;
import com.smarttoni.server.controlles.assignment.UpdateTask;
import com.smarttoni.server.controlles.chat.PostChat;
import com.smarttoni.server.controlles.devtools.DevStationList;
import com.smarttoni.server.controlles.devtools.DevUserList;
import com.smarttoni.server.controlles.devtools.DownloadDatabase;
import com.smarttoni.server.controlles.inventory.AddNewIngredient;
import com.smarttoni.server.controlles.inventory.GetInventoryList;
import com.smarttoni.server.controlles.inventory.UpdateInventory;
import com.smarttoni.server.controlles.media.GetImageFile;
import com.smarttoni.server.controlles.media.GetVideoFile;
import com.smarttoni.server.controlles.order.CheckOrderStarted;
import com.smarttoni.server.controlles.order.DeleteExternalOrder;
import com.smarttoni.server.controlles.order.DeleteOrder;
import com.smarttoni.server.controlles.order.DeletePrinterMessage;
import com.smarttoni.server.controlles.order.FetchOrder;
import com.smarttoni.server.controlles.order.FinishOrder;
import com.smarttoni.server.controlles.order.GetOrderDetails;
import com.smarttoni.server.controlles.order.GetPrinterData;
import com.smarttoni.server.controlles.order.LoadArchivedOrder;
import com.smarttoni.server.controlles.order.LoadOrderFromWeb;
import com.smarttoni.server.controlles.order.PlaceExternalOrder;
import com.smarttoni.server.controlles.order.getExternalOrders;
import com.smarttoni.server.controlles.order.getOrderItems;
import com.smarttoni.server.controlles.pos.NewPosOrder;
import com.smarttoni.server.controlles.recipe.GetAllRecipes;
import com.smarttoni.server.controlles.recipe.GetRecipe;
import com.smarttoni.server.controlles.recipe.GetRecipesAndLabels;
import com.smarttoni.server.controlles.recipe.RecipeDetails;
import com.smarttoni.server.controlles.recipe.getAllLabels;
import com.smarttoni.server.controlles.settings.GetWebCredentials;
import com.smarttoni.server.controlles.settings.SyncRequest;
import com.smarttoni.server.controlles.station.GetStationTaskList;
import com.smarttoni.server.controlles.station.GetStations;
import com.smarttoni.server.controlles.station.StationList;
import com.smarttoni.server.controlles.station.TestStationPrinter;
import com.smarttoni.server.controlles.task.CheckDetailsToOpen;
import com.smarttoni.server.controlles.task.GetTask;
import com.smarttoni.server.controlles.task.ImageUpload;
import com.smarttoni.server.controlles.task.PingIp;
import com.smarttoni.server.controlles.task.getPrinters;
import com.smarttoni.server.controlles.user.AddStationUser;
import com.smarttoni.server.controlles.user.GetAllUsers;
import com.smarttoni.server.controlles.user.GetStationUsers;
import com.smarttoni.server.controlles.user.GetUserRights;
import com.smarttoni.server.controlles.user.Login;
import com.smarttoni.server.controlles.user.UserLogout;


public class HttpRoute {

    public static void setRoutes(AsyncHttpServer mHttpServer, Context context) {

        mHttpServer.post("/assign-task", new AssignTaskToMe(context));
        mHttpServer.post("/add-station-user", new AddStationUser(context));
        mHttpServer.post("/check-detail-to-open", new CheckDetailsToOpen(context));
        mHttpServer.get("/play-video", new GetVideoFile(context));
        mHttpServer.get("/get-image", new GetImageFile(context));
        mHttpServer.post("/login", new Login(context));
        mHttpServer.post("/new-pos-order", new NewPosOrder(context));
        mHttpServer.post("/get-task", new GetTask(context));
        mHttpServer.post("/update-task", new UpdateTask(context));
        mHttpServer.post("/get-stations", new GetStations(context));
        mHttpServer.post("/get-station-users", new GetStationUsers(context));
        mHttpServer.post("/get-all-users", new GetAllUsers(context));
        mHttpServer.post("/get-recipes", new GetRecipe(context));
        mHttpServer.get("/get-recipe-labels", new GetRecipesAndLabels(context));
        mHttpServer.post("/station-tasks", new GetStationTaskList(context));
        mHttpServer.post("/check-order-started", new CheckOrderStarted(context));
        mHttpServer.post("/logout", new UserLogout(context));
        mHttpServer.post("/check-task-to-finish", new CheckTaskToFinish(context));
        mHttpServer.post("/fetch-order-data", new FetchOrder(context));
        mHttpServer.post("/finish-order", new FinishOrder(context));
        mHttpServer.post("/get-station-list", new StationList(context));
        mHttpServer.post("/unassign-task", new UnassignTask(context));
        mHttpServer.post("/ping", new Ping(context));
        mHttpServer.post("/delete-order", new DeleteOrder(context));
        mHttpServer.post("/get-inventory-data", new GetInventoryList(context));
        mHttpServer.post("/update-inventory-quantity", new UpdateInventory(context));
        mHttpServer.post("/get-printer-data", new GetPrinterData(context));
        mHttpServer.post("/delete-printer-message", new DeletePrinterMessage(context));
        mHttpServer.post("/store-to-analyze", new StoreToAnalyze(context));
        mHttpServer.post("/on-call", new PostOnCall(context));
        mHttpServer.post("/get-order-details", new GetOrderDetails(context));
        mHttpServer.post("/overview-task", new getOrderItems(context));
        mHttpServer.post("/update-intervention", new UpdateIntervention(context));
        mHttpServer.post("/chat", new PostChat(context));
        mHttpServer.post("/recipe-Details", new RecipeDetails(context));
        mHttpServer.post("/get-web-credentials", new GetWebCredentials(context));
        mHttpServer.post("/get-all-labels", new getAllLabels(context));
        mHttpServer.post("/sync-request", new SyncRequest(context));
        mHttpServer.post("/image-upload", new ImageUpload(context));
        mHttpServer.post("/get-user-right", new GetUserRights(context));
        mHttpServer.get("/server/details", new ServerDetails());
        mHttpServer.post("/place-external-order", new PlaceExternalOrder(context));
        mHttpServer.post("/external-overview-orders", new getExternalOrders(context));
        mHttpServer.post("/delete-external-order", new DeleteExternalOrder(context));
        mHttpServer.post("/load-order-from-web", new LoadOrderFromWeb(context));
        mHttpServer.post("/load-archived-order", new LoadArchivedOrder(context));
        mHttpServer.post("/get-all-recipes", new GetAllRecipes(context));
        mHttpServer.post("/add-new-ingredient", new AddNewIngredient(context));
        mHttpServer.post("/test=station-printer", new TestStationPrinter(context));
        mHttpServer.post("/ping-ip", new PingIp(context));
        mHttpServer.post("/get-printers", new getPrinters(context));

        //Devv
        mHttpServer.get("/dev-stations", new DevStationList(context));
        mHttpServer.get("/dev-users", new DevUserList(context));
        mHttpServer.get("/dev-db", new DownloadDatabase(context));

    }
}
