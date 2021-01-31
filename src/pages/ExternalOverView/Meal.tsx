import AbstractComponent from '@components/AbstractComponent';
import SwipeView from '@components/SwipeView';
import colors from '@theme/colors';
import React from 'react';
import {
    FlatList,
    Image,
    Modal,
    StyleSheet, Text, TouchableOpacity, View
} from 'react-native';
import ioc, { Bind } from '../../ioc/ServiceContainer';
import t from '@translate';
import { getTimeDiffrece } from '../../utils/timeUtils';
import Recipe from '@models/Recipe';
import courses from '@models/Course';
import Meals from '@models/Meal'
import placeHolder from '../../components/assets/placeholder.png';

interface Props {
    navigation?: any;
    course?: [];
    order: [],
    recipe?: any
}

interface State {
    modalVisible: boolean;
}

class Meal extends AbstractComponent<Props, State> {
    apiBuilder = ioc.ServiceFactory.getServiceBy('apiBuilder');

    constructor(props: Props) {
        super(props);
        this.state = {
            modalVisible: false,
        };
        this.renderMeals = this.renderMeals.bind(this);
    }

    renderMeals = (mealsObject: Meals) => {
        let orderLine = !!mealsObject.recipes ? mealsObject.recipes : []
        let status = this.props.order.status === 0 ? 'Not placed' : (this.props.order.status === 1 ? 'Completed' : (this.props.order.status === 2 ? 'Canceled' : (this.props.order.status === 3 ? 'Placed' : '')))

        return (
            orderLine.map((meals: Meals) => {
                let unit = !!meals.OutPutUnit ? meals.OutPutUnit : ''
                return (
                    <View style={{
                        flexDirection: 'row',
                    }}>
                        <Image style={{ flex: 1.5, height: 70, width: 70, margin: 5, borderRadius: 15 }}
                            source={!!meals.recipe && meals.recipe.image ? { uri: this.apiBuilder.paths.imageUrl + meals.recipe.image } : placeHolder} >
                        </Image>
                        <Text style={{ flex: 3.5, color: colors.white, marginTop: 15 }}>
                            {(!!meals.recipeName && meals.recipeName != '') ? meals.recipeName + ' (' + meals.qty + ' ' + unit + ')' : ''}
                        </Text>

                    </View>
                );
            })


            /* <View style={{ flex: 4.7 }}>
                {index===0?<View style={{ flexDirection: 'row', marginTop: 5 }}>
                    <Text style={{ color: colors.white, flex: 6 }}>
                        {'Expected delivery:'}
                    </Text>
                    <Text style={{ flex: 4, color: colors.white, }}>
                        {this.msToTime(!!this.props.course.deliveryTime ? this.props.course.deliveryTime : '')}
                    </Text>
                </View>:null}
                {index===0?<View style={{ flexDirection: 'row', }}>
                    <Text style={{ flex: 6, color: colors.white, }}>
                        {'Status:'}
                    </Text>
                    <Text style={{ flex: 4, color: colors.white, }}>
                        {status}
                    </Text>
                </View>:null}
               {!!mealsObject.supplier?<View style={{ flexDirection: 'row', marginTop: 10, }}>
                    <Text style={{ flex: 6, color: colors.white, }}>
                        {'Supplier:'}
                    </Text>
                    <Text style={{ flex: 4, color: colors.white, }}>
                        {!!mealsObject.supplier ? mealsObject.supplier : ''}
                    </Text>
                </View>:null}
            </View> */
        );
    }

    formatDate = (deliveryTime: string) => {
        let date = new Date(Number(deliveryTime));
        if (date.toString() != 'Invalid Date') {
            let day = date.getDate();
            let month = date.getMonth() + 1;
            let year = date.getFullYear();
            let hours = date.getHours();
            let minutes = date.getMinutes();
            return `${day}-${month}-${year} ${hours}:${minutes}`;
        }
        return '';
    }

    render() {
        let courses = this.props.course;

        let status = this.props.order.status === 0 ? 'Not placed' : (this.props.order.status === 1 ? 'Completed' : (this.props.order.status === 2 ? 'Canceled' : (this.props.order.status === 3 ? 'Placed' : '')))
        return (
            <View style={{
                flexDirection: 'row',
                flex: 1,
                elevation: 2,
                paddingRight: 5
            }}>

                <View style={styles.container}>
                    {courses.meals.map(this.renderMeals)}
                </View>

                <View
                    style={{ flex: 4 }}
                >
                    <View style={{ flex: 4.7 }}>
                        <View style={{ flexDirection: 'row', marginTop: 5 }}>
                            <Text style={{ color: colors.white, flex: 6 }}>
                                {'Expected delivery:'}
                            </Text>
                            <Text style={{ flex: 4, color: colors.white, }}>
                                {this.formatDate(!!this.props.course.deliveryTime ? this.props.course.deliveryTime : '')}
                            </Text>
                        </View>
                        <View style={{ flexDirection: 'row', }}>
                            <Text style={{ flex: 6, color: colors.white, }}>
                                {'Status:'}
                            </Text>
                            <Text style={{ flex: 4, color: colors.white, }}>
                                {status}
                            </Text>
                        </View>
                        {/* {!!mealsObject.supplier ? <View style={{ flexDirection: 'row', marginTop: 10, }}>
                            <Text style={{ flex: 6, color: colors.white, }}>
                                {'Supplier:'}
                            </Text>
                            <Text style={{ flex: 4, color: colors.white, }}>
                                {!!mealsObject.supplier ? mealsObject.supplier : ''}
                            </Text>
                        </View> : null} */}
                    </View>
                </View>
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
        flex: 6,
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

    },
    dataConatinerBlack: {
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
export default Meal;
