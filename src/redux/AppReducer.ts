import { combineReducers } from 'redux';

import Action from './Action';
import {
  AUTH_TOKEN,
  AUTO_ASSIGN,
  READ_DESCRIPTION_IN_DETAIL,
  READ_DESCRIPTION_IN_STEPS,
  READ_INGREDIENT_IN_DETAILS,
  READ_INGREDIENT_IN_STEPS,
  READ_TASK_ON_ASSIGN,
  SET_IP,
  SET_USER_ID,
  SWITCH_TTS,
  SWITCH_VR,
  TASK_AUTO_OPEN,
  STEP_AUTO_OPEN,
  UPDATE_ACL,
  POS_LOADING,
} from './ActionTypes';
import TaskReducer from './reducers/TaskReducer';

const INITIAL_STATE = {
  ip: null,
  userId: null,
  isVROn: false,
  isTTSOn: false,
  readTaskOnAssign: false,
  readDescriptionInDetail: false,
  readDescriptionInSteps: false,
  readIngredientInDetail: false,
  readIngredientInSteps: false,
  taskAutoOpen: false,
  stepAutoOpen: false,
  userRights: {'ACCESS_POS': false, 'ACCESS_INVENTORY': false },
  authToken: '',
  isPosLoading : false
};

const appReducer = (state = INITIAL_STATE, action: Action) => {
  switch (action.type) {
    case SET_IP:
      return (<any>Object).assign({}, state, { ip: action.payload.ip });
    case SET_USER_ID:
      return (<any>Object).assign({}, state, { userId: action.payload.userId });
    case SWITCH_VR:
      return (<any>Object).assign({}, state, { isVROn: action.payload.value });
    case SWITCH_TTS:
      return (<any>Object).assign({}, state, { isTTSOn: action.payload.value });
    case READ_TASK_ON_ASSIGN:
      return (<any>Object).assign({}, state, { readTaskOnAssign: action.payload.readTaskOnAssign });
    case READ_DESCRIPTION_IN_DETAIL:
      return (<any>Object).assign({}, state, { readDescriptionInDetail: action.payload.readDescriptionInDetail });
    case READ_DESCRIPTION_IN_STEPS:
      return (<any>Object).assign({}, state, { readDescriptionInSteps: action.payload.readDescriptionInSteps });
    case READ_INGREDIENT_IN_DETAILS:
      return (<any>Object).assign({}, state, { readIngredientInDetail: action.payload.readIngredientInDetail });
    case READ_INGREDIENT_IN_STEPS:
      return (<any>Object).assign({}, state, { readIngredientInSteps: action.payload.readIngredientInSteps });
    case AUTO_ASSIGN:
      return (<any>Object).assign({}, state, { autoAssign: action.payload.autoAssign });
    case TASK_AUTO_OPEN:
      return (<any>Object).assign({}, state, { taskAutoOpen: action.payload.taskAutoOpen });
    case STEP_AUTO_OPEN:
      return (<any>Object).assign({}, state, { stepAutoOpen: action.payload.stepAutoOpen });
    case UPDATE_ACL:
      let userRights = state.userRights;
      userRights[action.payload.acl_data.key] = action.payload.acl_data.value;
      return (<any>Object).assign({}, state, { userRights: userRights });
    case AUTH_TOKEN:
      return (<any>Object).assign({}, state, { authToken: action.payload.authToken });
    case POS_LOADING:
      return (<any>Object).assign({}, state, { isPosLoading: action.payload.isPosLoading });
    default:
      return state;
  }
};

export default combineReducers({
  appState: appReducer, task: TaskReducer
});
