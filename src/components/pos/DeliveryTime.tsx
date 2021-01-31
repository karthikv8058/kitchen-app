import AbstractComponent from '@components/AbstractComponent';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    Keyboard,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import DateTimePicker from 'react-native-modal-datetime-picker';

import { getDateTime } from '../../utils/commonUtil';
import { DeliveryTypes } from '../../utils/constants';

interface Props {
    onUpdateDelivaryTime: Function;
    updateDateAndTime: Function;
    deliveryType: string;
    deliveryTime: Date;
    isInventory: boolean;
}

interface State {
    showDateTimePicker: boolean;
    deliveryType: string;
}
export default class DeliveryTime extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            showDateTimePicker: false,
            deliveryType: DeliveryTypes.ON_CALL
        };
    }

    _hideDateTimePicker = () => this.setState({
        showDateTimePicker: false
    });

    _showDatePicker = () => {
        Keyboard.dismiss();
        this.setState({ showDateTimePicker: true });
    }

    _onDateChange = (date: any) => {
        this.props.updateDateAndTime(date);
        this._hideDateTimePicker();
    };

    renderDateAndTime() {
        return (
            <TouchableOpacity onPress={this._showDatePicker} style={styles.showDatePicker} >
                <Text style={{ alignSelf: 'center', color: colors.black, textAlign: 'center', paddingTop: 10 }} >
                    {getDateTime(this.props.deliveryTime)}</Text>
            </TouchableOpacity>
        );
    }

    toggleDeliveryType = () => {
        let delivaryTime = DeliveryTypes.ON_CALL;
        if (this.props.deliveryType === DeliveryTypes.ON_CALL) {
            delivaryTime = DeliveryTypes.ASAP;
        } else if (this.props.deliveryType === DeliveryTypes.ASAP) {
            delivaryTime = DeliveryTypes.ON_TIME;
        } else if (this.props.isInventory) {
            delivaryTime = DeliveryTypes.ASAP;
        }
        if (this.props.onUpdateDelivaryTime) {
            this.props.onUpdateDelivaryTime(delivaryTime);
        }
    }

    render() {
        return (
            <View style={{ flexDirection: 'column' }}>
                <Text style={{ color: colors.white, flex: 1, marginLeft: 5 }}>{t('pos.delivery-at')}</Text>
                <View style={{ flexDirection: 'row' }}>
                    <TouchableOpacity style={styles.toggleDeliveryTimeWrap} onPress={this.toggleDeliveryType}  >
                        <Text style={styles.toggleDelivery}>{this.props.deliveryType}</Text>
                    </TouchableOpacity>
                    {this.props.deliveryType === DeliveryTypes.ON_TIME && this.renderDateAndTime()}
                    {this.state.showDateTimePicker ? <DateTimePicker
                        isVisible={true}
                        onConfirm={this._onDateChange}
                        onCancel={this._hideDateTimePicker.bind(this)}
                        mode='datetime'
                    /> : null}
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    showDatePicker: {
        borderRadius: 20,
        height: 40,
        margin: 5,
        flex: 1,
        backgroundColor: colors.white,
        paddingBottom: 5
    },
    toggleDeliveryTimeWrap: {
        borderRadius: 20,
        height: 40,
        margin: 5,
        color: colors.black,
        width: 100,
        backgroundColor: colors.liteGray,
        justifyContent: 'center',
        alignItems: 'center',
    },
    inventoryTextInputWrapper: {
        textAlign: 'center',
        height: 40,
        borderRadius: 20,
        flex: 1,
        backgroundColor: colors.white
    },
    toggleDelivery: {
        color: colors.black,
    },
});
