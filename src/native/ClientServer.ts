import { DeviceEventEmitter, NativeModules } from 'react-native';

import CancellableSubscription from './CancellableSubscription';
import Restaurant from '@models/Restaurant';

const SERVER_PUSH = 'SERVER_PUSH';
const SERVER_UPDATE = 'SERVER_UPDATE';
const PRINTER_MESSAGE_RECEIVED = 'com.smarttoni.react:PRINTER_MESSAGE_RECEIVED';

export const ApiBaseUrl = NativeModules.RNWebServer.API_BASE_URL;
export const WebUrl = NativeModules.RNWebServer.WEB_URL;

export default class WebServer {

    constructor() {
    }

    addPrinterMessageListener(listener: (message: String) => void): CancellableSubscription {
        return DeviceEventEmitter.addListener(
            PRINTER_MESSAGE_RECEIVED,
            listener
        );
    }

    addListener(listener: (message: string) => void): CancellableSubscription {
        return DeviceEventEmitter.addListener(
            SERVER_PUSH,
            listener
        );
    }

    removeListener() {
        DeviceEventEmitter.removeAllListeners();
    }

    getCurrentRestaurant(): Promise<Restaurant> {
        return NativeModules.RNWebServer.getCurrentRestaurant();
    }

    setUserToken(username: String, token: String) {
        NativeModules.RNWebServer.setUserToken(username, token);
    }

    startServerAndSync(restaurant: Restaurant, token: string) {
        return NativeModules.RNWebServer.startServerAndSync(restaurant.uuid, restaurant.name, token, !restaurant.server_ip);
    }

    getIPAddress(): Promise<string> {
        return NativeModules.RNWebServer.getIPAddress();
    }


    addListenerForServerUpdate(listener: (message: string) => void): CancellableSubscription {
        return DeviceEventEmitter.addListener(
            SERVER_UPDATE,
            listener
        );
    }

    removeServerUpdateListener(listener: (message: string) => void) {
        DeviceEventEmitter.removeListener(SERVER_UPDATE, listener);
    }

    sendSSR() {
        NativeModules.RNWebServer.sendSSR();
    }

    logout() {
        return NativeModules.RNWebServer.logout();
    }

    getLastRestaurantToken(): Promise<Restaurant> {
        return NativeModules.RNWebServer.getLastRestaurantToken();
    }

}
