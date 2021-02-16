package com.smarttoni.react.modules.server;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.smarttoni.BuildConfig;
import com.smarttoni.MainActivity;
import com.smarttoni.R;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.utils.Strings;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Restaurant;
import com.smarttoni.models.ServerStartRequest;
import com.smarttoni.models.ServerStartResponse;
import com.smarttoni.http.HttpClient;
import com.smarttoni.SmartTONiService;
import com.smarttoni.sync.SyncFromWeb;
import com.smarttoni.sync.UpdateManager;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.udp.UdpManager;
import com.smarttoni.utils.WifiUtils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RNWebServer extends ReactContextBaseJavaModule {


    private SharedPreferences pref;
    private ReactApplicationContext context;
    private static String PREFS_NAME = "restuarantId";


    private static final String API_BASE_URL_KEY = "API_BASE_URL";

    private static final String WEB_URL_KEY = "WEB_URL";


    public RNWebServer(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "RNWebServer";
    }

    @ReactMethod
    public void startServices(Promise promise) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "23");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentText("Starting Server")
                .setSmallIcon(R.drawable.ic_notification)
                .setProgress(0, 0, true);
        notificationManager.notify(1000, builder.build());
        //TODO Refactor
        new LocalStorage(context).setLong(LocalStorage.LAST_SYNC_RECIPE, 0);
        SyncFromWeb.Companion.syncCloudDb(context, true, () -> {
            if (promise != null) {
                promise.resolve(true);
            }
        });
    }


    @ReactMethod
    public void startServerAndSync(String restaurantId, String name, String token,boolean forceToken, Promise promise) {
        
        pref = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String id = pref.getString("restaurantId", "");
        if (!id.equals(restaurantId)) {
            pref.edit().putString("restaurantId", restaurantId).apply();
            ServiceLocator.getInstance().getQueue().clear();
            new GreenDaoAdapter(context).clear();
        }
        LocalStorage localStorage = new LocalStorage(context);

        localStorage.putString(LocalStorage.RESTAURANT_NAME, name);
        localStorage.putString(LocalStorage.RESTAURANT_UUID, restaurantId);

        localStorage.setAuthToken(token);
        localStorage.setRestaurant(restaurantId,name);

        if(!forceToken && Strings.isNotEmpty(localStorage.getString(LocalStorage.RESTAURANT_TOKEN))){
            startServices(promise);
        }else{
            new HttpClient(context).getHttpClient().startServer(restaurantId,new ServerStartRequest(WifiUtils.getLocalIpAddress())).enqueue(new Callback<ServerStartResponse>() {
                @Override
                public void onResponse(Call<ServerStartResponse> call, Response<ServerStartResponse> response) {
                    ServerStartResponse serverStartResponse =response.body();
                    if(serverStartResponse != null && serverStartResponse.getSignature() != null){
                        localStorage.putString(LocalStorage.RESTAURANT_TOKEN,serverStartResponse.getSignature());
                        localStorage.putString(LocalStorage.RESTAURANT_TOKEN,serverStartResponse.getSignature());
                        startServices(promise);
                    }else{
                        context.sendBroadcast(new Intent(MainActivity.ACTION_SERVER_START_FAILED));
                    }
                }

                @Override
                public void onFailure(Call<ServerStartResponse> call, Throwable t) {
                    context.sendBroadcast(new Intent(MainActivity.ACTION_SERVER_START_FAILED));
                }
            });
        }
    }

    public void _startServerAndSync(String restaurantId, String name, String token, Promise promise) {

    }

    @ReactMethod
    public void setUserToken(String username, String token) {
        LocalStorage session = new LocalStorage(context);
        session.setUsername(username);
        session.setAuthToken(token);
    }

    @ReactMethod
    public void stopIpRequest() {
    }

    @ReactMethod
    public void getIPAddress(Promise promise) {
        promise.resolve(WifiUtils.getLocalIpAddress());
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(API_BASE_URL_KEY, BuildConfig.BASE_URL);
        constants.put(WEB_URL_KEY, BuildConfig.WEB_URL);
        return constants;
    }

    @ReactMethod
    private void getLastRestaurantToken(Promise promise) {
        LocalStorage localStorage = new LocalStorage(context);
        String restaurantId = localStorage.getRestaurantId();
        String token = localStorage.getString(LocalStorage.RESTAURANT_TOKEN);
        if(Strings.isNotEmpty(token)){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("uuid", restaurantId);
            hm.put("token", token);
            WritableMap map = new WritableNativeMap();
            for (Map.Entry<String, String> entry : hm.entrySet()) {
                map.putString(entry.getKey(), entry.getValue());
            }
            promise.resolve(map);
        } else {
            promise.resolve(null);
        }
    }

    @ReactMethod
    private void getCurrentRestaurant(Promise promise) {

        SmarttoniContext context = ServiceLocator.getInstance().getSmarttoniContext();
        if (context != null && context.isServerRunning()) {
            Restaurant restaurant = context.getRestaurant();
            HashMap<String, String> hm = new HashMap<>();
            hm.put("ip", WifiUtils.getLocalIpAddress());
            hm.put("uuid", restaurant.getUuid());
            hm.put("name", restaurant.getName());
            WritableMap map = new WritableNativeMap();
            for (Map.Entry<String, String> entry : hm.entrySet()) {
                map.putString(entry.getKey(), entry.getValue());
            }
            promise.resolve(map);
        } else {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void sendSSR() {
        UdpManager udpManager = (UdpManager) ServiceLocator.getInstance().getService(ServiceLocator.UDP_MANAGER);
        udpManager.sendSSR(BuildConfig.VERSION_CODE);
        // ServiceLocator.getInstance().getSmarttoniContext().
        // promise.resolve(WifiUtils.getLocalIpAddress());
    }

    @ReactMethod
    public void logout(Promise promise) {
        UpdateManager updateManager = new UpdateManager();
        updateManager.syncAll(context, new SyncFromWeb.SyncFinishCallback() {
            @Override
            public void onFinish() {
                Intent i = new Intent(context, SmartTONiService.class);
                context.stopService(i);

                LocalStorage localStorage = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);
                String id = localStorage.getRestaurantId();

                new HttpClient(context).getHttpClient().stopServer(id).enqueue(new Callback<ServerStartResponse>() {
                    @Override
                    public void onResponse(Call<ServerStartResponse> call, Response<ServerStartResponse> response) {
                        localStorage.putString(LocalStorage.RESTAURANT_TOKEN,"");
                        localStorage.logout();
                        promise.resolve(true);
                    }

                    @Override
                    public void onFailure(Call<ServerStartResponse> call, Throwable t) {
                        localStorage.putString(LocalStorage.RESTAURANT_TOKEN,"");
                        localStorage.logout();
                        promise.resolve(true);
                    }
                });
            }
        });
    }
}
