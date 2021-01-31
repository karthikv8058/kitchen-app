import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import AbstractComponent from './AbstractComponent';
import NumericInput from './NumericInput';
import DeliveryTime from './pos/DeliveryTime';

interface Props {
    updateDelivaryTime: Function;
    order?:{};
    updateDateAndTime: Function;
    placeOrder:Function
}

interface State {
    show: boolean
}

export default class OrderDeliveryDetails extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            show: false
        };

    }

    show = () => {
        this.setState({
            show: true
        });
    }

    close = () => {
        this.setState({
            show: false
        });
    }

    updateDelivaryTime = (deliveryType: string) => {
        this.props.updateDelivaryTime!(deliveryType)
    }

    updateDateAndTime = (date: Date) => {
        this.props.updateDateAndTime(date)
    }
    render() {
        return (
            <Modal transparent={true}
                visible={this.state.show}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center', }}>
                    <View style={styles.orderModal}>
                        <View style={{ flexDirection: 'column', }}>
                            <Text style={styles.toggleInventoryWrap}>{"Please choose the order type"}</Text>
                            <DeliveryTime
                                onUpdateDelivaryTime={this.updateDelivaryTime}
                                updateDateAndTime={this.updateDateAndTime.bind(this)}
                                isInventory={true}
                                deliveryTime={this.props.order.deliveryDate}
                                deliveryType={this.props.order.deliverableType} />
                            <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 8 }}>
                                <TouchableOpacity onPress={this.close}>
                                    <Icon name='x' size={30} color={colors.white} />
                                </TouchableOpacity>
                                <TouchableOpacity onPress={()=>this.props.placeOrder()}>
                                    <Icon name='check' size={30} color={colors.white} />
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </View>
            </Modal>
        );
    }
}

const styles = StyleSheet.create({
    orderModal: {
        flexDirection: 'column',
        width: 300,
        borderRadius: 8,
        minHeight: 100,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    },
    toggleInventoryWrap: {
        height: 40,
        margin: 5,
        padding: 10,
        color: colors.white,
        justifyContent: 'center',
        alignItems: 'center',
    },
});
