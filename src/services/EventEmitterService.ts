export default class EventEmitterService {

    private emitters: EventListner[] = [];

    addListner(listner: EventListner) {
        this.emitters.push(listner);
    }

    removeListner(listner: EventListner) {
        this.emitters = this.emitters.filter((i) => listner != i);
    }

    emit(event: { type: EventTypes, data?: any, extras?: any }) {
        for (let listner of this.emitters) {
            if (listner.type == event.type) {
                listner.callback(event.data, event.extras);
            }
        }
    }
}

export interface EventListner {
    type: number;
    callback: (data: any, extras?: any) => void;
}

export enum EventTypes {
    SYNC_TASK = 1,
    SYNC_CHECKING_TASK = 2,
    SERVER_LOGGED_OUT = 3,
    SERVER_FOUND = 4,
    Manual = 5
}
