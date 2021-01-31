import AbstractComponent from '@components/AbstractComponent';
import Meal from '@models/Meal';
import colors from '@theme/colors';
import React from 'react';
import Icon from 'react-native-vector-icons/Feather';
import { StyleSheet, Text, View, FlatList, TouchableWithoutFeedback, TouchableOpacity, Dimensions } from 'react-native';
import Recipe from '@models/Recipe';
import RecipeLabel from '@models/RecipeLabel';
import t from '@translate';
import { getFontContrast } from '@components/FontColorHelper';


const CELL_WIDTH = 130;

interface Props {
    meal: Meal;
    recipes: Recipe[];
    labels: RecipeLabel[];
    onDelete: Function;
    toggleMealSelection: Function;
    onSelectRecipe: Function;
    serviceSet: any,
    width: any,
    splitterWidth: any,
}

interface State {
    meal: Meal | null;
    stack: string[];
    pageContent: (Recipe | RecipeLabel)[];
    numberOfColumn: number,
    dimensions?: { width: number, height: number }
}

const styles = StyleSheet.create({
    navigation: {
        marginTop: 10,
        flexDirection: 'row',
        height: 40
    },
    wrapBackNav: {
        flex: 0,
        flexShrink: 1,
        marginLeft: 8,
        width: 100,
        marginTop: 10,
        borderRadius: 10,
        alignItems: 'center',
        backgroundColor: colors.lookUpBackground
    },
    textNav: {
        color: colors.white
    },
    textNav2: {
        color: colors.white,
        fontSize: 10,
    },
    recipe: {
        flex: 1,
        padding: 4,
        borderRadius: 10,
        margin: 4,
        justifyContent: 'center',
        color: colors.white,
        height: 40,
    },
});

