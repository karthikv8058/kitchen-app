import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { StyleSheet, Text, View, Image } from 'react-native';
import { formatDate, getDifferenceTime } from '../../utils/dateUtil';
import AbstractComponent from '@components/AbstractComponent';
import ICourse from '@models/Course';
import { Bind } from '../../ioc/ServiceContainer';
import Meal from './Meal';
import Order from '@models/Order';

interface Props {
    navigation?: any;
    course: ICourse;
    order: Order;
    recipe?: any
}

interface State {

}

export default class Course extends AbstractComponent<Props, State> {

    apiBuilder = Bind('apiBuilder');

    constructor(props: Props) {
        super(props);
        this.state = {

        };
    }


    renderOnCallTable() {
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


    getOrderNameByStatus = () => {

        switch (this.props.order.status) {
            case 1:
                return t('completedOrder.completed')
            case 2:
                return t('completedOrder.deleted')
            default: ''
        }
    }

    renderTableContent = () => {
        let course = this.props.course;


        // if(course.deliveryDate != null && course.expectedDate !=null){


        //     let start = Date.parse(course.deliveryDate);
        //     let end = Date.parse(course.expectedDate);

        //     if(start < end){

        //     }

        //}



        let timing = "";

        let deliveryDate = null;
        let expectedDate = null;

        if(course.deliveryDate != null){
            deliveryDate = new Date(course.deliveryDate);
        }
        
        if(course.expectedDate != null){
            expectedDate = new Date(course.expectedDate);
        }
        if (!!course.deliveryDate && !!course.expectedDate  && !!deliveryDate) {
            timing = getDifferenceTime(deliveryDate)
        }


        return (
            <View style={styles.viewHolder}>


                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                    <Text style={{ marginTop: 10, color: colors.black, fontWeight: 'bold' }}>  {this.getOrderNameByStatus()}</Text>
                </View>

                <View style={styles.overviewHolder}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.table')} {this.props.order.tableNo ? this.props.order.tableNo : "-"}</Text>
                </View>
                {deliveryDate != null && <View style={{ flexDirection: 'row' }}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.to-be-delivered-at')} {formatDate(deliveryDate)}</Text>
                </View>
                }
                <View style={{ flexDirection: 'row' }}>
                    {!!timing &&
                        <Text style={styles.tableItemsWhite}>{
                            t('order-overview.timing')} {timing} </Text>
                    }
                </View>
                {expectedDate != null && <View style={{ flexDirection: 'row' }}>
                    <Text style={styles.tableItemsWhite}>{
                        t('order-overview.actual-delivery-time')} {formatDate(expectedDate)}</Text>
                </View>}

            </View>
        );
    }

    getOrderStatusImage = () => {
        switch (this.props.order.status) {
            case 1: return require("@components/assets/completed.png");
            case 2: return require("@components/assets/deleted.png");
            default: return require("@components/assets/opened.png");
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
            }}>
                <View style={{ justifyContent: 'center' }}>
                    <Image style={{ width: 50, height: 50, }} source={this.getOrderStatusImage()} />
                </View>

                <View style={{ flex: 1 }}>
                    {course.meals.map(meal => <Meal
                        meal={meal}
                    />)
                    }
                </View>
                {(course.isOnCall ? this.renderOnCallTable() : this.renderTableContent(course))}
            </View>
        );
    }
}

const styles = StyleSheet.create({
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
