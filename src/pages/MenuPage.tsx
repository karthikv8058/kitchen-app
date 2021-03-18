import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import StorageService, { Storage } from '@services/StorageService';
import {
    Alert,
    AsyncStorage,
    Dimensions,
    Image,
    PixelRatio,
    Text,
    ToastAndroid,
    View,
} from 'react-native';
import {
    TouchableNativeFeedback, TouchableOpacity,
} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Feather';
import { responseChecker } from '../utils/responseChecker';
import { setAuthToken } from '../redux/AppActions';
import store from '../redux/store';
import { Cancel, LogOut } from '../utils/constants';
import { userRights } from '../utils/userRights';
import UserService from '@services/UserService';
import NavigationService from '@services/NavigationService';
import PermissionService from '@services/PermissionService';

interface Props {
    navigation: any;
}
interface State {
    height: any;
    width: any;
    posAccess: boolean;
    inventoryAccess: boolean;
    viewOrder: boolean;
}
export default class MenuPage extends AbstractComponent<Props, State> {

    private userService: UserService = Bind('userService');
    private storageService: StorageService = Bind('storageService');
    private navigationService: NavigationService = Bind('navigationService');
    private permissionService: PermissionService = Bind('permissionService');

    private menuMyTasks: Menu = { title: 'My Tasks', icon: require('@components/assets/mytask.png'), route: 'TaskOverview' };
    private menuOrder: Menu = { title: 'Orders', icon: require('@components/assets/order.png'), route: 'OrderOverview' };
    private menuInventory: Menu = { title: 'Inventory', icon: require('@components/assets/inventory.png'), route: 'InventoryPage' };
    private menuConcept: Menu = { title: 'Concept', icon: require('@components/assets/concept.png'), route: 'RecipeLookUp' };

    constructor(props: Props) {
        super(props);
        this.state = {
            height:  Dimensions.get('window').height,
            width: Dimensions.get('window').width,
            posAccess: false,
            inventoryAccess: false,
            viewOrder: false
        };
    }
    componentDidMount() {
        this.storageService.set(Storage.CONNECTED_SERVER_IP, this.apiBuilder.getIP());
        this.storageService.set(Storage.USER_READY, true);
    }

    getuserRight() {
        this.userService.getUserRights().then(response => {
            if (responseChecker(response, this.props.navigation)) {
                AsyncStorage.getItem('userRights').then((userRight: string) => {
                    if (!!response.length > 0 && JSON.stringify(response) !== userRight) {
                        AsyncStorage.setItem('userRights', JSON.stringify(response));
                        this.setState({
                            inventoryAccess: userRights('app_view_inventory', JSON.stringify(response)),
                            posAccess: userRights('app_new_orders', JSON.stringify(response)),
                            viewOrder: userRights('app_view_orders', JSON.stringify(response)),
                        });
                    } else {
                        this.setState({
                            inventoryAccess: userRights('app_view_inventory', userRight),
                            posAccess: userRights('app_new_orders', userRight),
                            viewOrder: userRights('app_view_orders', userRight),
                        });
                    }
                });
            }
        });
    }

    navigate = (route: string, hasPermssion: boolean) => {

        if (hasPermssion) {
            this.navigationService.push(route);
        }

        // if (title == 'Inventory') {
        //     this.navigationService.push(route);
        //     this.props.navigation.navigate(route)
        //     // if (!!this.state.inventoryAccess) {
        //     //     this.props.navigation.navigate(route)
        //     // } else {
        //     //     ToastAndroid.show(t('order-overview.view-inventory'), ToastAndroid.SHORT);
        //     // }
        // } else if (title == 'Orders') {
        //     this.props.navigation.navigate(route)
        //     // if (!!this.state.viewOrder) {
        //     //     this.props.navigation.navigate(route)
        //     // } else {
        //     //     ToastAndroid.show(t('order-overview.view-order'), ToastAndroid.SHORT);
        //     // }
        // } else {
        //     this.props.navigation.navigate(route)
        // }
    }

    renderMenu(menu: Menu) {
        let disabled = !this.permissionService.checkRoutePermission(menu.route);
        return (<TouchableOpacity onPress={this.navigate.bind(this, menu.route, !disabled)}>
            <View style={{
                justifyContent: 'center',
                alignItems: 'center', flexDirection: 'column',  flex: 1, padding: 40,
            }} >
                <Image style={{ alignSelf: 'center',width:45,height:50, resizeMode: 'center', tintColor: disabled ? "#888" : colors.white }}
                    source={menu.icon} />
                <Text style={{marginTop:10, color: disabled ? "#888" : colors.white, textAlign: 'center', fontSize: 16 * PixelRatio.getFontScale() }}>{menu.title}</Text>
            </View>
        </TouchableOpacity>
        );
    }

    logoutUser() {
        this.userService.userLogout().then(response => {
            AsyncStorage.setItem('loginStatus', '0');
            AsyncStorage.setItem('authToken', '');
            store.dispatch(setAuthToken(''));
            this.navigationService.reset('LoginUserListPage');
        });
    }

    logout() {
        Alert.alert(LogOut, t('menu-component.are-you-sure-you-want-to-logout'),
            [{
                text: Cancel, style: 'cancel'
            },
            {
                text: LogOut, onPress: () => {
                    this.logoutUser();
                }
            }],
            { cancelable: false });
    }

