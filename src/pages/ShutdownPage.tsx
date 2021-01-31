import * as React from 'react';
import { View, StyleSheet } from 'react-native';
import AppBackground from '@components/AppBackground';
import LoaderWithText from '@components/LoaderWithText';

import NavigationService from '@services/NavigationService';
import { Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage } from '@services/StorageService';

import WebServer from '@native/ClientServer';

interface Props {
}

interface State {
}

class ShutdownPage extends React.Component<Props, State> {

    private storageService: StorageService = Bind('storageService');
    private navigationService: NavigationService = Bind('navigationService');
    private webServer: WebServer = Bind('webServer');

    componentDidMount() {
        this.logoutServer();
    }
    logoutServer = () => {
        this.webServer.logout().then(r => {
            this.storageService.unset(Storage.SERVER_IP);
            this.storageService.unset('loginStatus');
            this.storageService.unset('isServerRunning');
            this.navigationService.reset('DetectedRestuarants');
        });
    }

    render() {
        return (
            <AppBackground
                navigation={null}
                hideBack={true}
                doNaviagte={true}
                hideChat={true}>
                <View style={styles.container}>
                    <LoaderWithText text='Server shutting down' />
                </View>
            </AppBackground>

        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    }
});
export default ShutdownPage;
