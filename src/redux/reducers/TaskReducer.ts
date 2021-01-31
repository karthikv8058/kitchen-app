import Action from '../Action';
import { SET_HP_TASK_COUNT } from '../ActionTypes';

const INITIAL_STATE = {
  priorityTaskcount: 0
};

const TaskReducer = (state = INITIAL_STATE, action: Action) => {
  switch (action.type) {
    case SET_HP_TASK_COUNT:
      return (<any>Object).assign({}, state, { priorityTaskcount: action.payload.count });
    default:
      return state;
  }
};

export default TaskReducer;
