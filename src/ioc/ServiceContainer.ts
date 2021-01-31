import EventEmitterService from '@services/EventEmitterService';
import HttpClient from '@services/HttpClient';
import OrderService from '@services/OrderService';
import TaskService from '@services/TaskService';
import ApiBuilder from '../api/routes';
import ServiceFactory from './ServiceFactory';
import NavigationService from '@services/NavigationService';
import StorageService from '@services/StorageService';
import RestrauntService from '@services/RestrauntService';
import UserService from '@services/UserService';
import InterventionService from '@services/InterventionService';
import VoiceService from '@services/VoiceService';
import Painter from '@services/Painter';
import WebServer from '@native/ClientServer';
import StationService from '@services/StationService';
import PermissionService from '@services/PermissionService';

const apiBuilder = new ApiBuilder();
const eventEmitterService = new EventEmitterService();
const storageService = new StorageService();
const permissionService = new PermissionService(storageService);
const navigationService = new NavigationService(permissionService);
const httpClient = new HttpClient(storageService,navigationService);
const restrauntService = new RestrauntService(httpClient);
const userService = new UserService(httpClient, apiBuilder);
const interventionService = new InterventionService();
const voiceService = new VoiceService();
const painter = new Painter();
const webServer = new WebServer();
const stationService = new StationService(httpClient,apiBuilder);
const taskService = new TaskService(httpClient, apiBuilder);
const orderService = new OrderService(httpClient,apiBuilder);

ServiceFactory.registerService('apiBuilder', apiBuilder);
ServiceFactory.registerService('httpClient', httpClient);
ServiceFactory.registerService('taskService', taskService);
ServiceFactory.registerService('orderService', orderService);
ServiceFactory.registerService('eventEmitterService', eventEmitterService);
ServiceFactory.registerService('navigationService', navigationService);
ServiceFactory.registerService('storageService', storageService);
ServiceFactory.registerService('restrauntService', restrauntService);
ServiceFactory.registerService('userService', userService);
ServiceFactory.registerService('interventionService', interventionService);
ServiceFactory.registerService('voiceService', voiceService);
ServiceFactory.registerService('painter', painter);
ServiceFactory.registerService('webServer', webServer);
ServiceFactory.registerService('stationService', stationService);
ServiceFactory.registerService('permissionService', permissionService);

const ioc = { ServiceFactory: ServiceFactory }
export default ioc;

export const HTTP_CLIENT = 'httpClient';

export function Bind(service: String) {

    return ioc.ServiceFactory.getServiceBy(service);
    // return function (target: any, key: string | symbol) {

    //     let val = ioc.ServiceFactory.getServiceBy(service);

    //     const getter = function () {
    //         return val;
    //     };
    //     const setter = (next: any) => {
    //         val = next;
    //     };

    //     Object.defineProperty(target, key, {
    //         get: getter,
    //         set: setter,
    //         enumerable: true,
    //         configurable: true,
    //     });

    // };
}