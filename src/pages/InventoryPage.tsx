import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import placeHolder from '@components/assets/placeholder.png';
import EditInventoryQuantity from '@components/EditInventoryQuantity';
import InventoryFilter from '@components/InventoryFilter';
import OrderDeliveryDetails from '@components/OrderDeliveryDetails';
import Course from '@models/Course';
import Meal from '@models/Meal';
import Order from '@models/Order';
import Place from '@models/Place';
import Rack from '@models/Rack';
import Recipe from '@models/Recipe';
import Room from '@models/Room';
import OrderService from '@services/OrderService';
import PermissionService, { Action } from '@services/PermissionService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import ImagePicker from 'react-native-image-crop-picker';
import NetInfo from '@react-native-community/netinfo';
import RNFetchBlob from 'rn-fetch-blob';
import { Picker } from '@react-native-picker/picker';

import {
    ActivityIndicator,
    Alert,
    Animated, AsyncStorage, Dimensions, FlatList,
    Image,
    LayoutAnimation,
    Modal,
    ScrollView, StyleSheet, Text,
    TextInput,
    ToastAndroid, TouchableHighlightBase, TouchableOpacity,
    Vibration,
    View
} from 'react-native';
import Orientation from 'react-native-orientation-locker';
import MenuDrawer from 'react-native-side-drawer';
import Icons from 'react-native-vector-icons/Entypo';
import Icon from 'react-native-vector-icons/Feather';
import ioc, { Bind, HTTP_CLIENT } from '../ioc/ServiceContainer';
import { DeliveryTypes } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import { userRights } from '../utils/userRights';
import { RNCamera } from 'react-native-camera';
import AntDesign from 'react-native-vector-icons/AntDesign';
import { ApiBaseUrl } from '@native/ClientServer';
import { List } from 'react-native-paper';

interface Props {
    navigation: any;
}

interface State {
    isLoading: boolean;
    recipeList: Recipe[];
    roomList: Room[] | any;
    inventoryQuantity: number;
    order: Order | any;
    date: Date;
    quantity: number;
    orderPlace: boolean;
    isSliderActive: boolean;
    outputQuantity: number;
    outputUnit: string;
    outputUnitId: string;
    editingUnitId: string;
    isInventoryOrderItemLoaded: boolean;
    adjustInventory: boolean;
    inventoryItem: Recipe[] | any;
    isPortrait: boolean,
    unitConversions: { from: string, to: string, name: string }[],
    units: { key: string, value: string }[],
    isBarcodeOpened: boolean,
    camera: any,
    barcodeData: any,
    newRecipeModalOpen: boolean,
    webAppCredentials: any,
    source: any,
    isLoaderVisible: boolean,
    recipeName: string,
    inventoryType: number,
    selectedUnit: any,
    isOpen: boolean
}

const SCHEME = 'http://';
const PORT = ':8888';

export default class InventoryPage extends AbstractComponent<Props, State> {
    httpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    apiBuilder = ioc.ServiceFactory.getServiceBy('apiBuilder');
    webUrl = ApiBaseUrl;

    private orderService: OrderService = Bind('orderService');
    private permissionService: PermissionService = Bind('permissionService');

    private tooblarItems: any[] = [];
    private tooblarList: any[] = [];
    private selectedRoom: string | undefined = '';
    private selectedStorage: string | undefined = '';
    private selectedRack: string | undefined = '';
    private selectedPlace: string | undefined = '';
    private searchText: string | undefined = '';
    private baseUrl: string | undefined = '';
    private room: string | undefined = '';
    private recipeId: string | undefined = '';
    private storage: string | undefined = '';
    private rack: string | undefined = '';
    private place: string | undefined = '';
    private shakeAnimation: Animated.Value;
    private editQuantityRef: any
    private orderDeliveryRef: any;
    private inventoryFilterRef: any;
    private recipes: Recipe[] = [];
    private selectedRecipe: Recipe | undefined;
    private manageInventory = false;
    private accessToOrder = false;
    camera: RNCamera | null;
    addedImageId: string;
    constructor(props: Props) {
        super(props);
        this.state = {
            isLoading: true,
            recipeList: [],
            roomList: [],
            inventoryQuantity: 1,
            order: {
                orderId: 0,
                meals: [],
                deliveryTime: new Date().getTime().toString(),
                tableNumber: '',
                isInventory: true,
                deliverableType: DeliveryTypes.ASAP,
                orderTo: 'MOBILE',
                deliveryDate: new Date(),
            },
            date: new Date(),
            quantity: 1,
            orderPlace: false,
            isSliderActive: false,
            outputQuantity: 1,
            outputUnit: '',
            outputUnitId: '',
            editingUnitId: '',
            isInventoryOrderItemLoaded: false,
            adjustInventory: false,
            inventoryItem: [],
            unitConversions: [],
            units: [],
            isBarcodeOpened: false,
            camera: {
                type: RNCamera.Constants.Type.back,
                flashMode: RNCamera.Constants.FlashMode.on,
            },
            barcodeData: '',
            newRecipeModalOpen: false,
            webAppCredentials: '',
            source: '',
            isLoaderVisible: false,
            recipeName: '',
            inventoryType: 1,
            selectedUnit: '',
            isOpen: false
        };
        this.shakeAnimation = new Animated.Value(0);
        this.loadInventoryList = this.loadInventoryList.bind(this);
    }

