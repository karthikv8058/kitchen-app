import colors from '@theme/colors';
import React, { Component } from 'react';
import { Text, ScrollView, StyleSheet, View, Modal } from 'react-native';
import t from '@translate';


interface Props {
    togglePrinterModal?: Function;
    printerModalVisible?: boolean;
    deletePrinterMessage?: Function;
    storeToWeb?: Function

}
interface State {
}
export default class PrinterOrder extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
        };
    }
    render() {
        return (
            <Modal
                transparent={true}
                visible={this.props.printerModalVisible}
                onRequestClose={() => { this.props.togglePrinterModal!(false); }}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={styles.modalContainer}>
                        <Text style={{ textAlign: 'center', color: colors.black, marginTop: 10 }}>{t('order-overview.choose-option')}</Text>
                        <ScrollView>
                            <Text style={styles.textHeader} onPress={() =>this.props.deletePrinterMessage!()}>{t('order-overview.delete')}</Text>
                            <Text style={styles.textHeader} onPress={() =>this.props.storeToWeb!()}>{t('order-overview.store-to-analyze')}</Text>
                        </ScrollView>
                    </View>
                </View>
            </Modal>
        )
    }
}
const styles = StyleSheet.create({
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
