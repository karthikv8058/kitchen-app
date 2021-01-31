import Course from './Course';
import Meal from './Meal';

export default interface Order {
    id?: number;
    isInventory?: boolean;
    isOnCall?:boolean,
    orderId?: number;
    meals?: Array<Meal>;
    deliveryTime?: string;
    deliveryDate?: Date;
    tableNumber?: string;
    deliverableType?: string;
    orderTo ?: string
    printerData?:Array<String>
    labels?:[],

    //verified
    status:number;
    uuid:string;
    courses:Course[];
    supplier:string;
}
