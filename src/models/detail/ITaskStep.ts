import IIngredientWithQuantity from "./IIngredientWithQuantity";
import ITaskWithQuantity from "./ITaskWithQuantity";

export default interface ITaskStep {
    name: string;
    description: string;
    image: string;
    video: string;
    ingredients:IIngredientWithQuantity[];
    tasks:ITaskWithQuantity[];
}
