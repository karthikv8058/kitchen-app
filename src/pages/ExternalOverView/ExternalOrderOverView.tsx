import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import LoaderWithText from '@components/LoaderWithText';
import IOrder from '@models/Order';
import EventEmitterService from '@services/EventEmitterService';
import OrderService from '@services/OrderService';
import PermissionService from '@services/PermissionService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    FlatList,
    StyleSheet,
    ToastAndroid,
    TouchableOpacity,
    View,
    Modal,
    Text
} from 'react-native';
import Icons from 'react-native-vector-icons/Entypo';
import ioc, { Bind } from '../../ioc/ServiceContainer';
import { menuCategory, OPEN_ORDER } from '../../utils/constants';
import { responseChecker } from '../../utils/responseChecker';
import Order from './Order'

interface Props {
    navigation: any;
}
interface State {
    order: IOrder[];
    archivedOrder: IOrder[];
    isLoading: boolean;
    activeOrderId: string;
}

export default class ExternalOrderOverView extends AbstractComponent<Props, State> {
    private orderService: OrderService = Bind('orderService');
    private permissionService: PermissionService = Bind('permissionService');
    private orderDeleteAcess = true;
    private tooblarItems: any[] = [];
    private eventEmitterService: EventEmitterService = ioc.ServiceFactory.getServiceBy('eventEmitterService');
    pushListener = { type: 1, callback: (data: string) => { this.loadOrders(); } };
    private alreadyLoaded: any;

    constructor(props: Props) {
        super(props);
        this.state = {
            order: [],
            archivedOrder: [],
            isLoading: true,
            activeOrderId: ''
        };
    }

    goToOrderOverView = () => {
        this.props.navigation.navigate('OrderOverview', {});
    }

    componentDidMount() {
        this.tooblarItems.push(
            <TouchableOpacity
                onPress={() => this.goToOrderOverView()}
                activeOpacity={0.8}
                style={{ height: 40, width: 40, }}>
                <Icons style={{}} name='swap' size={35} color={colors.white} />
            </TouchableOpacity>

        );
        this.loadOrders();
        this.eventEmitterService.addListner(this.pushListener);
    }

    componentWillUnmount() {
        this.eventEmitterService.removeListner(this.pushListener);
    }

    loadOrders() {
        this.setState({
            isLoading: true
        })

        this.orderService.getAllOrders(1).then(orders => {
            if(orders != null && orders.length > 0 ){
                this.setState({
                    isLoading: false,
                    order: orders,
                    isLazyLoading: false
                })
                if (orders.length <= 10) {
                    this.loadOrderFromWeb();
                }
            }else{
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

        this.setState({
            isLoading: true
        })

        let lastOrder = ""
        let orders = this.getOrders();
        if(orders.length > 0 ){
            let o = orders[orders.length-1];
            if(o){
                lastOrder = o.uuid
            }
        }

        this.orderService.loadOrderFromWeb(true,lastOrder).then(orders => {
            this.setState({
                archivedOrder: this.state.archivedOrder.concat(orders),
                isLoading: false
            })

        }, (error: Error) => {
            this.setState({ isLoading: false });
        });
    }

    deleteOrder = () => {
        const orderId = this.state.activeOrderId;
        if (!orderId) {
            return;
        }
        if (!!this.orderDeleteAcess) {
            this.orderService.deleteExternalOrder(orderId).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    if (response) {
                        this.loadOrders();
                    } else {
                        ToastAndroid.show("Can't delete this external order", ToastAndroid.SHORT)
                    }
                }
                this.setState({
                    activeOrderId: ''
                });
            });
        } else {
            ToastAndroid.show(t('order-overview.permission-delete'), ToastAndroid.SHORT);
            this.setState({
                activeOrderId: ''
            });
        }
    }

    placeExternalOrder = (order: IOrder) => {
        this.setState({
            isLoading: true
        });
        if (order.status === 0) {
            this.orderService.placeExternalOrder(order.uuid).then(response => {
                if (responseChecker(response, this.props.navigation)) {
                    this.setState({
                        isLoading: false
                    });
                    this.loadOrders();
                }
            }).catch(error => {
                this.setState({
                    isLoading: false
                });
            });
        } else if (order.status === 3) {
            return new Promise((resolve, reject) => {
                this.orderService.finishOrder(order.uuid, (response) => {
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

    }

    toggleModal = (orderId: string) => {
        this.setState({
            activeOrderId: orderId
        });
    }

    renderOrder = (order: { item: IOrder }) => {
        return (
            <Order
                navigation={this.props.navigation}
                order={order.item}
                placeExternalOrder={this.placeExternalOrder}
                onClick={this.toggleModal}>
            </Order>
        );
    }



    getOrders = () => {

        let orders = [];
        if(this.state.order != null && this.state.order.length > 0){
            orders = orders.concat(this.state.order);
        }

        if(this.state.archivedOrder != null && this.state.archivedOrder.length > 0){
            orders = orders.concat(this.state.archivedOrder);
        }

        return orders.filter(o =>  o!= null);
    }

    render() {

        let orders =  this.getOrders();

        return (
            <AppBackground
                navigation={this.props.navigation}
                index={OPEN_ORDER}
                category={menuCategory.orderCategory}
                toolbarMenu={this.tooblarItems} >
                <Modal
                    transparent={true}
                    visible={this.state.activeOrderId != ''}
                    onRequestClose={() => { this.toggleModal('') }}>
                    <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                        <View style={styles.modalContainer}>
                            <Text style={{ textAlign: 'center', color: colors.black, marginTop: 10 }}>{t('order-overview.choose-option')}</Text>
                            <Text style={styles.modalTextHeader}
                                onPress={() => this.deleteOrder()}>
                                {'Cancel'}</Text>
                        </View>
                    </View>
                </Modal>
                <View style={styles.viewContainer}>
                    <View style={styles.container}>
                        <View style={styles.overviewHolder}>
                            <FlatList
                                style={styles.basicContainer}
                                extraData={this.state}
                                data={orders}
                                contentContainerStyle={{ flexGrow: 1 }}
                                ListFooterComponent=
                                {<View style={styles.bottomView}>
                                    {this.state.isLoading ? <LoaderWithText text='Loading order' /> : null}
                                </View>}
                                onEndReached={({ distanceFromEnd }) => {
                                    this.loadOrderFromWeb()
                                }}
                                renderItem={this.renderOrder}
                            />
                        </View>
                    </View>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    overviewHolder: {
        flexDirection: 'row',
        marginTop: 5,
    },
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
    modalTextHeader: {
        backgroundColor: colors.white,
        borderRadius: 10,
        textAlign: 'center',
        textAlignVertical: 'center',
        margin: 10,
        height: 50,
        color: colors.black
    },
});
