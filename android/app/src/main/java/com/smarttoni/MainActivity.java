package com.smarttoni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventCallback;
import com.smarttoni.grenade.EventManager;
import com.smarttoni.react.modules.Events;
import com.smarttoni.react.modules.server.route.ClientHttpRoute;
import com.smarttoni.utils.AppUpdater;
import com.smarttoni.utils.LocalStorage;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;

public class MainActivity extends ReactActivity implements EventCallback {


    public final static String ACTION_SERVER_START = "com.smarttoni.SERVER_START";
    public final static String ACTION_SERVER_START_FAILED = "com.smarttoni.START_FAILED";
    public final static String ACTION_SERVER_STOP = "com.smarttoni.SERVER_STOP";

    private AsyncHttpServer mHttpServer;
    private ServerUpdateReceiver serverUpdateReceiver;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "SmartToni";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Override
            protected ReactRootView createRootView() {
                return new RNGestureHandlerEnabledRootView(MainActivity.this);

            }
        };
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Intent intent = new Intent("onConfigurationChanged");
        intent.putExtra("newConfig", newConfig);
        this.sendBroadcast(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppUpdater().check(this);
        startServer();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SERVER_START);
        intentFilter.addAction(ACTION_SERVER_STOP);
        intentFilter.addAction(ACTION_SERVER_START_FAILED);

        serverUpdateReceiver = new ServerUpdateReceiver();
        registerReceiver(serverUpdateReceiver, intentFilter);

        //TransportHelper.getInstance().test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServer();
        if (serverUpdateReceiver != null) {
            unregisterReceiver(serverUpdateReceiver);
        }
    }

    private void startServer() {
        mHttpServer = new AsyncHttpServer();
        mHttpServer.listen(8889);
        ClientHttpRoute.setRoutes(mHttpServer, this);
        EventManager.getInstance().addEventListner(Event.REACT_PUSH, this);
    }

    private void stopServer() {
        mHttpServer.stop();
        EventManager.getInstance().removeEventListner(this);
    }

    @Override
    public void onEvent(Object data) {
        //on push data
        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(Events.SERVER_PUSH, data);
    }


    public class ServerUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = 0;
            switch (intent.getAction()) {
                case MainActivity.ACTION_SERVER_START:
                    type = 1;
                    break;
                case MainActivity.ACTION_SERVER_STOP:
                    type = 2;
                    break;
                case MainActivity.ACTION_SERVER_START_FAILED:
                    new LocalStorage(MainActivity.this).putString(LocalStorage.RESTAURANT_TOKEN, "");
                    type = 3;
                    break;
            }
            getReactInstanceManager().getCurrentReactContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(Events.SERVER_UPDATE, type);

        }
    }

}
