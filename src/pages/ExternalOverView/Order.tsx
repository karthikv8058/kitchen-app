import AbstractComponent from '@components/AbstractComponent';
import SwipeView from '@components/SwipeView';
import colors from '@theme/colors';
import React from 'react';
import {
    Image,
    StyleSheet, Text, TouchableOpacity, View
} from 'react-native';
import { Bind } from '../../ioc/ServiceContainer';
import IOrder from '@models/Order';

interface Props {
    navigation?: any;
    order: IOrder;
    deletOrder?: Function;
    placeExternalOrder?: Function;
    recipe?: any;
    onClick: Function
}

interface State {
}

const placeholderImage = require('../../components/assets/placeholder.png');

class Order extends AbstractComponent<Props, State> {

    private orderId = '';
    apiBuilder = Bind('apiBuilder');

    constructor(props: Props) {
        super(props);
        this.state = {
        };
    }

    onClick = () => {
        if (this.props.order == null) {
            return;
        }
        let status = this.props.order.status;
        if (status === 0) {
            if (this.props.onClick) {
                this.props.onClick(this.props.order.uuid);
            }
        }
    }

    formatDate = (deliveryTime: string) => {
        if(deliveryTime){
            let date = new Date(Number(deliveryTime));
            if (date.toString() != 'Invalid Date') {
                let day = date.getDate();
                let month = date.getMonth() + 1;
                let year = date.getFullYear();
                let hours = date.getHours();
                let minutes = date.getMinutes();
                return `${day}-${month}-${year} ${hours}:${minutes}`;
            }
        }
        return '';
    }

    renderMeal = (course: any) => {
        let status = 'Not placed';
        switch (this.props.order.status) {
            case 1: status = 'Completed'; break;
            case 2: status = 'Canceled'; break;
            case 3: status = 'Placed'; break;
        }
        return (
            <View style={{
                backgroundColor: colors.lookUpBackground,
                borderRadius: 10,
                borderColor: colors.lookUpBackground,
                flexDirection: 'row',
                margin: 8,
                padding: 4
            }}>
                <View style={{
                    flex: 6,
                    flexDirection: 'column',
                }}>
                    {
                        course.meals.map((orderLine: any) => {
                            return orderLine.recipes.map((meals) => {
                                let unit = !!meals.OutPutUnit ? meals.OutPutUnit : ''
                                return (
                                    <View style={{
                                        flexDirection: 'row',
                                    }}>
                                        <Image style={{ flex: 1.5, height: 70, width: 70, margin: 5, borderRadius: 15 }}
                                            source={!!meals.recipe && meals.recipe.image ? { uri: this.apiBuilder.paths.imageUrl + meals.recipe.image } : placeholderImage} >
                                        </Image>
                                        <Text style={{ flex: 3.5, color: colors.white, marginTop: 15 }}>
                                            {(!!meals.recipeName && meals.recipeName != '') ? meals.recipeName + ' (' + meals.qty+')' : ''}
                                        </Text>

                                    </View>
                                );
                            })
                        })
                    }
                </View>

                <View style={{ flex: 4 }}>
                    <View style={{ flexDirection: 'row', marginTop: 5 }}>
                        <Text style={{ color: colors.white, flex: 6 }}>
                            {'Expected delivery:'}
                        </Text>
                        <Text style={{ flex: 4, color: colors.white, }}>
                            {this.formatDate(course.deliveryTime)}
                        </Text>
                    </View>
                    <View style={{ flexDirection: 'row' }}>
                        <Text style={{ flex: 6, color: colors.white, }}>
                            {'Status:'}
                        </Text>
                        <Text style={{ flex: 4, color: colors.white, }}>
                            {status}
                        </Text>
                    </View>
                    {this.props.order.supplier ? <View style={{ flexDirection: 'row' }}>
                        <Text style={{ flex: 6, color: colors.white, }}>
                            {'Supplier:'}
                        </Text>
                        <Text style={{ flex: 4, color: colors.white, }}>
                            {this.props.order.supplier}
                        </Text>
                    </View> : null
                    }
                </View>
            </View>
        );
    }

    render() {
        let order = this.props.order;
        return (
            <SwipeView onSwipedRight={this.props.placeExternalOrder!.bind(this, order)}
                disableSwipeToLeft={true}
                disableSwipeToRight={order.status == 1 || order.status == 2}>
                <TouchableOpacity
                    onPress={this.onClick}>
                    {order.courses.map(this.renderMeal)}
                </TouchableOpacity>
            </SwipeView>
        );
    }
}

export default Order;
