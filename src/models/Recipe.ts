import Modifier from './Modifier';
import Unit from './Unit';
import Work from './Work';

export default interface 
Recipe {
    uuid?: string;
    description?: string;
    duration?: number;
    id?: number;
    recipe?:Recipe;
    OutPutUnit:number;
    recipeName?:String;
    image?: string;
    inventory?: number;
    modifiers?: Array<Modifier>;
    name?: string; 
    selectedModifiers?: Array<Modifier>;
    recipeLabels?: string;
    color?: string;
    isLabel?: boolean;
    quantity?: number;
    outputQuantity?: number;
    outputUnit?:Unit;
    work?:Work;
    mealsId?:string;
    roomId?:string;
    storage_id?:string;
    place_id?:string;
    recipe_uuid?:String| undefined;
    rack_id?:string;
    type:number,
    lookup:string,
    outputUnits?:Unit;
    recipeUuid:string;
    recipeId:string;
    qty:number;
    recipes:Array<Recipe>;
    outputData:string;
    item:Recipe;
    isExpanded:boolean;
    recipeIngredients:Array<Recipe>;
    isServiceSet:boolean
}
