export const TRIGGER_PHRASE = 'TONI ';
export const SHOW_ALL = 'Show All';
export const CHOOSE_ACTION = 'Please choose an action';
export const LANDSCAPE = 'LANDSCAPE';
export const PORTRAIT = 'PORTRAIT';
export const BACK = 'BACK';
export const LogOut = 'Logout';
export const cancel = 'cancel';
export const number = 'number';
export const object = 'object';
export const Cancel = 'Cancel';
export const menuCategory = {
    orderCategory: 'Order',
    inventoryCategory: 'Inventory',
    taskCategory: 'Task',
    profileCategory: 'Settings'
};
export const MY_TASKS = 0;
export const STATION_VIEW = 1;
export const RECIPE_LOOKUP = 2;
export const OPEN_ORDER = 3;
export const COMPLETED_ORDER = 4;
export const NEW_ORDER = 5;
export const WORK_STATION = 7;
export const SETTINGS_RESTURANT = 8;
export const LOGOUT = 4;
export const INVENTORY = 2;
export const INTERVENTION_TASK_TYPE = 1;
export const TYPE_SERVER_LIST = 2;
export const PUSH_USER_SETTINS = 3;
export const orderItems = {
    order: 'Table',
    inventory: 'Inventory'
};
export const DeliveryTypes = {
    ON_CALL: 'On call',
    ASAP: 'ASAP',
    ON_TIME: 'Deliver at'
};

export const MenuItem = [
    {
        title: 'Tasks',
        isShown : true,
        headerId: 0,
        content: [
            {
                item: 'My tasks',
                id: 0
            }, {
                item: 'All tasks',
                id: 1
            }, {
                item: 'Recipes',
                id: 2
            },
        ]
    },
    {
        title: 'Orders',
        isShown : false,
        headerId: 1,
        content: [
            {
                item: 'Overview',
                id: 3
            },  {
                item: 'New order',
                id: 5
            }
        ]
    },
    {
        title: 'Inventory',
        isShown : false,
        headerId: 2,
        content: []
    },
    {
        title: 'Settings',
        isShown : true,
        headerId: 3,
        content: [
            {
                item: 'My stations',
                id: 7
            }, {
                item: 'Restaurant settings',
                id: 8
            }
        ]
    },
    {
        title: 'Logout',
        isShown : true,
        headerId: 4,
        content: []
    }
];
export const taskMode = {
    taskMode: 'Task mode',
    deliveryMode: 'Delivery mode',
};
