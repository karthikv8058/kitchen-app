import Recipe from "@models/Recipe";

export default interface IngredientWithQuantity {
    qty: number;
    quantity:number;
    recipe:Recipe;
    name:string;
    image:string;
}