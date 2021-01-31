import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import ConceptFilter from '@components/ConceptFilter';
import { getFontContrast } from '@components/FontColorHelper';
import FullWidthImage from '@components/FullWidthImage';
import { ImageType } from '@models/ImageType';
import Recipe from '@models/Recipe';
import OrderService from '@services/OrderService';
import colors from '@theme/colors';
import React from 'react';
import {
    ActivityIndicator,
    FlatList,
    StyleSheet,
    Text,
    TouchableOpacity,
    View
} from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import { Bind } from '../ioc/ServiceContainer';
import { menuCategory, RECIPE_LOOKUP } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import RecipeLabel from '@models/RecipeLabel';
import Order from '@models/Order';
import NavigationService from '@services/NavigationService';

interface Props {
    navigation: any;
}

interface State {
    isLoading: boolean;
    labelList: [];
    recipeList: Recipe[];
}

export default class RecipeLookUp extends AbstractComponent<Props, State> {

    private orderService: OrderService = Bind('orderService');
    private navigationService: NavigationService = Bind('navigationService');

    private tooblarItems: any[] = [];
    private selectedItems: Array<String> = [];
    private recipeName  = '';
    private conceptFilterRef: ConceptFilter | undefined;
    private recipes: Recipe[] = [];

    constructor(props: Props) {
        super(props);
        this.state = {
            isLoading: true,
            labelList: [],
            recipeList: []
        };
        this.goToRecipeDetails = this.goToRecipeDetails.bind(this);
    }

    componentDidMount() {
        this.loadRecipeList();
        this.loadToolbarItems();
    }

    loadRecipeList() {
        this.orderService.getRecipes().then(response => {
            if (responseChecker(response, this.props.navigation)) {
                let filterdRecipe: Recipe[] = response.recipes;
                this.recipes = filterdRecipe;
                this.setState({
                    recipeList: filterdRecipe,
                    labelList: response.labels,
                    isLoading: false
                });
            } else {
                this.recipes = [];
                this.setState({
                    recipeList: [],
                    isLoading: false
                });
                this.applyFilter();
            }
        }).catch(error => {
            this.setState({
                isLoading: false
            });
        });
    }

    loadToolbarItems = () => {
        this.tooblarItems.push(<TouchableOpacity
            activeOpacity={0.8}
            onPress={() => this.conceptFilterRef?.show() } style={{ height: 40, width: 40 }}>
            <Icon style={{}} name='filter' size={30} color={colors.white} />
        </TouchableOpacity>);
    }

    goToRecipeDetails(item: Recipe) {
        this.navigationService.push('Recipe', {
            recipeId: item.uuid,
            recipeName: item.recipeName,
            lookUp: item.lookup
        });
    }

    LoadConceptPage= () => {
        this.loadRecipeList();
    }

    renderRecipe=(recipeItem: Recipe)=> {
        let backgroundcolor = recipeItem.colors;
        let textcolor = getFontContrast(backgroundcolor);                
        return (
            <TouchableOpacity style={{ minHeight: 130 }} onPress={() => this.goToRecipeDetails(recipeItem)}>
                <View style={{
                    borderRadius: 10, overflow: 'hidden', height: 130,
                    margin: 10,
                }}>
                    <View style={[{ backgroundColor: backgroundcolor, flexDirection: 'row' }]}  >
                        <View style={styles.viewContainer}>
                            <Text numberOfLines={2} style={[styles.recipeContainer, { color: textcolor }]}>
                                {recipeItem.recipeName}
                            </Text>
                            {recipeItem.description? <Text numberOfLines={2} ellipsizeMode='tail' style={[styles.descriptionContainer, { color: textcolor }]}>
                                {recipeItem.description}
                            </Text>
                            :null}
                            <View style={{flex:1}}/>
                            <Text style={{color: textcolor , marginBottom:10}}>
                            {recipeItem.outPutQuantity} {recipeItem.OutPutUnit ?recipeItem.OutPutUnit : ""}
                            </Text>
                        </View>
                        <View style={{ height: 130 }}>
                            <FullWidthImage
                                loadData={this.LoadConceptPage}
                                onPress={() => this.goToRecipeDetails(recipeItem)}
                                imageType={ImageType.RECIPE}
                                intervention={false}
                                isFromRecipe={true}
                                index={0} item={recipeItem}
                                imageStyle={styles.image}
                                source={recipeItem.image} />
                        </View>

                    </View>
                </View>
            </TouchableOpacity>
        );
    }

