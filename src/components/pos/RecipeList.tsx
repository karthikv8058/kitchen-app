import AbstractComponent from '@components/AbstractComponent';
import { getFontContrast } from '@components/FontColorHelper';
import Meal from '@models/Meal';
import Recipe from '@models/Recipe';
import colors from '@theme/colors';
import React from 'react';
import { FlatList, StyleSheet, Text, TouchableWithoutFeedback, View } from 'react-native';

interface Props {
    recipeCategory: [];
    filteredRecipe: [];
    getSelectedMeal: Function;
    toggleRecipeSelection: Function;
    recipeWidth: number;
}

interface State {
    recipeCategory: [];
    filteredRecipe: [];
    recipeWidth: number;
}

export default class RecipeList extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            recipeCategory: this.props.recipeCategory,
            filteredRecipe: this.props.filteredRecipe,
            recipeWidth: this.props.recipeWidth
        };
    }

    componentWillReceiveProps(props) {
        this.setState({ recipeCategory: props.recipeCategory, filteredRecipe: props.filteredRecipe, recipeWidth: props.recipeWidth });
    }

    getReceipeBackground(item: Recipe) {
        let receipeColor = '';
        this.state.recipeCategory.map((data) => {
            if (data.catgeoryId === item.recipe_category_uuid) {
                receipeColor = data.color;
            }
        });
        return receipeColor;
    }

    getFontColor(hexcolor: string) {
        return getFontContrast(hexcolor);
    }

    renderRecipeItem(item: Recipe) {
        // check whether the recipy is in selected meals
        const receipecolor = this.getReceipeBackground(item);
        let meal: Meal = this.props.getSelectedMeal();
        let isSelected = false;
        if (meal && meal.recipes.length > 0) {
            meal.recipes.forEach(function (a) {
                if (a.uuid === item.uuid) {
                    isSelected = true;
                }
            });
        }
        return (
            <TouchableWithoutFeedback
                onPress={() => this.props.toggleRecipeSelection(item)}
            >
                <View >
                    <Text style={[styles.recipe, {
                        width: this.state.recipeWidth - 10,
                        backgroundColor: [(isSelected) ? colors.white : receipecolor],
                        color: (isSelected) ? colors.black : this.getFontColor(receipecolor)
                    }]}>{item.name}</Text>
                </View>
            </TouchableWithoutFeedback>
        );
    }

    render() {
        return (
            <View style={{ justifyContent: 'space-between' }}>
                <FlatList numColumns={2}
                    data={this.state.filteredRecipe}
                    extraData={this.state}
                    renderItem={({ item }) => this.renderRecipeItem(item)}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    recipe: {
        flex: 1,
        elevation: 5,
        padding: 10,
        borderRadius: 10,
        margin: 4,
        backgroundColor: colors.posItemColor,
        borderWidth: 0,
        color: colors.white,
        height: 40,
    },
});
