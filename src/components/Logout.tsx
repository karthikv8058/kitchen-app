import t from '@translate';
import { Alert, AsyncStorage } from 'react-native';

import { Cancel, LogOut } from '../utils/constants';
import AbstractComponent from './AbstractComponent';

interface Props {
    navigations: any;
    force: boolean;
}

interface State {
    force: boolean;
  }

export default class Logout extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.setState({
            force : false
        });
    }

    logout() {
        if (this.state.force !== false) {
            Alert.alert(LogOut, t('Logout.are-you-sure-wantto-logout'),
                [{
                    text: Cancel, onPress: () => { }, style: 'cancel'
                },
                {
                    text: LogOut, onPress: () => {
                        AsyncStorage.setItem('loginStatus', '' + 0);
                        this.props.navigations.navigate('LoginUserListPage');
                    }
                }],
                { cancelable: false });

        } else {
            AsyncStorage.setItem('loginStatus', '' + 0),
                this.props.navigations.navigate('LoginUserListPage');
        }
    }
    render() {
        return (
            this.logout
        );
    }
}
