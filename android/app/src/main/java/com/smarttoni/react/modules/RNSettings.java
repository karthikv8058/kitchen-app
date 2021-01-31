package com.smarttoni.react.modules;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNSettings extends ReactContextBaseJavaModule {

    Context context;

    public RNSettings(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "RNSettings";
    }

    @ReactMethod
    public void getIPFromSettings(Promise promise) {
        String s = context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString("providedIP", "");
        promise.resolve(s);
    }

    @ReactMethod
    public void resetIPFromSettings() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit()
                .putString("providedIP", "")
                .apply();
    }
}
