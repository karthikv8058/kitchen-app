import Station from "@models/Station";
import Recipe from "@models/Recipe";
import ITaskStep from "./ITaskStep";
import Work from "@models/Work";

export default interface DetailPageResponse {
    station: Station;
    recipe: Recipe;
    work: Work;
    steps: ITaskStep[];
    meta: { position: number, startedAt: number, time: number }
}
