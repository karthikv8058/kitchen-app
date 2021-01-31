/**
 * @format
 * @lint-ignore-every XPLATJSCOPYRIGHT1
 */

import 'react-native';
import { msToTime } from '../src/utils/commonUtil';

describe('MillieSecond to Time', () => {

  it('gives 1234567889 Millisecond to time', () => {
    expect(msToTime(1234567889)).toBe('12:26');
  });

  it('gives 0 Millisecond to time', () => {
    expect(msToTime(0)).toBe('05:30');
  });

  it('gives 1 Millisecond to time', () => {
    expect(msToTime(1)).toBe('05:30');
  });

  it('gives 45648646 Millisecond to time', () => {
    expect(msToTime(45648646)).toBe('18:10');
  });

  it('gives 456486555546 string  to time', () => {
    expect(msToTime('456486555546')).toBe('15:19');
  });

});
