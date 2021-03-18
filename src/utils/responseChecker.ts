import { AsyncStorage } from 'react-native';
import { StackActions, NavigationActions } from 'react-navigation';
import { setAuthToken } from '../redux/AppActions';
import store from '../redux/store';

export const responseChecker = (response: any, navigation: any) => {
    if (response.error === 403) {
        //ToastAndroid.show(t('passwordScreen.session-expired'), ToastAndroid.LONG);
        AsyncStorage.setItem('loginStatus', '0');
        AsyncStorage.setItem('authToken', '');
        store.dispatch(setAuthToken(''));
        const resetAction = StackActions.reset({
            index: 0,
            actions: [NavigationActions.navigate({ routeName: 'LoginUserListPage' })],
        });
        navigation.dispatch(resetAction);
        return false;
    } else {
        return true;
    }
};