    sortByName = () => {
        let newData = this.state.recipeList;
        newData.sort(function (a: Recipe, b: Recipe) {
            let nameA = a.recipeName!.toUpperCase();
            let nameB = b.recipeName!.toUpperCase();
            if (nameA < nameB) {
                return -1;
            }
            if (nameA > nameB) {
                return 1;
            }

            return 0;
        });

        return newData;
    }

    getRecipeList() {        
        let recipeList = this.sortByName();
        return (
            <View style={{ flexDirection: 'column', flex: 1, minHeight: 130 }}>
                {recipeList.length > 0 ? <FlatList
                    extraData={this.state}
                    data={recipeList}
                    renderItem={({ item }) => this.renderRecipe(item)}
                /> : <Text style={{ margin: 15, color: colors.white, fontSize: 20 }}>No recipes found</Text>}
            </View>
        );
    }

    applyFilter = () => {
        let filterdArray: Recipe[] = [];
        let latestArray: Recipe[] = [];
        let isItemMatch: Boolean = false;
        if (this.selectedItems.length > 0 && this.recipes.length > 0) {
            this.recipes.forEach((recipe: Recipe) => {
                this.selectedItems.forEach(element => {
                    if (recipe.recipeLabels!.split(',').indexOf(element.toString()) > -1) {
                        if (!filterdArray.includes(recipe)) {
                            filterdArray.push(recipe);
                        }
                    }
                });
            });
        }

        if (this.recipeName !== '') {
            if (filterdArray.length > 0) {
                filterdArray.forEach((element: Recipe) => {
                    if (element.recipeName!.indexOf(this.recipeName) > -1 && this.recipeName !== '') {
                        latestArray.push(element)
                        isItemMatch = true;
                    }
                });
                if (!isItemMatch) {
                    if (this.recipeName !== '') {
                        latestArray = [];
                    } else {
                        latestArray = filterdArray;
                    }
                }
            } else {
                this.recipes.forEach((recipe: Recipe) => {
                    let recipeName = recipe.recipeName!.toUpperCase();
                    let searchItem = this.recipeName.toUpperCase();
                    if (recipeName.indexOf(searchItem) > -1 && this.recipeName !== '') {
                        latestArray.push(recipe);
                    }
                });
            }
        } else if (filterdArray.length > 0) {
            latestArray = filterdArray;
        } else {
            this.recipes.forEach((recipe: Recipe) => {
                let recipeName = recipe.recipeName!.toUpperCase();
                let searchItem = this.recipeName.toUpperCase();
                if (recipeName === searchItem) {
                    latestArray.push(recipe);
                }
            });
        }

        if (this.selectedItems.length > 0 && latestArray.length === 0) {
            this.setState({
                recipeList: []
            });
        } else if (this.selectedItems.length === 0 && this.recipeName === '') {
            this.setState({
                recipeList: this.recipes
            });
        } else {
            this.setState({
                recipeList: latestArray
            });
        }
        this.conceptFilterRef?.close();
    }

    setSelectedItems = (selectedItems: string[], serachName: string) => {
        if (selectedItems.length > 0) {
            this.selectedItems = selectedItems;
        } else {
            this.selectedItems = [];
        }
        if (serachName !== '') {
            this.recipeName = serachName;
        } else {
            this.recipeName = '';
        }
        this.applyFilter();
    }

    rendetFilterModal() {
        return (
            <ConceptFilter
                ref={(ref: ConceptFilter) => this.conceptFilterRef = ref}
                labelList={this.state.labelList}
                setSelectedItems={this.setSelectedItems}>
            </ConceptFilter>
        );
    }

    onDidFocus = () => {
        this.loadRecipeList();
    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                category={menuCategory.taskCategory}
                index={RECIPE_LOOKUP}
                toolbarMenu={this.tooblarItems}>
                <View style={styles.container}>
                    {this.rendetFilterModal()}
                    {this.state.isLoading ?
                        <ActivityIndicator size='large' color={colors.white} />
                        : this.getRecipeList()}
                </View>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column'
    },
    viewContainer: {
        flex: 1,
        marginTop: 5,
        marginLeft: 15,
        flexDirection: 'column',
    },
    descriptionContainer: {
        width: '90%',
        color: colors.white,
        marginTop: 8
    },
    recipeContainer: {
        width: '50%',
        fontWeight: 'bold',
        fontSize: 18
    },
    image: {
        height: 100,
        width: 120,
        marginTop: 10,
        marginRight: 10,
        borderRadius: 10,
        marginLeft: 50,
    }
});
