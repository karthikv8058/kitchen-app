import store from '../redux/store';
import { responseChecker } from '../utils/responseChecker';
import ioc from '../ioc/ServiceContainer';
import NavigationService from './NavigationService';
import StorageService, { Storage } from '@services/StorageService';

export default class HttpClient {

    private storageService: StorageService;
    private navigationService: NavigationService;

    constructor(storageService: StorageService, navigationService: NavigationService) {
        this.storageService = storageService;
        this.navigationService = navigationService
    }

    public request(method: string, url: string, body: any[], useAuthorization = false, additionalHeaders: string[][] = []) {
        let header: string[][] = [
            ['Accept', 'application/json'],
            ['Content-Type', 'application/json'],
            ['ChannelType', 'Mobile'],
        ];
        if (this.storageService) {
            header.push(['authentication', this.storageService.getFast(Storage.AUTH_TOKEN)]);
        }

        if (additionalHeaders) {
            header = header.concat(additionalHeaders)
        }
        switch (method) {
            case 'GET':
            case 'DELETE':
                return fetch(url, {
                    method,
                    headers: header,
                });
            case 'POST':
                return fetch(url, {
                    method,
                    headers: header,
                    body: JSON.stringify(body),
                });
            case 'PUT':
                return fetch(url, {
                    method,
                    headers: header,
                    body: JSON.stringify(body),
                });
            default:
                return fetch(url, {
                    method,
                    headers: header,
                });
        }
    }

    public get(url: string, useAuthorization = false, additionalHeaders: string[][] = []) {
        return this.request('GET', url, [], useAuthorization, additionalHeaders).then(response => response.json());
    }

    public post(url: string, body: any, useAuthorization = false) {
        return this.request('POST', url, body, useAuthorization)
            .then(response => response.json())
            .then(response => {
                if (responseChecker(response, this.navigationService.getNavigation())) {
                    return response;
                } else {
                    return null;
                }
            });
    }

    public put(url: string, body: any, useAuthorization = false) {
        return this.request('PUT', url, body, useAuthorization).then(response => response.json());
    }

    public delete(url: string, useAuthorization = false) {
        return this.request('DELETE', url, [], useAuthorization).then(response => response.json());
    }
}
