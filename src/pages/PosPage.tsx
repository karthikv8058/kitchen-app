import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import Meal from '@models/Meal';
import Order from '@models/Order';
import Recipe from '@models/Recipe';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    FlatList,
    ScrollView,
    StyleSheet,
    ToastAndroid,
    TouchableOpacity,
    View,
    Modal,
    Alert,
    Image,
    AsyncStorage,
    Dimensions,
    PanResponder,
    Animated
} from 'react-native';
import { connect } from 'react-redux';
import DeliveryTime from '../components/pos/DeliveryTime';
import MealComponent from '../components/pos/Meal';
import TableAndType from '../components/pos/TableAndType';
import ioc, { Bind } from '../ioc/ServiceContainer';
import { DeliveryTypes, menuCategory, NEW_ORDER, orderItems } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import PosRecipeBook from '@components/pos/PosRecipeBook.';
import RecipeLabel from '@models/RecipeLabel';
import Icon from 'react-native-vector-icons/Feather';
import QuantityDialog from '@components/QuantityDialog';
import { userRights } from '../utils/userRights';
import OrderService from '@services/OrderService';
import Orientation from 'react-native-orientation-locker';
import Splitter from '@components/Splitter';

interface Props {
    navigation: any;
}

interface State {
    date: Date;
    order: Order;
    orderType: string;
    showLoader: boolean;
    currentMeal: Meal | null;
    recipes: Recipe[];
    labels: RecipeLabel[];
    modalVisible: boolean;
    showQuantityDialog: boolean;
    recipe: Recipe | null;
    isModalVisible: boolean;
    inventoryAccess: boolean;
    screen: any,
    offset: number,
    topHeight: number,
    bottomHeight: number,
    deviceHeight: number,
    deviceWidth: number,
    isDividerClicked: boolean,
    pan: any,
    serviceSet: any
}
class PosPage extends AbstractComponent<Props, State> {
    private orderService: OrderService = Bind('orderService');

    private tooblarItems: any[] = [];

    private quantityDialog: (QuantityDialog | null) = null;
    private _panResponder: any;

    constructor(props: Props) {
        super(props);
        this.state = {
            recipes: [],
            date: new Date(),
            time: new Date(),
            order: {
                orderId: this.props.route?.params?.orderId ? this.props.route?.params?.orderId : 0,
                meals: [],
                deliveryTime: '0',
                tableNumber: '',
                isInventory: false,
                deliverableType: DeliveryTypes.ASAP,
                orderTo: 'MOBILE',
                deliveryDate: new Date()
            },
            orderType: orderItems.order,
            showLoader: true,
            currentMeal: null,
            labels: [],
            modalVisible: true,
            showQuantityDialog: false,
            recipe: null,
            isModalVisible: false,
            inventoryAccess: false,
            screen: Dimensions.get('window'),
            offset: 0,
            topHeight: (Dimensions.get('window').width > Dimensions.get('window').height ? Dimensions.get('window').width / 3 : Dimensions.get('window').height / 3),
            bottomHeight: (Dimensions.get('window').width > Dimensions.get('window').height ? Dimensions.get('window').width / 2 : Dimensions.get('window').height / 2),
            deviceHeight: Dimensions.get('window').height,
            isDividerClicked: false,
            pan: new Animated.ValueXY(),
            deviceWidth: Dimensions.get('window').width,
            serviceSet: []
        };
        this.placeOrder = this.placeOrder.bind(this);
        this.placeWebOrder = this.placeWebOrder.bind(this);
        this.updateDateAndTime = this.updateDateAndTime.bind(this);
    }

