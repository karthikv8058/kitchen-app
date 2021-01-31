import React, { Component } from 'react';
import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import { convertRGBToHex, getBackgroundColorTone, getFontColorTone, getShadowColor } from '@components/FontColorHelper';
import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import t from '@translate';
import { AsyncStorage, FlatList, StyleSheet, Text, View, ActivityIndicator, Alert, ToastAndroid, TouchableOpacity, Modal, ScrollView } from 'react-native';
import { connect } from 'react-redux';
import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import { setHighPriorityTaskCount } from '../redux/actions/TaskAction';
import { menuCategory, STATION_VIEW } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import Icon from 'react-native-vector-icons/Feather';
import SectionedMultiSelect from '@components/sectioned-multi-select/sectioned-multi-select';
import SwipeView from '@components/SwipeView';
import TaskService from '@services/TaskService';
import Work, { WORK_STARTED } from '@models/Work';
import CheckBox from 'react-native-check-box';
import TaskFilter from '@models/TaskFilter';
import Station from '@models/Station';

interface Props {
    close: Function;
    onApplyFilter: Function;
    filter: TaskFilter;
    stations: Station[];
}
interface State {
    show: boolean;
    selectedStations: any;
    includeOtherChefTasks: boolean;
    includeWaitingMachineTasks: boolean;
}

class FilterStations extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            selectedStations: [],
            show: false,
            includeOtherChefTasks: false,
            includeWaitingMachineTasks: false
        };
    }

    onSelectedStationChange = (selectedStations: any) => {
        this.setState({ selectedStations });
    };

    applyFilter = () => {
        if (this.props.onApplyFilter) {
            this.props.onApplyFilter({
                showOtherChefTasks: this.state.includeOtherChefTasks,
                showWaitingMachineTasks: this.state.includeWaitingMachineTasks,
                stations: this.state.selectedStations,
            });
        }
        this.setState({
            show: false
        });
    };

    // close = () => {
    //     if (this.props.close) {
    //         this.props.close();
    //     }
    // };

    onchefSelection = () => {
        this.setState({
            includeOtherChefTasks: !this.state.includeOtherChefTasks
        });
    }

    onMachineSelection = () => {
        this.setState({
            includeWaitingMachineTasks: !this.state.includeWaitingMachineTasks
        });
    }

    show = (taskFilter: TaskFilter) => {
        this.setState({
            includeOtherChefTasks: taskFilter.showOtherChefTasks,
            includeWaitingMachineTasks: taskFilter.showWaitingMachineTasks,
            selectedStations: !!taskFilter.stations ?
                (taskFilter.stations.length === 1 && taskFilter.stations[0] !== '' ? taskFilter.stations :
                    (taskFilter.stations.length > 1 ? taskFilter.stations : [])) : [],
            show: true
        });
    }

    close = () => {
        this.setState({
            show: false
        });
    }

    render() {
        let stations = this.props.stations;
        let selectedStations = stations ? this.state.selectedStations : [];
        return (

            <Modal
                transparent={true}
                visible={this.state.show}
                onRequestClose={this.close}>
                <View style={{ flex: 1, flexDirection: 'column', justifyContent: 'center' }}>
                    <ScrollView >
                        <View style={styles.modalContainer}>
                            <View style={{ marginTop: 10, }}>
                                <SectionedMultiSelect
                                    styles={{
                                        selectedItemText: { color: colors.primaryButton },
                                        selectToggle: {
                                            marginTop: 10
                                        }
                                    }}
                                    showCancelButton={true}
                                    showRemoveAll={true}
                                    confirmText='Apply'
                                    colors={{
                                        chipColor: '#000', selectToggleTextColor: '#ffffff',
                                        primary: colors.primaryButton, cancel: colors.red,
                                        success: colors.primaryButton
                                    }}
                                    items={stations}
                                    uniqueKey='uuid'
                                    searchPlaceholderText='Search station'
                                    selectText=' Filter by station...'
                                    onSelectedItemsChange={this.onSelectedStationChange}
                                    selectedItems={selectedStations}
                                />
                                <View style={{ flex: 1, flexDirection: 'row' }}>
                                    <CheckBox
                                        style={{ flex: 1, padding: 8 }}
                                        onClick={this.onchefSelection}
                                        isChecked={this.state.includeOtherChefTasks}
                                        checkBoxColor={'white'}
                                        rightTextStyle={{ color: colors.white }}
                                        rightText={'Include other chef\'s task'}
                                    />
                                    <CheckBox
                                        style={{ flex: 1, padding: 8 }}
                                        onClick={this.onMachineSelection}
                                        isChecked={this.state.includeWaitingMachineTasks}
                                        checkBoxColor={'white'}
                                        rightTextStyle={{ color: colors.white }}
                                        rightText={'Waiting machine tasks'}
                                    />

                                </View>

                            </View>
                            <View style={{
                                flexDirection: 'row',
                                justifyContent: 'space-around', marginVertical: 8
                            }}>
                                <TouchableOpacity onPress={this.close}>
                                    <Icon name='x' size={30} color={colors.white} />
                                </TouchableOpacity>
                                <TouchableOpacity onPress={this.applyFilter}>
                                    <Icon name='check' size={30} color={colors.white} />
                                </TouchableOpacity>
                            </View>
                        </View>
                    </ScrollView>
                </View>
            </Modal>
        );
    }
}

const styles = StyleSheet.create({
    modalContainer: {
        flexDirection: 'column',
        width: 350,
        borderRadius: 8,
        minHeight: 100,
        marginTop: 80,
        justifyContent: 'center',
        alignSelf: 'center',
        color: colors.black,
        backgroundColor: colors.primaryButton
    },

});

export default FilterStations;
