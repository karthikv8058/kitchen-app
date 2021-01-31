import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import Order from '@models/Order';
import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import { responseChecker } from '../utils/responseChecker';

interface Props {
    navigation: any;
}
interface State {
    order: Order;
}
export default class PrinterMessageView extends AbstractComponent<Props, State> {

    private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    constructor(props: Props) {
        super(props);
        this.state = {
            order: null
        };

    }

    componentDidMount() {
        let orderId = this.props.navigation.getParam('orderId', '');
        this.httpClient.post(this.apiBuilder.paths!.getPrinterData, { orderId: orderId }).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                this.setState({ order: response });
            }
        }).catch((error) => {
            console.error(error);
        });
    }
    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}>
                <View style={styles.messageContainer}>
                    <Text style={styles.textStyle}>{(this.state.order) ? this.state.order.printerData.message : ''}</Text>
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    messageContainer: {
        flex: 1,
        backgroundColor: colors.white,
        padding: 10,
        color: colors.red
    },
    textStyle: {
        color: colors.red
    }
});