    loadOrder() {
        this.orderService.fetchOrderList(this.state.order.orderId, (order: Order) => {
            if (responseChecker(order, this.props.navigation)) {
                let orderType = order.isInventory ? orderItems.inventory : orderItems.order;
                let deliveryType = order.isOnCall ? DeliveryTypes.ON_CALL :
                    (order.deliveryTime === 0 ? DeliveryTypes.ASAP : DeliveryTypes.ON_TIME);
                order.deliverableType = deliveryType;
                let deliveryTime = new Date(Number(order.deliveryTime));
                order.deliveryDate = deliveryTime;
                let currentMeal = order.meals.length ? order.meals[order.meals.length - 1] : null;
                this.setState({
                    order: order,
                    orderType: orderType,
                    date: deliveryTime,
                    showLoader: false,
                    currentMeal
                });
                this.setDateTime();
                let orderData = this.state.order;
                orderData.meals.forEach(function (a, b) {
                    if (b === 0) {
                        a.isSelected = true;
                    } else {
                        a.isSelected = false;
                    }
                });
                this.setState({ order: orderData });
            }
        }, (error: any) => {

        });
    }

    componentDidMount() {
        AsyncStorage.getItem('userRights').then((right: String) => {
            this.setState({
                inventoryAccess: userRights('app_view_inventory', right),
            })
        })
        this.orderService.getRecipesAndLabels((response: any) => {
            if (responseChecker(response, this.props.navigation)) {

                this.setState({
                    recipes: response.recipes ? response.recipes : [],
                    labels: response.labels ? response.labels : [],
                    serviceSet: response.serviceSets ? response.serviceSets : [],
                    showLoader: false,
                });
                if (this.state.order.orderId) {
                    this.loadOrder();
                } else {
                    this.addNewMeal();
                }
            }
        }, (error: any) => {

        });
        this.loadToolbarItems();

    }
    validateNewOrder = () => {
        let isValidOrder: boolean = true
        if (!this.state.order.meals || !this.state.order.meals.length) {
            return false;
        }
        for (let meal of this.state.order.meals) {
            if (meal.recipes && meal.recipes.length > 0) {
                return true;
            } else {
                isValidOrder = false
            }
        }
        return isValidOrder
    }
    openOrderPopUp = () => {
        if (this.validateNewOrder()) {
            this.setState({
                isModalVisible: true
            })
        } else {
            ToastAndroid.show('Not a valid meal', ToastAndroid.SHORT);
        }
    }
    loadToolbarItems = () => {
        this.tooblarItems.push(
            <View>
                <TouchableOpacity
                    activeOpacity={0.8}
                    onPress={this.openOrderPopUp}
                    style={{ height: 40, width: 40, marginTop: 10, marginBottom: 15 }}>
                    <Icon name='check' size={40} color={colors.white} />
                </TouchableOpacity>
            </View>
        );
    }

    onChangeOrderType = () => {
        let order = this.state.order;
        if (this.state.order.orderId > 0) {
            if (order.isInventory) {
                ToastAndroid.show(t('pos.inventory-validation'), ToastAndroid.SHORT);
            } else {
                ToastAndroid.show(t('pos.order-caannot-changed-inventory'), ToastAndroid.SHORT);
            }
            return;
        }
        order.isInventory = !order.isInventory;
        if (order.isInventory) {
            if (order.deliverableType === DeliveryTypes.ON_CALL) {
                order.deliverableType = DeliveryTypes.ASAP;
            }
        } else {
            for (let meal of this.state.order.meals) {
                for (let recipe of meal.recipes) {
                    recipe.quantity = 1;
                }
            }
        }
        this.setState({ order });
    }

