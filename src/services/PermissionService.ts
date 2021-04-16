import { act } from "react-test-renderer";
import StorageService, { Storage } from "./StorageService";

export enum Action {
    NAVIGATE_POS = 1,
    VIEW_ORDER = 2,
    EDIT_ORDER = 3,
    DELETE_ORDER = 4,
    VIEW_INVENTORY = 5,
    EDIT_INVENTORY = 6,
    DELETE_INVENTORY = 7,
    CREATE_INVENTORY = 8,
}

export enum Permissions {
    VIEW_ORDER = "app_view_orders",
    VIEW_INVENTORY = "app_view_inventory",
    CREATE_ORDER = "app_new_orders",
    CREATE_INVENTORY_ORDER = "app_new_inventory_orders",
    DELETE_ORDER = "app_delete_orders",
    MANAGE_ORDER = "app_manage_orders",
    MANAGE_INVENTORY = "app_manage_inventory",
    MANAGE_SERVICE = "manage_service",
}

const pagePermissions: any = {
    "PosPage": Permissions.CREATE_ORDER,
    "OrderOverview": Permissions.VIEW_ORDER,
    "InventoryPage": Permissions.VIEW_INVENTORY
}

export default class PermissionService {

    private storageService: StorageService;
    private permssions: string[] = [];

    constructor(storageService: StorageService) {
        this.storageService = storageService;
    }

    setPermissions(permssions: string[]) {
        this.permssions = permssions;
    }

    checkRoutePermission(screen: string): boolean {
        //et permssions: string[] = this.storageService.getFast(Storage.ROLES);
        console.log(this.permssions)
        let screenPermission = pagePermissions[screen];
        if (screenPermission) {
            return this.permssions.includes(pagePermissions[screen]);
        }
        //permssions.includes();
        return true;
    }

    hasPermission(action: Action): boolean {
        let permission = "";
        switch (action) {
            case Action.EDIT_ORDER: permission = Permissions.MANAGE_ORDER; break;
            case Action.DELETE_ORDER: permission = Permissions.DELETE_ORDER; break;
            case Action.VIEW_ORDER: permission = Permissions.VIEW_ORDER; break;
            case Action.CREATE_INVENTORY: permission = Permissions.CREATE_INVENTORY_ORDER; break;
            case Action.EDIT_INVENTORY: permission = Permissions.MANAGE_INVENTORY; break;
        }
        return this.permssions.includes(permission);
    }

}