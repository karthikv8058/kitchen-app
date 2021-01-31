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
    meal?: any;
}

interface State {

}

export default class MealView extends AbstractComponent<Props, State> {
    render() {
        let orderLine = this.props.meal.recipes ? this.props.meal.recipes : [];
        return (
            orderLine.map((recipe: Meals, index: number) => {
                return (
                    <View style={{ flexDirection: 'column' }}>
                        <Text style={(index === 0 ? styles.recipeContainer : styles.recipeItemContainer)}>
                            {recipe.recipeName}
                        </Text>
                    </View>
                );
            }));
    }
}

const styles = StyleSheet.create({
    recipeContainer: {
        marginLeft: 10,
        marginTop: 5,
        color: colors.black,
        fontWeight: 'bold',
        fontSize: 15
    },
    recipeItemContainer: {
        color: colors.black,
        marginLeft: 25
    },

});
