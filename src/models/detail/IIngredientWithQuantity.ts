import Recipe from "@models/Recipe";

export default interface IngredientWithQuantity {
    quantity:number;
    recipe:Recipe;
    name:string;
    image:string;
}