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
        this.taskService.pingIp(this.props.route?.params?.task.uuid).then((response) => {
            this.setState({
                isPingSuccess: response,
                isLoading: false
            })
        }).catch(e => console.log(e));
    }


    completeTask = () => {
        this.props.navigation.pop()
    }
    getStationName(){        
        let station='';
        this.props.route?.params?.station.forEach(stationItem => {
            if(stationItem.uuid===this.props.route?.params?.task.station){
                station=!!stationItem?stationItem.name:''
            }
        });
        return station
    }

    render() {

        return (
            <Appbackground
                hideBack={false}
                doNaviagte={!this.state.isBackButtonVisible}
                navigation={this.props.navigation}
               >

                <View style={{ flex: 1, flexDirection: 'column', alignSelf: 'stretch', }}>
                    <SwipeView
                        onSwipedRight={this.completeTask}
                        disableSwipeToLeft={false}
                        disableSwipeToRight={false}>
                        {this.state.isLoading? <ActivityIndicator size='large' color={colors.white} />:<View>
                            <Text style={{fontWeight:'bold', marginTop:20,fontSize:15, textAlign: 'center', color: !!this.state.isPingSuccess  ? colors.black : colors.red, }}>{!!this.state.isPingSuccess  ? 'Please label your product! The label is printed.' : 'Please label your product! could not be printed.'}</Text>
                            {this.state.isPingSuccess?<View style={{height: Dimensions.get('window').height}}>
                            <Text style={{marginTop:20,fontSize:15, marginLeft:20, marginRight:15,color:  colors.black  }}>{'Task      :  '+!!this.props.route?.params?.task?this.props.route?.params?.task.name:''}</Text>
                            <Text style={{marginTop:20,fontSize:15, marginLeft:20,marginRight:15, color:  colors.black  }}>{'Station   :  '+this.getStationName()}</Text>
                            <Text style={{marginTop:20,fontSize:15, marginLeft:20,marginRight:15, color:  colors.black  }}>{'Recipe    :  '+!!this.props.route?.params?.task && this.props.route?.params?.task.recipes?this.props.route?.params?.task.recipes.name:''}</Text>
                            </View>:null}
                        </View>}
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