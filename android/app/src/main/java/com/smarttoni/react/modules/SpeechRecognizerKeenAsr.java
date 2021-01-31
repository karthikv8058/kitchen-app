package com.smarttoni.react.modules;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.keenresearch.keenasr.KASRBundle;
import com.keenresearch.keenasr.KASRDecodingGraph;
import com.keenresearch.keenasr.KASRRecognizer;
import com.keenresearch.keenasr.KASRRecognizerListener;
import com.keenresearch.keenasr.KASRRecognizerTriggerPhraseListener;
import com.keenresearch.keenasr.KASRResult;
import com.smarttoni.react.modules.voice.VoiceReceiver;

import java.io.IOException;
import java.util.ArrayList;


public class SpeechRecognizerKeenAsr {

    private static SpeechRecognizerKeenAsr instance = null;
    private Context context;
    private KASRRecognizer recognizer;
    private String TAG = "KEENASR";
    private String TRIGGER_PHRASE = "TONI";
    KASRRecognizerListener kasrRecognizerListener;
    String bundleName = "keenB2sQT-nnet3chain-en-us";


    private SpeechRecognizerKeenAsr(Context context) {
        this.context = context;
        KASRBundle asrBundle = new KASRBundle(context);
        ArrayList<String> assets = new ArrayList<String>();


        assets.add(bundleName + "/decode.conf");
        assets.add(bundleName + "/final.dubm");
        assets.add(bundleName + "/final.ie");
        assets.add(bundleName + "/final.mat");
        assets.add(bundleName + "/final.mdl");
        assets.add(bundleName + "/global_cmvn.stats");
        assets.add(bundleName + "/ivector_extractor.conf");
        assets.add(bundleName + "/mfcc.conf");
        assets.add(bundleName + "/online_cmvn.conf");
        assets.add(bundleName + "/splice.conf");
        assets.add(bundleName + "/splice_opts");
        assets.add(bundleName + "/wordBoundaries.int");
        assets.add(bundleName + "/words.txt");

        assets.add(bundleName + "/lang/lexicon.txt");
        assets.add(bundleName + "/lang/phones.txt");
        assets.add(bundleName + "/lang/tree");


        String asrBundleRootPath = context.getApplicationInfo().dataDir;
        String asrBundlePath = asrBundleRootPath + "/" + bundleName;
        try {
            asrBundle.installASRBundle(assets, asrBundleRootPath);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when installing ASR bundle" + e);
        }
    }

    public static SpeechRecognizerKeenAsr getInstance(Context context) {
        if (instance == null) {
            instance = new SpeechRecognizerKeenAsr(context);
        }

        return instance;
    }

    public KASRRecognizer getRecognizer() {
        String asrBundleRootPath = context.getApplicationInfo().dataDir;
        String asrBundlePath = asrBundleRootPath + "/" + bundleName;
        KASRRecognizer.initWithASRBundleAtPath(asrBundlePath, context.getApplicationContext());

        this.recognizer = KASRRecognizer.sharedInstance();
        if (recognizer != null) {
            if (kasrRecognizerListener != null) {
                recognizer.removeListener(kasrRecognizerListener);
            }

            kasrRecognizerListener = new KASRRecognizerListener() {
                @Override
                public void onPartialResult(KASRRecognizer kasrRecognizer, KASRResult kasrResult) {
                    for (String phrase : getPhrases()) {
                        if (kasrResult.getText().indexOf(TRIGGER_PHRASE + " " + phrase.toUpperCase()) > -1) {
                            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForGoodMatch, 0.2f);
                            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForAnyMatch, .02f);

                        }
                    }
                }

                @Override
                public void onFinalResult(KASRRecognizer kasrRecognizer, KASRResult kasrResult) {
                    Log.d("keenasr", kasrResult.getText());
                    Intent intent = new Intent(VoiceReceiver.ACTION);
                    intent.putExtra(VoiceReceiver.TEXT, kasrResult.getText());
                    context.sendBroadcast(intent);
                    recognizer.stopListening();
                    recognizer.startListening();
                    recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForGoodMatch, 0.5f);
                    recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForAnyMatch, 0.5f);
                }
            };
            recognizer.addListener(kasrRecognizerListener);
            recognizer.addTriggerPhraseListener(new KASRRecognizerTriggerPhraseListener() {
                @Override
                public void onTriggerPhrase(KASRRecognizer kasrRecognizer) {
                    Log.i(TAG, "   Trigger phrase detected");
                }
            });

            recognizer.addTriggerPhraseListener(new KASRRecognizerTriggerPhraseListener() {
                @Override
                public void onTriggerPhrase(KASRRecognizer kasrRecognizer) {
                }
            });

            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForGoodMatch, 0.5f);
            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutEndSilenceForAnyMatch, 0.5f);
            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutMaxDuration, 10.0f);
            recognizer.setVADParameter(KASRRecognizer.KASRVadParameter.KASRVadTimeoutForNoSpeech, 3.0f);
            recognizer.setCreateAudioRecordings(true);

            String[] phrases = SpeechRecognizerKeenAsr.getPhrases();
            String dgName = "words";
            KASRDecodingGraph.createDecodingGraphFromSentencesWithTriggerPhrase(phrases, TRIGGER_PHRASE, recognizer, dgName);
            recognizer.prepareForListeningWithCustomDecodingGraphWithName(dgName);
            recognizer.startListening();

        }

        return recognizer;
    }

    public void removeListener() {
        recognizer.removeListener(kasrRecognizerListener);
    }

    private static String[] getPhrases() {
        return new String[]{
                "Next", "Done", "Step by Step", "Ingredients", "Detail", "Previous", "Completed", "Assign first task",
                "Show details", "Read ingredients", "Read task", "Show tasks"
        };
    }


}
