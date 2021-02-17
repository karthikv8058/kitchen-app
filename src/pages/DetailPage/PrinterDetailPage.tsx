import AbstractComponent from '@components/AbstractComponent';
import React from 'react';
import  { Bind } from '../../ioc/ServiceContainer';
import {
    Text,
    View,
    StyleSheet
} from 'react-native';
import { connect } from 'react-redux';
import Appbackground from '../../components/AppBackground';
import colors from '@theme/colors';
import SwipeView from '@components/SwipeView';
import TaskService from '@services/TaskService';

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


    completeTask = () => {
        this.props.navigation.pop()
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
                                <Text style={{ padding: 8, color: isSuccess ? colors.gradientStart : colors.red }}>{isSuccess ? 'The label is printed.' : 'Label could not be printed!'}</Text>
                                { (!!printDetails.intervention) && <Text style={{ padding: 8 }}>Intervention      :  {printDetails.intervention}</Text> }
                                <Text style={{ padding: 8 }}>Task      :  {printDetails.task}</Text>
                                <Text style={{ padding: 8 }}>Station   :  {printDetails.station}</Text>
                                <Text style={{ padding: 8 }}>Recipe    :   {printDetails.recipe}</Text>
                                <Text style={{ padding: 8 }}>Table     :   {printDetails.table}</Text>
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