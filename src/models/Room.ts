import Recipe from "./Recipe";

export default interface Room {
    orders?: Array<Element>;
    rooms?: Array<Element>;
    units?: Array<Element>;
    inventories?:Array<Element>
    uuid?:string| undefined
    storage:Array<any>;
    name?:string| undefined;
    Recipes?:Array<Recipe>
}
