import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { FlatList, StyleSheet, Text, View, Image } from 'react-native';
import AbstractComponent from './AbstractComponent';
import Recipe from '@models/Recipe';
import Work from '@models/Work';
import { getFontContrast } from './FontColorHelper';
import { msToTime } from '../utils/commonUtil';
import ChefActivityLog from '@models/ChefActivityLog';
import Station from '@models/Station';
import OrderList from '@models/OrderList';
import User from '@models/User';

interface Props {
    recipes: Array<Recipe>,
    stationList:Array<Station>,
    orderList:Array<OrderList>,
    userList: Array<User>
}
interface State {
}
export default class Recipes extends AbstractComponent<Props, State> {
    private startedTime: string = '';
    private completdTime: string = '';
    private userName: string = '';
    private textcolors: string = '';
    constructor(props: Props) {
        super(props);
        this.state = {
        };
    }

    renderTask(works: Work[]) {
        return (
            <FlatList
                data={works}
                style={{ marginLeft: 20, marginRight: 20 }}
                extraData={this.state}
                renderItem={this.renderWork.bind(this)}
            />
        );
    }

    calculateTaskTime(workId: number) {
        this.props.orderList.chefActivityLogs.forEach((log: ChefActivityLog) => {
            if (log.workId === workId) {
                this.props.userList.forEach(user => {
                    if (user.uuid === log.userId) {
                        this.userName = user.name;
                    }
                });
                if (log.status === 2) {
                    if (this.startedTime === '') {
                        this.startedTime = msToTime(log.createdAt);
                    }
                } else if (log.status === 4) {
                    this.completdTime = msToTime(log.createdAt);
                }
            }
        });
    }

    renderWork(workItems: Work) {
        this.startedTime = '';
        this.completdTime = '';
        this.userName = '';
        let work = workItems.item;        
        let isTranportTask = work.transportType === 8 || work.transportType === 16 || work.transportType === 32;
        let backgroundColor = isTranportTask ?
            colors.white : this.getStationColor(work.task.station);
        let textColor = (isTranportTask) ? colors.black : this.textcolors;
        { this.calculateTaskTime(work.id); }
        return (
            <View style={{
                alignItems: 'center',
                justifyContent: 'center', marginTop: 4, flex: 1, flexDirection: 'row', minHeight: 50,
                backgroundColor: backgroundColor, borderRadius: 10, marginBottom: 3
            }}>
                <Text style={{ color: textColor, marginLeft: 20, flex: 1 }}>
                    {(work.transportType === 0 || work.transportType === 4) ? work.task.name : work.title}</Text>
                <Text style={{ color: textColor, marginLeft: 20, flex: 1 }}>{work.quantity}</Text>
                <Text style={{ color: textColor, marginLeft: 20, flex: 1 }}>{this.startedTime}</Text>
                <Text style={{ color: textColor, marginLeft: 20, flex: 1.5 }}>{this.completdTime}</Text>
                <Text style={{ color: textColor, marginLeft: 20, flex: 1.5 }}> {this.userName}</Text>
            </View>
        );
    }

    getStationColor(stationId: number) {
        let stationColor = '';
        this.props.stationList.map((station) => {
            if (station.uuid === stationId) {
                stationColor = station.color;
            }
        });
        this.textcolors = getFontContrast(stationColor);
        return stationColor;
    }

    renderRecipes = (recipe: Recipe, index: number) => {
        return (
            <View style={styles.recipeContainer}>
                {recipe.work ? <Text style={(this.props.recipes.id === recipe.mealsId && (index === 0)) ?
                    styles.recipeNameContainer : styles.leftContainer}>{recipe.recipe.name} </Text>
                    : null}
                {recipe.work !== undefined ? this.renderTask(recipe.work) : null}
            </View>
        )
    }

    render() {
        return (
            <View style={styles.delayedConatiner}>
                < FlatList
                    data={this.props.recipes.orderLine}
                    extraData={this.state}
                    renderItem={({ item, index }) => this.renderRecipes(item, index)}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    delayedConatiner: {
        flexDirection: 'column',
        flex: 1,
    },
    recipeContainer: {
        marginTop: 10,
        flexDirection: 'column',
        flex: 1
    },
    recipeNameContainer: {
        color: colors.white,
        margin: 10,
        fontWeight: 'bold',
    },
    leftContainer: {
        color: colors.white,
        margin: 10,
        marginLeft: 20,
        fontWeight: 'bold'
    }
});
