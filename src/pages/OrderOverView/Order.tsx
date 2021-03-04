import OrderService from '@services/OrderService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Bind } from '../../ioc/ServiceContainer';
import AbstractComponent from '@components/AbstractComponent';
import SwipeView from '@components/SwipeView';
import Course from './Course';
import PermissionService, { Action } from '@services/PermissionService';

interface Props {
    navigation?: any;
    order?: any;
    finishOrder?: Function;
    editOrder?: Function;
    deletOrder?: Function;
    onCall?: Function;
    placeExternalOrder?: Function;
    recipe?: any
}

interface State {
    modalVisible: boolean;
}

export default class OrderView extends AbstractComponent<Props, State> {

    private permissionService: PermissionService = Bind('permissionService');

    private orderId = '';
    private isOnCall: Boolean = false;

    constructor(props: Props) {
        super(props);
        this.state = {
            modalVisible: false,
        };
        this.goToDetails = this.goToDetails.bind(this);
    }

    goToDetails() {
        this.setState({
            modalVisible: false,
        });
        this.props.navigation.push('OrderDetailPage', {
            orderId: this.orderId
        });
    }


    showDeleteOrderAlert(orderId: string) {
        let status = this.props.order.status;
        if (status === 0) {
            this.toggleModal(true);
            this.orderId = orderId;
        } else {
            this.props.navigation.push('OrderDetailPage', {
                orderId: orderId
            });
        }
    }

    renderDeleteOrderAlert() {
        return (
            <Modal
                transparent={true}
                visible={this.state.modalVisible}
                onRequestClose={() => { this.toggleModal(false); }}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={styles.modalContainer}>
                        <Text style={{ textAlign: 'center', color: colors.black, marginTop: 10 }}>{t('order-overview.choose-option')}</Text>

                        <View style={{ flexDirection: 'column' }}>
                            {this.permissionService.hasPermission(Action.EDIT_ORDER) &&
                                <Text style={styles.textHeader}
                                    onPress={() => this.props.editOrder!(this.orderId)}>
                                    {t('order-overview.edit')}</Text>}
                            <Text style={styles.textHeader}
                                onPress={this.goToDetails}>{t('order-overview.Details')}</Text>
                            {this.permissionService.hasPermission(Action.DELETE_ORDER) &&
                                <Text style={styles.textHeader}
                                    onPress={() => this.deleteOrder()}>{t('order-overview.delete')}</Text>}
                            {this.isOnCall ?
                                <Text style={styles.textHeader} onPress={this.onCallItemClick}>
                                    {t('order-overview.onCall')}</Text> : null}
                        </View>
                    </View>
                </View>
            </Modal>
        );
    }

    deleteOrder = () => {
        this.toggleModal(false);
        this.props.deletOrder!(this.orderId);
    }

    onCallItemClick = () => {
        this.toggleModal(false);
        this.props.onCall!(this.props.order?.courses[0]?.courseId);
    }

    toggleModal(toggleBoolean: boolean) {
        this.setState({
            modalVisible: toggleBoolean
        });
    }

    render() {

        this.isOnCall = this.props.order?.courses[0]?.isOnCall;
        let orderId = this.props.order.uuid;

        return (
            <View>
                {this.renderDeleteOrderAlert()}
                <SwipeView onSwipedRight={this.props.finishOrder!.bind(this, orderId)}
                    disableSwipeToLeft={true}
                    disableSwipeToRight={this.props.order.status == 1}
                    enableScroll={this.props.enableScroll}
                >
                    <TouchableOpacity
                        onPress={() => this.showDeleteOrderAlert(orderId)}>
                        <View style={styles.orderContainer}>
                            <Course
                                order={this.props.order}
                                navigation={this.props.navigation}
                                course={!!this.props.order.courses[0] ? this.props.order.courses[0] : []} />
                        </View>
                    </TouchableOpacity>
                </SwipeView>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    orderContainer: {
        marginLeft: 10,
        marginRight: 10,
        marginTop: 10,
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
});
