import 'react-native';

import { getBackgroundViewStyle } from '../src/utils/taskColorUtils';

const testStationList = [
  {
    title: 'Station1',
    id: 1,
    color: '#2EE7F1'
  },
  {
    title: 'Station2',
    id: 2,
    color: '#ADC1C2'
  },
  {
    title: 'Station3',
    id: 3,
    color: '#070808'
  },
  {
    title: 'Station4',
    id: 4,
    color: '#23CACA'
  },
  {
    title: 'Station5',
    id: 5,
    color: '#095151'
  },
];

describe('Task color code', () => {

  it('gives colorCode  for stationId 1', () => {
    expect(getBackgroundViewStyle(1, testStationList)).toBe('#2EE7F1');
  });
  it('gives colorCode  for stationId 2', () => {
    expect(getBackgroundViewStyle(2, testStationList)).toBe('#ADC1C2');
  });
  it('gives colorCode  for stationId 3', () => {
    expect(getBackgroundViewStyle(3, testStationList)).toBe('#070808');
  });
  it('gives colorCode  for stationId 4', () => {
    expect(getBackgroundViewStyle(4, testStationList)).toBe('#23CACA');
  })
  it('gives colorCode  for stationId 5', () => {
    expect(getBackgroundViewStyle(5, testStationList)).toBe('#095151');
  });
});
