import 'react-native';
import { renderProps } from '../src/utils/commonUtil';

const props = {
    unit: {
        symbol: 'kg'
    }
};
const units = {
    unit: {
        symbol: 'ml'
    }
};
const ingredient = {
    unit: {
        symbol: 'g'
    }
};

const Unit = {
    unit: {
        symbol: 'L'
    }
};
const symbol = {
};
describe('return the unit', () => {

    it('gives the symbol for unit kg ', () => {
        expect(renderProps(props)).toBe('kg');
    });

    it('gives the symbol for unit ml ', () => {
        expect(renderProps(units)).toBe('ml');
    });

    it('gives the symbol for unit of null  ', () => {
        expect(renderProps(symbol)).toBe('');
    });

    it('gives the symbol for unit g ', () => {
        expect(renderProps(ingredient)).toBe('g');
    });

    it('gives the symbol for unit L ', () => {
        expect(renderProps(Unit)).toBe('L');
    });

});