    componentDidMount() {
        // if (this.props.navigation.getParam('isInventoryPos', false)) {
        //     this.setState({
        //         orderPlace: true
        //     })
        //     if (!this.state.isInventoryOrderItemLoaded) {
        //         this.loadToolbarSecondColoumnItems()
        //     }
        // } else {
        // this.loadToolbarItems();
        // }

        this.loadToolbarItems();

        AsyncStorage.getItem('serverIP').then((ip) => {
            this.baseUrl = SCHEME + ip + PORT;
            this.loadInventoryList();
        });
        AsyncStorage.getItem('userRights').then((right) => {
            this.manageInventory = userRights('app_manage_inventory', right),
                //this.accessToOrder = userRights('app_new_inventory_orders', right);
                this.accessToOrder = true;
        });
        this.httpClient.post(this.apiBuilder.paths.webCredentials, {}).then((response: any) => {
            this.setState({
                webAppCredentials: response
            });
        }).catch((err: Error) => {
        });
    }

    loadInventoryList = () => {
        this.orderService.getInventoryList().then(inventoryResponse => {
            if (responseChecker(inventoryResponse, this.props.navigation)) {
                this.setState({
                    recipeList: inventoryResponse.recipes,
                    isLoading: false,
                    roomList: inventoryResponse.rooms,
                    unitConversions: inventoryResponse.unitConversions,
                    units: inventoryResponse.units
                });
                this.recipes = inventoryResponse.recipes;
                this.applyFilter();
            }
        }).catch(error => {
        });
    }
    getorderQuantity = (inventoryList: any) => {
        let orderQuantity: number = 0
        inventoryList!.forEach((element: Order) => {
            orderQuantity = orderQuantity + element.qty
        });
        return orderQuantity;
    }
    getExpectedQuantity = (inventory: Recipe) => {
        let expected: number = this.getorderQuantity(inventory.externalOrderWrappers)
        let consumption: number = this.getorderQuantity(inventory.openOrderWrappers);
        return ((parseInt(inventory.inventoryQuantity) + (expected)) - consumption)
    }
    renderOutputUnit(inventory: Recipe) {
        let qty = '';
        switch (inventory.inventoryType) {
            case 1: qty = 'Current:  ' + inventory.currentInventory; break;
            case 2: qty = 'No Stock'; break;
            case 3: qty = 'Infinity'; break;
        }
        return (
            <View style={{ flex: 2, }}>
                <Text style={{
                    color: colors.white,
                    fontSize: 15,
                    marginRight: 5,
                    marginTop: 30,
                    flex: 1
                }}>
                    {qty}
                </Text>
                <Text style={{
                    color: colors.white,
                    fontSize: 15,
                    marginRight: 5,
                    marginTop: 5,
                    flex: 1
                }}>
                    {inventory.inventoryType == 1 ? 'Expected: ' + inventory.expectedInventory : ''}
                </Text>
            </View>
        );
    }

    addInventoryOrder(item: Recipe | any) {
        this.selectedRecipe = item;

        let unit = "";
        if (this.state.outputUnitId && this.state.units) {
            for (let u of this.state.units) {
                if (u.uuid == this.state.outputUnitId) {
                    unit = u.symbol;
                    break;
                }
            }
        } else if (this.state.outputUnit != null) {
            unit = this.state.outputUnit;
        }
        const recipes: any = [
            {
                'name': item.recipeName,
                'quantity': this.state.inventoryQuantity,
                'uuid': item.uuid,
                'outputData': this.state.inventoryQuantity + ' ' + unit,
                'unit': this.state.outputUnitId,
            }
        ];
        const newMeal: Meal = {
            isSelected: true,
            recipes: recipes,
        };

        let order = this.state.order;
        order.meals!.forEach((element: Recipe) => {
            element.recipes.forEach((recipe: Recipe) => {
                if (recipe.uuid === item.uuid) {
                    const index = order.meals!.indexOf(element);
                    if (index > -1) {
                        order.meals!.splice(index, 1);
                    }
                }
            });
        });
        if (this.state.inventoryQuantity !== 0) {
            order.meals!.push(newMeal);
        }
        this.setState({
            order: order,
        });
    }

    toggle = (item: Recipe) => {
        if (!!this.state.orderPlace) {
            let inventoryQuantity = 1;
            this.state.order.meals!.forEach((element: Meal) => {
                element.recipes.forEach((recipe: Recipe) => {
                    if (recipe.uuid === item.uuid) {
                        inventoryQuantity = !!recipe.quantity ? recipe.quantity : 0;
                    }
                });
            });
            this.selectedRecipe = item;
            this.setState({
                inventoryQuantity: inventoryQuantity,
                outputQuantity: !!item.outPutQuantity ? item.outPutQuantity : 0,
                outputUnit: item.OutPutUnit,
                outputUnitId: item.unitId,
                editingUnitId: item.unitId,
                unitId: item.unitId,
                isSliderActive: false,
                adjustInventory: false,
                inventoryItem: item
            });
            this.editQuantityRef?.show();
        } else {
            if (!this.permissionService.hasPermission(Action.EDIT_INVENTORY) &&
                !this.permissionService.hasPermission(Action.CREATE_INVENTORY)) {
                ToastAndroid.show("You don't have permission to create and edit inventory orders", ToastAndroid.SHORT)
            } else {
                this.setState({
                    adjustInventory: true,
                    inventoryItem: item
                });
            }
        }
    }
    changeLayout = (uuid: string | undefined) => {
        LayoutAnimation.configureNext(LayoutAnimation.Presets.easeInEaseOut);
        this.state.recipeList.forEach((element: Recipe) => {
            if (element.uuid === uuid) {
                element.isExpanded = !element.isExpanded
            } else {
                element.isExpanded = false
            }
        });
        this.setState({
            recipeList: this.state.recipeList
        })
    }
    renderRecipeItem = (inventory: Recipe) => {
        this.getroomList(inventory);
        return (
            <TouchableOpacity onPress={this.toggle.bind(this, inventory)}>
                <View style={{
                    flexDirection: 'column',
                    minHeight: 100,
                    backgroundColor: colors.lookUpBackground,
                    borderColor: colors.lookUpBackground,
                    borderRadius: 10,
                    margin: 10,
                }}>
                    <View style={{
                        flexDirection: 'row', flex: 10
                    }}>
                        <TouchableOpacity style={{ height: 70 }} onPress={() => this.changeLayout(inventory.uuid)}>
                            <Icons name={!!inventory.isExpanded ? 'chevron-small-up' : 'chevron-small-down'} size={35} color={colors.white} />
                        </TouchableOpacity>
                        <Image style={{
                            height: 65, width: 65, borderRadius: 10, marginLeft: 5, marginTop: 10, flex: 1.3
                        }}
                            source={inventory.image ? { uri: this.baseUrl + '/get-image?filename=' + inventory.image } : placeHolder} >
                        </Image>
                        <View style={{ flex: 4 }}>
                            <Text numberOfLines={3} style={{
                                color: colors.white, fontWeight: 'bold', fontSize: 15, marginLeft: 10,
                                marginTop: 10,

                            }}>{inventory.recipeName}</Text>
                            {/* <View style={{
                                flexDirection: 'column', marginLeft: 10, marginTop: 5, marginBottom: 5
                            }}>
                                <Text style={{ color: colors.white }}>{this.room !== '' ? 'Room     :    ' + this.room : ''}</Text>
                                <Text style={{ color: colors.white }}>{this.storage !== '' ? 'Storage  :    ' + this.storage : ''}</Text>
                                <Text style={{ color: colors.white }}>{this.rack !== '' ? 'Rack       :    ' + this.rack : ''}</Text>
                                <Text style={{ color: colors.white }}>{this.place !== '' ? 'Place      :    ' + this.place : ''}</Text>
                            </View> */}
                        </View>
                        {this.renderOutputUnit(inventory)}
                    </View>

                    {!!inventory.isExpanded ? this.ExpandedItems(inventory)
                        : null}
                </View>
            </TouchableOpacity >
        );
    }

