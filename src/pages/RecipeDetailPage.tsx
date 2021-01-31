import AbstractComponent from '@components/AbstractComponent';
import AppBackground from '@components/AppBackground';
import HttpClient from '@services/HttpClient';
import colors from '@theme/colors';
import t from '@translate';
import React from 'react';
import { ActivityIndicator, Text, View, StyleSheet, Image, FlatList, ScrollView, ListRenderItemInfo } from 'react-native';
import { TouchableOpacity } from 'react-native-gesture-handler';

import AclNavigation from '../acl/AclNavigation';
import ioc, { HTTP_CLIENT, Bind } from '../ioc/ServiceContainer';
import { menuCategory, RECIPE_LOOKUP } from '../utils/constants';
import { responseChecker } from '../utils/responseChecker';
import { getFontContrast } from '@components/FontColorHelper';
import Icon from 'react-native-vector-icons/Feather';
import Recipe from '@models/Recipe';
import Task from '@models/Task';
import Station from '@models/Station';
import NavigationService from '@services/NavigationService';

interface Props {
    navigation: any;
    route:any
}

interface State {
    recipeDetails: Array<Recipe> | any;
    isLoading: boolean;
    recipeList: Recipe[];
    stationList: Station[];
}

export default class RecipeDetailPage extends AbstractComponent<Props, State> {
    private httpClient: HttpClient = ioc.ServiceFactory.getServiceBy(HTTP_CLIENT);
    private navigationService: NavigationService = Bind('navigationService');
    private recipeId: String = '';
    private recipeName: String | undefined = '';
    private isIngredeint: Boolean = false;
    constructor(props: Props) {
        super(props);
        // this.aclNavigation = new AclNavigation(this.props.navigation);
        this.recipeId = this.props.route.params.recipeId;
        this.state = {
            recipeDetails: [],
            isLoading: true,
            recipeList: [],
            stationList: []
        };
    }

    componentDidMount() {
        this.loadStations();
    }

