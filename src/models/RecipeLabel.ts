export default interface RecipeLabel {
    uuid: string;
    isLabel: boolean;
    name?: string;
    recipeLabels?: string;
    color?: string;
    isServiceSet:boolean;
    parentLabel:string

}
