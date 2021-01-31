import ChefActivityLog from "./ChefActivityLog";
import Order from "./Order";
import Work from "./Work";

export default interface OrderList {
    chefActivityLogs?: Array<ChefActivityLog>;
    order?: Array<Order>;
    work?: Array<Work>;
}
