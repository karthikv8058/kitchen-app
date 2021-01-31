import Recipe from './Recipe';

export default interface Meal {
    id?:string;
    recipes: Array<Recipe>;
    isSelected: boolean;
    orderLine?:Array<Recipe>;
}
