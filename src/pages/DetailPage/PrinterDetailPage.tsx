import AbstractComponent from '@components/AbstractComponent';
import React from 'react';
import HttpClient from '@services/HttpClient';
import ioc, { HTTP_CLIENT, Bind } from '../../ioc/ServiceContainer';
import {
    Text,
    View,
    StyleSheet,
    Dimensions,
    ActivityIndicator,
    GestureResponderEvent,
    TouchableOpacity,
    Alert
} from 'react-native';
import { connect } from 'react-redux';
import Appbackground from '../../components/AppBackground';
import CheckingTask from '@components/CheckingTask'
import Carousel from 'react-native-snap-carousel';
import TaskStep from './TaskStep';
import VoiceRecognitionComponent, { VOICE_ACTIONS } from '../../components/VoiceRecognitionComponent';
import StorageService, { Storage, StorageKeys } from '@services/StorageService';
import {
    getFontContrast,
} from '@components/FontColorHelper';
import Icon from 'react-native-vector-icons/Entypo';
import colors from '@theme/colors';
import ConfirmationDialog from '@components/ConfirmationDialog';
import InterventionService from '@services/InterventionService';
import { ManualPages } from '@components/Manual';
import EventEmitterService, { EventTypes } from '@services/EventEmitterService';
import ClientServer, { ApiBaseUrl } from '@native/ClientServer';
import IDetailPage from '@models/detail/IDetailPage';
import ITaskStep from '@models/detail/ITaskStep';
import TtsService from '@services/TtsService';
import SwipeView from '@components/SwipeView';
import TaskService from '@services/TaskService';
import InterventionJob from '@models/InterventionJob';
import RNBeep from 'react-native-a-beep';
import Orientation from 'react-native-orientation-locker';

interface Props {
    navigation: any;
    route: any;
}

interface State {
    isPingSuccess: any,
    isLoading: any
}

class PrinterDetailPage extends AbstractComponent<Props, State> {
    private taskService: TaskService = Bind('taskService');



    constructor(props: Props) {
        super(props);
        this.state = {
            isPingSuccess: false,
            isLoading: true
        };
    }

    componentDidMount() {
        // this.taskService.pingIp(this.props.route?.params?.printDetailstask.uuid).then((response) => {
        //     this.setState({
        //         isPingSuccess: response,
        //         isLoading: false
        //     })
        // }).catch(e => console.log(e));
    }


    completeTask = () => {
        this.props.navigation.pop()
    }

    getStationName() {
        // let station = '';
        // this.props.route?.params?.printDetailsstation.forEach(stationItem => {
        //     if (stationItem.uuid === this.props.route?.params?.printDetailstask.station) {
        //         station = !!stationItem ? stationItem.name : ''
        //     }
        // });
        // return station
    }

    render() {
        let printDetails = this.props.route?.params?.printDetails;

        let isSuccess =printDetails.isSuccess;
        
        return (
            <Appbackground
                hideBack={false}
                doNaviagte={!this.state.isBackButtonVisible}
                navigation={this.props.navigation}
            >

                <View style={{ flex: 1, flexDirection: 'column', alignSelf: 'stretch', backgroundColor: colors.white }}>
                    <SwipeView
                        onSwipedRight={this.completeTask}
                        disableSwipeToLeft={false}
                        disableSwipeToRight={false}>
                        <View>
                            <View style={{ backgroundColor: colors.darkGrey, padding: 16 }}>
                                <Text style={{ fontWeight: 'bold', fontSize: 15, textAlign: 'center', color: colors.black  }}>Please label your product</Text>
                            </View>

                            <View style={{ flexDirection: "column", margin: 16 }}>
                                <Text style={{ padding: 8, color: isSuccess ? colors.borderGreen : colors.red }}>{isSuccess ? 'The label is printed.' : 'Label could not be printed!'}</Text>
                                { (!!printDetails.intervention) && <Text style={{ padding: 8 }}>Intervention      :  {printDetails.intervention}</Text> }
                                <Text style={{ padding: 8 }}>Task      :  {printDetails.task}</Text>
                                <Text style={{ padding: 8 }}>Station   :  {printDetails.station}</Text>
                                <Text style={{ padding: 8 }}>Recipe    :   {printDetails.recipe}</Text>
                            </View>
                        </View>
                    </SwipeView>
                </View>
            </Appbackground>
        )
    }

}


const mapStateToProps = (state: any) => {
    return {
        ip: state.appState.ip,
        priorityTaskcount: state.task.priorityTaskcount,
        userId: state.appState.userId,
    };
};

export default connect(mapStateToProps, null)(PrinterDetailPage);


const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        flexDirection: 'column'
    },
    touchableOpacityStyle: {
        position: 'absolute',
        alignItems: 'center',
        justifyContent: 'center',
        right: 20,
        top: 0,
        bottom: 0,
        elevation: 6,
    },
    floatingButtonStyle: {
        resizeMode: 'contain',
        width: 70,
        height: 45,
        backgroundColor: 'red',
        justifyContent: 'center',
        borderRadius: 5,
    },
});