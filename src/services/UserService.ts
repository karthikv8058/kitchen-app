import HttpClient from '@services/HttpClient';
import ApiBuilder from '@api/routes';
import User from '@models/User';
import UserRights from '@models/userRights';

export default class UserService {

    private apiBuilder: ApiBuilder;
    private httpClient: HttpClient;

    constructor(httpClient: HttpClient, apiBuilder: ApiBuilder) {
        this.apiBuilder = apiBuilder;
        this.httpClient = httpClient;
    }
    webCredentials(): Promise<any> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.webCredentials, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    requestSync(): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.requestSync, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    getUsersAndStations(): Promise<User[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.getAllUsers, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    getUserRights(): Promise<UserRights[]> {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.getUserRights, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }

    
   userLogout() {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.logoutUser, {}).then(response => {
                resolve(response);
            }).catch(error => {
                resolve([]);
            });
        });
    }
    addStationUser(userId: String[]){
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.addStationUser,
                {stationId:userId}).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    testStationPrinter(userId: String[]){
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.testStationPrinter,
                {stationId:userId}).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }
    
    login(user: User, ip: string) {
        return new Promise((resolve, reject) => {
            this.httpClient.post(this.apiBuilder!.paths!.login,
                {
                    username: user?.username,
                    password: user?.password,
                    ip: ip
                }).then(response => {
                    resolve(response);
                }).catch(error => {
                    resolve(null);
                });
        });
    }

}
