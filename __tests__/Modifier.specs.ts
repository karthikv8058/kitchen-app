import 'react-native';
import { getModifierName } from '../src/utils/commonUtil';

 const modifiers = {
    modifier: 'More crispy'
 };

describe('return Modifer', () => {

    it('gives modifer in accordance  with index 1', () => {
      expect(getModifierName(modifiers, 1)).toBe(',  More crispy');
    });

    it('gives modifer in accordance  with index 0', () => {
        expect(getModifierName(modifiers, 0)).toBe('More crispy';
      });

      it('gives modifer in accordance  with index 2', () => {
        expect(getModifierName(modifiers, 2)).toBe(',  More crispy');
      });

      it('gives modifer in accordance  with index 852', () => {
        expect(getModifierName(modifiers, 852)).toBe(',  More crispy');
      });

      it('gives modifer in accordance  with index -2', () => {
        expect(getModifierName(modifiers, -2)).toBe(',  More crispy');
      });

  });
