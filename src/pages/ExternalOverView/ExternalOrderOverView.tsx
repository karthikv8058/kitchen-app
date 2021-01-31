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
    isLoading: boolean;
    activeOrderId: string;
}

export default class ExternalOrderOverView extends AbstractComponent<Props, State> {
    private orderService: OrderService = Bind('orderService');
    private permissionService: PermissionService = Bind('permissionService');
    private orderDeleteAcess = false;
    private tooblarItems: any[] = [];
    private eventEmitterService: EventEmitterService = ioc.ServiceFactory.getServiceBy('eventEmitterService');
    pushListener = { type: 1, callback: (data: string) => { this.loadOrders(); } };
    private alreadyLoaded: any;

    constructor(props: Props) {
        super(props);
        this.state = {
            order: [],
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
        this.orderService.loadExternalOrders((response: IOrder[]) => {
            if (responseChecker(response, this.props.navigation)) {
                if (response == null || response.length == 0) {
                    this.loadorederFromWeb();
                } else {
                    this.setState({
                        order: response.reverse(),
                        isLoading: false,
                    });
                }
            } else {
                this.setState({
                    order: [],
                    isLoading: false,
                });
            }
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
            });
        } else {
            ToastAndroid.show(t('order-overview.permission-delete'), ToastAndroid.SHORT);
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

    loadorederFromWeb = () => {
        this.alreadyLoaded = true
        this.setState({
            isLoading: true
        })
        this.orderService.loadorederFromWeb(1).then((response) => {
            if (responseChecker(response, this.props.navigation)) {
                if (!!response) {
                    this.orderService.loadArchivedOrder(1).then((archivedOrder: any) => {
                        let orderList: any =  this.state.order
                        archivedOrder.orders.forEach((element: any) => {
                            let isExist: boolean = false;
                            this.state.order.forEach((order: IOrder) => {
                                if (order.orderId === element.orderId) {
                                    isExist = true
                                }
                            });
                            if (!isExist) {
                                orderList.push(element)
                            }
                        });

                        if (orderList.length > 0) {
                            this.setState({
                                order: orderList,
                                isLoading: false
                            })
                        } else {
                            this.setState({
                                isLoading: false
                            })
                        }

                    }, (error: Error) => {
                        this.setState({ isLoading: false });
                    });
                } else {
                    this.setState({
                        isLoading: true
                    })
                }

            } else {
                this.setState({
                    isLoading: true
                })
            }
        }, (error: Error) => {
            this.setState({ isLoading: false });
        });
    }

    toggleModal = (orderId: string) => {
        this.setState({
            activeOrderId: orderId
        });
    }

    renderDeleteOrderAlert() {
        return (
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
        );
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

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                index={OPEN_ORDER}
                category={menuCategory.orderCategory}
                toolbarMenu={this.tooblarItems} >
                <View style={styles.viewContainer}>
                    <View style={styles.container}>
                        <View style={styles.overviewHolder}>
                            <FlatList
                                style={styles.basicContainer}
                                extraData={this.state}
                                data={this.state.order}
                                contentContainerStyle={{ flexGrow: 1 }}
                                ListFooterComponent=
                                {<View style={styles.bottomView}>
                                    {this.state.isLoading ? <LoaderWithText text='Loading order' /> : null}
                                </View>}
                                onEndReached={({ distanceFromEnd }) => {
                                    this.loadorederFromWeb()
                                }}
                                renderItem={this.renderOrder}
                            />
                        </View>
                        {!this.alreadyLoaded ? this.loadorederFromWeb() : null}
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
