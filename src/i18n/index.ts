import i18n from 'i18n-js';
import * as RNLocalize from 'react-native-localize';

import en from './translations/en.json';

i18n.translations = { en };
i18n.fallbacks = true;

const fallback = { languageTag: 'en', isRTL: false };
const { languageTag, isRTL } = RNLocalize.findBestAvailableLanguage(Object.keys(i18n.translations)) || fallback;

i18n.missingTranslation = () => '';

export default (name: string, params = {}) => {
    return i18n.t(name, params);
};
