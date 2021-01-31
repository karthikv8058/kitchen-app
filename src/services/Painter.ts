import Station from '@models/Station';
import Work, { WORK_STARTED } from '@models/Work';

export default class Painter {

    convertRGBToHex(color: number[]): string {
        return '#' + [color[0], color[1], color[2]].map(x => {
            x = Math.round(x);
            const hex = x.toString(16);
            return hex.length == 1 ? '0' + hex : hex;
        }).join('');
    }

    getFontColorTone(p: number, initialColor: any) {
        let contrastColor = this.convertHexToRGB(this.getFontContrast(initialColor));
        let x = 0.5 + p * 0.5;
        let R = (1 - x) * 0 + x * contrastColor[0];
        let G = (1 - x) * 149 + x * contrastColor[1];
        let B = (1 - x) * 157 + x * contrastColor[2];
        return [R, G, B];
    }

    convertHexToRGB(hex: string) {
        let R, G, B;
        R = parseInt(hex.slice(1, 3), 16),
            G = parseInt(hex.slice(3, 5), 16),
            B = parseInt(hex.slice(5, 7), 16);
        return [R, G, B];
    }

    getFontContrast(hex: string) {
        let C, L;
        let R, G, B;
        let fontcolor;
        R = parseInt(hex.slice(1, 3), 16),
            G = parseInt(hex.slice(3, 5), 16),
            B = parseInt(hex.slice(5, 7), 16);
        C = [R / 255, G / 255, B / 255];
        for (let i = 0; i < C.length; ++i) {
            if (C[i] <= 0.03928) {
                C[i] = C[i] / 12.92;
            } else {
                C[i] = Math.pow((C[i] + 0.055) / 1.055, 2.4);
            }
        }
        L = 0.2126 * C[0] + 0.7152 * C[1] + 0.0722 * C[2];
        if (L > 0.179) {
            fontcolor = '#000000';
        } else {
            fontcolor = '#ffffff';
        }
        return fontcolor;
    }

    getShadowColor(p: number) {
        return [255 * p, 255 * p, 255 * p];
    }

    getBackgroundColorTone(p: number, initialColor: any) {
        let rgbColor = this.convertHexToRGB(initialColor);
        let x = 0.3 + p * 0.7;
        let R = (1 - x) * 0 + x * rgbColor[0];
        let G = (1 - x) * 149 + x * rgbColor[1];
        let B = (1 - x) * 157 + x * rgbColor[2];
        return [R, G, B];
    }

    getLikelyHood(likelyHoodLimit: number, priority: number): number {
        if (priority > 0) {
            return 1;
        } else if (priority > (-1 * likelyHoodLimit)) {
            let a = -1 * priority;
            return 1 - (a / likelyHoodLimit);
        } else {
            return 0;
        }
    }

    generateColorMap(station: Station, workArray: Work[], likelyHoodLimit: number) {
        let likelyHood = 0;
        workArray.map((work: Work, index: number) => {

            //TODO Refactor
            let isMachineTask = work.task && work.task.machine && !work.task.chefInvolved;
            let isAssigned = (work.transportType & 4) == 0 && !isMachineTask && work.userId && work.status == WORK_STARTED;
            let priority = isAssigned?1:work.priority;

            
            likelyHood = this.getLikelyHood(likelyHoodLimit,priority);
            let colorTone = this.getBackgroundColorTone(likelyHood, station.color);
            work.backgroundColor = this.convertRGBToHex(colorTone);
            work.fontColor = this.convertRGBToHex(this.getFontColorTone(likelyHood, station.color));
            work.shadowColor = this.convertRGBToHex(this.getShadowColor(likelyHood));
            work.likelyHood = likelyHood;
        });
    }
}
