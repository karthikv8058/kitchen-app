import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default class TaskHeader extends Component {
    render() {
        return (
            <View style={[styles.container]}>
                <Text style={{flex: 1, color: '#fff', fontSize: 16}}>Task</Text>

                <Text
                style = {[{width : 100, color: '#fff', fontSize: 16}]}>
                    Station
                </Text>

                <Text
                style = {[{width: 100, color: '#fff', fontSize: 16}]}>
                    Amount
                </Text>

                <Text
                style = {[{width: 50, color: '#fff', fontSize: 16}]}>
                    Timing
                </Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        marginLeft : 10,
        marginRight : 10,
        flexDirection: 'row',
    }});
