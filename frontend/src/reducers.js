const initialStateView = {
    currentView: 'login',
    sendingInProgress: false
};

export const view = (state = initialStateView, action) => {
    switch (action.type) {
        case 'CHANGE_VIEW':
            return Object.assign({}, state, {currentView: action.view});
        case 'MESSAGE_SENDING_START':
            return Object.assign({}, state, {sendingInProgress: true});
        case 'MESSAGE_SENDING_FINISH':
            return Object.assign({}, state, {sendingInProgress: false});
        default:
            return state;
    }
};

const initialStateLogin = {
    loggedIn: false,
    loginError: false,
    username: null,
    token: null
};

export const login = (state = initialStateLogin, action) => {
    switch (action.type) {
        case 'LOGIN_SUCCESSFULL':
            return {loggedIn: true, loginError: false, username: action.username, token: action.token};
        case 'LOGIN_ERROR':
            return Object.assign({}, state, initialStateLogin, {loginError: true});
        case 'LOGOUT':
            return initialStateLogin;
        default:
            return state;
    }
};

const initialStateHealth = {healthy: true};
export const health = (state = initialStateHealth, action) => {
    switch (action.type) {
        case 'BACKEND_HEALTH_CHECK_FAIL':
            return Object.assign({}, state, {healthy: false});
        case 'BACKEND_HEALTH_CHECK_SUCCESS':
            return Object.assign({}, state, {healthy: true});
        default:
            return state;
    }
};

const initialStateContacts = [];
export const contacts = (state = initialStateContacts, action) => {
    switch (action.type) {
        case 'LOAD_CONTACTS':
            return action.contacts;
        default:
            return state;
    }
};

const initialStateQueue = [];
export const queue = (state = initialStateQueue, action) => {
    switch (action.type) {
        case 'LOAD_QUEUE':
            return action.queue;
        default:
            return state;
    }
};
