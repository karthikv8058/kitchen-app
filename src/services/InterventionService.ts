import { InterventionPosition } from '@models/Intervention';

export default class InterventionService {
    checkForInterventionReductionMesssage(meta: { position: number, startedAt: number, time: number }): number {
        if(meta == null){
            return 0;
        }
        if (meta.position != InterventionPosition.POSITION_END) {
            let diff = Math.abs(new Date().getTime() - meta.startedAt);
            let diffInMins = Math.abs(diff);
            return diffInMins < meta.time ? diffInMins : 0;
        } else {
            return 0;
        }
    }
}
