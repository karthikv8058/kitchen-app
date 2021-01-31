import AbstractComponent from '@components/AbstractComponent';
import colors from '@theme/colors';
import React from 'react';
import {
    StyleSheet,
    TouchableOpacity,
    View,
    Text
} from 'react-native';
import Modal from "react-native-modal";
import Icon from 'react-native-vector-icons/Feather';
import NumericInput from '@components/NumericInput';
import Recipe from '@models/Recipe';

const MAX_QTY = 1000;

interface Props {
    show?: boolean;
    onChangeValue?: Function;
}

interface State {
    value: number;
}
class QuantityDialog extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            value: 0
        };
    }

    onChangeValue = (value: number) => {
        if (value > MAX_QTY) {
            this.setState({ value: MAX_QTY });
        } else {
            this.setState({ value });
        }
    }

    updateValue = () => {
        if (this.props.onChangeValue) {
            this.props.onChangeValue(this.state.value);
        }
    }

    delete = () => {
        if (this.props.onChangeValue) {
            this.props.onChangeValue(0);
        }
    }

    close = () => {
        if (this.props.onChangeValue) {
            this.props.onChangeValue(this.state.value);
        }
    }

    render() {
        return (
            <Modal isVisible={this.props.show}>
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
                            <Text>Update recipe quantity</Text>
                            <TouchableOpacity onPress={this.close}>
                                <Icon name='x' size={30} color={colors.white} />
                            </TouchableOpacity>
                        </View>
                        <NumericInput value={this.state.value} onChangeValue={this.onChangeValue} />
                        <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 8 }}>
                            <TouchableOpacity onPress={this.delete}>
                                <Icon name='trash-2' size={30} color={colors.white} />
                            </TouchableOpacity>
                            <TouchableOpacity onPress={this.updateValue}>
                                <Icon name='check' size={30} color={colors.white} />
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        );
    }
}

const styles = StyleSheet.create({
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

export default QuantityDialog;