export default class PosRecipeBook extends AbstractComponent<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            stack: [],
            pageContent: [],
            meal: null,
            numberOfColumn: 3,
        };
    }

    onSelectRecipe = (recipe: Recipe | RecipeLabel) => {
        if (!this.props.meal) {
            return;
        }
        if (recipe.isServiceSet) {
            let stack = this.state.stack;
            stack.push(recipe);
            this.setState({ stack });
        } else if (recipe.isLabel) {
            let stack = this.state.stack;
            stack.push(recipe.uuid);
            this.setState({ stack });
        } else {
            if (this.props.onSelectRecipe) {
                this.props.onSelectRecipe(recipe);
            }
        }
    }

    isRecipeSelected = (recipe: Recipe | RecipeLabel) => {
        if (recipe.isLabel) {
            return true;
        }
        let meal: Meal = this.props.meal;
        if (!meal || !meal.recipes) {
            return false;
        }
        for (let r of meal.recipes) {
            if (r.uuid === recipe.uuid) {
                return true;
            }
        }
        return false;
    }

    goBack = () => {
        let stack = this.state.stack;
        stack.pop();
        this.setState({ stack });
    }

    goHome = () => {
        this.setState({ stack: [] });
    }
    goServiceSet = () => {
        let item = [];
        item.push(this.state.stack[0])
        this.setState({ stack: item });

    }
    generatePageContent = () => {

        let stack = this.state.stack;
        let label: string;
        if (stack) {
            label = stack[stack.length - 1];
        }
        let pageContent: (RecipeLabel | Recipe)[] = []
        if (stack.length === 0) {
            pageContent = this.props.serviceSet.filter((s: any) => this.checkLabel(label, s, false, true));
        } else if (stack.length === 1) {
            pageContent = pageContent.concat(this.getRootLabels(label));
        } else {
            let stack = this.state.stack[0]
            let recipes: any = []
            stack.recipes.forEach((r: Recipe) => {
                recipes.push(this.props.recipes.filter(r1 => r1.uuid === r.recipe_uuid)[0])
            });
            recipes = recipes.filter((recipe: Recipe, index: number) => (recipe != undefined) && recipes.indexOf(recipe) === index)
            pageContent = pageContent.concat(this.props.labels.filter(l => this.checkLabel(label, l)));
            pageContent = pageContent.concat(recipes.filter((r: Recipe) => this.checkRecipes(label, r,)));
        }
        if (pageContent.length % 2 !== 0) {
            pageContent.push({ uuid: '', isLabel: true });
        }

        return pageContent;
    }

    checkRecipes = (source: string, target: RecipeLabel | Recipe) => {
        target.isLabel = false;
        let label = this.props.labels.filter(l => l.uuid === source)
        let labelList = !!label.length && label[0].childLabels.split(',');
        labelList.push(source)
        return this.traverseRecipeTree(labelList, target);

    }
    traverseRecipeTree(labels: any, target: any) {
        let reciperLabels = target.recipeLabels && target.recipeLabels.split(',')
        let present = false;
        for (var i = 0; i < labels.length; i++) {
            if (labels[i] && labels[i].indexOf(reciperLabels) !== -1) {
                present = true
            } else {
                let label = this.props.labels.filter(l => l.uuid === labels[i])
                let labelList = !!label.length && label[0].childLabels.split(',');
                this.traverseRecipeTree(labelList, target)
            }

        }
        return present
    }

    getChildRecipe = (labelList: RecipeLabel[], recipeLabels: String) => {
        let isPresent = false
        labelList.forEach((label: any) => {
            let labels = recipeLabels && recipeLabels.split(',');
            if (!isPresent && labels && labels.indexOf(label) !== -1) {
                isPresent = true;
            }
        });
        return isPresent
    }

    getRootLabels(source: any) {
        let rootLabels: any = [];
        let data: any = [];
        this.props.recipes.forEach(element => {
            source.recipes.forEach((elem: any) => {
                if (element.uuid == elem.recipe_uuid) {
                    data.push(element)
                }
            });
        });

        data.forEach((element: any) => {
            let labels = element.recipeLabels && element.recipeLabels.split(',');
            if(!!labels){
                labels.forEach((labelId: any) => {
                    rootLabels = this.findRootLabels(labelId, [])
                });
            }
            
        });
        return rootLabels.concat(data)
    }
    findRootLabels = (labelId: any, filterdLabels: any) => {
        let l: any = this.props.labels.find(r => r.uuid === labelId)
        if (!l.parentLabel) {
            l.isLabel = true
            filterdLabels.push(l)
        } else {
            let l2 = l.length > 0 && !!l.parentLabel ? l.parentLabel.split(',') : !!l.parentLabel ? (l.parentLabel.split(',')) : []
           if(!!l2){
            l2.forEach((labels: any) => {
                filterdLabels = this.findRootLabels(labels, filterdLabels)
            });
           }
            
        }

        return filterdLabels
    }

    checkLabel = (source: string, target: RecipeLabel | Recipe, isLabel: boolean = true, isServiceSet: boolean = false) => {
        target.isServiceSet = isServiceSet
        target.isLabel = isLabel;
        let labelId: string = !!isLabel ? target.parentLabel : target.recipeLabels
        if (!source && !labelId) {
            return true;
        } else if (source) {
            let labels = labelId && labelId.split(',');
            if (labels && labels.indexOf(source) !== -1) {
                return true;
            }
        }
        return false;
    }

    renderRecipe = (recipe: Recipe | RecipeLabel) => {
        let isRecipeSelected = this.isRecipeSelected(recipe);
        let bgColor = '#ffffff';
        if (!(!recipe.isLabel && isRecipeSelected) && recipe.color) {
            bgColor = recipe.color;
        }
        return (<TouchableWithoutFeedback onPress={() => this.onSelectRecipe(recipe)} >
            <View style={[styles.recipe, { marginLeft: 6, backgroundColor: bgColor, width: CELL_WIDTH, maxWidth: CELL_WIDTH, minWidth: CELL_WIDTH }]}>
                <Text ellipsizeMode='tail' numberOfLines={2} style={{
                    color: getFontContrast(bgColor)
                }}>{recipe.name}</Text>
            </View>
        </TouchableWithoutFeedback>);
    }

    findDimesions = (layout: any) => {
        const { width, height } = layout;

        if (this.state.dimensions == null || this.state.dimensions.width != width) {

            let sh = Dimensions.get('window').height;
            let sw = Dimensions.get('window').height;

            let size = sh > sw ? height : width
            //10 is buffer
            let w = Math.floor(size / (CELL_WIDTH + 10));

            this.setState({
                numberOfColumn: w,
                dimensions: { width, height }
            })
        }
    }
    renderTopMenu = () => {
        let currentLabel = this.props.labels.filter(l => l.uuid === this.state.stack[this.state.stack.length - 1])
        return (
            <View style={{
                flexDirection: 'row', flexWrap: 'wrap',
            }} >
                {this.state.stack.length > 0 ?
                    <TouchableOpacity style={[styles.wrapBackNav, { width: CELL_WIDTH, height: 40, alignItems: 'center', justifyContent: "center", }]} onPress={this.goHome}>
                        <Text style={styles.textNav}>{'Home'}</Text>
                    </TouchableOpacity> : null}
                { this.state.stack.length > 0 ?
                    <TouchableOpacity style={[styles.wrapBackNav, { width: CELL_WIDTH }]} onPress={this.goServiceSet}>
                        <Text style={[styles.textNav2, { textAlign: 'center' }]}>{'Current Service Set:'}</Text>
                        <Text numberOfLines={1} style={[styles.textNav, { textAlign: 'center', paddingBottom: 5 }]}>{this.state.stack[0].name}</Text>
                    </TouchableOpacity> : null}
                { this.state.stack.length > 1 ?
                    <TouchableOpacity style={[styles.wrapBackNav, { width: CELL_WIDTH }]} onPress={this.goBack}>
                        <Text style={[styles.textNav2, { textAlign: 'center' }]}>{'Current Category:'}</Text>
                        <Text numberOfLines={1} style={[styles.textNav, { textAlign: 'center', paddingBottom: 5 }]}>{currentLabel.length > 0 ? currentLabel[0].name : ''}</Text>
                    </TouchableOpacity> : null}
            </View>
        )
    }

    render() {
        let pageContent = this.generatePageContent()
        return (
            <View onLayout={(event) => { this.findDimesions(event.nativeEvent.layout) }} style={[{ flexDirection: 'column', },]}>
                {/* {this.state.stack.length > 0 ? <FlatList
                    numColumns={this.state.numberOfColumn}
                    key={this.state.numberOfColumn}
                    data={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                    style={{marginTop: 5, }}
                    renderItem={({ item }) => this.renderTopMenu(item)}
                /> : null} */}
                <FlatList
                    ListHeaderComponent={this.renderTopMenu()}
                    numColumns={this.state.numberOfColumn}
                    key={this.state.numberOfColumn}
                    keyExtractor={(item) => item.uuid ? item.uuid : ""}
                    data={pageContent}
                    horizontal={false}
                    extraData={this.state}
                    //style={this.state.numberOfColumn > 1 && { marginLeft: 5, marginTop: 5, marginRight: 8 }}
                    columnWrapperStyle={this.state.numberOfColumn > 1 && { maxWidth: CELL_WIDTH, width: CELL_WIDTH }}
                    renderItem={({ item }) =>
                        (item.uuid ? this.renderRecipe(item) : <View style={{ flex: 1 }} />)
                    }
                />
            </View>

        );
    }

}