    ExpandedItems = (inventory: Recipe) => {
        return (
            <View style={{ height: !!inventory.isExpanded ? null : 0, overflow: 'hidden', }}>
                <View style={{ flexDirection: 'row' }}>
                    <Text style={{ flex: .8, color: colors.white, fontSize: 15, marginLeft: 10, marginTop: 5 }}> Expected delivery:</Text>
                    <View style={{ flex: 1.2, }}>
                        {this.getOpenOrders(inventory.externalOrderWrappers)}
                    </View>
                </View>
                <View>
                    <Text style={{ flex: .8, color: colors.white, fontSize: 15, marginLeft: 10, marginTop: 5 }}> Expected consumption:</Text>
                    <View style={{ flex: 1.2, }}>
                        {this.getOpenOrders(inventory.openOrderWrappers)}
                    </View>
                </View>
            </View>
        )
    }

    getOpenOrders(inventoryList: Recipe) {
        let rootView: any = [];
        inventoryList!.sort(function (a: Order | any, b: Order | any) {
            let nameA = a.deliveryTime;
            let nameB = b.deliveryTime;
            if (nameA < nameB) {
                return -1;
            }
            if (nameA > nameB) {
                return 1;
            }
            return 0;
        });

        inventoryList!.forEach((element: Order) => {
            rootView.push(
                <View style={{ flex: 1.2, marginLeft: 30, flexDirection: 'row', marginTop: 10, marginBottom: 10 }}>
                    <Text style={{ flex: .8, color: colors.white, textAlign: 'right', marginRight: 10 }}>
                        {
                            this.renderDate(element.deliveryTime) + '     ' +
                            this.msToTime(element.deliveryTime)}</Text>
                    <Text style={{ flex: .4, color: colors.white, textAlign: 'left', marginLeft: 8 }}>
                        {element.quantity}</Text>
                </View>
            );
        });
        return rootView;
    }

    checkDate = (dueDate: string, currentDate: string) => {
        let isToday: boolean = false
        let q = new Date(dueDate);
        let m = q.getMonth() + 1;
        let d = q.getDate();
        let y = q.getFullYear()

        let q1 = new Date(currentDate);
        let m1 = q1.getMonth() + 1;
        let d1 = q1.getDate();
        let y1 = q1.getFullYear()
        if (m === m1 && d === d1 && y === y1) {
            isToday = true
        } else {
            isToday = false
        }
        return isToday;
    }

    renderDate(deliveryTime: number | undefined) {
        let date = new Date(!!deliveryTime ? deliveryTime : '');
        let isToday: boolean = this.checkDate(date.toString(), Date().toLocaleString())

        let dd = date.getDate();
        let mm = date.getMonth() + 1;
        let yy = date.getFullYear();
        return !!isToday ? '' : '    ' + dd + '-' + mm + '-' + yy;
    }

    msToTime(duration: number | undefined) {
        let d = new Date(!!duration ? +duration : '');
        let datetext = d.toTimeString().split(' ')[0];
        let hours = (datetext.split(':')[0]);
        let minutes = (datetext.split(':')[1]);
        return (hours + ': ' + minutes);
    }

    getroomList(inventory: Recipe) {
        this.storage = '';
        this.rack = '';
        this.room = '';
        this.place = '';
        this.state.roomList.forEach((element: Room) => {
            if (!!inventory.roomId && inventory.roomId === element.uuid) {
                this.room = element.name;
            }
            element.storage.forEach(storage => {
                if (!!storage.uuid && storage.uuid === inventory.storageId) {
                    this.storage = storage.name;
                }
                storage.storageracks.forEach((rack: Rack) => {
                    if (rack.uuid && rack.uuid === inventory.rackId) {
                        this.rack = rack.name;
                    }
                    rack.places.forEach((places: Place | any) => {
                        if (places.uuid && places.uuid === inventory.placeId) {
                            this.place = places.name;
                        }
                    });
                });
            });
        });
    }

