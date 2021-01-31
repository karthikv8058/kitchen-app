import AsyncStorage from '@react-native-community/async-storage';
import { ManualPages } from '@components/Manual';

export enum Storage {
    SERVER_IP = 'serverIP',
    USER_ID = 'userId',
    CURRENT_INTERVENTION = 'SHOW_INTERVENTION',
    SERVER_RUNNING = 'isServerRunning',
    DEVICE_IP = 'deviceIp',
    MANUAL = 'manual',
    USER_READY = 'USER_READY',



    USERNAME = 'username',
    AUTH_TOKEN = "auth_token",
    ADVANCED_CONNECTION_IP = "ADVANCED_CONNECTION_IP",
    CONNECTED_SERVER_IP = "CONNECTED_SERVER_IP",
    REMIND_USERNAME = "REMIND_USERNAME",
    CHEF_NAME = "CHEF_NAME",
    LIKELY_HOOD_LIMIT = "LikelyHoodLimit",
    LAST_INTERVENTION_TIME = "LAST_INTERVENTION_TIME",
    LAST_INTERVENTION = "LAST_INTERVENTION",
    ROLES = "ROLES",
    ORIENTATION = "ORIENTATION",
}

export enum UserSettings {
    AUTO_OPEN_TASK = 'taskAutoOpen',
    AUTO_OPEN_STEP = 'stepAutoOpen',
    READ_ALL = 'isTTSOn',
    READ_ON_ASSIGN = 'readOnAssign',
    READ_DETAIL_DESCRIPTION = 'readDescriptionInDetail',
    READ_DETAIL_INGREDIENT = 'readIngredientInDetail',
    READ_STEP_DESCRIPTION = 'readDescriptionInSteps',
    READ_STEP_INGREDIENT = 'readIngredientInSteps',
    VOICE_RECOGNITION = 'isVROn',
}

export enum StorageKeys {
    LAST_READ = 'lastRead',
    LAST_OPENED_TASK = 'lastOpened',
    LIKELY_HOOD = 'LikelyHoodLimit',
    AUTO_OPEN_TASK = 'taskAutoOpen',
    AUTO_OPEN_STEP = 'stepAutoOpen',
    READ_ALL = 'isTTSOn',
    READ_ON_ASSIGN = 'readOnAssign',
    READ_DETAIL_DESCRIPTION = 'readDescriptionInDetail',
    READ_DETAIL_INGREDIENT = 'readIngredientInDetail',
    READ_STEP_DESCRIPTION = 'readDescriptionInSteps',
    READ_STEP_INGREDIENT = 'readIngredientInSteps',
    VOICE_RECOGNITION = 'isVROn',
    USER_ID = 'userid',
    TASKK_FILTER = 'task_filter',
}

export default class StorageService {

    static SERVICE_KEY = "storageService";

    registry: any = {};

    get(key: string): Promise<string> {
        return AsyncStorage.getItem(key).then(value => {
            let val = value ? value : "";
            this.registry[key] = val;
            return val;
        });
    }

    getFast(key: string) {
        return this.registry[key];
    }

    set(key: string, value: any) {
        this.registry[key] = value;
        return AsyncStorage.setItem(key, value);
    }

    setBoolean(key: string, value: boolean) {
        return this.set(key, value ? "1" : "0");
    }

    unset(key: string) {
        this.registry[key] = '';
        return AsyncStorage.setItem(key, '');
    }

    clearFlags() {
        this.unset(Storage.CURRENT_INTERVENTION);
    }

    init() {
        for (let [k, v] of Object.entries(StorageKeys)) {
            AsyncStorage.getItem(v).then(value => {
                this.registry[v] = value;
            });
        }
    }

    updateUser(user: any) {
        this.set(StorageKeys.AUTO_OPEN_TASK, user['taskAutoOpen']);
        this.set(StorageKeys.AUTO_OPEN_STEP, user['stepAutoOpen']);
        this.set(StorageKeys.READ_ALL, user['isTTSOn']);
        this.set(StorageKeys.READ_ON_ASSIGN, user['readOnAssign']);
        this.set(StorageKeys.READ_DETAIL_DESCRIPTION, user['readDescriptionInDetail']);
        this.set(StorageKeys.READ_DETAIL_INGREDIENT, user['readIngredientInDetail']);
        this.set(StorageKeys.READ_STEP_DESCRIPTION, user['readDescriptionInSteps']);
        this.set(StorageKeys.READ_STEP_INGREDIENT, user['readIngredientInSteps']);
        this.set(StorageKeys.VOICE_RECOGNITION, user['isVROn']);
    }

    getObject(key: string): Promise<any> {
        return this.get(key).then((value: string) => {
            if (value) {
                try {
                    return JSON.parse(value);
                } catch{
                    return {};
                }
            } else {
                return {};
            }
        })
    }

    saveObject(key: string, obj: any): Promise<any> {
        return this.set(key, JSON.stringify(obj));
    }

    resetUser() {
        // this.unset(StorageKeys.AUTO_OPEN_TASK);
        // this.unset(StorageKeys.READ_ALL);
        // this.unset(StorageKeys.READ_ON_ASSIGN);
        // this.unset(StorageKeys.READ_DETAIL_DESCRIPTION);
        // this.unset(StorageKeys.READ_DETAIL_INGREDIENT);
        // this.unset(StorageKeys.READ_STEP_DESCRIPTION);
        // this.unset(StorageKeys.READ_STEP_INGREDIENT);
        // this.unset(StorageKeys.VOICE_RECOGNITION);
    }

}
