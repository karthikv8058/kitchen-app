import ApiBuilder from '@api/routes';
import Work from '@models/Work';

import HttpClient from './HttpClient';
import Station from '@models/Station';

export default class StationService {

    private apiBuilder: ApiBuilder;
    private httpClient: HttpClient;

    constructor(httpClient: HttpClient, apiBuilder: ApiBuilder) {
        this.httpClient = httpClient;
        this.apiBuilder = apiBuilder;
    }

    loadStations(): Promise<Station[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder.paths!.getstations, {}).then(response => {
                resolve(response);
            }).catch(() => resolve([]));
        });
    }
    printerList(): Promise<any> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder.paths!.getPrinterList, {}).then(response => {
                resolve(response);
            }).catch((erro) => 

             resolve([]));
        });
    }
    loadStationsList(): Promise<Station[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder.paths!.getstationlist, {}).then(response => {
                resolve(response);
            }).catch(() => resolve([]));
        });
    }

    loadStationsAsObject(): Promise<any> {
        return new Promise((resolve, reject) => {
            this.loadStations().then(stations => {
                let result: any = {};
                for (let station of stations) {
                    result[station.uuid] = station;
                }
            }).catch(() => resolve({}));
        });
    }
}
