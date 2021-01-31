import OrderService from '@services/OrderService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { ActivityIndicator, FlatList, Modal, StyleSheet, Text, ToastAndroid, TouchableOpacity, View, AsyncStorage } from 'react-native';
import { Bind } from '../ioc/ServiceContainer';
import { responseChecker } from '../utils/responseChecker';
import AbstractComponent from './AbstractComponent';
import SwipeView from './SwipeView';
import Meal from '@components/Meal';
import courses from '@models/Course';
import PermissionService, { Action } from '@services/PermissionService';

interface Props {
    navigation?: any;
    course?: any;
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

export default class Course extends AbstractComponent<Props, State> {

    private orderService: OrderService = Bind('orderService');
    private permissionService: PermissionService = Bind('permissionService');

    private orderId = '';
    private isOnCall: Boolean = false;
    private courseId = '';
    constructor(props: Props) {
        super(props);
        this.state = {
            modalVisible: false,
        };
        this.renderMeal = this.renderMeal.bind(this);
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
        let status = this.props.course.status;
        if (status === 0) {
            this.toggleModal(true);
            this.orderId = orderId;
        } else {
            this.props.navigation.push('OrderDetailPage', {
                orderId: orderId
            });
        }
    }

    renderMeal(course: courses) {
        this.isOnCall = course.item.isOnCall;
        this.courseId = course.item.id;
        return (
            <Meal
                order={this.props.course}
                navigation={this.props.navigation}
                course={course}>
            </Meal>
        );
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
        this.props.onCall!(this.courseId);
    }

    toggleModal(toggleBoolean: boolean) {
        this.setState({
            modalVisible: toggleBoolean
        });
    }

    render() {
        let orderId = this.props.course.uuid;
        return (
            <View>
                {this.renderDeleteOrderAlert()}
                <SwipeView onSwipedRight={this.props.finishOrder!.bind(this, orderId)}
                    disableSwipeToLeft={true}
                    disableSwipeToRight={this.props.course.status == 1}>
                    <TouchableOpacity
                        onPress={() => this.showDeleteOrderAlert(orderId)}>
                        <View >
                            <FlatList
                                listKey={this.props.course.uuid}
                                style={styles.orderContainer}
                                extraData={this.state}
                                data={this.props.course.courses}
                                renderItem={this.renderMeal}
                            />
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
