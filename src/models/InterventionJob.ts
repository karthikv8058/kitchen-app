import Intervention from "./Intervention";

export default interface InterventionJob {
    id:number;
    intervention: Intervention;
    startedAt: string;
}