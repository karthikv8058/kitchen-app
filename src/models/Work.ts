import Order from './Order';
import Task from './Task';
import Recipe from './Recipe';
import Course from './Course';

export const WORK_QUEUED= 1;
export const WORK_STARTED = 2;

export default interface Work {
    id: number;
    Orderobject: Order;
    task: Task;
    status: number;
    userId: string;
    title: string;
    transportType: number;
    backgroundColor: string;
    fontColor: string;
    shadowColor: string;
    likelyHood: number;
    priority: number;
    recipe: Recipe;
    course: Course;
    timeRemaining: number;
    timeLeft: number;
    output: String;
    readyToStart:boolean;
    wishes: string[]
}
