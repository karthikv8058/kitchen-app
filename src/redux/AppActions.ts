import {
    SET_IP,
    SET_USER_ID,
    SWITCH_VR,
    SWITCH_TTS,
    READ_DESCRIPTION_IN_DETAIL,
    READ_DESCRIPTION_IN_STEPS,
    READ_INGREDIENT_IN_DETAILS,
    READ_INGREDIENT_IN_STEPS,
    READ_TASK_ON_ASSIGN,
    AUTO_ASSIGN,
    TASK_AUTO_OPEN,
    STEP_AUTO_OPEN,
    UPDATE_ACL,
    AUTH_TOKEN,
    POS_LOADING
} from './ActionTypes';

export const setIP = (ip: string) => {
    return ({
        type: SET_IP,
        payload: {
            ip: ip
        }
    });
};

export const setUserId = (userId: string) => {
    return ({
        type: SET_USER_ID,
        payload: { userId }
    });
};

export const switchVR = (value: boolean) => {
    return ({
        type: SWITCH_VR,
        payload: {
            value: value
        }
    });
};

export const switchTTS = (value: boolean) => {
    return ({
        type: SWITCH_TTS,
        payload: {
            value: value
        }
    });
};

export const readTaskOnAssign = (value: boolean) => {
    return ({
        type: READ_TASK_ON_ASSIGN,
        payload: {
            readTaskOnAssign: value
        }
    });
};

export const readDescriptionInDetail = (value: boolean) => {
    return ({
        type: READ_DESCRIPTION_IN_DETAIL,
        payload: {
            readDescriptionInDetail: value
        }
    });
};

export const readDescriptionInSteps = (value: boolean) => {
    return ({
        type: READ_DESCRIPTION_IN_STEPS,
        payload: {
            readDescriptionInSteps: value
        }
    });
};

export const readIngredientInDetail = (value: boolean) => {
    return ({
        type: READ_INGREDIENT_IN_DETAILS,
        payload: {
            readIngredientInDetail: value
        }
    });
};

export const readIngredientInSteps = (value: boolean) => {
    return ({
        type: READ_INGREDIENT_IN_STEPS,
        payload: {
            readIngredientInSteps: value
        }
    });
};

export const autoAssign = (value: boolean) => {
    return ({
        type: AUTO_ASSIGN,
        payload: {
            autoAssign: value
        }
    });
};

export const taskAutoOpen = (value: boolean) => {
    return ({
        type: TASK_AUTO_OPEN,
        payload: {
            taskAutoOpen: value
        }
    });
};

export const stepAutoOpen = (value: boolean) => {
    return ({
        type: STEP_AUTO_OPEN,
        payload: {
            stepAutoOpen: value
        }
    });
};

export const updateACL = (value: Object) => {
    return ({
        type: UPDATE_ACL,
        payload: {
            acl_data: value
        }
    });
};

export const setAuthToken = (value: string) => {
    return ({
        type: AUTH_TOKEN,
        payload: {
            authToken: value
        }
    });
};

export const isPosLoading = (value: string) => {
    return ({
        type: POS_LOADING,
        payload: {
            isPosLoading: value
        }
    });
};
