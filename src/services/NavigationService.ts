import { CommonActions } from '@react-navigation/native';
import { Bind } from 'src/ioc/ServiceContainer';
import PermissionService from './PermissionService';

export default class NavigationService {

    private navigation: any;

    private permissionService: PermissionService;

    constructor(permissionService: PermissionService) {
        this.permissionService = permissionService;
    }

    public setNavigation(navigation: any) {
        if (navigation) {
            this.navigation = navigation;
        }
    }
    public getNavigation() {
        return this.navigation;
    }

    public push(screen: string, params: any = null) {
        if(this.permissionService.checkRoutePermission(screen)){
            this.navigation?.navigate(screen, params);
        }
    }

    public reset(routeName: string, params: any = null) {
        this.navigation.dispatch(CommonActions.reset({
            index: 0,
            routes: [
                { name: routeName, params: params }
            ],
        }));
    }

    public pop() {
        this.navigation?.pop();
    }
}