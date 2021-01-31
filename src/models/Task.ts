import Unit from "./Unit";

export default interface Task {
    id: number;
    uuid: string;
    name: string;
    station: string;
    machine: string;
    chefInvolved: boolean;
    image:string;
    unit:Unit;
    outputQuantity:number;
    item?:Task;
    stationColor:String;
    description:String;
    intervention_time:String;
    outputUnitName:String;
    intervention:Array<Task>
}
