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

interface Props {
    message: string;
    show?: boolean;
    onComplete?: Function;
    onClose?: Function;
}

class ConfirmationDialog extends AbstractComponent<Props, {}> {

    onClose = () => {
        if (this.props.onClose) {
            this.props.onClose();
        }
    }

    onComplete = () => {
        if (this.props.onComplete) {
            this.props.onComplete();
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
                            <Text style={{color:colors.white}}>{this.props.message}</Text>
                        </View>

                        <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginVertical: 8 }}>
                            <TouchableOpacity onPress={this.onClose}>
                                <Icon name='x' size={30} color={colors.white} />
                            </TouchableOpacity>
                            <TouchableOpacity onPress={this.onComplete}>
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

export default ConfirmationDialog;
