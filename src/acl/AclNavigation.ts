export default class AclNavigation {
    private navigation;
    constructor(navigation: any) {
        this.navigation = navigation;
    }

    push(path: string, params?: object) {
        if (this.checkAccess(path)) {
            this.navigation.push(path, params);
        }
    }
    navigate(path: string, params?: object) {
        if (this.checkAccess(path)) {
            this.navigation.push(path, params);
        }

    }

    // check whther the url has access
    checkAccess(path:string) {
        return true;
    }
}
