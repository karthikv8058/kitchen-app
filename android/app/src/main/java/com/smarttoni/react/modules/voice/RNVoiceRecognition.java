package com.smarttoni.react.modules.voice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.smarttoni.react.modules.services.VoiceRecognitionService;


public class RNVoiceRecognition extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private BroadcastReceiver receiver;
    private boolean isReceiverRegistered = false;
    VoiceRecognitionService mService;
    boolean mBound = false;

    public RNVoiceRecognition(ReactApplicationContext reactContext) {
        super(reactContext);
        System.out.print("In RNVoiceRecognition constructor");
        this.receiver = new VoiceReceiver(reactContext);
        getReactApplicationContext().addLifecycleEventListener(this);
        registerReceiverIfNecessary(this.receiver);

        Intent intent = new Intent(reactContext, VoiceRecognitionService.class);
        reactContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public String getName() {
        return "RNVoiceRecognition";
    }

    @ReactMethod
    public void startVoiceRecognitionService() {
        getReactApplicationContext().startService(new Intent(getReactApplicationContext(), VoiceRecognitionService.class));
    }

    @ReactMethod
    public void stopVoiceRecognitionService() {
        getReactApplicationContext().stopService(new Intent(getReactApplicationContext(), VoiceRecognitionService.class));
    }


    @ReactMethod
    public void start() {
        mService.startVoiceListening();
    }

    @ReactMethod
    public void stop() {
        mService.stopVoiceListening();
    }

    private void registerReceiverIfNecessary(BroadcastReceiver receiver) {
        if (getCurrentActivity() != null) {
            getCurrentActivity().registerReceiver(
                    receiver,
                    new IntentFilter(VoiceReceiver.ACTION)
            );
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver(BroadcastReceiver receiver) {
        if (isReceiverRegistered && getCurrentActivity() != null) {
            getCurrentActivity().unregisterReceiver(receiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    public void onHostResume() {
        registerReceiverIfNecessary(receiver);
    }

    @Override
    public void onHostPause() {
        unregisterReceiver(receiver);
    }

    @Override
    public void onHostDestroy() {
        unregisterReceiver(receiver);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            VoiceRecognitionService.LocalBinder binder = (VoiceRecognitionService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
