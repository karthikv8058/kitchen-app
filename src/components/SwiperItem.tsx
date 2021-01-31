import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import t from '@translate';
import React, { Component } from 'react';
import { ActivityIndicator, FlatList, ScrollView, StyleSheet, Text, View } from 'react-native';

import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import AbstractComponent from './AbstractComponent';
import { getFontContrast } from './FontColorHelper';

interface Props {
    stationId: any;
    stationName: any;
    userId: Function;
    stations: Function;
    stationColor: string;
    goToStationList: Function;
}
interface State {
    tasks: [];
    stationUser: any;
    isLoading: any;
    modalVisible: any;
}
export default class SwiperItem extends AbstractComponent<Props, State> {
    private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    constructor(props: Props) {
        super(props);
        this.showAlertForStations = this.showAlertForStations.bind(this);
        this.state = {
            tasks: [],
            stationUser: [],
            isLoading: true,
            modalVisible: true,
        };
    }
    componentDidMount() {
        this.getListofTasks();
        this.getStationUsers();
    }

    getStationUsers() {
        this.httpClient.post(this.apiBuilder.paths!.getstationusers, { stationId: this.props.stationId }).then(response => {
            this.setState({
                stationUser: response,
                isLoading: false
            });
        }).catch(error => {
            this.setState({ isLoading: false });
        });
    }

    renderTaskList() {
        return (
            <ScrollView style={styles.container}>
                <View>
                    <FlatList
                        style={styles.listContainer}
                        extraData={this.state}
                        data={this.state.stationUser}
                        renderItem={this.renderUsers.bind(this)}
                    />
                    <FlatList
                        style={styles.listStyle}
                        extraData={this.state}
                        data={this.state.tasks}
                        renderItem={this.renderTasks.bind(this)}
                    />
                </View>
            </ScrollView>
        );
    }

    showAlertForStations(id: string) {
        this.httpClient.post(this.apiBuilder.paths!.getstationlist, {}).then(response => {
            {
                this.props.userId(id), this.props.stations(response);
            }
        }).catch(error => {
            this.setState({ isLoading: false });
        });
    }

    getFontColor(hexcolor: string) {
        return getFontContrast(hexcolor);
    }

    goToStation(usersId: string) {
        this.props.userId(usersId);
        this.props.goToStationList();
    }
    renderUsers(user: any) {
        let users = user.item;
        return (
            <Text style={styles.textContainer}
                onPress={() => this.props.goToStationList(users.uuid)}>
                {users.name}</Text>
        );
    }

    getTextStyle(status: any) {
        return (
            (status === 2 ? styles.textBold : styles.textNotBold)
        );
    }
    renderTasks(TaskItem: any) {
        let taskList = TaskItem.item;
        const textColor = this.getFontColor(this.props.stationColor);
        return (
            <View style={[styles.taskContainer, { backgroundColor: this.props.stationColor }]}>
                <Text numberOfLines={1} style={[{ color: textColor, fontSize: 10 }, styles.textNotBold]}>
                    {taskList.recipe.name}</Text>
                <Text numberOfLines={1} style={[{ color: textColor }, this.getTextStyle(taskList.status)]}>
                    {(taskList.transportType !== 0 ? taskList.title
                        : taskList.task.name)}</Text>
                {((taskList.status === 2 && taskList.userObject) ?
                    this.renderUserName(taskList.userObject.name, textColor, taskList.status) : null)}

            </View>
        );
    }
    renderUserName(chefName: string, textColor: string, taskStatus: number) {
        return (
            <Text numberOfLines={1} style={[this.getTextStyle(taskStatus), { color: textColor }]}>
                {t('task-overview.chef-name') + chefName}
            </Text >
        );
    }
    getListofTasks() {
        this.httpClient.post(this.apiBuilder.paths!.stationTasks, { stationId: this.props.stationId }).then(response => {
            this.setState({
                tasks: response,
                isLoading: false
            });
        }).catch(error => {
            this.setState({ isLoading: false });
        });
    }

    render() {
        return (
            <View style={{flex:1}}>
                <Text style={styles.headingContainer}
                >{this.props.stationName}</Text>
                {this.state.isLoading ?
                    <ActivityIndicator size='large' color={colors.white} />
                    :
                    this.renderTaskList()}
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        // borderWidth:1,
        flex: 1,
        flexDirection: 'column',
    },
    textBold: {
        fontWeight: 'bold',
    },
    textNotBold: {
        marginLeft: 5,
        marginTop: 2,
        fontWeight: 'normal',
    },
    headingContainer: {
        marginTop: 20,
        marginLeft: 10,
        color: colors.white,
    },
    textContainer: {
        marginTop: 10,
        elevation: 5,
        padding: 10,
        borderRadius: 10,
        borderWidth: 2,
        backgroundColor: colors.liteGray,
        borderColor: colors.liteGray,
        color: colors.black,
        fontWeight: 'bold',
        height: 40,
    },

    listStyle: {
        marginLeft: 10,
        marginTop: 20,
        paddingRight: 10,
        fontWeight: 'bold',
    },

    listContainer: {
        height: 110,
        flexGrow: 1,
        marginTop: 20,
        // marginLeft: 20,
        paddingRight: 10,
        color: colors.white,
        fontWeight: 'bold',
    },
    taskContainer: {
        flexDirection: 'column',
        marginTop: 5,
        borderRadius: 10,
        padding: 5,
        fontWeight: 'bold',
        color: colors.black,
        minHeight: 50,
    },
});
