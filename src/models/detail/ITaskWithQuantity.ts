import Task from "@models/Task";
import Work from "@models/Work";

export default interface ITaskWithQuantity {
    quantity:number;
    task:Task;
}