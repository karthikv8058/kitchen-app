let _registry = [];
class ServiceFactory {
    static get registeredClasses() {
        return _registry;
    }

    static registerService(name, object) {
        ServiceFactory.registeredClasses[name.toLowerCase()] = object;
    }
    static getServiceBy(name) {
        return ServiceFactory.registeredClasses[name.toLowerCase()];
    }
}
export default  ServiceFactory;
