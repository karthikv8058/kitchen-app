import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { FlatList, StyleSheet, Text, View, Image, AsyncStorage, Dimensions } from 'react-native';
import { convertMS, getDifferenceTime } from '../utils/dateUtil';
import { getTimeDiffrece } from '../utils/timeUtils';
import AbstractComponent from './AbstractComponent';
import Meals from '@models/Meal';
import courses from '@models/Course';
import Recipe from '@models/Recipe';
import placeHolder from '@components/assets/placeholder.png';
import ioc, { HTTP_CLIENT } from '../ioc/ServiceContainer';
import MealView from './MealView';
import Orientation from 'react-native-orientation-locker';

interface Props {
    navigation?: any;
    course?: [];
    order: [];
    recipe?: any
}
interface State {
    modalVisible: boolean;
    screen:any,
    isPortrait:boolean
}

export default class Meal extends AbstractComponent<Props, State> {
    private ORDERTO: string = 'WEB'
    apiBuilder = ioc.ServiceFactory.getServiceBy('apiBuilder');

    constructor(props: Props) {
        super(props);
        this.state = {
            modalVisible: false,
            screen: Dimensions.get('window'),
            isPortrait:true
        };
        this.renderActualDeliveryTime = this.renderActualDeliveryTime.bind(this)
    }

    renderOnCallTable(courses: courses) {
        return (
            <View style={styles.viewHolder}>
                <View style={styles.overviewHolder}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.table')}</Text>
                    <Text style={styles.dataConatinerBlack}>{
                        (!this.props.order.isInventory && this.props.order.tableNo) ? this.props.order.tableNo : "-"}</Text>
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.to-be-delivered-at')}</Text>
                    <Text style={{ color: colors.black, fontWeight: 'bold' }}>
                        {t('order-overview.onCall')} </Text>
                </View>
            </View>
        );
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
        let returnedDate: String = ''
        if (date.toString() != 'Invalid Date') {
            let dd = date.getDate();
            let mm = date.getMonth() + 1;
            let yy = date.getFullYear();
            returnedDate = ('    ' + dd + '-' + mm + '-' + yy)
        } else {
            let dates = new Date(Number(deliveryTime))
            let dd = dates.getDate();
            let mm = dates.getMonth() + 1;
            let yy = dates.getFullYear();
            returnedDate = ('    ' + dd + '-' + mm + '-' + yy)
        }

        return returnedDate;
    }

    msToTime(duration: string) {
        let d = new Date(+duration);
        let datetext = d.toTimeString().split(' ')[0];
        let hours = datetext != 'Invalid' ? (datetext.split(':')[0]) : new Date(duration).getHours();
        let minutes = datetext != 'Invalid'
            ? (datetext.split(':')[1]) : new Date(duration).getMinutes();
        return (hours + ': ' + minutes);
    }
    onLayout(){ 
        Orientation.getOrientation((orientation) => {
            if (orientation === 'PORTRAIT') {          
              this.setState({
                isPortrait: true,
                screen: Dimensions.get('window')
              })
            } else {
              this.setState({
                isPortrait: false,
                screen: Dimensions.get('window')
              })
            }
          })        
      }
    
    renderTableContent(courses: courses) {
        let timing = getDifferenceTime(courses.deliveryTime)
        return (
            <View style={styles.viewHolder}>
                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                    {this.props.order.status == 1 &&
                        <Text style={{ marginTop: 10, color: colors.black, fontWeight: 'bold' }}>{t('completedOrder.completed')}</Text>}

                    {this.props.order.status == 2 &&
                        <Text style={{ marginTop: 10, color: colors.black, fontWeight: 'bold' }}>{t('completedOrder.deleted')}</Text>}
                </View>
                <View style={styles.overviewHolder}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.table')}</Text>
                    <Text style={styles.dataConatinerBlack}>{
                        this.props.order.tableNo ? this.props.order.tableNo : "-"}</Text>
                </View>
                <View style={{flexDirection:!!this.state.isPortrait?'column':'row'}}>
                    <Text numberOfLines={1} style={styles.orderDetaillLabel}>{
                        t('order-overview.to-be-delivered-at')}</Text>
                    <Text style={styles.dataConatinerBlack}>
                        {this.renderDate(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}  {this.msToTime(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
                </View>
                <View style={{flexDirection:'row'}}>
                    <Text  style={styles.tableItemsWhite}>{
                        t('order-overview.timing')}</Text>
                    <Text style={styles.dataConatinerBlack}>
                        {!!timing ? timing : ''}</Text>
                </View>
                {(this.props.order.orderTo === this.ORDERTO) ? <Text style={styles.tableItemsWhite}>Order to Web</Text> :
                    this.renderActualDeliveryTime(courses,this.state.isPortrait)}
            </View>
        );
    }

    renderActualDeliveryTime(courses: courses,isPortrait:boolean) {
        return (
            <View style={{flexDirection:!!isPortrait?'column':'row'}}>
                <Text style={styles.tableItemsWhite}>{
                    t('order-overview.actual-delivery-time')}</Text>
                <Text style={styles.dataConatinerBlack}>
                    {this.renderDate(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}  {this.msToTime(!!courses.deliveryTime ? courses.deliveryTime : courses.deliveryDate)}</Text>
            </View>
        )
    }


    getOrderStatusImage = () => {
        if (this.props.order.status == 1) {
            return require("@components/assets/completed.png");
        } else if (this.props.order.status == 2) {
            return require("@components/assets/deleted.png");
        } else {
            return require("@components/assets/opened.png");
        }
    }
    render() {
        let course = this.props.course;
        return (
            <View style={{
                flexDirection: 'row',
                flex: 1,
                elevation: 2,
                borderRadius: 10,
                backgroundColor: colors.white,
                borderColor: colors.white,
                paddingRight: 5
            }}onLayout = {this.onLayout.bind(this)} >
                {<View style={{ justifyContent: 'center' }}>
                    <Image style={{ width: 50, height: 50, }} source={this.getOrderStatusImage()} />
                </View>}
                <FlatList
                    keyExtractor={(item) => item.mealId}
                    style={styles.container}
                    data={!!course.meals?course.meals:[]}
                    renderItem={item =>
                        <MealView
                            meal={item.item}
                        />}
                />
                {(course.isOnCall ? this.renderOnCallTable(course) : this.renderTableContent(course))}
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
    },
    tableItemsWhite: {
        flex: 1,
        marginTop: 5,
        color: colors.black,
        fontSize: 13

    },
    orderDetaillLabel: {
        marginTop: 5,
        color: colors.black,
        fontSize: 13
    },
    dataConatinerBlack: {
        textAlign: 'right',
        marginRight: 5,
        marginTop: 5,
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
