import ApiBuilder from '@api/routes';
import AppBackground from '@components/AppBackground';
import ClientServer from '@native/ClientServer';
import EventEmitterService, { EventTypes } from '@services/EventEmitterService';
import StorageService, { Storage } from '@services/StorageService';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import { AsyncStorage, FlatList, PermissionsAndroid, StyleSheet, ToastAndroid, TouchableOpacity, View } from 'react-native';
import { Text } from 'react-native-animatable';
import { connect } from 'react-redux';
import ioc, { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import Restaurant from '@models/Restaurant';
import NavigationService from '@services/NavigationService';
import RestrauntService from '@services/RestrauntService';

interface Props {
    navigation: any;
}
interface State {
    restuarantList: Restaurant[];
    isLoading: boolean;
    isServerRunning: boolean;
}

class DetectedRestuarants extends Component<Props, State> {

    private eventEmitterService: EventEmitterService = ioc.ServiceFactory.getServiceBy('eventEmitterService');
    private clientServer: ClientServer = new ClientServer();
    private storageService: StorageService = ioc.ServiceFactory.getServiceBy('storageService');
    private udpListener: any;


    private navigationService: NavigationService = Bind('navigationService');
    private restrauntService: RestrauntService = Bind('restrauntService');

    constructor(props: Props) {
        super(props);
        this.state = {
            restuarantList: [],
            isLoading: true,
            isServerRunning: false
        };
        this.clientServer.getIPAddress().then(ip => {
            this.storageService.set('deviceIp', ip);
        });
    }

    componentDidMount() {
        this.navigationService.setNavigation(this.props.navigation);
        
        this.clientServer.getCurrentRestaurant().then(restuarant => {
            if (restuarant) {
                this.state.restuarantList.push(restuarant)
                this.setState({
                    isServerRunning: restuarant != null
                })
            }
        });

        this.udpListener = {
            type: EventTypes.SERVER_FOUND, callback: (restuarant: Restaurant) => {
                if (restuarant.ip) {
                    if(!this.state.restuarantList.some(r => r.ip == restuarant.ip )){
                        this.state.restuarantList.push(restuarant);
                        this.setState({ restuarantList: this.state.restuarantList });
                    }
                }
            }
        };
        this.eventEmitterService.addListner(this.udpListener);
        this.clientServer.sendSSR();
    }

    componentWillUnmount() {
        this.eventEmitterService.removeListner(this.udpListener);
    }

    showAdvancedStartup = () => {
        AsyncStorage.getItem('connectionSettings').then(serverIP => {
            let ipFound: boolean = serverIP ? true : false;
            this.navigationService.push('AdvancedStartupPage', { manualSettings: ipFound });
        });
    }

    openNewServer = () => {
        PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE).then(response => {
            if (response) {
                this.navigationService.push('ServerStartupPage');
            } else {
                ToastAndroid.show(t('connect-to-server.cannot-proceed-without-storage-permission'), ToastAndroid.SHORT);
            }
        });
    }

    showLogin = (restaurant: Restaurant) => {

        this.restrauntService.checkRestrauntServer(restaurant.ip).then(isRestaurantAvailable => {
            if (isRestaurantAvailable) {
                this.storageService.set(Storage.SERVER_IP, restaurant.ip);
                let apiBuilder:ApiBuilder = Bind('apiBuilder');
                apiBuilder.setIP(restaurant.ip);
                this.navigationService.push('LoginUserListPage');
            }
        });
    }

    renderRestaurant = (item: any) => {
        let restaurant = item.item;
        return (
            <TouchableOpacity
                onPress={this.showLogin.bind(this, restaurant)}
                style={styles.listItem}>
                <Text style={styles.textButton}>{restaurant.name}</Text>
            </TouchableOpacity>
        );
    }

    renderFooter = () => {
        return (
            <View>
                {!this.state.isServerRunning &&
                    <TouchableOpacity
                        onPress={this.openNewServer}
                        style={styles.listItem} >
                        <Text style={styles.textButton}>{t('detected-restaurant.open-restaurant')}</Text>
                    </TouchableOpacity>
                }
                <TouchableOpacity
                    onPress={this.showAdvancedStartup}
                    style={{ alignItems: 'center', marginRight: 35, marginTop: 10 }}>
                    <Text style={{ color: colors.white, fontSize: 16, }}>{t('detected-restaurant.advanced')}</Text>
                </TouchableOpacity >
            </View>
        );
    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideBack={true}
                hideChat={true}
                doNaviagte={true}
                checkBackgroundColor={true}>
                <View style={styles.container}>
                    <View style={{ flexDirection: 'column', alignItems: 'center', flex: 1 }}>
                        <FlatList
                            data={this.state.restuarantList}
                            renderItem={this.renderRestaurant}
                            keyExtractor={item => item.uuid}
                            style={{ marginTop: 20, marginLeft: 5 }}
                            ListFooterComponent={this.renderFooter}
                        />
                    </View>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: colors.black,
    },
    textButton: {
        color: colors.white,
        margin: 10,
        fontSize: 15,
        textAlign: 'center'
    },
    listItem: {
        backgroundColor: colors.gradientStart,
        color: colors.white,
        minHeight: 45,
        borderRadius: 10,
        marginTop: 10,
    }
});

export default DetectedRestuarants;
