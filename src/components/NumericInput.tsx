import AbstractComponent from '@components/AbstractComponent';
import colors from '@theme/colors';
import React from 'react';
import {
    TouchableOpacity,
    View,
    TextInput
} from 'react-native';
import Icon from 'react-native-vector-icons/Feather';

interface Props {
    value: number;
    navigation?: any;
    onChangeValue?: Function;
}
class NumericInput extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    onChangeValue = (value: number) => {
        if (this.props.onChangeValue) {
            this.props.onChangeValue(value);
        }
    }

    onChangeText = (value: string) => {
        let v = parseInt(value) || 0;
        this.onChangeValue(v);
    }

    onIncremnet = () => {
        this.onChangeValue(this.props.value + 1);
    }

    onDecremnet = () => {
        let value = this.props.value;
        if (value > 0) {
            this.onChangeValue(this.props.value - 1);
        }
    }

    render() {
        return (
            <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 16 }}>
                <TouchableOpacity style={{ marginHorizontal: 6 }} onPress={this.onDecremnet}>
                    <Icon name='minus' size={30} color={colors.white} />
                </TouchableOpacity>
                <TextInput
                    style={{ height: 40, flex: 1, borderRadius: 8, backgroundColor: colors.white, borderWidth: 1 }}
                    onChangeText={this.onChangeText}
                    value={this.props.value.toString()}
                    keyboardType='number-pad'
                />
                <TouchableOpacity style={{ marginHorizontal: 6 }} onPress={this.onIncremnet}>
                    <Icon name='plus' size={30} color={colors.white} />
                </TouchableOpacity>
            </View>
        );
    }
}

export default NumericInput;
