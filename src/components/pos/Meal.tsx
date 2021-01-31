import AbstractComponent from '@components/AbstractComponent';
import Meal from '@models/Meal';
import colors from '@theme/colors';
import React from 'react';
import { StyleSheet, Text, TouchableWithoutFeedback, View } from 'react-native';
import Swipeout from 'react-native-swipeout';

interface Props {
    meal: Meal;
    onDelete?: Function;
    onSelectMeal?: Function;
}

interface State {
    meal: Meal;
}

export default class MealComponent extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            meal: this.props.meal
        };
        this.onMealpress = this.onMealpress.bind(this);
    }

    UNSAFE_componentWillReceiveProps(props) {
        const { meal } = props;
        this.setState({ meal });
    }

    renderMealsRecipes(meal: Meal) {
        let textColor = !!meal?(meal.isSelected ? colors.black : colors.white):colors.white;
        return meal.recipes.map((recipe, index) => {
            return (<View style={{ flexDirection: 'row' }} key={index}>
                <Text style={[index === 0 ? styles.firstRecipe : styles.subRecipe, { color: textColor, flex: 1 }]}>
                    {recipe.name}
                </Text>
                <Text style={[styles.quantity, { color: textColor }]}>
                    x{recipe.quantity ? recipe.quantity : 1}
                </Text>
            </View>);
        });
    }

    getSwipeoutButton() {
        let swipeoutBtns = [
            {
                text: '',
                backgroundColor: 'rgba(0, 0, 0, 0.0)'
            }
        ];
        return swipeoutBtns;
    }

    onMealpress() {
        this.props.onSelectMeal(this.state.meal);
    }

    onDelete = (sectionId: any, rowId: any, direction: any) => {
        this.props.onDelete(this.state.meal, sectionId, rowId, direction);
    }

    render() {
        return (
            <Swipeout close={true} left={this.getSwipeoutButton()} backgroundColor={'rgba(0, 0, 0, 0.0)'} buttonWidth={600}
                onOpen={this.onDelete}>
                <TouchableWithoutFeedback onPress={this.onMealpress}>
                    <View style={[styles.mealView,
                    { backgroundColor: (this.props.meal.isSelected) ? colors.white : colors.lookUpBackground }]}>
                        {this.state.meal.recipes && this.renderMealsRecipes(this.state.meal)}
                    </View>
                </TouchableWithoutFeedback>
            </Swipeout>
        );
    }

}

const styles = StyleSheet.create({
    subRecipe: {
        marginLeft: 10,
        color: colors.white
    },
    mealView: {
        backgroundColor: colors.grey,
        color: colors.white,
        borderRadius: 10,
        elevation: 5,
        flex: 1,
        minHeight: 50,
        margin: 5
    },
    firstRecipe: {
        marginLeft: 5,
        fontWeight: 'bold',
        color: colors.white
    },
    quantity: {
        alignSelf: 'flex-end',
        marginRight: 4
    },
    recipeModifier: {
        backgroundColor: colors.red,
        marginLeft: 5,
        color: colors.white
    },
});
