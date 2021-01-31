package com.smarttoni.react.modules.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.smarttoni.react.modules.Events;

public class VoiceReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.smarttoni.react.VOICE_RECOGNITION";
    public static final String TEXT = "TEXT";

    private ReactApplicationContext context;

    public VoiceReceiver(ReactApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(Events.VOICE_RECOGNITION, intent.getStringExtra(TEXT));
    }
}
