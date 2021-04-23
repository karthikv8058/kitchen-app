import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import Order from './Order';
import EventEmitterService from '@services/EventEmitterService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    AsyncStorage,
    FlatList,
    StyleSheet,
    Text,
    ToastAndroid,
    TouchableOpacity,
    View,
    Dimensions,
    RefreshControl,
} from 'react-native';
import ioc, { Bind } from '../../ioc/ServiceContainer';
import { menuCategory, OPEN_ORDER } from '../../utils/constants';
import { responseChecker } from '../../utils/responseChecker';
import SwipeView from '@components/SwipeView';
import Icons from 'react-native-vector-icons/Entypo';
import OrderService from '@services/OrderService';
import courses from '@models/Course';
import PermissionService from '@services/PermissionService';
import LoaderWithText from '@components/LoaderWithText';
import Orientation from 'react-native-orientation-locker';

interface Props {
    navigation: any;
}
interface State {
    order: any;
    archivedOrder: any;
    isLoading: boolean;
    printerModalVisible: boolean;
    enableScroll: boolean;
    isLazyLoading: boolean;
    screen: any,
    isPortrait: boolean,
    endReached: boolean,
    height: number
}

export default class OrderOverview extends AbstractComponent<Props, State> {
    private orderService: OrderService = Bind('orderService');
    private permissionService: PermissionService = Bind('permissionService');
    private orderId = '';
    private orderEditAcess = false;
    private orderDeleteAcess = false;
    private tooblarItems: any[] = [];
    private eventEmitterService: EventEmitterService = ioc.ServiceFactory.getServiceBy('eventEmitterService');
    private pageCount = 0;
    pushListener = { type: 1, callback: (data: string) => { this.loadOrders(); } };

    constructor(props: Props) {
        super(props);
        this.state = {
            order: [],
            archivedOrder: [],
            isLoading: true,
            printerModalVisible: false,
            enableScroll: true,
            isLazyLoading: false,
            screen: Dimensions.get('window'),
            isPortrait: false,

            endReached: false,
            height: 0
        };

    }

    goToPos = (orderId: number) => {
        //TODO POS Permission
        this.props.navigation.navigate('PosPage', {
            orderId: orderId
        });
    }

    gotToPrinterDetails(orderId: number) {
        this.props.navigation.navigate('PrinterMessage', {
            orderId: orderId
        });
    }

    componentDidMount() {
        //this.pageCount = 1;
        AsyncStorage.getItem('userRights').then((right: String) => {
            this.posAccess = true;//userRights('app_new_orders', right);
            this.orderDeleteAcess = true;//userRights('app_delete_orders', right);
            this.orderEditAcess = true;//userRights('app_manage_orders', right);
        });
        this.eventEmitterService.addListner(this.pushListener);
        this.loadOrders();
    }

    goToExternalOrderOverView = () => {
        this.props.navigation.navigate('ExternalOrderOverView', {});
    }

    loadToolbarItems = () => {
        this.tooblarItems = []
        if (this.permissionService.checkRoutePermission('PosPage')) {
            this.tooblarItems.push(
                <View style={{ flexDirection: this.state.isPortrait ? 'row' : 'column', marginBottom: this.state.isPortrait ? 10 : 0 }}>
                    <TouchableOpacity
                        onPress={this.goToExternalOrderOverView}
                        activeOpacity={0.8}
                        style={{ height: 40, width: 40, marginBottom: 10 }}>
                        <Icons style={{ marginTop: 10 }} name='swap' size={35} color={colors.white} />
                    </TouchableOpacity>
                    <TouchableOpacity
                        onPress={() => this.goToPos(0)}
                        activeOpacity={0.8}
                        style={{ marginLeft: this.state.isPortrait ? 10 : 0, marginBottom: this.state.isPortrait ? 20 : 0, height: 40, width: 40, marginTop: this.state.isPortrait ? 0 : 10 }}>
                        <Icons style={{ marginTop: 10 }} name='circle-with-plus' size={40} color={colors.white} />
                    </TouchableOpacity>
                </View>

            );
        }
    }

    componentWillUnmount() {
        this.eventEmitterService.removeListner(this.pushListener);
    }

