package com.smarttoni.react.modules.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.keenresearch.keenasr.KASRRecognizer;
import com.smarttoni.BuildConfig;
import com.smarttoni.react.modules.SpeechRecognizerKeenAsr;

public class VoiceRecognitionService extends Service {

    //private static SpeechRecognizer speechRecognizer;
    private IBinder mServiceBinder = new RunServiceBinder();
    private KASRRecognizer recognizer;
    private SpeechRecognizerKeenAsr speechRecognizerKeenAsr;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSpeechRecognizer();
        return START_STICKY;
    }

    private void initSpeechRecognizer() {
        if (BuildConfig.isVoiceRecognitionEnabled) {
            initKeenAsrSpeechRecognizer();
        }

    }

    private void initKeenAsrSpeechRecognizer() {
        speechRecognizerKeenAsr = SpeechRecognizerKeenAsr.getInstance(this);
        recognizer = speechRecognizerKeenAsr.getRecognizer();
    }


    @Override

    public void onCreate() {
        super.onCreate();
        mServiceBinder = new LocalBinder();
        // speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    }

    public class RunServiceBinder extends Binder {
        public VoiceRecognitionService getService() {
            return VoiceRecognitionService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //xxspeechRecognizer.cancel();
        //speechRecognizer.destroy();
        if (speechRecognizerKeenAsr != null)
            speechRecognizerKeenAsr.removeListener();
    }

    public class LocalBinder extends Binder {
        public VoiceRecognitionService getService() {
            // Return this instance of LocalService so clients can call public methods
            return VoiceRecognitionService.this;
        }
    }

    public void startVoiceListening() {
        if (recognizer != null) {
            Log.d("keenasr", "start voice listening");
            recognizer.getRecognizerState();
            recognizer.startListening();
        }

    }

    public void stopVoiceListening() {
        if (recognizer != null) {
            Log.d("keenasr", "stop voice listening");
            recognizer.stopListening();
        }
    }
}