    loadRecipeList(stationList: Station[]) {
        this.httpClient.post(this.apiBuilder.paths!.getAllRecipes, {}).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                this.loadRecipeDetails(response, stationList);
            }
        }).catch(error => {
        });
    }

    loadStations() {
        this.httpClient.post(this.apiBuilder.paths!.getstations, {}).then(response => {
            if (responseChecker(response, this.props.navigation)) {
                this.loadRecipeList(response);
            }
        }).catch(error => {
        });
    }

    loadRecipeDetails(recipeList: Recipe[], stationList: Station[]) {        
        this.httpClient.post(this.apiBuilder.paths!.recipeDetails, { recipeId: this.recipeId }).then(response => {
            if (responseChecker(response, this.props.navigation)) {

                this.setState({
                    recipeDetails: response,
                    isLoading: false,
                    recipeList: recipeList,
                    stationList: stationList
                });
            }

        }).catch(error => {
        });
    }

    getRecipeName=(recipeId: String | undefined |unknown )=> {
        let recipeArray=this.state.recipeList
        recipeArray.recipes.forEach((recipe: Recipe) => {
            if (recipe.uuid === recipeId) {
                this.recipeName = recipe.recipeName;
                if (recipe.type === 1) {
                    this.isIngredeint = false;
                } else {
                    this.isIngredeint = true;
                }
            }
        });
    }

    goToRecipeDetails(recipeId: String | undefined) {
        this.state.recipeList.recipes.forEach((recipe: Recipe) => {
            if (recipe.uuid === recipeId && recipe.type === 1) {
                this.props.navigation.push('Recipe', {
                    recipeId: recipe.uuid,
                    recipeName: recipe.name,
                    lookUp: recipe.lookup
                });
            }
        });
    }

    renderIngredienItems=(ingredient:Recipe | any)=> {
        let recipeItem:Recipe = ingredient.item;        
        this.getRecipeName(recipeItem.recipe_uuid);
        return (
            <View style={{ flexDirection: 'column' }}>
                <TouchableOpacity onPress={() => this.goToRecipeDetails(recipeItem.recipe_uuid)} style={{
                    marginTop: 10,
                    backgroundColor: !!this.isIngredeint ? colors.ingredientColor : colors.white,
                    borderRadius: 10,
                    flexDirection: 'row'
                }}>
                    {!!this.isIngredeint ?
                        <Image style={{ height: 40, width: 40, marginLeft: 10, resizeMode: 'contain', marginTop: 5 }}
                            source={require('@components/assets/ingredientTask.png')} />
                        : <Image style={{ height: 40, width: 40, marginLeft: 10, resizeMode: 'contain', marginTop: 5 }}
                            source={require('@components/assets/recipetask.png')} />}
                    <Text numberOfLines={1} style={{ color: colors.black, minHeight: 50, textAlignVertical: 'center', marginLeft: 5 }}>
                        {this.recipeName}
                    </Text>
                </TouchableOpacity>
            </View>
        );
    }

    goToTaskDetail(task: Task | undefined, stationcolor: string | String, textColor: string) {
        this.navigationService.push('DetailPage', {
            taskId: task!.uuid, assignmentid: 0, recipeId: this.recipeId, index: RECIPE_LOOKUP,
            recipeName: this.recipeName, background: stationcolor, textColor: textColor, assignedTask: task
        });
    }

    goToInterventionTaskDetail(task: Task  | undefined) {
        this.navigationService.push('DetailPage', {
            taskId: task!.uuid, 
            index: RECIPE_LOOKUP,
            isIntervention: true, 
        });
    }

    formatNumber = (n: number) => ('0' + n).slice(-2);

    formatDate(time: number) {
        let mils = time;
        let days = Math.floor(mils / 86400000);
        mils = mils % 86400000;
        let hours = Math.floor(mils / 3600000);
        mils = mils % 3600000;
        let minutes = Math.floor(mils / 60000);
        mils = mils % 60000;
        let seconds = Math.floor(mils / 1000);
        let formated = days ? days + 'd ' : '';
        formated += this.formatNumber(hours) + ':' + this.formatNumber(minutes) + ':' + this.formatNumber(seconds);
        return formated;
    }

    renderInterventions=(intervention: ListRenderItemInfo<Task> | any)=> {        
        let interventionItem = intervention.item;
        let bacColor:any = !!interventionItem!.stationColor ? interventionItem!.stationColor : colors.white;
        return (
            <TouchableOpacity onPress={() => this.goToInterventionTaskDetail(interventionItem)}
                style={{
                    flex: 1,
                    flexDirection: 'row',
                    marginTop: 10,
                    backgroundColor: colors.white,
                    borderBottomRightRadius: 10,
                    borderTopRightRadius: 10, marginLeft: 30
                }}>
                <View style={{ width: 10, backgroundColor: bacColor }}/>
                <Icon style={{ alignSelf: 'center' }} name='square' size={30} color={colors.black} />
                <View style={{ marginLeft: 10, flexDirection: 'row', flex: 1 }}>
                    <Text numberOfLines={1} style={{ textAlignVertical: 'center', color: colors.black, minHeight: 50, flex: .7 }}>
                        {interventionItem!.description}
                    </Text>
                    {!!interventionItem!.intervention_time ?
                        <Text  style={{ padding: 15, textAlign: 'right', color: colors.black, minHeight: 50, flex: .3 }}>
                            {'After ' + this.formatDate(Number(interventionItem!.intervention_time))}
                        </Text> : null}

                </View>
            </TouchableOpacity >
        );
    }

    getStationColor(stationId:number) {
        let stationColor: String = '';
        this.state.stationList.forEach((station: Station) => {
            if (station.uuid === stationId) {
                stationColor = station.color;
            }
        });
        return stationColor;
    }

    renderTaskItems=(task: Task | any)=> {
        let taskItem = task.item;
        let isMachineTask: boolean = (!!taskItem!.machine && !taskItem!.chefInvolved) ? true : false;
        let backColor:any = this.getStationColor(Number(taskItem!.station));
        let textColor: string = getFontContrast(backColor);
        return (
            <View style={{ flexDirection: 'column' }}>
                <TouchableOpacity onPress={() => this.goToTaskDetail(taskItem, backColor, textColor)}
                    style={isMachineTask ? styles.machinetaskContainer : styles.normalTaskContainer}>
                    <View style={{ width: 10, backgroundColor: backColor }}></View>
                    {!!isMachineTask ? <Image style={{ height: 40, width: 40, marginLeft: 10, resizeMode: 'contain', marginTop: 5 }}
                        source={require('@components/assets/machineTask.png')} /> :
                        <Icon style={{ alignSelf: 'center' }} name='square' size={30} color={colors.black} />
                    }
                    <View style={{ flex: 1, flexDirection: 'row' }}>
                        <Text numberOfLines={1} style={{ flex: .6, color: colors.black,
                             minHeight: 50, textAlignVertical: 'center', marginLeft: 10 }}>
                            {taskItem!.name}
                        </Text>
                        {(!!taskItem!.outputQuantity && !!taskItem!.outputUnitName) ?
                            <Text numberOfLines={1} style={{ paddingRight: 15, paddingTop: 15,
                             textAlign: 'right', color: colors.black, minHeight: 50, flex: .4 }}>
                                {'(' + taskItem!.outputQuantity + ' ' + taskItem!.outputUnitName + ')'}
                            </Text> : null}
                    </View>

                </TouchableOpacity>
                <FlatList
                    extraData={this.state}
                    data={taskItem!.intervention}
                    renderItem={(item)=>this.renderInterventions(item)}
                />
            </View>
        );
    }

    renderIngredients() {        
        return (
            <View style={{ flexDirection: 'column' }}>
                < Text style={{ color: colors.white, fontSize: 20 }}>Ingredients</Text>
                <FlatList
                    extraData={this.state}
                    data={this.state.recipeDetails.recipeIngredients}
                    renderItem={(item)=>this.renderIngredienItems(item)}
                />
            </View>
        );
    }

    renderTaskList() {
        return (
            <View style={{ flexDirection: 'column' }}>
                <Text style={{ color: colors.white, fontSize: 20, marginTop: 20 }}>Tasks</Text>
                <FlatList
                    extraData={this.state}
                    data={this.state.recipeDetails.tasks}
                    renderItem={(item)=>this.renderTaskItems(item)}
                />
            </View>
        );
    }

    renderRecipeDetail() {
        return (
            <View style={{
                flexDirection: 'column', margin: 20
            }}>
                {(!!this.state.recipeDetails.recipeIngredients && this.state.recipeDetails.recipeIngredients.length > 0) ?
                    this.renderIngredients() : null}
                {(!!this.state.recipeDetails.tasks && this.state.recipeDetails.tasks.length > 0) ?
                    this.renderTaskList() : null}
            </View >
        );
    }

    render() {
        return (
            <AppBackground
                navigation={this.props.navigation}
                category={menuCategory.taskCategory}
                index={RECIPE_LOOKUP}>
                <ScrollView style={{
                    flex: 1,
                    flexDirection: 'column'
                }}>
                    {this.state.isLoading ?
                        <ActivityIndicator size='large' color={colors.white} />
                        : this.renderRecipeDetail()}
                </ScrollView>
            </AppBackground>
        );
    }
}

const styles = StyleSheet.create({
    machinetaskContainer: {
        flex: 1,
        marginTop: 10,
        backgroundColor: colors.ingredientColor,
        borderRadius: 10,
        flexDirection: 'row'
    },
    normalTaskContainer: {
        marginTop: 10,
        flex: 1,
        backgroundColor: colors.white,
        borderBottomRightRadius: 10,
        borderTopRightRadius: 10,
        flexDirection: 'row'
    },
});
