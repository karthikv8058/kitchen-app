import colors from '@theme/colors';
import React, { Component } from 'react';
import { StyleSheet, View } from 'react-native';
import RadialGradient from 'react-native-radial-gradient';

export default class AppGradient extends Component {
    render() {
        return (
            <View
                style={styles.container}>
                <RadialGradient style={styles.container}
                    colors={[colors.gradientStart, colors.gradientEnd]}
                    stops={[0.1, 0.4, 0.3, 0.75]}
                    radius={300}>
                    {this.props.children}
                </RadialGradient>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#e5e5e5',
        justifyContent: 'center',
    }
});
