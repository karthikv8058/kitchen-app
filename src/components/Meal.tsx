import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { FlatList, StyleSheet, Text, View, Image, AsyncStorage } from 'react-native';
import { convertMS, getDifferenceTime } from '../utils/dateUtil';
import { getTimeDiffrece } from '../utils/timeUtils';
import AbstractComponent from './AbstractComponent';
import Meals from '@models/Meal';
import courses from '@models/Course';
import Recipe from '@models/Recipe';
import placeHolder from '@components/assets/placeholder.png';
import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';

interface Props {
    navigation?: any;
    course?: [];
    order: [];
    recipe?: any
}
interface State {
    modalVisible: boolean;
}

export default class Meal extends AbstractComponent<Props, State> {
    private ORDERTO: string = 'WEB'
    apiBuilder = ioc.ServiceFactory.getServiceBy('apiBuilder');

    constructor(props: Props) {
        super(props);
        this.state = {
            modalVisible: false,
        };
        this.renderMeals = this.renderMeals.bind(this);
        this.renderActualDeliveryTime = this.renderActualDeliveryTime.bind(this)
    }

    checkStyle(deliveryTime: string | undefined| number ) {
        let delayTime = getTimeDiffrece(deliveryTime);
        return (
            (delayTime < 0 ? styles.tableItemsWhite : styles.tableItemsblack)
        );
    }

    renderOnCallTable(courses: courses) {
        return (
            <View style={styles.viewHolder}>
                <View style={styles.overviewHolder}>
                    <Text style={this.checkStyle(courses.deliveryTime)}>{
                        t('order-overview.table')}</Text>
                    <Text style={this.checkDataStyle(courses.deliveryTime)}>{
                        (!this.props.order.isInventory && this.props.order.tableNo) ? this.props.order.tableNo : "-"}</Text>
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={this.checkStyle(courses.deliveryTime)}>{
                        t('order-overview.to-be-delivered-at')}</Text>
                    <Text style={{ color: colors.black, fontWeight: 'bold' }}>
                        {t('order-overview.onCall')} </Text>
                </View>
            </View>
        );
    }

    checkDataStyle(deliveryTime: string) {
        let delayTime = getTimeDiffrece(deliveryTime);
        return (
            (delayTime < 0 ? styles.dataConatinerWhite : styles.dataConatinerBlack)
        );
    }
    
    renderMeals = (meal: Meals) => {
        let mealsObject = meal.item;
        let orderLine = !!mealsObject.recipes ? mealsObject.recipes : (!!mealsObject.recipes ? mealsObject.recipes : [])
        return (
            orderLine.map((meals: Meals, index: number) => {
                return (
                        <View style={{ flexDirection: 'column' }}>
                            <Text style={(index === 0 ? styles.recipeContainer
                                : styles.recipeItemContainer)}>
                                {(!!meals.recipeName && meals.recipeName!='') ? meals.recipeName :''}
                            </Text>
                        </View>
                );
            }));
    }

    getActualTime(timeInmilliSecs: string) {
        let diffhrs = '';
        let diffMins = '';
        let currentTime = Date.now();
        let actualDate = new Date(timeInmilliSecs);
        let currentDate = new Date(currentTime);
        let hours = actualDate.getHours() - currentDate.getHours();
        let minutes = actualDate.getMinutes() - currentDate.getMinutes();
        if (minutes < 0) {
            minutes = minutes * -1;
        }
        if (hours < 0) {
            hours = hours * -1;
        }
        if (hours < 10) {
            diffhrs = '0' + hours;
        } else {
            diffhrs = '' + hours;
        }
        if (minutes < 10) {
            diffMins = '0' + minutes;
        } else {
            diffMins = '' + minutes;
        }
        return diffhrs + ':' + diffMins;
    }

    renderDate(deliveryTime: string) {
        let date = new Date(deliveryTime);
        let returnedDate:String=''
        if(date.toString()!='Invalid Date'){
            let dd = date.getDate();
            let mm = date.getMonth() + 1;
            let yy = date.getFullYear();
            returnedDate=('    ' + dd + '-' + mm + '-' + yy)
        }else{
            let dates=new Date(Number(deliveryTime))
            let dd = dates.getDate();
            let mm = dates.getMonth() + 1;
            let yy = dates.getFullYear();
            returnedDate=('    ' + dd + '-' + mm + '-' + yy)
        }
       
        return returnedDate;
    }

    msToTime(duration: string) {
        let d = new Date(+duration);
        let datetext =  d.toTimeString().split(' ')[0] ;        
        let hours = datetext !='Invalid'?(datetext.split(':')[0]):new Date(duration).getHours();
        let minutes = datetext !='Invalid'
        ?(datetext.split(':')[1]):new Date(duration).getMinutes();
        return (hours + ': ' + minutes) ;
    }

