import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import AbstractComponent from './AbstractComponent';
import NumericInput from './NumericInput';
import { Picker } from '@react-native-picker/picker';

interface Props {
    inventoryQuantity: number;
    isInventoryOrder: boolean;
    outputQuantity: number;
    outputUnit: string;
    outputUnitId: string;
    editingUnitId: string;
    onChangeValue: Function;
    delete: Function;
    updateValue: Function;
    unitConversions: { from: string, to: string, name: string }[]
}

interface State {
    show: boolean;
}

export default class EditInventoryQuantity extends AbstractComponent<Props, State> {

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

    onChange = (value: number, unit: string) => {
        if (unit == null) {
            unit = this.props.outputUnitId;
        }
        if (this.props.onChangeValue) {
            this.props.onChangeValue(value, unit);
        }
    }

    onChangeValue = (value: number) => {
        this.onChange(value, this.props.outputUnitId)
    }

    getUnits = () => {
        let units: any = [];
        units.push({ value: this.props.editingUnitId, name: this.props.outputUnit });
        if (this.props.unitConversions) {
            for (let unitConversion of this.props.unitConversions) {
                if (unitConversion.from == this.props.editingUnitId) {
                    units.push({ value: unitConversion.to, name: unitConversion.name });
                }
            }
        }
        return units;
    }

    render() {
        return (
            <View style={{ flex: 1 }}>
                <Modal transparent={true}
                    visible={this.state.show}>
                    <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                        <View style={styles.modalContainer}>
                            <View style={{
                                padding: 8,
                                borderTopLeftRadius: 8,
                                borderTopRightRadius: 8,
                                backgroundColor: colors.primaryButton,
                                flexDirection: 'row',
                                justifyContent: 'space-between'
                            }}>
                                <Text style={{ color: colors.white }}>{this.props.isInventoryOrder ? t('inventory.updateorder') : t('inventory.updateinventory')}</Text>
                            </View>
                            <NumericInput value={this.props.inventoryQuantity} onChangeValue={this.onChangeValue} />
                            <View style={{ flexDirection: 'row', margin: 8, alignItems: 'center' }}>
                                <Text style={{ color: colors.white }}>Unit</Text>
                                <View style={{ backgroundColor: colors.white, borderRadius: 8, borderWidth: 1, flex: .89, marginHorizontal: 8 }}>
                                    <Picker
                                        selectedValue={this.props.outputUnitId}
                                        style={{ height: 36 }}
                                        onValueChange={(itemValue, itemIndex) => {
                                            let unitId = itemValue.toString();
                                            this.onChange(this.props.inventoryQuantity, unitId)
                                        }}>
                                        {this.getUnits().map((unitConversion) => {
                                            return <Picker.Item label={unitConversion.name} value={unitConversion.value} />
                                        })}
                                    </Picker>
                                    
                                </View>
                            </View>
                            <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 10 }}>
                                <TouchableOpacity onPress={() => this.props.delete()}>
                                    <Icon name='x' size={30} color={colors.white} />
                                </TouchableOpacity>
                                <TouchableOpacity onPress={() => this.props.updateValue()}>
                                    <Icon name='check' size={30} color={colors.white} />
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </Modal>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    filterModal: {
        flexDirection: 'column',
        minWidth: 300,
        borderRadius: 8,
        minHeight: 100,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
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