    updateDateAndTime(date: Date) {
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

    updateDelivaryTime = (deliveryType: string) => {
        let order = this.state.order;
        order.deliverableType = deliveryType;
        this.setState({ order });
    }

    _updateTableNumber = (number: any) => {
        let order = this.state.order;
        order.tableNumber = number;
        this.setState({ order });
    }

    selectRecipe = (recipe: Recipe) => {
        if (this.state.order.isInventory) {
            let meal = this.state.currentMeal;
            if (!meal) {
                return;
            }
            let _recipe = meal.recipes.find(r => r.uuid === recipe.uuid);
            if (this.quantityDialog) {
                this.quantityDialog.onChangeValue(_recipe ? _recipe.quantity : 1);
            }
            let r = { uuid: recipe.uuid, name: recipe.name, quantity: 1 };
            this.setState({ showQuantityDialog: true, recipe: r });
        } else {
            recipe.quantity = 1;
            this.addRecipeToMeal(recipe);
        }
    }

    addRecipeToMeal = (recipe: Recipe | null) => {
        if (!recipe) {
            return;
        }
        let meal = this.state.currentMeal;
        if (!meal) {
            return;
        }
        let _recipe;
        if (meal && meal.recipes) {
            _recipe = meal.recipes.find(r => r.uuid === recipe.uuid);
            if (!_recipe && recipe.quantity) {
                meal.recipes.push({ uuid: recipe.uuid, name: recipe.name, quantity: recipe.quantity });
            } else if (!this.state.order.isInventory || !recipe.quantity) {
                meal.recipes = meal.recipes.filter((r) => r.uuid !== recipe.uuid);
            } else {
                _recipe.quantity = recipe.quantity;
            }
        }
        let meals = this.state.order.meals;
        if (!this.state.order.isInventory && meal === meals[meals.length - 1] && meal.recipes && meal.recipes.length) {
            this.addNewMeal();
        } else {
            this.setState({ currentMeal: meal, showQuantityDialog: false });
        }
    }

    addNewMeal = () => {
        let order = this.state.order;
        if (this.state.currentMeal) {
            this.state.currentMeal.isSelected = false;
        }
        const newMeal: Meal = {
            isSelected: true,
            recipes: [],
        };
        order.meals.push(newMeal);
        this.setState({ order, currentMeal: newMeal, showQuantityDialog: false });
    }

    deleteMeal = (meal: Meal, sectionId: any, rowId: any, direction: any) => {
        if (direction === 'left') {
            let order = this.state.order;
            order.meals.splice(order.meals.indexOf(meal), 1);
            let currentMeal = meal.isSelected ? null : this.state.currentMeal;
            this.setState({ order, currentMeal });
        }
    }

    changeCurrentMeal = (meal: Meal) => {
        let order = this.state.order;
        let oldMeal = this.state.currentMeal;
        if (oldMeal) {
            oldMeal.isSelected = false;
        }
        meal.isSelected = true;
        this.setState({ order, currentMeal: meal });
    }

    placeWebOrder() {
        if (this.state.order.isInventory) {
            let order = this.state.order;
            order.orderTo = "WEB";
            this.setState({ order });
            this.placeOrder();
        } else {
            ToastAndroid.show(t('pos.non-inventory-web-order'), ToastAndroid.SHORT);
        }
    }

    placeOrder = () => {
        for (let meal of this.state.order.meals) {
            if (!meal.recipes || !meal.recipes.length) {
                this.deleteMeal(meal, 0, 0, 'left')
            }
        }
        if (this.validateOrder()) {
            this.setDateTime();
            this.setState({
                showLoader: true
            });
            this.orderService.placeOrder(this.state.order).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    this.setState({
                        order: response,
                        showLoader: false,
                        isModalVisible: false
                    });
                    this.props.navigation.goBack();
                } else {
                    this.setState({
                        order: response,
                        showLoader: false,
                        isModalVisible: false
                    });
                }
            }).catch(error => {
                this.setState({
                    showLoader: false,
                });
            })
            return true;

        } else {
            return false;
        }
    }
    toggle = () => {
        this.setState({
            isModalVisible: !this.state.isModalVisible
        })
    }
    renderOrderAlert() {
        return (
            <Modal
                transparent={true}
                visible={this.state.isModalVisible}
                onRequestClose={() => { this.toggle }}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={styles.modalContainer}>
                        <TableAndType
                            onChangeOrderType={this.onChangeOrderType}
                            onUpdateTableNumber={this._updateTableNumber}
                            order={this.state.order}
                            tableNumber={this.state.order.tableNumber} />
                        <DeliveryTime
                            onUpdateDelivaryTime={this.updateDelivaryTime}
                            updateDateAndTime={this.updateDateAndTime}
                            isInventory={this.state.order.isInventory}
                            deliveryTime={this.state.date}
                            deliveryType={this.state.order.deliverableType} />
                        <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 8 }}>
                            <TouchableOpacity onPress={this.toggle}>
                                <Icon name='x' size={30} color={colors.white} />
                            </TouchableOpacity>
                            <TouchableOpacity onPress={this.placeOrder.bind(this)}>
                                <Icon name='check' size={30} color={colors.white} />
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal >
        );
    }
    validateOrder() {
        if (!this.state.order.meals || !this.state.order.meals.length) {
            ToastAndroid.show(t('pos.no-meal-added'), ToastAndroid.SHORT);
            return false;
        }
        if (this.state.order.meals.length > 0 && this.state.order.meals[this.state.order.meals.length - 1].recipes.length === 0) {
            this.deleteMeal(this.state.order.meals[this.state.order.meals.length - 1], 0, 0, 'left');
        }
        for (let meal of this.state.order.meals) {
            if (!meal.recipes || !meal.recipes.length) {
                ToastAndroid.show(t('pos.empty-meal'), ToastAndroid.SHORT);
                return false;
            }
        }
        return true;
    }

    onChangeQuantity = (value: number) => {
        if (this.state.recipe) {
            this.state.recipe.quantity = value;
        }
        this.addRecipeToMeal(this.state.recipe);
    }

    renderLeftColumn() {
        return (
            <ScrollView>
                <View style={{ flexDirection: 'column', flex: 1, justifyContent: 'flex-start' }}>
                    <FlatList
                        style={{ flex: 1 }}
                        data={this.state.order.meals}
                        extraData={this.state}
                        renderItem={({ item, index }) =>
                            <MealComponent
                                key={index}
                                meal={item}
                                onDelete={this.deleteMeal}
                                onSelectMeal={this.changeCurrentMeal} />
                        }
                    />
                    {/* Render New Meal  */}
                    <View style={{ flex: 1 }}>
                        <TouchableOpacity
                            style={styles.btnNewMeal}
                            onPress={this.addNewMeal}>
                            <Icon name='plus' size={30} color={colors.white} />
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>
        );
    }

    setQuantityDialog = (element: any) => {
        this.quantityDialog = element;
    };


    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                pressWebOrderCallback={this.placeWebOrder}
                pressOrderCallback={this.placeOrder}
                moreOptions={true}
                showLoader={this.state.showLoader}
                placeOrderCallback={this.placeOrder}
                category={menuCategory.orderCategory}
                index={NEW_ORDER}
                toolbarMenu={this.tooblarItems}>
                <View style={{ flex: 1 }}>
                    {this.renderOrderAlert()}
                    <QuantityDialog ref={this.setQuantityDialog} show={this.state.showQuantityDialog} onChangeValue={this.onChangeQuantity} />
                    <Splitter>
                        {this.renderLeftColumn()}
                        <PosRecipeBook
                            recipes={this.state.recipes}
                            labels={this.state.labels}
                            splitterWidth={this.state.bottomHeight}
                            width={this.state.screen.width}
                            serviceSet={this.state.serviceSet}
                            onSelectRecipe={this.selectRecipe}
                            meal={this.state.currentMeal} />
                    </Splitter>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    btnNewMeal: {
        backgroundColor: colors.darkButton,
        elevation: 5,
        borderRadius: 10,
        flexDirection: 'row',
        justifyContent: 'center',
        height: 40,
        margin: 8,
        alignItems: 'center'
    },
    modalContainer: {
        flexDirection: 'column',
        width: 300,
        borderRadius: 8,
        minHeight: 100,
        maxHeight: 300,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    },
});

export default PosPage;
