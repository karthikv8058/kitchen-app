import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import Order from '@models/Order';
import colors from '@theme/colors';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

interface Props {
    navigation: any;
}
interface State {
    order: Order;
}
export default class Interventiontaskpage extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
    }
    componentDidMount() {

    }
    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}>
                <View style={styles.messageContainer}>
                    <Text style={styles.textStyle}>Intervention Task</Text>
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
