import Work, { WORK_STARTED } from '@models/Work';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import { StyleSheet, Text, TouchableOpacity, TouchableWithoutFeedback, View, Alert } from 'react-native';
import Swipeout from 'react-native-swipeout';
import Icon from 'react-native-vector-icons/Feather';

import { getBackgroundViewStyle } from '../utils/taskColorUtils';
import { getFontContrast } from './FontColorHelper';
import SwipeView from './SwipeView';
import Course from '@models/Course';

interface Props {

    isActive?: boolean;
    work: Work;
    index?: any;
    course: Course;
    onFinishTask?: Function;
    navigateToDetail?: Function;
    onLongPress?: Function;
    onPress?: Function;
    unassignTask?: Function;
    isMachineTask: boolean;
    itemcolor?: string;
    stationColorDetailList?: [];
}

interface State {
    show: true;
    textcolors: String;
}

export default class Task extends Component<Props, State> {

    private stationColor = '';
    private textcolors = '';

    constructor(props: Props) {
        super(props);
        this.state = {
            textcolors: '',
            show: true,
        };
        this.showAssignTask = this.showAssignTask.bind(this);
        this.showTaskDetail = this.showTaskDetail.bind(this);
        this.onTaskPress = this.onTaskPress.bind(this);
    }

    renderSecondCol(assignedStyle: any) {
        if (this.props.isMachineTask) {
            return;
        }
        return (
            <Text style={[assignedStyle, { flex: 1 }]}>
                {this.props.work.output}
            </Text>
        );
    }

    renderTimeleft(assignedStyle: any) {
        let timeLeft = Math.round(this.props.work.timeLeft/60000);
        let textForNormalTask =
            (this.props.work.course && this.props.work.course.isOnCall) ? t('order-overview.onCall') : (timeLeft < 0 ? 0 : timeLeft);
        let text = this.props.isMachineTask ? (Math.round(this.props.work.timeRemaining / (60000))) : textForNormalTask;
        return (
            <Text style={[assignedStyle, { flex: 1, textAlign: 'right', marginRight: 10 }]}>
                {text}
            </Text>
        );
    }

    showAssignTask() {
        let isMachineTask =this.props.work.task&& this.props.work.task.machine && !this.props.work.task.chefInvolved;
        if (!isMachineTask && this.props.onLongPress) {
            this.props.onLongPress();
        }
    }

    onTaskPress() {
        let isAssigned = ((this.props.work.transportType & 4) == 0 && this.props.work.userId && this.props.work.status == WORK_STARTED);
        if (isAssigned) {
            this.showTaskDetail()
        } else {
            let isMachineTask = this.props.work.task && this.props.work.task.machine && !this.props.work.task.chefInvolved;
            if (!isMachineTask) {                
                this.props.assignTask(this.props.work);
            }
        }
    }

    showTaskDetail() {
        let isAssigned = ((this.props.work.transportType & 4) == 0 && this.props.work.userId && this.props.work.status == WORK_STARTED);
        let isMachineTask = this.props.work.task && this.props.work.task.machine && !this.props.work.task.chefInvolved;
        if (isAssigned && !isMachineTask && this.props.navigateToDetail) {
            this.props.navigateToDetail(this.props.work);
        }
    }

    renderIcon(iconColor: string) {
        return (
            <Icon style={{ marginLeft: 10, marginTop: 10 }} name='log-in' size={15} color={iconColor} />
        );
    } 
    onFinishTask = () => {
        if (this.props.onFinishTask) {
            return this.props.onFinishTask(this.props.work);
        }
    }

    unassignTask = () => {
        if (this.props.unassignTask) {
            return this.props.unassignTask();
        }
    }

    render() {
        let textColor = (this.props.work.fontColor ? this.props.work.fontColor : colors.black);
        let backColor = (this.props.work.backgroundColor ? this.props.work.backgroundColor : colors.white);
        let isMachineTask = this.props.work.task && this.props.work.task.machine && !this.props.work.task.chefInvolved;
        let isAssigned = (this.props.work.transportType & 4) == 0 &&
            !isMachineTask &&
            this.props.work.userId && this.props.work.status === WORK_STARTED;
        let assignedStyle = isAssigned && !isMachineTask ? { color: textColor, fontWeight: 'bold' } : { color: textColor };
        let isAutoAssign = this.props.work.userObject && this.props.work.userObject.autoAssign;
        let button = [
            {
                text: '',
                backgroundColor: 'rgba(0, 0, 0, 0.0)'
            }
        ];
        let rigtButton = (isMachineTask || isAutoAssign || !isAssigned) ? undefined : button;
        let disableSwipeToLeft = !isAssigned || isMachineTask;
        return (
            <SwipeView
                disableSwipeToLeft={disableSwipeToLeft}
                onSwipedLeft={this.unassignTask}
                onSwipedRight={this.onFinishTask.bind(this)}>
                <TouchableWithoutFeedback
                    onPress={this.onTaskPress}
                    onLongPress={this.showAssignTask}>
                    <View style={{
                        flexDirection: 'column', backgroundColor: backColor, margin: 5,
                        borderRadius: 10, minHeight: 50
                    }}>
                        <Text style={[styles.recipeText, { color: textColor }]}>
                            {(this.props.work.recipe && this.props.work.transportType != 32 ? this.props.work.recipe.name : '')}
                        </Text>
                        <View key={this.props.index} style={[styles.container,
                        { shadowColor: !isMachineTask ? this.props.work.shadowColor : '' }]} >
                            {(this.props.work.transportType > 0 && (this.props.work.transportType & 4) === 0) ?
                                this.renderIcon(textColor) : null}
                            <Text
                                style={[styles.taskTitle, assignedStyle]}>
                                {this.props.work.title ? this.props.work.title : this.props.work.task.name}
                            </Text>
                           
                       
                            {this.renderSecondCol(assignedStyle)}
                            {this.renderTimeleft(assignedStyle)}
                        </View>
                        {((isAssigned && !isMachineTask) ?
                            <Text numberOfLines={1}
                                style={{ color: textColor, fontSize: 10, marginLeft: 5, marginBottom: 2, fontWeight: 'bold' }}>
                                {(this.props.work.userObject ? t('task-overview.chef-name') + this.props.work.userObject.name : '')}
                            </Text > : null)}
                    </View>
                </TouchableWithoutFeedback>
            </SwipeView>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
    },
    activeRed: { backgroundColor: colors.activeRed },
    activeGreen: { backgroundColor: colors.backgroundGreen },
    activeYellow: { backgroundColor: colors.yellowLine },

    inactiveYellow: {
        backgroundColor: colors.inactiveBackground,
        borderWidth: 2,
        borderColor: colors.inactiveYellow
    },
    inactiveGreen: {
        backgroundColor: colors.inactiveBackground,
        borderWidth: 2,
        borderColor: colors.inActiveGreen
    },
    recipeText: {
        justifyContent: 'center',
        marginLeft: 5,
        marginTop: 5,
        alignItems: 'center',
        fontSize: 10,
        flex: 1.5,
        fontWeight: 'bold',
    },
    taskTitle: {
        flex: 1.5,
        marginTop: 5,
        marginBottom: 5,
        justifyContent: 'center',
        marginLeft: 5,
        alignItems: 'center'
    }
});
