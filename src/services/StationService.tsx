import ApiBuilder from "@api/routes";
import Work from "@models/Work";

import HttpClient from "./HttpClient";
import Station from "@models/Station";
import Room from "@models/Room";

export default class StationService {
  private apiBuilder: ApiBuilder;
  private httpClient: HttpClient;

  constructor(httpClient: HttpClient, apiBuilder: ApiBuilder) {
    this.httpClient = httpClient;
    this.apiBuilder = apiBuilder;
  }

  loadStations(): Promise<Station[]> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.paths!.getstations, {})
        .then((response) => {
          resolve(response);
        })
        .catch(() => resolve([]));
    });
  }
  printerList(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.paths!.getPrinterList, {})
        .then((response) => {
          resolve(response);
        })
        .catch((erro) => resolve([]));
    });
  }
  loadStationsList(): Promise<Station[]> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.paths!.getstationlist, {})
        .then((response) => {
          resolve(response);
        })
        .catch(() => resolve([]));
    });
  }

  loadStationsAsObject(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.loadStations()
        .then((stations) => {
          let result: any = {};
          for (let station of stations) {
            result[station.uuid] = station;
          }
        })
        .catch(() => resolve({}));
    });
  }

  loadRooms(): Promise<Room[]> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .get(this.apiBuilder.getRoute("rooms"))
        .then((response) => {
          resolve(response);
        })
        .catch(() => resolve([]));
    });
  }

  listStationsByRoom(roomId: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("stations_by_room"), { roomId })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  listGuestgoup() {
    return new Promise((resolve, reject) => {
      this.httpClient
        .get(this.apiBuilder.getRoute("guestGroup/list"))
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  mergeGuestgroup(fromId: any, toId: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("guestGroup/merge"), {
          from: fromId,
          to: toId,
        })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  splitGuestgroup(fromId: any, toId: any, guests: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("guestGroup/split"), {
          from: fromId,
          to: toId,
          guest: guests,
        })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  listGuests(guestgroupId: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("guestGroup/guests "), {
          guestgroup: guestgroupId,
        })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  updateGuestgroup(guestgroupId: any, stationId: any, tableNumber: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("guestGroup/update"), {
          guestgroup: guestgroupId,
          station: stationId,
          table: tableNumber,
        })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }

  passQRcodeData(qr: any) {
    return new Promise((resolve, reject) => {
      this.httpClient
        .post(this.apiBuilder.getRoute("guestGroup/qr"), {
          qr: qr,
        })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          resolve(null);
        });
    });
  }
}
