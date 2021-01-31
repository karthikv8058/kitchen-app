import { getTimeDiffrece } from '../src/utils/timeUtils';

describe('get Time Diffrence', () => {

    it('Get time diffrence when delayTime is 12212121212', () => {
        expect(getTimeDiffrece(12212121212)).toBe(TimeDiffrece(12212121212));
    });

    it('Get time diffrence when delayTime is 3685421', () => {
        expect(getTimeDiffrece(3685421)).toBe(TimeDiffrece(3685421));
    });

    it('Get time diffrence when delayTime is 1566982107926', () => {
        expect(getTimeDiffrece(1566982107926)).toBe(TimeDiffrece(1566982107926));
    });

    it('Get time diffrence when delayTime is 4545648945614565', () => {
        expect(getTimeDiffrece(4545648945614565)).toBe(TimeDiffrece(4545648945614565));
    });

});

let TimeDiffrece = (timeInmilliSecs: any) => {
    let currentTime = Date.now();
    let diff = (timeInmilliSecs - currentTime);
    let diffMins = Math.round(((diff % 86400000) % 3600000) / 60000);
    return diffMins;
};
