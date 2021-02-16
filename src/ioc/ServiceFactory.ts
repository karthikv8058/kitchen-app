let _registry: any = [];
class ServiceFactory {
    static get registeredClasses() {
        return _registry;
    }

    static registerService(name: string, object: any) {
        ServiceFactory.registeredClasses[name.toLowerCase()] = object;
    }

    static getServiceBy(name: string): any {
        return ServiceFactory.registeredClasses[name.toLowerCase()];
    }
}
export default ServiceFactory;
