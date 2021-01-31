import TTS from 'react-native-tts';

export default class VoiceService {

    constructor() {
        TTS.getInitStatus().then(() => {
            //TTS.speak('Welcome to smarttoni');
        });
    }

    speak(text: string) {
        TTS.getInitStatus().then(() => {
            TTS.stop();
            TTS.speak(text);
        });
    }

    silence() {
        TTS.getInitStatus().then(() => {
            TTS.stop();
        });
    }
}
