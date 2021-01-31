import Recipe from "./Recipe";

export default interface Inventory {
    Inventory: Array<Element>;
    Recipes: Array<Recipe>;
    rooms: Array<Element>;
    storages:Array<Element>;
    units: Array<Element>;
    outputUnits:string;
    inventories:Array<Recipe>;
    orders?: Array<Element>;
}
