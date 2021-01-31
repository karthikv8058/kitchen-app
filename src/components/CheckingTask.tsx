import React, { Component } from 'react';
import { TouchableOpacity, Text, Slider, View, StyleSheet, ToastAndroid, AsyncStorage } from 'react-native';
import colors from '@theme/colors';
import HttpClient from '@services/HttpClient';
import ApiBuilder from '@api/routes';
import Modal from 'react-native-modal';
import t from '@translate';
import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import AclNavigation from './src/acl/AclNavigation';

export default class CheckingTask extends Component {
    state = {
        modalVisible: false,
        checkingTask: null,
        timeValue: 30,
        displayTime: '30 S'
    };
    private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    private apiBuilder?: ApiBuilder;

    constructor(props) {
        super(props);
        this.onPressPending = this.onPressPending.bind(this);
    }

    hideCheckingTask() {
        this.setState({ modalVisible: false });
    }

    showCheckingTask(checkingTask: any) {
        if (this.state.modalVisible) {
            return;
        }
        this.setState({ modalVisible: true, timeValue: 30, checkingTask: checkingTask });
    }

    onPressPending() {
        if (this.state.timeValue && this.state.timeValue > 0) {
            this.checkingTaskToPending();
        } else {
            ToastAndroid.show(t('checkin-task.enter-valid-timein-minute'), ToastAndroid.SHORT);
        }
    }

    checkingTaskToPending() {
        AsyncStorage.getItem('serverIP').then(ip => {
            this.apiBuilder = new ApiBuilder(ip);
            this.httpClient.post(this.apiBuilder.paths.updateIntervention, {
                status: 0,
                intervention: this.props.intervention,
                time: this.state.timeValue,
                reduceValue: false,
            }).then(response => {
                this.props.pendingCallback();
            }).catch((er) => console.log(er));
        });
        this.hideCheckingTask();
    }
    showTextBox() {
        return (
            <View>
                <View style={{ flexDirection: 'row' }}>
                    <Text
                        style={{ fontSize: 10, marginTop: 8,paddingLeft:8 }}>{t('checking-task.remind')}</Text>
                    <Text
                        style={{ fontSize: 15, margin: 4, color: colors.black, alignSelf: 'flex-end' }}>
                        {this.state.displayTime}
                    </Text>
                </View>
                <Slider
                    minimumValue={1}
                    maximumValue={11}
                    minimumTrackTintColor='#00ABB4'
                    maximumTrackTintColor='#000000'
                    style={styles.reminderInput}
                    onValueChange={(data) => this.setpendingTime(data)}
                />
            </View>
        );

    }
    setDisplayTime = (displayTime: string, timeValue: number) => {
        this.setState({
            displayTime: displayTime,
            timeValue: timeValue
        })
    }
    setpendingTime = (data: any) => {
        switch (Number((data).toFixed())) {
            case 1: this.setDisplayTime('30 S', 30)
                break;
            case 2: this.setDisplayTime('1 M', 60)
                break;
            case 3: this.setDisplayTime('2 M', 120)
                break;
            case 4: this.setDisplayTime('4 M', 240)
                break;
            case 5: this.setDisplayTime('5 M', 300)
                break;
            case 6: this.setDisplayTime('10 M', 600)
                break;
            case 7: this.setDisplayTime('15 M', 900)
                break;
            case 8: this.setDisplayTime('30 M', 1800)
                break;
            case 9: this.setDisplayTime('1 H', 3600)
                break;
            case 10: this.setDisplayTime('1.30 H', 5400)
                break;
            case 11: this.setDisplayTime('2 H', 7200)
                break;
            default: this.setDisplayTime('30 S', 30)
                break;
        }
    }

    showName = () => {
        // if (this.state.checkingTask != null) {
        //     return this.props.intervention.name;
        // }
        return "";
    }

    render() {
        return (
            <Modal
                hasBackdrop={true}
                isVisible={this.state.modalVisible}>
                <View style={styles.container}>
                    <View style={{
                        width: 250,
                        backgroundColor: colors.white,
                        borderRadius: 4,
                    }}>
                        {/* <Text style={styles.titleText}> {this.showName()}</Text> */}

                        {this.showTextBox()}
                        <View style={{
                            flexDirection: 'row'
                        }}>

                            <TouchableOpacity
                                style={{ flex: 1 }}
                                onPress={this.onPressPending}>
                                <Text style={styles.buttonText}>Pending</Text>
                            </TouchableOpacity>

                        </View>
                    </View>
                </View>
            </Modal>);
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    },
    titleText: {
        color: '#00767C',
        margin: 4,
        padding: 4,
        fontSize: 20,
        textAlign: 'center'
    },
    reminderInput: {
        borderWidth: 1,
        borderRadius: 4,
        margin: 8,
        height: 80,
    },
    buttonText: {
        backgroundColor: '#00ABB4',
        margin: 4,
        padding: 4,
        fontSize: 20,
        textAlign: 'center'
    }
});
