import Work from '@models/Work';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import { Modal, StyleSheet, Text, View } from 'react-native';
import AbstractComponent from './AbstractComponent';

interface Props {
    show?: boolean;
    onClose?: Function;
    assignTask: Function;
}

interface State {
    show: boolean;
}

export default class TaskAssignmentConfirmation extends AbstractComponent<Props, State> {

    private work?: Work;

    constructor(props: Props) {
        super(props);
        this.state = {
            show: false
        }
    }


    show = (work: Work) => {
        this.work = work;
        this.setState({
            show: true
        });
    }

    close = () => {
        this.setState({
            show: false
        });
    }

    onAssign = () => {
        if (this.props.assignTask) {
            this.props.assignTask(this.work, true);
        }
    }

    render() {
        return (
            <Modal
                transparent={true}
                visible={this.state.show}
                onRequestClose={this.onClose}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <View style={styles.modalContainer}>
                        <Text style={{ textAlign: 'center', color: colors.black, marginTop: 10 }}>
                            {t('task-overview.change-assigned')}
                        </Text>
                        <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
                            <Text style={styles.textHeader} onPress={this.onAssign}>{t('task-overview.yes')}</Text>
                            <Text style={styles.textHeader} onPress={this.close}>{t('task-overview.no')}</Text>
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
        flex: 1,
        color: colors.black
    }
});
