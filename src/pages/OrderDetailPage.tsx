import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import Recipes from '@components/Recipes';
import Course from '@models/Course';
import Meal from '@models/Meal';
import OrderList from '@models/OrderList';
import Station from '@models/Station';
import User from '@models/User';
import OrderService from '@services/OrderService';
import StationService from '@services/StationService';
import UserService from '@services/UserService';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { ActivityIndicator, FlatList, StyleSheet, Text, View } from 'react-native';
import { Bind } from '../ioc/ServiceContainer';
import { menuCategory, OPEN_ORDER } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';

interface Props { navigation: any; }

interface State {
    isLoading: boolean;
    orderList: Array<OrderList>;
    userList: Array<User>;
    stationList: Array<Station>;
}

export default class OrderDetailPage extends AbstractComponent<Props, State> {
    private orderId = '';
    private stationService: StationService = Bind('stationService');
    private userService: UserService = Bind('userService');
    private orderService: OrderService = Bind('orderService');

    constructor(props: Props) {
        super(props);
        this.state = {
            isLoading: true,
            orderList: [],
            stationList: [],
            userList: [],
        };
        this.renderCourse = this.renderCourse.bind(this);
        this.renderMeals = this.renderMeals.bind(this);
        this.renderRecipes = this.renderRecipes.bind(this);
        this.loadOrder = this.loadOrder.bind(this);
        this.loadStationList = this.loadStationList.bind(this);
        this.loadUserList = this.loadUserList.bind(this);
    }

    componentDidMount() {
        this.orderId = this.props.route.params.orderId;
        this.loadStationList();
    }

    loadStationList() {
        this.stationService.loadStations().then((stations) => {
            if (responseChecker(stations, this.props.navigation)) {
                this.loadOrder(stations);
            }
        });
    }

    loadOrder(stationList: Station[]) {
        this.orderService.getOrderList(this.orderId, (response) => {
            if (responseChecker(response, this.props.navigation)) {
                if(response.order != null){
                    this.loadUserList(response, stationList);
                }else{
                    this.setState({ isLoading: false });
                }
                
            }
        }, (error: Error) => {
            this.setState({ isLoading: false });
        });
    }

    loadUserList(orderList: OrderList[], stationList: Station[]) {
        this.userService.getUsersAndStations().then(users => {
            if (responseChecker(users, this.props.navigation)) {
                this.updateState(orderList, stationList, users);
            }
        });
    }

    updateState(orderList: OrderList[], stationList: Station[], userList: User[]) {
        orderList.order.courses.forEach((courses) => {
            courses.meals.forEach((meals) => {
                meals.orderLine.forEach((recipe) => {
                    orderList.work.forEach((work) => {
                        {
                            (this.orderId === work.orderId && meals.id === work.mealsId &&
                                recipe.id === work.orderLineId ? (recipe['work'] ?
                                    recipe.work.push(work) : recipe['work'] = [work]) : null);
                        }
                    });
                });
            });
        });
        this.setState({
            stationList: stationList,
            orderList: orderList,
            userList: userList,
            isLoading: false,
        });
    }

    renderTitles() {
        return (
            <View style={{ flexDirection: 'row' }}>
                <Text style={{ flex: 1, color: colors.white, margin: 5 }}>{t('order-details.recipe-task')}</Text>
                <Text style={{ flex: 1, color: colors.white, margin: 5 }}>Quantity</Text>
                <Text style={{ flex: 1, color: colors.white, margin: 5 ,textAlign:'center'}}>{t('order-details.started-at')}</Text>
                <Text style={{ flex: 1.5, color: colors.white, margin: 5 ,textAlign:'center'}}>{t('order-details.completed-at')}</Text>
                <Text style={{ flex: 1.5, color: colors.white, margin: 5,textAlign:'center' }}>{t('order-details.assigned-to')}</Text>
            </View>
        );
    }

    renderMeals(courseItem: Course) {
        let Items = courseItem.item;
        return (
            < FlatList
                data={Items.meals}
                extraData={this.state}
                renderItem={(item) => this.renderRecipes(item)}
            />
        );
    }

    renderRecipes = (meals: Meal ) => {
        return (
            <Recipes
                recipes={meals.item}
                stationList={this.state.stationList}
                orderList={this.state.orderList}
                userList={this.state.userList}>
            </Recipes>
        );
    }

    renderCourse() {
        return (
            <FlatList
                data={this.state.orderList.order.courses}
                extraData={this.state}
                renderItem={this.renderMeals.bind(this)}
            />
        );
    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                index={OPEN_ORDER}
                category={menuCategory.orderCategory} >
                <View style={styles.mainContainer}>
                    {this.renderTitles()}
                    {this.state.isLoading ?
                        <ActivityIndicator size='large' color={colors.white} />
                        : this.state.orderList.order ? this.renderCourse() : null}
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        flexDirection: 'column'
    },

});
