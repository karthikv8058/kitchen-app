import AbstractComponent from '@components/AbstractComponent';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import {
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View,
} from 'react-native';
import Order from '@models/Order';

interface Props {
    onUpdateTableNumber: (tableNumber: string) => void;
    onChangeOrderType: Function;
    order: Order;
    tableNumber: string;
}

interface State {
    tableNumber: string;
}

export default class TableAndType extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            tableNumber: this.props.tableNumber
        };
    }

    onUpdateTableNumber = (tableNumber: string) => {
        if (this.props.onUpdateTableNumber) {
            this.props.onUpdateTableNumber(tableNumber);
        }
        this.setState({ tableNumber: tableNumber });
    }

    renderTableNo() {
        return (
            <TextInput style={styles.inputTable} onChangeText={this.props.onUpdateTableNumber} value={this.props.tableNumber} />
        );
    }

    onChangeOrderType = () => {
        if (this.props.onChangeOrderType) {
            // this.props.onChangeOrderType();
        }
    }

    render() {
        return (
            <View style={{ flexDirection: 'column', }}>
                <Text style={{ color: colors.white, flex: 1, marginLeft: 5 }}>{t('pos.delivering-to')}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <TouchableOpacity style={styles.toggleInventoryWrap} onPress={this.onChangeOrderType} >
                        <Text style={styles.toggleInventory}>{this.props.order.isInventory ? t('pos.order-type-inventory') : t('pos.order-type-table')}</Text>
                    </TouchableOpacity>
                    {!this.props.order.isInventory && this.renderTableNo()}
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    inputTable: {
        textAlign: 'center',
        borderRadius: 20,
        height: 40,
        width: 60,
        margin: 5,
        flex: 1,
        backgroundColor: colors.white,
    },
    toggleInventoryWrap: {
        borderRadius: 20,
        height: 40,
        margin: 5,
        color: colors.black,
        width: 100,
        backgroundColor: colors.liteGray,
        justifyContent: 'center',
        alignItems: 'center',
    },
    toggleInventory: {
        color: colors.black,
    }
});