    toggleFilterModal = () => {
        this.inventoryFilterRef?.show();
    }


    loadToolbarItems = () => {
        this.tooblarItems = []
        this.tooblarItems.push(
            <>
                <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={() => this.setState({ isBarcodeOpened: true })} style={{ minHeight: 40, zIndex: 999, height: 20, width: 40, }}>
                    <AntDesign name='scan1' size={30} color={colors.white} />
                </TouchableOpacity>
                <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={this.toggleFilterModal.bind(this)} style={{ height: 40, width: 40, marginTop: 5 }}>
                    <Icon name='filter' size={30} color={colors.white} />
                </TouchableOpacity>
                {!this.state.orderPlace && this.permissionService.hasPermission(Action.CREATE_INVENTORY) ? <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={this.toggleOrder} style={{ height: 40, width: 40,}}>
                    <Icons  name='circle-with-plus' size={30} color={colors.white} />
                </TouchableOpacity> : null}
            </>
        );
    }

    loadToolbarSecondColoumnItems = () => {
        this.tooblarList = []
        this.setState({
            isInventoryOrderItemLoaded: true
        });
        this.tooblarList.push(
            <>
             
                <TouchableOpacity
                    onPress={this.closeOrder}
                    activeOpacity={0.8}
                    style={{ height: 40, width: 40,  }}>
                    <Icon name='x' size={40} color={colors.white} />
                </TouchableOpacity>
                {!this.state.orderPlace ?
                    <TouchableOpacity
                        onPress={this.toggleOrderType.bind(this)}
                        activeOpacity={0.8}
                        style={{ height: 40, width: 40,}}>
                        <Icon name='check' size={40} color={colors.white} />
                    </TouchableOpacity> : null}
            </>
        );
    }

    toggleOrderType = () => {
        if (this.state.order.meals!.length > 0) {
            this.orderDeliveryRef?.show();
            this.setState({
                isSliderActive: false
            });
        } else {
            ToastAndroid.show(t('inventory.validorder'), ToastAndroid.SHORT);
        }
    }

    goToInventoryPos = () => {
        if (!!this.accessToOrder) {
            let inventoryQuantity: number | undefined = 0;
            this.state.order.meals!.forEach((element: Recipe) => {
                element.recipes.forEach(recipe => {
                    if (recipe.uuid === this.state.inventoryItem.uuid) {
                        inventoryQuantity = recipe.quantity;
                    }
                });
            });
            this.selectedRecipe = this.state.inventoryItem;
            this.setState({
                orderPlace: true,
                adjustInventory: false,
                inventoryQuantity: inventoryQuantity,
                outputQuantity: this.state.inventoryItem.outPutQuantity,
                outputUnit: this.state.inventoryItem.OutPutUnit,
                outputUnitId: this.state.inventoryItem.unitId,
                editingUnitId: this.state.inventoryItem.unitId,
                isSliderActive: false,
            });
            this.editQuantityRef?.show();
            if (!this.state.isInventoryOrderItemLoaded) {
                this.loadToolbarSecondColoumnItems();
            }
        } else {
            ToastAndroid.show(t('order-overview.new-inventory-order'), ToastAndroid.SHORT);
        }
    }

    toggleOrder = () => {
        if (!!this.accessToOrder) {
            this.setState({
                orderPlace: true,
                adjustInventory: false
            });
            if (!this.state.isInventoryOrderItemLoaded) {
                this.loadToolbarSecondColoumnItems();
            }
        } else {
            ToastAndroid.show(t('order-overview.new-inventory-order'), ToastAndroid.SHORT);
        }
    }

    onChangeValue = (qty: number, unit: string) => {
        this.setState({ inventoryQuantity: qty, outputUnitId: unit });
    }

