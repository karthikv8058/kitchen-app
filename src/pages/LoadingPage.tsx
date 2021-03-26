import React, { Component } from 'react';
import {
    StyleSheet,
    View,
    Image,
} from 'react-native';
import RadialGradient from 'react-native-radial-gradient';
import Orientation from 'react-native-orientation-locker';
import ApiBuilder from '@api/routes';
import HttpClient from '@services/HttpClient';
import { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage } from '@services/StorageService';
import NavigationService from '@services/NavigationService';

interface Props {
    navigation: any;
    switchVR: Function;
    readDescriptionInSteps: Function;
    readIngredientInDetail: Function;
}

class LoadingPage extends Component<Props> {

    private httpClient: HttpClient = Bind(HTTP_CLIENT);
    private storageService: StorageService = Bind('storageService');
    private apiBuilder?: ApiBuilder = Bind("apiBuilder");

    private navigationService: NavigationService = Bind('navigationService');

    constructor(props: Props) {
        super(props);
        setTimeout(() => {
            this.navigateToStartup();
        }, 3000);
    }

    componentDidMount() {
        this.navigationService.setNavigation(this.props.navigation);
        // Orientation.lockToLandscapeLeft();
    }

    navigateToStartup = async () => {

        this.storageService.get(Storage.AUTH_TOKEN).then(token => {
            this.storageService.get(Storage.CONNECTED_SERVER_IP).then(serverIP => {
                if (token && serverIP) {
                    this.httpClient.get(this.apiBuilder!.paths!.ping).then(response => {
                        this.navigationService.reset("MenuPage");
                    }).catch(error => {
                        this.navigationService.reset("DetectedRestuarants");
                    })
                } else {
                    this.navigationService.reset("DetectedRestuarants");
                }
            });
        });
    }

    render() {
        return (
            <RadialGradient style={{ flex: 1 }}
                colors={['#00ABB4', '#00767C']}
                radius={300}>
                <View style={styles.container}>
                    <Image style={styles.imageContainer} resizeMode='contain' source={(require('@components/assets/splashimage.png'))} />
                </View>
            </RadialGradient>
        );
    }

}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    imageContainer: {
        width: '100%',
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
        alignSelf: 'center'
    }
});

export default LoadingPage;
