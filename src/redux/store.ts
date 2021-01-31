import { createStore } from 'redux';
import appReducer from './AppReducer';

const store = createStore(appReducer);

export default store;
