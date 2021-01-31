import { NativeModules } from 'react-native';

export default class Settings {
    getProvidedIP() {
        return NativeModules.RNSettings.getIPFromSettings();
    }

    resetIPFromSettings() {
        NativeModules.RNSettings.resetIPFromSettings();
    }
}