    renderTableContent(courses: courses) {
        let timing=getDifferenceTime( courses.deliveryTime )        
        return (
            <View style={styles.viewHolder}>
                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>

                    {this.props.order.status == 1 ?
                        <Text style={{ marginTop: 10, color: colors.black, fontWeight: 'bold' }}>{t('completedOrder.completed')}</Text> : null}

                    {this.props.order.status == 2 ?
                        <Text style={{ marginTop: 10, color: colors.black, fontWeight: 'bold' }}>{t('completedOrder.deleted')}</Text> : null}
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={this.checkStyle(courses.deliveryTime)}>{
                        t('order-overview.table')}</Text>
                    <Text style={this.checkDataStyle(courses.deliveryTime)}>{
                        (!this.props.order.isInventory && this.props.order.tableNo) ? this.props.order.tableNo : "-"}</Text>
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={this.checkStyle(courses.deliveryTime)}>{
                        t('order-overview.to-be-delivered-at')}</Text>
                    <Text style={this.checkDataStyle(courses.deliveryTime)}>
                        {this.renderDate(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
                    <Text style={this.checkDataStyle(courses.deliveryTime)}>
                        {this.msToTime(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={this.checkStyle(courses.deliveryTime)}>{
                        t('order-overview.timing')}</Text>
                    <Text style={this.checkDataStyle(courses.deliveryTime)}>
                        {!!timing?timing:''}</Text>
                </View>
                {(this.props.order.orderTo === this.ORDERTO) ? <Text style={this.checkStyle(courses.deliveryTime)}>Order to Web</Text> :
                    this.renderActualDeliveryTime(courses)}
            </View>
        );
    }

    renderActualDeliveryTime(courses: courses) {
        return (
            <View style={styles.overviewHolder}>
                <Text style={this.checkStyle(courses.deliveryTime)}>{
                    t('order-overview.actual-delivery-time')}</Text>
                <Text style={this.checkDataStyle(courses.deliveryTime)}>
                    {this.renderDate(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
                <Text style={this.checkDataStyle(courses.deliveryTime)}>
                    {this.msToTime(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
            </View>
        )
    }

    render() {
        let courses = this.props.course.item;
       
        
        return (
            <View style={{
                flexDirection: 'row',
                flex: 1,
                elevation: 2,
                borderRadius: 10,
                backgroundColor: colors.white,
                borderColor: colors.white,
                paddingRight: 5
            }}>
                {<View style={{ justifyContent: 'center' }}>
                    {this.props.order.status == 1 ? <Image style={{ width: 50, height: 50, }} source={require('@components/assets/completed.png')} /> : null}
                    {this.props.order.status == 0 || this.props.order.status == 3 ? <Image style={{ width: 50, height: 50, }} source={require('@components/assets/opened.png')} /> : null}
                    {this.props.order.status == 2 ? <Image style={{ width: 50, height: 50, }} source={require('@components/assets/deleted.png')} /> : null}
                </View>}
                <FlatList
                    listKey={courses.id}
                    style={styles.container}
                    data={courses.meals}
                    renderItem={this.renderMeals}
                />
                {(courses.isOnCall ? this.renderOnCallTable(courses) : this.renderTableContent(courses))}
            </View>
        );
    }
}

const styles = StyleSheet.create({
    delayedConatiner: {
        flexDirection: 'row',
        flex: 1,
        elevation: 2,
        borderRadius: 10,
        backgroundColor: colors.white,
        color: colors.white,
        borderColor: colors.white,
        paddingRight: 5
    },
    container: {
        flexDirection: 'column',
        flex: 1,
        marginBottom: 15
    },
    viewHolder: {
        flexDirection: 'column',
        marginLeft: 20,
        fontWeight: 'bold',
        flex: 1
    },
    overviewHolder: {
        flexDirection: 'row'
    }, tableItemsWhite: {
        flex: 1,
        marginTop: 5,
        color: colors.black,
        fontSize: 13

    }, tableItemsblack: {
        flex: 1,
        marginTop: 5,
        color: colors.black,
        fontSize: 13
    },
    dataConatinerBlack: {
        textAlign: 'right',
        marginRight: 5,
        flex: 1,
        marginTop: 10,
        color: colors.black,
        fontSize: 13
    },
    dataConatinerWhite: {
        textAlign: 'right',
        marginRight: 5,
        flex: 1,
        marginTop: 10,
        color: colors.black,
        fontSize: 13
    },
    recipeContainer: {
        marginLeft: 10,
        marginTop: 5,
        color: colors.black,
        fontWeight: 'bold',
        fontSize: 15
    }, recipeItemContainer: {
        color: colors.black,
        marginLeft: 25
    },

});
