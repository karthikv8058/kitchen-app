import { DeviceEventEmitter, NativeModules } from 'react-native';

import CancellableSubscription from './CancellableSubscription';

const VOICE_RECOGNITION = 'com.smarttoni.react:VOICE_RECOGNITION';

export default class VoiceRecognition {

    private voicerecognizer: any;
    private listener: any;

    start() {
        NativeModules.RNVoiceRecognition.startVoiceRecognitionService();
    }

    stop() {
        NativeModules.RNVoiceRecognition.stopVoiceRecognitionService();
    }

    startListening() {
        NativeModules.RNVoiceRecognition.start();
    }

    stopListening() {
        NativeModules.RNVoiceRecognition.stop();
    }

    addListener(listener: (message: string) => void): CancellableSubscription {
        this.listener = listener;
        this.voicerecognizer = DeviceEventEmitter.addListener(
            VOICE_RECOGNITION,
            listener
        );
        return this.voicerecognizer;
    }

    removeListener() {
        DeviceEventEmitter.removeListener(VOICE_RECOGNITION, this.listener);
        if (this.voicerecognizer) {
            this.voicerecognizer.remove();
        }
    }
}
