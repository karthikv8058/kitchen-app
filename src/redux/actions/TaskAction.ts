import { SET_HP_TASK_COUNT } from '../ActionTypes';

export const setHighPriorityTaskCount = (count: number) => {
    return({
    type: SET_HP_TASK_COUNT,
    payload: {
        count: count
    }
}); };