    updateValue = () => {
        // if (this.state.inventoryQuantity) {
        //     this.onChangeValue(this.state.inventoryQuantity, "");
        // }
        if (!!this.state.orderPlace) {
            this.addInventoryOrder(this.selectedRecipe);
            this.editQuantityRef?.close();
            this.startShake();
        } else {
            this.orderService.updateInventoryQuantity(this.recipeId, this.state.inventoryQuantity, this.state.outputUnitId).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    this.editQuantityRef?.close();
                    this.recipeId = ''
                    this.loadInventoryList();;
                }
            }).catch(error => {
                this.editQuantityRef?.close();
                this.recipeId = '';
                this.loadInventoryList();;
            });

        }
    }

    delete = () => {
        if (this.state.inventoryQuantity) {
            this.onChangeValue(0, "");
        }
        this.editQuantityRef?.close();
    }

    close = () => {
        if (this.state.inventoryQuantity) {
            this.onChangeValue(this.state.inventoryQuantity, "");
        }
        this.editQuantityRef?.close();
    }

    renderEditQuantiyAlert = () => {
        return (
            <EditInventoryQuantity
                ref={(ref: EditInventoryQuantity) => this.editQuantityRef = ref}
                inventoryQuantity={this.state.inventoryQuantity}
                isInventoryOrder={this.state.orderPlace}
                outputQuantity={this.state.outputQuantity}
                outputUnit={this.state.outputUnit}
                outputUnitId={this.state.outputUnitId}
                editingUnitId={this.state.editingUnitId}
                unitConversions={this.state.unitConversions}
                onChangeValue={this.onChangeValue}
                delete={this.delete}
                updateValue={this.updateValue}>
            </EditInventoryQuantity>
        );
    }

    updateDelivaryTime = (deliveryType: string) => {
        let order = this.state.order;
        order.deliverableType = deliveryType;
        this.setState({ order });
    }

    updateDateAndTime = (date: Date) => {
        let order = this.state.order;
        order.deliveryDate = date;
        this.setState({ date: date, order });
        this.setDateTime();
    }

    setDateTime() {
        let dateTime = this.state.date.getTime();
        let order = this.state.order;
        order.deliveryTime = dateTime.toString();
        this.setState({ order });
    }

    closeOrder = () => {
        this.orderDeliveryRef?.close();
        this.setState({
            quantity: 1,
            orderPlace: false,
            order: {
                orderId: 0,
                meals: [],
                deliveryTime: new Date().getTime().toString(),
                tableNumber: '',
                isInventory: true,
                deliverableType: DeliveryTypes.ASAP,
                orderTo: 'MOBILE',
                deliveryDate: new Date(),
            },
            isSliderActive: false
        });

    }

    placeOrder = () => {
        let order = this.state.order;
        if (order.deliverableType === 'ASAP') {
            let d = new Date();
            order.deliveryTime = d.getTime().toString();
        }
        this.setState({
            order: order
        });
        this.orderService.placeOrder(this.state.order).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                this.closeOrder();
                this.loadInventoryList();;
            }
        }).catch(error => {
            this.closeOrder();
            this.loadInventoryList();;
        });
    }

    renderDeliveryDetailAlert = () => {
        return (
            <OrderDeliveryDetails
                ref={(ref: OrderDeliveryDetails) => this.orderDeliveryRef = ref}
                updateDelivaryTime={this.updateDelivaryTime}
                order={this.state.order}
                updateDateAndTime={this.updateDateAndTime}
                placeOrder={this.placeOrder}>
            </OrderDeliveryDetails>
        );
    }

    onDecremnet = () => {
        this.setState({
            quantity: this.state.quantity !== 1 ? this.state.quantity - 1 : 1
        });
    }

    onIncremnet = () => {
        this.setState({
            quantity: this.state.quantity !== 1000 ? this.state.quantity + 1 : 1000
        });
    }
    toggleAdjustInventory = () => {

        this.setState({
            adjustInventory: !this.state.adjustInventory
        });

    }

    adjustInventory = (item: Recipe) => {
        //TODO !!this.manageInventory        
        if (true) {
            this.recipeId = item.uuid;
            this.selectedRecipe = item;
            this.setState({
                adjustInventory: false,
                inventoryQuantity: item.outPutQuantity,
                outputUnit: item.OutPutUnit,
                outputUnitId: item.unitId,
                editingUnitId: item.unitId,
                isSliderActive: false
            });
            this.editQuantityRef?.show()
        } else {
            ToastAndroid.show(t('order-overview.manage-inventory'), ToastAndroid.SHORT);
        }
    }

    renderOrderType = () => {
        return (
            <Modal
                transparent={true}
                visible={this.state.adjustInventory}
                onRequestClose={() => { this.toggleAdjustInventory(); }}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={styles.modalContainer}>
                        <View style={{ flexDirection: 'row', }}>
                            <Text style={{
                                flex: .85, alignSelf: 'center', marginBottom: 5,
                                textAlign: 'center', color: colors.white, marginTop: 10
                            }}>
                                {t('order-overview.choose-option')}</Text>
                            <TouchableOpacity style={{ flex: .15, alignSelf: 'center' }} onPress={this.toggleAdjustInventory}>
                                <Icon name='x' size={25} color={colors.white} />
                            </TouchableOpacity >
                        </View>
                        <ScrollView>
                            {this.permissionService.hasPermission(Action.EDIT_INVENTORY) && (this.state.inventoryItem.inventoryType == 1) &&
                                <Text style={styles.textHeader} onPress={this.adjustInventory.bind(this, this.state.inventoryItem)}>{t('inventory.adjust-inventory')}</Text>}
                            {this.permissionService.hasPermission(Action.CREATE_INVENTORY) &&
                                <Text style={styles.textHeader} onPress={this.goToInventoryPos}>{t('inventory.place-order')}</Text>}
                        </ScrollView>
                    </View>
                </View>
            </Modal>
        );
    }
    setCameraOpen = () => {
        this.addedImageId = ''
        ImagePicker.openCamera({
            cropping: true, width: 500, height: 500, cropperCircleOverlay: true,
            compressImageMaxWidth: 640, compressImageMaxHeight: 480, freeStyleCropEnabled: true,
            includeBase64: true
        }).then((image) => {
            this.setState({
                isLoaderVisible: true
            });
            NetInfo.fetch().then(state => {
                if (state.isConnected) {
                    RNFetchBlob.fetch('POST', this.webUrl + 'api/1.0/assets/upload', {
                        Accept: 'application/json',
                        Authorization: 'Bearer ' + this.state.webAppCredentials.webCredentialData.accessToken,
                        'Content-Type': 'multipart/form-data',
                    }, [
                        { name: 'object_type', data: 'restaurant' },
                        { name: 'object_uuid', data: this.state.webAppCredentials.webCredentialData.resturantId },
                        { name: 'images[]', filename: image.modificationDate, data: RNFetchBlob.wrap(image.path) }
                    ]).then((resp) => {
                        this.addedImageId = JSON.parse(resp.data).data.images[0].uuid
                        this.setState({
                            isLoaderVisible: false,
                            source: JSON.parse(resp.data).data.images[0].permalink
                        })

                    }).catch((error: Error) => {
                        ToastAndroid.show('Something went wrong..', ToastAndroid.SHORT);
                        this.setState({
                            isLoaderVisible: false
                        });
                    });
                } else {
                    ToastAndroid.show('Please check your internet connectivity', ToastAndroid.SHORT);
                }
            });
        }).catch(e => {
            this.setState({
                isLoaderVisible: false
            });
        });
    }
    setInventoryType = () => {
        let item = this.state.inventoryType === 1 ? 2 : (this.state.inventoryType === 2) ? 3 : (this.state.inventoryType === 3) ? 1 : 1;
        this.setState({
            inventoryType: item
        })
    }
    saveNewRecipe = () => {
        if (this.addedImageId == '') {
            ToastAndroid.show("Please add ingredient image", ToastAndroid.SHORT)
        } else if (this.state.recipeName === '') {
            ToastAndroid.show("Please enter ingredient name", ToastAndroid.SHORT)
        } else if (this.state.selectedUnit === '') {
            ToastAndroid.show("Please enter ingredient unit", ToastAndroid.SHORT)
        } else {
            this.orderService.addNewIngredient(this.addedImageId, this.state.selectedUnit, this.state.recipeName, this.state.inventoryType, this.state.barcodeData).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    ToastAndroid.show("Ingredient added successfully", ToastAndroid.SHORT)
                    this.componentDidMount()
                    // this.loadInventoryList();
                    this.closeRecipeModal();

                }
            }).catch(error => {
                this.closeRecipeModal();
                this.loadInventoryList();;
            });
        }
    }
    closeRecipeModal = () => {
        this.addedImageId = '';
        this.setState({
            newRecipeModalOpen: false,
            selectedUnit: this.state.units.length>0?this.state.units[0].uuid:'',
            recipeName: '',
            inventoryType: 1
        })
    }

    getUnits=()=>{
        let filterdUnits:any=[];
        this.state.units.forEach(element => {            
            if(!element.recipe_uuid){
                filterdUnits.push(element)
            }
        });
        return filterdUnits
    }
    
    addNewRecipe = () => {
        let image = !!this.state.source ? { uri: this.state.source } : placeHolder;
        let units=this.getUnits()
        return (
            <Modal
                transparent={true}
                visible={this.state.newRecipeModalOpen}
                onRequestClose={() => this.closeRecipeModal()}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={{
                        flexDirection: 'column',
                        width: 300,
                        borderRadius: 10,
                        minHeight: 100,
                        maxHeight: 700,
                        justifyContent: 'center',
                        alignSelf: 'center',
                        backgroundColor: colors.primaryButton
                    }}>
                        <View style={{ flexDirection: 'row', alignSelf: 'flex-start', marginTop: 5, marginBottom: 10 }}>
                            <Text style={{
                                flex: 1, alignSelf: 'center', marginBottom: 5,
                                textAlign: 'center', color: colors.white, marginTop: 5, fontSize: 18, fontWeight: 'bold'
                            }}>
                                {'Add Ingredient'}</Text>
                        </View>
                        <View style={{ flexDirection: 'row', marginRight: 10, marginLeft: 10 }}>
                            <View style={{ flex: .35, }}>

                                <View style={{ flexDirection: 'column', }}>
                                    <Image
                                        resizeMode='cover'
                                        source={image}
                                        key={(new Date()).getTime()}
                                        CACHE='reload'
                                        resizeMode='cover'
                                        style={{ borderRadius: 10, height: 55, width: 60, marginLeft: 5 }}
                                    />
                                    {this.state.isLoaderVisible ?
                                        <ActivityIndicator style={{ marginTop: 18, position: 'absolute', right: 20, padding: 8 }} size='small' color={colors.black} />
                                        : <View style={{ marginTop: 18, position: 'absolute', right: 20, padding: 8 }}>
                                            <TouchableOpacity onPress={this.setCameraOpen}>
                                                <Icon name='camera' size={20} color={!!this.addedImageId ? colors.black : colors.red} />
                                            </TouchableOpacity>
                                        </View>}
                                </View>

                            </View>
                            <View style={{
                                flex: .75, height: 40, margin: 8, alignItems: 'center',
                                backgroundColor: colors.white, borderRadius: 8, borderWidth: 1, 
                            }}>
                                <TextInput
                                    value={this.state.recipeName}
                                    selectionColor={'rgba(0,0,0,0.2)'}
                                    onChangeText={recipeName => this.setState({ recipeName })}
                                    placeholder={'Ingredient name'}
                                    selectTextOnFocus
                                    placeholderTextColor={'#000'}
                                    underlineColorAndroid="transparent"
                                />
                            </View>
                        </View>
                        <View style={{ flexDirection: 'row', margin: 8, alignItems: 'center' }}>

                            <TouchableOpacity onPress={this.setInventoryType} style={{ backgroundColor: colors.white, borderRadius: 8, borderWidth: 1, flex: 1, marginHorizontal: 8, height: 40 }}>
                                <Text
                                    style={{
                                        height: 40, color: colors.black, paddingLeft: 5, paddingTop: 6
                                    }}
                                >{this.state.inventoryType == 1 ? 'Managed' : (this.state.inventoryType == 2) ? 'No Stock' : 'Infinite'}</Text>
                            </TouchableOpacity>
                        </View>

                        <View style={{ flexDirection: 'row', margin: 8, alignItems: 'center' }}>
                            <View style={{ backgroundColor: colors.white, borderRadius: 8, borderWidth: 1, flex: 1, marginHorizontal: 8 }}>
                                <Picker
                                    selectedValue={this.state.selectedUnit}
                                    style={{ height: 36 }}
                                    onValueChange={(itemValue, itemIndex) => {
                                        let unitId = itemValue.toString();
                                        this.setState({ isOpen: false, selectedUnit: unitId })
                                    }}>
                                    {units.map((unitConversion) => {
                                        let label= unitConversion.name + ' ( ' + unitConversion.symbol + ' ) ';
                                        if(label){
                                            return <Picker.Item label={label} value={unitConversion.uuid} />
                                        }
                                    })}
                                </Picker>

                            </View>
                        </View>
                        <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 10 }}>
                                <TouchableOpacity onPress={() =>  this.closeRecipeModal()}>
                                    <Icon name='x' size={30} color={colors.white} />
                                </TouchableOpacity>
                                <TouchableOpacity onPress={() => this.saveNewRecipe()}>
                                    <Icon name='check' size={30} color={colors.white} />
                                </TouchableOpacity>
                            </View>
                    </View>

                </View>
            </Modal >
        );
    }
    renderInventory = () => {
        return (
            <View style={{ flexDirection: 'row', }}>
                {!!this.state.orderPlace ? this.renderPopButton() : null}
                <View style={{ flex: 1 }}>
                    {this.renderDeliveryDetailAlert()}
                    {this.renderFilterModal()}
                    {this.renderOrderType()}
                    <FlatList
                        data={this.state.recipeList}
                        extraData={this.state}
                        renderItem={({ item }) => this.renderRecipeItem(item)}
                    />
                </View>
            </View>
        );
    }

    toggleOpen = () => {
        this.setState({ isSliderActive: !this.state.isSliderActive });
    };

    renderPopButton() {
        return (
            <Animated.View style={{
                flex: 0.06, flexDirection: 'column',
                justifyContent: 'center', transform: [{ translateX: this.shakeAnimation }]
            }}>
                <TouchableOpacity onPress={this.toggleOpen}
                    style={{
                        flexDirection: 'column',
                        justifyContent: 'center',
                        flex: 1, marginTop: 8,
                        marginBottom: 8,
                        backgroundColor: colors.innershade
                    }} >
                    <Icons name='chevron-thin-right' size={22} color={colors.white} />
                </TouchableOpacity>
            </Animated.View>
        );
    }

    setSelectedItems = (room: string, storage: string, rack: string, place: string, searchText: string) => {
        if (!!room) {
            this.selectedRoom = room[0];
        } else {
            this.selectedRoom = '';
        }
        if (!!storage) {
            this.selectedStorage = storage[0];
        } else {
            this.selectedStorage = '';
        }
        if (!!rack) {
            this.selectedRack = rack[0];
        } else {
            this.selectedRack = '';
        }
        if (!!place) {
            this.selectedPlace = place[0];
        } else {
            this.selectedPlace = '';
        }
        if (!!searchText) {
            this.searchText = searchText;
        } else {
            this.searchText = '';
        }
        this.applyFilter();
    }

    renderFilterModal() {
        return (
            <InventoryFilter
                ref={(ref: InventoryFilter) => this.inventoryFilterRef = ref}
                roomList={this.state.roomList}
                setSelectedItems={this.setSelectedItems}>
            </InventoryFilter>
        );
    }

    applyFilter = () => {
        let roomArray: Recipe[] = [];
        if (!!this.selectedRoom) {
            this.recipes.forEach((recipe: Recipe) => {
                if (recipe.roomId === this.selectedRoom) {
                    roomArray.push(recipe);
                }
            });
        }
        let storageArray: Recipe[] = [];
        if (roomArray.length > 0 && !!this.selectedStorage) {
            roomArray.forEach((item: Recipe) => {
                if (item.storageId === this.selectedStorage) {
                    storageArray.push(item);
                }
            });
        }

        let rackArray: Recipe[] = [];
        if (storageArray.length > 0 && !!this.selectedRack) {
            storageArray.forEach((item: Recipe) => {
                if (item.rackId === this.selectedRack) {
                    rackArray.push(item);
                }
            });
        }

        let placeArray: Recipe[] = [];
        if (rackArray.length > 0 && !!this.selectedPlace) {
            rackArray.forEach((item: Recipe) => {
                if (item.placeId === this.selectedPlace) {
                    placeArray.push(item);
                }
            });
        }

        let filterArray: Recipe[] = [];
        if (!!this.selectedRoom) {
            filterArray = roomArray;
            if (!!this.selectedStorage) {
                filterArray = storageArray;
                if (!!this.selectedRack) {
                    filterArray = rackArray;
                    if (!this.selectedPlace) {
                        filterArray = placeArray;
                    }
                }
            }
        }

        let finalArray: Recipe[] = [];
        if (!!this.selectedRoom || !!this.selectedStorage ||
            !!this.selectedRack || !!this.selectedPlace) {
            if (filterArray.length === 0) {
                finalArray = [];
            } else if (filterArray.length > 0 && this.searchText !== '') {
                filterArray.forEach((recipe: Recipe) => {
                    let recipeName = recipe.recipeName!.toUpperCase();
                    let searchItem = this.searchText!.toUpperCase();
                    if (recipeName.indexOf(searchItem) > -1 && this.searchText !== '') {
                        finalArray.push(recipe);
                    }
                });
            } else if (filterArray.length > 0 && this.searchText === '') {
                finalArray = filterArray;
            }
        } else if (filterArray.length === 0 && this.searchText !== '') {
            this.recipes.forEach((recipe: Recipe) => {
                let recipeName = recipe.recipeName!.toUpperCase();
                let searchItem = this.searchText!.toUpperCase();
                if (recipeName.indexOf(searchItem) > -1 && this.searchText !== '') {
                    finalArray.push(recipe);
                }
            });
        } else if (filterArray.length > 0 && this.searchText !== '') {
            filterArray.forEach((recipe: Recipe) => {
                let recipeName = recipe.recipeName!.toUpperCase();
                let searchItem = this.searchText!.toUpperCase();
                if (recipeName.indexOf(searchItem) > -1 && this.searchText !== '') {
                    finalArray.push(recipe);
                }
            });
        } else if (filterArray.length === 0 && this.searchText === '') {
            finalArray = this.recipes;
        }

        this.inventoryFilterRef?.close();
        this.setState({
            recipeList: finalArray
        });
    }

    renderSLdingButton = () => {
        return (
            <TouchableOpacity onPress={this.toggleOpen}
                style={{ flex: 0.1, flexDirection: 'column', justifyContent: 'center', }}>
                <Icons name='chevron-thin-left' size={30} color={colors.white} />
            </TouchableOpacity>
        );
    }

    renderRecipe = (item: Meal | any) => {
        return (
            <View style={{
                flexDirection: 'row',
                borderRadius: 10, height: 50,
                backgroundColor: colors.black,
                alignItems: 'center', marginRight: 10,
                marginTop: 10
            }}>
                <Text numberOfLines={2}
                    style={{
                        paddingLeft: 10,
                        color: colors.white, flex: 1.5,
                    }}>{item.recipes ? item.recipes[0].name : ''}</Text>
                <Text style={{
                    flex: .5,
                    paddingLeft: 10,
                    color: colors.white,
                }}>{item.recipes ? item.recipes[0].outputData : ''}</Text>
            </View>
        );
    }

    drawerContent = () => {
        return (
            <View style={{ flex: 1, backgroundColor: colors.inactiveBackground, flexDirection: 'row' }}>
                {this.renderSLdingButton()}
                {this.state.order.meals!.length === 0 ?
                    <Text style={{ color: colors.white }}>No item found </Text> :
                    <FlatList
                        data={this.state.order.meals}
                        extraData={this.state}
                        renderItem={({ item }) => this.renderRecipe(item)}
                    />}
            </View>
        );
    }

    didFocus = () => {
        if (this.props.navigation.getParam('isInventoryPos', false)) {
            this.setState({
                orderPlace: true
            });
            if (!this.state.isInventoryOrderItemLoaded) {
                this.loadToolbarSecondColoumnItems();
            }
        }
    }

    startShake = () => {
        Animated.sequence([
            Animated.timing(this.shakeAnimation, { toValue: 10, duration: 100, useNativeDriver: true }),
            Animated.timing(this.shakeAnimation, { toValue: -10, duration: 100, useNativeDriver: true }),
            Animated.timing(this.shakeAnimation, { toValue: 10, duration: 100, useNativeDriver: true }),
            Animated.timing(this.shakeAnimation, { toValue: 0, duration: 100, useNativeDriver: true })
        ]).start();
    }
    openNewRecipeModal = () => {
        this.addedImageId = '';
        this.setState({
            newRecipeModalOpen: true,
            selectedUnit: this.state.units.length>0?this.state.units[0].uuid:'',
            recipeName: '',
            inventoryType: 1
        })
    }
    checkBarcodeData = (data: any) => {
        let found = false;
        let recipeItem: any = ''
        this.state.recipeList.forEach(recipe => {
            if (!!recipe.productBarcode && (recipe.productBarcode === data)) {
                found = true;
                recipeItem = recipe
            }
        });

        if (!!found) {
            this.toggle(recipeItem)
        } else {
            Alert.alert(
                'No ingredient found',
                'Would you like to add a new ingredient?',
                [
                    {
                        text: 'No',

                        style: 'cancel',
                    },
                    { text: 'Yes', onPress: this.openNewRecipeModal },
                ]
            );

        }
    }
    barcodeRecognized = ({ barcodes }) => {
        barcodes.forEach(barcode => {
            if (barcode.type != 'UNKNOWN_FORMAT') {                
                this.setState({
                    isBarcodeOpened: false,
                    barcodeData: barcode.data
                })
                Vibration.vibrate();
                this.checkBarcodeData(barcode.data)
            }
        }
        )

    };
    renderBarCodeReader() {
        return (
            <View style={{ flex: 1 }}>
                <RNCamera
                    ref={ref => {
                        this.camera = ref;
                    }}
                    style={{
                        flex: .85,
                        width: '100%',
                    }}
                    androidCameraPermissionOptions={{
                        title: 'Permission to use camera',
                        message: 'We need your permission to use your camera',
                        buttonPositive: 'Ok',
                        buttonNegative: 'Cancel',
                    }}
                    androidRecordAudioPermissionOptions={{
                        title: 'Permission to use audio recording',
                        message: 'We need your permission to use your audio',
                        buttonPositive: 'Ok',
                        buttonNegative: 'Cancel',
                    }}
                    flashMode={RNCamera.Constants.FlashMode.torch}
                    onGoogleVisionBarcodesDetected={this.barcodeRecognized}
                >

                </RNCamera>
                <TouchableOpacity onPress={() => this.setState({ isBarcodeOpened: false })} style={{ justifyContent: 'center', flex: .15, backgroundColor: colors.black }}>
                    <Text style={{ color: colors.white, textAlign: 'center', fontSize: 20 }}>Cancel</Text>
                </TouchableOpacity>
            </View>
        )
    }
    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                hideBack={!!this.state.orderPlace ? true : false}
                doNaviagte={false}
                toolbarMenu={!!this.state.orderPlace ? this.tooblarList : this.tooblarItems}>
                <MenuDrawer
                    open={this.state.isSliderActive}
                    drawerContent={this.drawerContent()}
                    drawerPercentage={45}
                    animationTime={700}
                    overlay={true}
                    opacity={0.4}
                />

                {!!this.state.isBarcodeOpened ? this.renderBarCodeReader() :
                    <View style={{ flex: 1 }}>
                        {this.renderEditQuantiyAlert()}
                        {this.addNewRecipe()}
                        {this.state.isLoading ?
                            <ActivityIndicator size='large' color={colors.white} />
                            : this.renderInventory()}

                    </View>}
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    modalContainer: {
        flexDirection: 'column',
        width: 300,
        borderRadius: 8,
        minHeight: 100,
        maxHeight: 500,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    }, textHeader: {
        backgroundColor: colors.white,
        borderRadius: 10,
        textAlign: 'center',
        textAlignVertical: 'center',
        margin: 10,
        height: 50,
        color: colors.black
    },
});