    deletePrinterMessage = () => {
        this.orderService.deletePrinterMessage(this.orderId).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                if (response) {
                    this.setState({
                        printerModalVisible: false,
                    });
                    this.loadOrders();
                }
            }
        }).catch(error => {
            this.setState({
                printerModalVisible: false,
            });
        });
    }

    storeToWeb = () => {
        this.orderService.storeToAnalyze(this.orderId).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                if (response) {
                    this.setState({
                        printerModalVisible: false,
                    });
                    this.loadOrders();
                    ToastAndroid.show(t('order-overview.stored'), ToastAndroid.SHORT);
                }
            } else {
                this.setState({
                    printerModalVisible: false,
                });
            }
        });
    }

    togglePrinterOrderModal(orderId: string) {
        this.togglePrinterModal(true);
        this.orderId = orderId;
    }

    deletOrder = (orderId: string) => {
        if (!!this.orderDeleteAcess) {
            this.orderService.deleteOrder(orderId).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    if (response) {
                        this.loadOrders();
                    }
                }
            });
        } else {
            ToastAndroid.show(t('order-overview.permission-delete'), ToastAndroid.SHORT);
        }
    }

    onCall = (courseId: string) => {
        this.orderService.postOnCall(courseId).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                if (response) {
                    this.loadOrders();
                }
            }
        });
    }

    setEnableScroll = (enableScroll: boolean) => {
        this.setState({ enableScroll })
    }

    renderOrder(order: courses) {
        return (
            <Order
                key={order.uuid}
                ref={(courseReference: Course) => this.course = courseReference}
                navigation={this.props.navigation}
                order={order}
                enableScroll={this.setEnableScroll}
                finishOrder={this.finishOrder}
                editOrder={this.editOrder}
                deletOrder={this.deletOrder}
                onCall={this.onCall}>
            </Order>
        );
    }
    editOrder = (orderId: number) => {
        if (!!this.orderEditAcess) {
            this.setState({
                isLoading: true
            });
            this.orderService.checkOrderStarted(orderId).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    this.setState({
                        isLoading: false
                    });
                    if (response) {
                        ToastAndroid.show(t('order-overview.the-order-cannot-edited'), ToastAndroid.SHORT);
                    } else {
                        this.goToPos(orderId);
                    }
                }
            }).catch(error => {
                ToastAndroid.show(t('order-overview.the-order-cannot-edited'), ToastAndroid.SHORT);
            });
        } else {
            ToastAndroid.show(t('order-overview.manage-order'), ToastAndroid.SHORT);
        }
    }

    renderPrinterMessage(order: Order) {
        return (
            <SwipeView onSwipedRight={this.finishOrder.bind(this, order.id)} disableSwipeToLeft={true}>
                <TouchableOpacity onPress={() => this.gotToPrinterDetails(order.id)}
                    onLongPress={() => this.togglePrinterOrderModal(order.id)}>
                    <View style={styles.printerMessageContainer} >
                        <Text style={styles.printerMessage}>
                            {order.printerData.message}
                        </Text>
                    </View>
                </TouchableOpacity>
            </SwipeView>
        );
    }

    renderOrderType = (task) => {

        switch (task.item.orderType) {
            case t('order-overview.order'):
                let taskItem = task.item;
                return !!taskItem.courses && taskItem.courses.length > 0 ? this.renderOrder(taskItem) : null;

            case t('order-overview.printer'):
                return this.renderPrinterMessage(task.item);
            default:
                let orderItem = task.item;
                return !!orderItem.courses && orderItem.courses.length > 0 ? this.renderOrder(orderItem) : null;
        }
    }

    finishOrder = (orderId: number) => {
        return new Promise((resolve, reject) => {
            this.orderService.finishOrder(orderId, (response) => {
                if (responseChecker(response, this.props.navigation)) {
                    if (response) {
                        this.loadOrders();
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                }
            }, (error: Error) => {
                this.setState({ isLoading: false });
                this.loadOrders();
                resolve(false);
            });
        });
    }


    loadOrders = () => {
        this.setState({
            isLoading: true
        })
        this.orderService.getAllOrders(0).then(orders => {
            if (orders != null && orders.length > 0) {
                this.setState({
                    isLoading: false,
                    order: orders,
                    isLazyLoading: false
                })
                if (orders.length <= 10) {
                    this.loadOrderFromWeb();
                }
            } else {
                this.setState({
                    isLoading: false
                })
                this.loadOrderFromWeb();
            }


        }, (error: Error) => {
            this.setState({ isLoading: false });
        });
    }

    loadOrderFromWeb() {
        if (this.state.isLoading) {
            return;
        }
        this.setState({
            isLoading: true
        })

        let lastOrder = ""
        let orders = this.getOrders();
        if (orders.length > 0) {
            let o = orders[orders.length - 1];
            if (o) {
                lastOrder = o.uuid
            }
        }
        this.orderService.loadOrderFromWeb(false, lastOrder).then(orders => {
            this.setState({
                archivedOrder: this.state.archivedOrder.concat(orders),
                isLoading: false
            })

        }, (error: Error) => {
            this.setState({ isLoading: false });
        });
    }

    togglePrinterModal = (toggleBoolean: boolean) => {
        this.setState({
            printerModalVisible: toggleBoolean
        });
    }

    onLayout() {
        const { height } = Dimensions.get('window');
        Orientation.getOrientation((orientation) => {
            if (orientation === 'PORTRAIT') {
                this.setState({
                    isPortrait: true,
                    screen: Dimensions.get('window'),
                    height
                })
            } else {
                this.setState({
                    isPortrait: false,
                    screen: Dimensions.get('window'),
                    height
                })
            }
        })
        this.loadToolbarItems();
    }


    getOrders = () => {
        let orders = [];
        if (this.state.order != null && this.state.order.length > 0) {
            orders = orders.concat(this.state.order);
        }

        if (this.state.archivedOrder != null && this.state.archivedOrder.length > 0) {
            orders = orders.concat(this.state.archivedOrder);
        }

        return orders.filter(o => o != null);
    }

    render() {

        let orders = this.getOrders();

        return (
            <AppBackground
                navigation={this.props.navigation}
                index={OPEN_ORDER}
                category={menuCategory.orderCategory}
                toolbarMenu={this.tooblarItems}
            >

                <View onLayout={this.onLayout.bind(this)} style={styles.viewContainer}>
                    <View style={{ flex: 1, height: this.state.height }}>
                        <FlatList
                            keyExtractor={(item) => item.uuid}
                            scrollEnabled={this.state.enableScroll}
                            style={styles.basicContainer}
                            extraData={this.state}
                            data={orders}
                            contentContainerStyle={{ flexGrow: 1 }}
                            refreshControl={
                                <RefreshControl
                                  refreshing={false}
                                  onRefresh={this.loadOrders}
                                />
                              }
                            ListFooterComponent={<View style={styles.bottomView}>
                                {!!this.state.isLoading ? <LoaderWithText text='Loading order' /> : null}
                            </View>}
                            onEndReachedThreshold={0.5}
                            onEndReached={({ distanceFromEnd }) => {
                                if (!this.state.endReached) {
                                    this.loadOrderFromWeb()
                                }
                            }}
                            renderItem={this.renderOrderType}
                        />
                    </View>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({

    viewContainer: {
        flex: 1,
        flexDirection: 'column'
    },
    container: {
        flexDirection: 'column',
        flex: 1,
    },
    basicContainer: {
        marginBottom: 5
    },
    bottomView: {
        paddingTop: 5,
        width: '100%',
        flexDirection: "row",
        height: 40,
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1,
    },
    loaderStyle: {
        paddingTop: 5,
        width: '100%',
        flexDirection: "row",
        height: 40,
        justifyContent: 'center',
        alignItems: 'center',
    },
    modalContainer: {
        flexDirection: 'column',
        width: 300,
        borderRadius: 20,
        minHeight: 100,
        maxHeight: 300,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.liteGray
    },
    textHeader: {
        backgroundColor: colors.white,
        borderRadius: 10,
        textAlign: 'center',
        textAlignVertical: 'center',
        margin: 10,
        height: 50,
        color: colors.black
    },
    printerMessageContainer: {
        backgroundColor: colors.white,
        borderRadius: 10,
        color: colors.red,
        marginLeft: 10,
        marginRight: 10,
        marginTop: 10,
        padding: 10,
    },
    printerMessage: {
        color: colors.red
    }
});
