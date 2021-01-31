import Tts from 'react-native-tts';

export default class TtsService {
    constructor(){
        // Tts.setDucking(true);
        // Tts.setDefaultLanguage('en-IE');
        // Tts.voices().then(voices =>{});
        // Tts.setDefaultRate(0.6);
    }

    isTtsReady(){
        return new Promise(function(resolve,reject){
            Tts.getInitStatus().then((res)=>{
                resolve(res);
            }).catch((er)=>{
                reject(err);
            })
        })
    }

    read(text:string) {
        Tts.getInitStatus().then((a) => {
            Tts.stop();
            Tts.speak(text);
        }).catch((error: any) => {
            console.error(error);
        });
    }

    stop(){
        Tts.stop();
    }

}