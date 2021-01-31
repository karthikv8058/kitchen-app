import AppBackground from '@components/AppBackground';
import ClientServer from '@native/ClientServer';
import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import {
    ActivityIndicator,
    FlatList,
    PermissionsAndroid,
    StyleSheet,
    Text,
    ToastAndroid,
    TouchableOpacity,
    View,
    RefreshControl
} from 'react-native';
import { setIP } from '../redux/AppActions';
import Orientation from 'react-native-orientation-locker';
import { connect } from 'react-redux';
import { Bind } from '../ioc/ServiceContainer';
import StorageService, { Storage } from '@services/StorageService';
import Restaurant from '@models/Restaurant';
import RestrauntService from '@services/RestrauntService';
import User from '@models/User';
import NavigationService from '@services/NavigationService';
import LoaderWithText from '@components/LoaderWithText';
import ApiBuilder from '@api/routes';

interface Props {
    navigation: any;
    readDescriptionInSteps: Function;
    readIngredientInDetail: Function;
    setIP: any;
}

interface State {
    restuarants: Restaurant[];
    lastSelectedRestuarant: Restaurant;
    loaderText: string;
    isStartingServer: boolean;
}


const LOADING_RESTRAUNTS = "Loading Restraunts";
const LOADING_START_SERVER = t('restraunt-list.synching');

class RestrauntList extends Component<Props, State> {

    private clientServer: ClientServer = new ClientServer();
    private storageService: StorageService = Bind('storageService');
    private restrauntService: RestrauntService = Bind('restrauntService');
    private navigationService: NavigationService = Bind('navigationService');


    private user: User | null = null;


    private serverStartListner?: any;
    private notWorkingIPs: any ={};

    private lastRestaurant : Restaurant | null = null;

    constructor(props: Props) {
        super(props);
        this.state = {
            restuarants: [],
            loaderText: LOADING_RESTRAUNTS,
            isStartingServer: false,
        };
    }

    componentDidMount() {
        //this.user = this.props.navigation.getParam('user', null);
        this.user = this.props.route.params.user;

        // Orientation.lockToLandscapeLeft();

        this.clientServer.getLastRestaurantToken().then(restraunt=>{
            this.lastRestaurant = restraunt;
        });

        this.getRestaurantList();
        
        this.serverStartListner = (type: number) => {
            if(type == 1 ){
                this.storageService.set(Storage.SERVER_IP, "localhost");
                let apiBuilder:ApiBuilder = Bind('apiBuilder');
                apiBuilder.setIP("localhost");
                this.showLogin();
            }else if(type == 3 ){
                ToastAndroid.show("Can't start server",ToastAndroid.SHORT);
                this.notWorkingIPs[this.state.lastSelectedRestuarant?.server_ip] = true;
                this.lastRestaurant = null;
                this.setState({
                    isStartingServer: false,
                    loaderText:""
                });
            }

        }

        this.clientServer.addListenerForServerUpdate(this.serverStartListner)
    }

    loginExistingServer = (ip:string) => {
        this.setState({
            loaderText: "Connecting "+ip,
            isStartingServer: false
        });
        this.restrauntService.checkRestrauntServer(ip).then(isRestaurantAvailable => {
            if (isRestaurantAvailable) {
                this.storageService.set(Storage.SERVER_IP, ip);
                Bind('apiBuilder').setIP(ip);
                this.props.setIP(ip);
                this.navigationService.push('LoginUserListPage');
            }else{
                this.setState({
                    loaderText: "",
                    isStartingServer: false
                });
                ToastAndroid.show("Can't connect to this server",ToastAndroid.SHORT);
            }
        });
    }

    getRestaurantList = () => {
        this.setState({
            loaderText: LOADING_RESTRAUNTS
        });
        if (this.user && this.user.token) {
            this.restrauntService
                .getRestaurantList(this.user.token)
                .then((response: Restaurant[]) => {
                    this.setState({
                        restuarants: response,
                        loaderText: ""
                    });
                });
        }
    }


    showLogin = () => {
        this.clientServer.getIPAddress().then(ip => {
            this.storageService.set(Storage.SERVER_IP, ip).then(() => {
                this.clientServer.removeServerUpdateListener(this.serverStartListner);
                this.navigationService.reset('LoginWithPasswordPage', {
                    user: this.user,
                    serverIp: ip,
                });
            });
        });
    }

    startServer(restaurant: Restaurant) {

        if(restaurant.server_ip && this.lastRestaurant?.uuid != restaurant.uuid){
            this.loginExistingServer(restaurant.server_ip);
            return;
        }

        this.setState({
            loaderText: LOADING_START_SERVER,
            isStartingServer: true,
            lastSelectedRestuarant:restaurant
        });
        let token = this.user && this.user.token ? this.user.token : "";
        PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE).then(response => {
            if (response) {
                this.storageService.set(Storage.SERVER_RUNNING, '1');
                this.clientServer.startServerAndSync(restaurant, token).then(() => {

                });
            } else {
                this.setState({
                    loaderText: "",
                    isStartingServer: false
                });
                ToastAndroid.show(t('connect-to-server.cannot-proceed-without-storage-permission'), ToastAndroid.SHORT);
            }
        });
    }



    renderRestuarant = (item: any) => {
        let restaurant: Restaurant = item.item;
        return (
            <TouchableOpacity
                style={styles.restuarant}
                onPress={() => { this.startServer(restaurant) }}>
                <Text style={styles.textRestuarant}>{restaurant.name}</Text>
                {restaurant.server_ip && this.lastRestaurant?.uuid != restaurant.uuid? <Text style={{paddingHorizontal : 8}}>Connect server @ {restaurant.server_ip}</Text> : null}
            </TouchableOpacity>
        );

    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideChat={true}
                doNaviagte={true}
                hideBack={this.state.isStartingServer}
            >

                <View style={styles.container}>
                    {this.state.loaderText ?
                        <LoaderWithText text={this.state.loaderText} />
                        :
                        <FlatList
                            style={{ width: '70%', marginTop: 10, marginLeft: 5 }}
                            data={this.state.restuarants}
                            renderItem={this.renderRestuarant}
                            keyExtractor={item => item.uuid}
                            refreshing={this.state.loaderText != ""}
                            onRefresh={this.getRestaurantList}
                        >
                              <RefreshControl refreshing={this.state.loaderText != ""} onRefresh={this.getRestaurantList} />

                            </FlatList>}
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    restuarant: {
        flex: 1,
        paddingLeft: 10,
        marginTop: 10,
        backgroundColor: colors.stationBackground,
        borderWidth: 2,
        borderColor: colors.stationBackground,
        color: colors.black,
        minHeight: 45,
        borderRadius: 10,
    },
    textRestuarant: {
        color: colors.black,
        marginHorizontal: 10,
        marginBottom:6,
        marginTop:10,
        fontSize: 15
    },
    loaderContainer: {
        flex: 1,
        flexDirection: 'column',
        color: colors.white,
    },
    message: {
        color: colors.white,
        fontSize: 20,
        paddingHorizontal: 20,
    },
    loader: {
        flex: 1,
        alignSelf: 'center'
    }
});

export default connect(null, { setIP })(RestrauntList);