    goToPos = () => {
        this.navigationService.push('PosPage', {
            orderId: 0
        });
    }

    renderFirstRow() {
        return (
            <View style={{ flex: 1, flexDirection: 'row', }}>
                <View style={{ flex: 1, flexDirection: 'row', }}>
                    <View style={{ flex: .9, alignItems: 'center', }}>
                        {this.renderMenu(this.menuMyTasks)}
                    </View>
                    <View style={{ flex: 1.1, alignItems: 'center', }}>
                        {this.renderMenu(this.menuOrder)}
                    </View>
                </View>
            </View>
        );
    }
    renderSecondRow() {
        return (
            <View style={{ flex: 1, flexDirection: 'row' }}>
                <View style={{ flex: .9, alignItems: 'center', }}>
                    {this.renderMenu(this.menuConcept)}
                </View>
                <View style={{ flex: 1.1, alignItems: 'center', }}>
                    {this.renderMenu(this.menuInventory)}
                </View>
            </View>
        );
    }
    goToWeb(goToProfile: boolean) {
        this.props.navigation.push('SmarttoniWeb', {
            goToProfile: goToProfile
        });
    }
    renderThirdRow() {
        let iconSize =25
        return (
            <View style={{ flex: .45, flexDirection: 'row', marginRight: 5 }}>
                <View style={{ flex: 1, justifyContent: 'center' }}>
                    <TouchableNativeFeedback onPress={() => this.goToWeb(true)}>
                        <View style={{ flexDirection: 'row', justifyContent: 'center' }}>
                            <Image style={{
                                alignSelf: 'center', maxWidth: iconSize,
                                maxHeight: iconSize
                            }}
                                source={require('@components/assets/myprofile.png')} />
                            <Text style={{
                                color: colors.white, alignSelf: 'center', marginLeft: 10,
                                fontSize: 15 * PixelRatio.getFontScale(),
                            }}>My Profile</Text>
                        </View>
                    </TouchableNativeFeedback>
                </View>
                <View style={{ flex: 1.5, justifyContent: 'center' }}>
                    <TouchableNativeFeedback onPress={() => this.goToWeb(false)} >
                        <View style={{ flexDirection: 'row', justifyContent: 'center' }}>
                            <Image style={{
                                alignSelf: 'center', height: iconSize, width: iconSize
                            }}
                                source={require('@components/assets/config.png')} />
                            <Text style={{
                                color: colors.white, alignSelf: 'center', marginLeft: 10, fontSize: 15 * PixelRatio.getFontScale(),
                            }}>Kitchen Configuration</Text>
                        </View>

                    </TouchableNativeFeedback>
                </View>
                <View style={{ flex: .5, justifyContent: 'center' }}>
                    <TouchableNativeFeedback onPress={() => this.logout()} style={{ marginLeft: 5 }}>
                        <Icon style={{ alignSelf: 'center' }} name='log-out' size={iconSize} color={colors.white} />
                    </TouchableNativeFeedback>
                </View>

            </View>
        );
    }

    popBack = () => {
        this.props.navigation.goBack();
    }

    renderPopButton() {
        return (
            <View style={{ flex: 0.05, justifyContent: 'center', flexDirection: 'column', }}>
                <TouchableNativeFeedback style={{ alignSelf: 'center' }} onPress={this.popBack}>
                    <Icon style={{ alignSelf: 'center' }} name='chevron-right'
                        size={Dimensions.get('window').width / 20} color={colors.white} />
                </TouchableNativeFeedback>
            </View>
        );
    }

    renderMenuItems() {
        return (
            <View style={{ flex: 1, flexDirection: 'row' }}>
                <View style={{ flex: 1, flexDirection: 'column' }} >
                    {this.renderFirstRow()}
                    <View style={{
                        zIndex: 999,
                        flexDirection: 'row', flex: .01, height: 60,
                        justifyContent: 'center', alignItems: 'center'
                    }}>
                        <View style={{ flex: 1, height: 1, backgroundColor: colors.white, }} />
                        {this.permissionService.checkRoutePermission('PosPage') && <TouchableOpacity onPress={this.goToPos} >
                            <View style={{ zIndex: 999, justifyContent: 'center', alignItems: 'center', marginBottom: 10 }}>
                                <Image style={{ maxHeight: 30 * PixelRatio.getFontScale(), width: 30 * PixelRatio.getFontScale(), resizeMode: 'center', }}
                                    source={require('@components/assets/neworder.png')} />
                                <Text style={{
                                    color: colors.white,
                                    marginTop: 5,
                                    fontSize: 16 * PixelRatio.getFontScale(),
                                }}>New Order</Text>
                            </View>
                        </TouchableOpacity>}
                        <View style={{ flex: .05, height: 1, backgroundColor: colors.white }} />
                    </View>
                    {this.renderSecondRow()}
                    <View style={{ height: 1, backgroundColor: colors.white }} />
                    {this.renderThirdRow()}
                </View>
            </View >
        );
    }
    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideBack={true}
                doNaviagte={true}>
                <View style={{ flex: 1 }}>
                    {this.renderMenuItems()}
                </View>
            </AppBackground>

        );
    }
}

interface Menu {
    title: string;
    icon: any;
    route: string;
}
