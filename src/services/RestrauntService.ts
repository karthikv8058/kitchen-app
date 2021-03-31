import { ApiBaseUrl } from '@native/ClientServer';
import HttpClient from '@services/HttpClient';
import Restaurant from '@models/Restaurant';
import ApiBuilder from '@api/routes';

export default class RestrauntService {

    private httpClient: HttpClient;

    constructor(httpClient: HttpClient) {
        this.httpClient = httpClient;
    }

    public checkRestrauntServer(ip: String): Promise<Boolean> {
        return new Promise((resolve, reject) => {
            this.httpClient.get("http://" + ip + ":8888/ping").then(r => {
                resolve(true);
            }).catch(e => {
                resolve(false);
            })
        });
    }

    getRestaurantList(token: string): Promise<Restaurant[]> {

        const headers: string[][] = []
        headers.push(['Authorization', 'Bearer ' + token]);
        headers.push(['locale', 'en']);

        return new Promise((resolve, reject) => {
            this.httpClient.get(ApiBaseUrl + 'api/1.0/mobile', false, headers).then(response => {
                if (response) {
                    response.sort((a: Restaurant, b: Restaurant) => {
                        let r = a.name ? a.name.toUpperCase() : '';
                        let s = b.name ? b.name.toUpperCase() : '';
                        return this.stringCompare(r, s);
                    });
                    resolve(response);
                } else {
                    resolve([]);
                }
            }).catch((error) => {
                resolve([]);
            });
        });
    }

    stringCompare(a: string, b: string): number {
        return a < b ? - 1 : (a > b ? 1 : 0);
    }

}
