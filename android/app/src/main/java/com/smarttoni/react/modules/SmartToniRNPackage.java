package com.smarttoni.react.modules;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.smarttoni.react.modules.server.RNWebServer;
import com.smarttoni.react.modules.voice.RNVoiceRecognition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartToniRNPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> list = new ArrayList<>();
        list.add(new RNWebServer(reactContext));
        list.add(new RNVoiceRecognition(reactContext));
        list.add(new RNSettings(reactContext));
        return list;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
