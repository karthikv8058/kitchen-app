import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import { convertRGBToHex, getFontColorTone } from '@components/FontColorHelper';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    ActivityIndicator,
    AsyncStorage,
    FlatList,
    Image,
    StyleSheet,
    Text,
    ToastAndroid,
    TouchableOpacity,
    View,
    TouchableNativeFeedback,
} from 'react-native';
import Orientation from 'react-native-orientation-locker';

import ioc, { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import { menuCategory, WORK_STATION } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import { setAuthToken } from '../redux/AppActions';
import store from '../redux/store';
import CheckBox from 'react-native-check-box';
import Icon from 'react-native-vector-icons/Feather';
import StorageService, { StorageKeys, Storage } from '@services/StorageService';
import StationService from '@services/StationService';
import UserService from '@services/UserService';
import NavigationService from '@services/NavigationService';

interface Props { navigation: any; route: any; }
interface State {
    stationList: any;
    isLoading: boolean;
    isStationSelected: any;
    printerList:any
}
export default class StationList extends AbstractComponent<Props, State> {
    private userService: UserService = Bind('userService');
    private storageService: StorageService = Bind('storageService');
    private stationService: StationService = Bind('stationService');
    private navigationService: NavigationService = Bind('navigationService');

    private stationId: String[] = [];
    private tooblarItems: any[] = [];
    private userName: any = '';
    private stationUserData = [
        {
            changeUser: 'Change user'
        },
    ];
    constructor(props: Props) {
        super(props);
        this.state = {
            stationList: [],
            isLoading: true,
            isStationSelected: [],
            printerList:[]
        };
        this.renderStation = this.renderStation.bind(this);
        this.setUserStations = this.setUserStations.bind(this);
        this.expireLogout = this.expireLogout.bind(this);
    }

    componentDidMount() {
        this.userName = this.storageService.getFast(Storage.CHEF_NAME);

        if (!this.props.route.params.navigationPath) {
            this.userId = this.props.route.params.userId;
        } else {
            AsyncStorage.getItem('userId').then(userId => {
                this.userId = userId;
            });
        }
        if (this.props.route.params.backNavigation) {
            this.stationId = [],
                setTimeout(() => {
                    this.LoadStationList();
                }, 3000);
        } else {
            this.LoadStationList();
        }
        this.loadToolbarItems();
    }

    loadToolbarItems = () => {
        this.tooblarItems.push(<TouchableOpacity
            activeOpacity={0.8}
            onPress={this.setUserStations.bind(this)} style={{ height: 40, width: 40, marginBottom: 15 }}>
            <Icon name='check' size={40} color={colors.white} />
        </TouchableOpacity>);
    }

    checkStationList() {
        if (this.props.route.params.navigationPath) {
            this.setState({
                isStationSelected: []
            });
        } else {
            this.getStationList();
        }
    }

    getStationList() {
        this.stationService.loadStationsList().then((stations: any) => {
            this.setState({ isLoading: false });
            this.getStationData(stations);
        });
    }

    getStationData(stationList: any) {
        let a = this.state.stationList;
        for (let i = 0; i < a.length; i++) {
            let stations = a[i];
            for (let j = 0; j < stationList.length; j++) {
                let stationlist = stationList[j];
                if (stations.uuid === stationlist.stationid) {
                    let { isStationSelected } = this.state;
                    isStationSelected[i] = true;
                    this.setState({ isStationSelected });
                    this.stationId.push(stationlist.stationid);
                }
            }
        }
    }

    LoadStationList() {
        this.stationService.loadStations().then((stations: any) => {
            this.checkStationList();
            this.setState({
                stationList: stations,
                isLoading: false
            });
            this.getPrinterList()
        });
    }
    getPrinterList(){
        this.stationService.printerList().then((res: any) => {
            this.setState({
                printerList: res,
                isLoading: false
            });
        });
    }
    renderStationList() {
        let stations = this.state.stationList;
        if (!this.props.route.params.fromTasks) {
            stations = stations.concat(this.stationUserData);
        }

        return (
            <View style={{ flexDirection: 'column', flex: 1, margin: 20 }}>
                <Text style={{ color: colors.white, marginTop: 20 }}>{t('station-list.hi') + this.userName + `${this.state.stationList.length > 0 ?
                    t('station-list.work-station') : t('station-list.no-station')}`}</Text>
                <FlatList
                    style={styles.listStyle}
                    data={stations}
                    extraData={this.state}
                    renderItem={({ item, index }) => this.renderStation(item, index)}
                />
            </View>

        );
    }
    checkStyle(isSelcted: boolean, itemColor: any) {
        return (!isSelcted ? {
            color: itemColor,
            marginBottom: 5
        } : {
                color: itemColor,
                marginBottom: 5,
                marginLeft: 8
            });

    }
    returnChnageUser(stations: any, index: number) {
        return (
            <TouchableOpacity style={{ width: 150 }} onPress={this.expireLogout}>
                <View style={{
                    flexDirection: 'row',
                    paddingLeft: 10,
                    marginTop: 10,
                    backgroundColor: stations.color,
                    borderColor: stations.color,
                    height: 45,
                    alignItems: 'center',
                    borderRadius: 10,
                }}>
                    <Text
                        style={{ color: colors.white, fontSize: 15 }}>{stations.changeUser}</Text>
                </View>
            </TouchableOpacity>
        );
    }
    returnItems(stations: any, index: number) {
        let likelyHood = 0;
        let itemColor = stations.changeUser ? '#fffff' : convertRGBToHex(getFontColorTone(likelyHood, stations.color));
        let isChangeUser: boolean = stations.changeUser ? true : false;
        return (
            <View style={{ flexDirection: 'row' ,flex:1}}>
                < TouchableOpacity style={{flex:.9}} onPress={() => this.selectStation(index, stations.uuid, isChangeUser)
                }>
                    <View style={{
                        flexDirection: 'row',
                        flex: 1,
                        paddingLeft: 10,
                        marginTop: 10,
                        backgroundColor: stations.color,
                        borderColor: stations.color,
                        height: 45,
                        alignItems: 'center',
                        borderRadius: 10,
                    }}>

                        <CheckBox
                            isChecked={this.state.isStationSelected[index]}
                            checkBoxColor={itemColor}
                            rightTextStyle={'white'}
                            onClick={() => this.selectStation(index, stations.uuid, isChangeUser)}
                        />
                        {stations.changeUser ? <Text onPress={this.expireLogout}
                            style={{ color: colors.white, fontSize: 15 }}>{stations.changeUser}</Text> :
                            <Text style={this.checkStyle(this.state.isStationSelected[index], itemColor)}>
                                {stations.name}</Text>}

                    </View>
                </TouchableOpacity >
               {!!this.props.route.params.fromTasks && stations.printer_uuid ? <TouchableOpacity onPress={()=>this.testStationPrinter(stations.uuid)} style={{flex:.1,marginTop:20,marginLeft:10
            }}>
                    <Icon name='printer' size={20} color={colors.white} />
                </TouchableOpacity>:null}
            </View>

        );
    }
    testStationPrinter=(stationId:any)=>{
        this.userService.testStationPrinter(stationId).then(response => {
                if (!!response) {
                    ToastAndroid.show('Print successful', ToastAndroid.SHORT);

                }else{
                    ToastAndroid.show('Something went wrong, Please check the printer', ToastAndroid.SHORT);

                }
        });
    }
    renderStation(stations: any, index: number) {
        let isChangeUser: boolean = stations.changeUser ? true : false;
        return (
            isChangeUser ? this.returnChnageUser(stations, index) : this.returnItems(stations, index)
        );

    }

    expireLogout() {
        this.userService.userLogout().then(response => {
            AsyncStorage.setItem('loginStatus', '0');
            AsyncStorage.setItem('authToken', '');
            store.dispatch(setAuthToken(''));
            this.navigationService.reset('LoginUserListPage');
        });
    }

    selectStation(index: number, stationId: string, isChangeUser: boolean) {
        if (!isChangeUser) {
            let { isStationSelected } = this.state;
            isStationSelected[index] = !isStationSelected[index];
            this.setState({ isStationSelected });
            if (this.state.isStationSelected[index]) {
                this.stationId.push(stationId);
            } else {
                for (let i = 0; i < this.stationId.length; i++) {
                    if (this.stationId[i] === stationId) {
                        this.stationId.splice(i, 1);
                    }
                }
            }
        }

    }

    setUserStations = () => {
        this.storageService.set(StorageKeys.TASKK_FILTER, JSON.stringify({
            showOtherChefTasks: false,
            showWaitingMachineTasks: false,
            stations: this.stationId ? this.stationId : []
        }));
        this.userService.addStationUser(this.stationId).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                if (response) {
                    if (!!this.props.route.params.fromTasks) {
                        this.navigationService.pop();
                    } else {
                        this.navigationService.reset('BlankPage');
                    }

                }
            }
        });
    }
    checkMenuAvailability() {
        return !this.props.route.params.backNavigation;
    }
    render() {
    
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideChat={!this.checkMenuAvailability()}
                hideBack={!this.checkMenuAvailability()}
                doNaviagte={true}
                setStations={true}
                category={menuCategory.profileCategory}
                toolbarMenu={this.tooblarItems}
                index={WORK_STATION}>
                <View style={styles.container}>
                    {this.state.isLoading ?
                        <ActivityIndicator size='large' color={colors.white} />
                        :
                        this.renderStationList()}
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        marginLeft: 10,
        marginRight: 10,
        justifyContent: 'center',
        alignItems: 'center',
    },
    listStyle: {
        flex: 1,
        marginLeft: 10,
        paddingRight: 10,
        margin: 10,
        fontWeight: 'bold',
    },
    stationContainer: {
        color: colors.black,
        marginBottom: 5
    },
    stationNameContainer: {
        color: colors.black,
        marginBottom: 5,
        marginLeft: 8
    },
});
