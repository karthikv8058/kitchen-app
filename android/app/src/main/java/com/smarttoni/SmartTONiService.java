package com.smarttoni;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.chef.UserManager;
import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.AuthManager;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.InterventionJob;
import com.smarttoni.entities.Restaurant;
import com.smarttoni.entities.User;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventCallback;
import com.smarttoni.grenade.EventManager;
import com.smarttoni.pegion.PushCallback;
import com.smarttoni.pegion.PushMessage;
import com.smarttoni.server.HttpRoute;
import com.smarttoni.http.ChefHttpClient;
import com.smarttoni.sync.IncrementalDbSync;
import com.smarttoni.sync.SyncFromWeb;
import com.smarttoni.sync.UpdateManager;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.utils.NotificationHelper;
import com.smarttoni.udp.UdpManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SmartTONiService extends Service {

    public static final String ACTION_SERVICE_STARTED = "com.smarttoni.SmartTONiService.ACTION_SERVICE_STARTED";

    private final static int INTERVAL_ONE_MINUTE = 1000 * 60; //one minute
    public final static int TIMER_INTERVAL = 1000;

    public final IBinder mServiceBinder = new RunServiceBinder();

    private static boolean mIsServerRunning = false;
    private Timer timer = new Timer();
    private AsyncHttpServer mHttpServer;
    private PowerManager.WakeLock wakeLock;


    private TimerTask doOrderSync;
    private TimerTask incrementalDbSync;
    private TimerTask interventionTimer;
    private TimerTask assignmentTimer;


    private GreenDaoAdapter daoAdapter;

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //init all
        ServiceLocator.getInstance().init(this);
        AssignmentFactory.getInstance().init(getApplicationContext(), true);
        AuthManager.getInstance().init(this);
        sendBroadcast(new Intent(ACTION_SERVICE_STARTED));
        daoAdapter = new GreenDaoAdapter(SmartTONiService.this);
        Handler handler = new Handler();

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    SyncFromWeb.Companion.syncCloudDb(SmartTONiService.this);
                    //incrementalDbUpload();
                });
            }
        };

        doOrderSync = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    UpdateManager updateManager = new UpdateManager();
                    updateManager.syncAll(SmartTONiService.this);
                });
            }
        };
        incrementalDbSync=new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    new IncrementalDbSync().onSync(getApplicationContext(),
                            ServiceLocator.getInstance().getDatabaseAdapter(),
                            new LocalStorage(getApplicationContext()).getRestaurantId(),
                            null, null);
                });
            }
        };
        timer.schedule(incrementalDbSync, 0, 15 * INTERVAL_ONE_MINUTE);
        timer.schedule(doOrderSync, 0, INTERVAL_ONE_MINUTE);
        timer.schedule(doAsynchronousTask, 30 * INTERVAL_ONE_MINUTE, 30 * INTERVAL_ONE_MINUTE);

        interventionTimer =new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
                    assignmentFactory.eventLoop(SmartTONiService.this);
                    getAvailableUser();
                });
            }
        };

        assignmentTimer =new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    AssignmentFactory assignmentFactory = (AssignmentFactory) ServiceLocator.getInstance().getService(ServiceLocator.ASSIGNMENT_SERVICE);
                    assignmentFactory.timer(SmartTONiService.this);
                });
            }
        };

        timer.schedule(interventionTimer, 0, INTERVAL_ONE_MINUTE);
        timer.schedule(assignmentTimer, 0, TIMER_INTERVAL);


        mHttpServer = new AsyncHttpServer();
        mIsServerRunning = false;
        startServer();
        bindBroadcastReceiver();

        Restaurant restaurant = new Restaurant();
        restaurant.setUuid(ServiceLocator.getInstance().getLocalStorage().getString(LocalStorage.RESTAURANT_UUID));
        restaurant.setName(ServiceLocator.getInstance().getLocalStorage().getString(LocalStorage.RESTAURANT_NAME));
        ServiceLocator.getInstance().getSmarttoniContext().updateRestaurant(restaurant); // 1 Running


        UdpManager udpManager = (UdpManager) ServiceLocator.getInstance().getService(ServiceLocator.UDP_MANAGER);
        udpManager.initUDP(this);

        EventManager.getInstance().emit(Event.SERVER_START,true);
        sendBroadcast(new Intent(MainActivity.ACTION_SERVER_START));
        //Toast.makeText(this, R.string.service_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().emit(Event.SERVER_STOP);

        timer.cancel();

        doOrderSync.cancel();
        mIsServerRunning = false;
        if (mHttpServer != null) {
            mHttpServer.stop();
        }
        if (wakeLock != null) {
            wakeLock.release();
        }

        PushMessage message = new PushMessage(PushMessage.TYPE_SERVER_LOGOUT);

        SmarttoniContext context = ServiceLocator.getInstance().getSmarttoniContext();
        context.pushAll(message, null);

        ServiceLocator.getInstance().getSmarttoniContext().updateRestaurant(null); // 0 Stopped

    }

    private void bindBroadcastReceiver() {
        EventManager.getInstance().addEventListner(Event.SEND_PEGION, new EventCallback() {
            @Override
            public void onEvent(Object data) {
                Bundle bundle = (Bundle) data;
                sendInterventionMessage(bundle.getString(InterventionJob.USER_ID),bundle.getLong(InterventionJob.INTERVENTION_ID));
            }
        });
    }

    private void sendInterventionMessage(String userId,Long interventionId) {
        //TODO
        SmarttoniContext sc = ServiceLocator.getInstance().getSmarttoniContext();
        sc.push(userId, new PushMessage(PushMessage.TYPE_INTERVENTION, interventionId), new PushCallback() {
            @Override
            public void onPushSuccess() {

            }

            @Override
            public void onPushFailed() {

                InterventionManager interventionManager = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                        .getInterventionManager();
                interventionManager.onInterventionPushFailed(userId);
            }
        });
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    /**
     * To set up Socket server and Http server connection to PORT
     */
    private void startServer() {
        if (mIsServerRunning) {
            return;
        }
        setUpHttpServer();
        mIsServerRunning = true;
    }

    /**
     * To direct the Server url and its call back function
     */
    private void setUpHttpServer() {
        HttpRoute.setRoutes(mHttpServer, SmartTONiService.this);
        mHttpServer.listen(8888);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startInForeground();
        startServer();
        //receiveUDPBroadcast();
        MainApplication.serverService = this;
        return START_STICKY;
    }

    /**
     * To bind the service to the activity
     */
    public class RunServiceBinder extends Binder {
        public SmartTONiService getService() {
            return SmartTONiService.this;
        }
    }

    /**
     * To start a foreground service notification
     */
    private void startInForeground() {
        startForeground(1000, NotificationHelper.getServerNotification(this));
    }

    private void getAvailableUser() {
        List<User> users = daoAdapter.loadUsers();
        if (users.size() > 0) {
            pingClient(0, users);
        }
    }

    private void pingClient(final int index, final List<User> users) {
        if (!(index >= 0 && index < users.size())) {
            return;
        }
        User user = users.get(index);
        if (user.getIpAddress() != null && user.getIpAddress() != "" && !user.getIpAddress().equals("")) {
            new ChefHttpClient().getHttpClient(users.get(index).getIpAddress()).clientPing().enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> isActive) {
                    if (isActive.body()) {
                        setUserStatus(users.get(index), true);
                        if ((index + 1) <= users.size()) {
                            pingClient(index + 1, users);
                        }
                    } else {
                        if ((index + 1) <= users.size()) {
                            pingClient(index + 1, users);
                        }
                        setUserStatus(users.get(index), false);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    if ((index + 1) <= users.size()) {
                        pingClient(index + 1, users);
                    }
                    setUserStatus(users.get(index), false);
                }
            });
        } else {
            setUserStatus(user, false);
        }
    }

    private void setUserStatus(User users, boolean userStatus) {
        User user = daoAdapter.getUserById(users.getId());
        if (user != null) {
            user.setOnline(userStatus);
            daoAdapter.updateUser(user);
        }
        if (!userStatus) {
            UserManager userManager = ((SmarttoniContext)ServiceLocator.getInstance().getService(ServiceLocator.SMARTTONI_CONTEXT))
                    .getUserManager();
            if(userManager!=null)
            userManager.logout(users.getId());
        }
    }
}
