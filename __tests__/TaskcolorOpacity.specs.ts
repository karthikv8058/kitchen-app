import 'react-native';
import { getBackgroundColorTone, convertRGBToHex } from '@components/FontColorHelper';
const likelyHood = 600000;
describe('Task color Tone', () => {

    it('gives ColorTone  for backGRoundColor #2EE7F1 ', () => {
        expect(convertRGBToHex(getBackgroundColorTone(likelyHood, '#2EE7F1'))).toBe('#126ccce20d83ee21a5536');
    });
    it('gives ColorTone  for backGRoundColor #ADC1C2 ', () => {
        expect(convertRGBToHex(getBackgroundColorTone(likelyHood, '#ADC1C2'))).toBe('#454b454119fc22ed1fc8');
    });
    it('gives ColorTone  for backGRoundColor #070808 ', () => {
        expect(convertRGBToHex(getBackgroundColorTone(likelyHood, '#070808'))).toBe('#2cdc62-3879fb5-3bae4b0');
    });
    it('gives ColorTone  for backGRoundColor #23CACA ', () => {
        expect(convertRGBToHex(getBackgroundColorTone(likelyHood, '#23CACA'))).toBe('#e04deb153a9c512064ca');
    });
    it('gives ColorTone  for backGRoundColor #095151 ', () => {
        expect(convertRGBToHex(getBackgroundColorTone(likelyHood, '#095151'))).toBe('#39ada3-1b3c9ff-1e70efa');
    });

});
