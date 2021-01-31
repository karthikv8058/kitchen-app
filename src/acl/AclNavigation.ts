import { ToastAndroid } from 'react-native';

import { navigator } from '../pages/AppNavigator';
import store from '../redux/store';

export default class AclNavigation {
    private navigation;
    constructor(navigation: any) {
        this.navigation = navigation;
    }

    push(path: string, params?: object) {
        if (this.checkAccess(path)) {
            this.navigation.push(path, params);
        }
    }
    navigate(path: string, params?: object) {
        if (this.checkAccess(path)) {
            this.navigation.push(path, params);
        }

    }

    // check whther the url has access
    checkAccess(path:string) {
        return true;
        // get allwed rights of route
        if (navigator[path]) {
            if (navigator[path].rights) {
                // check whether the user have enough rights to the path
                let hasAccess = false;
                let userRights = store.getState().appState.userRights;
                navigator[path].rights.forEach(right => {
                    if(userRights[right]) {
                        hasAccess = true;
                    }
                });
                if (!hasAccess) {
                    ToastAndroid.show('User has no access', ToastAndroid.SHORT);
                }
                return hasAccess;
            } else {
                // public
                return true;
            }
        } else {
            return false;
        }
    }
}
