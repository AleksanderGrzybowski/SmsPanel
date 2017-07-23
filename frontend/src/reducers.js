const initialStateView = {
    currentView: 'login',
    sendingInProgress: false,
    notification: '',
    bsClass: ''
};

export const view = (state = initialStateView, action) => {
    switch (action.type) {
        case 'CHANGE_VIEW':
            return Object.assign({}, state, {currentView: action.view});
        case 'MESSAGE_SENDING_START':
            return Object.assign({}, state, {sendingInProgress: true});
        case 'MESSAGE_SENDING_FINISH':
            return Object.assign({}, state, {sendingInProgress: false});
        case 'SHOW_NOTIFICATION':
            return Object.assign({}, state, {notification: action.text, bsClass: action.bsClass});
        case 'HIDE_NOTIFICATION':
            return Object.assign({}, state, {notification: '', bsClass: ''});
        default:
            return state;
    }
};

const initialStateAccount = {balance: '...'};
export const account = (state = initialStateAccount, action) => {
    switch (action.type) {
        case 'LOAD_BALANCE':
            return {balance: action.balance};
        default:
            return state;
    }
};

const initialStateLogin = {
    loggedIn: false,
    loginError: false,
    username: null,
    token: null,
    requestInProgress: false
};

export const login = (state = initialStateLogin, action) => {
    switch (action.type) {
        case 'LOGIN_REQUEST_START':
            return Object.assign({}, state, {requestInProgress: true});
        case 'LOGIN_SUCCESSFUL':
            return {loggedIn: true, loginError: false, username: action.username, token: action.token, requestInProgress: false};
        case 'LOGIN_ERROR':
            return Object.assign({}, state, initialStateLogin, {loginError: true, requestInProgress: false});
        case 'LOGOUT':
            return initialStateLogin;
        default:
            return state;
    }
};


const removeSpinner = () => { // sorry, I love you FP, but I'm forced to
    const loadingSpinner = document.getElementsByClassName('loading')[0];
    loadingSpinner.remove();
};
const initialStateHealth = {wasCheckPerformed: false, healthy: true};
export const health = (state = initialStateHealth, action) => {
    switch (action.type) {
        case 'BACKEND_HEALTH_CHECK_FAIL':
            removeSpinner();
            return Object.assign({}, state, {wasCheckPerformed: true, healthy: false});
        case 'BACKEND_HEALTH_CHECK_SUCCESS':
            removeSpinner();
            return Object.assign({}, state, {wasCheckPerformed: true, healthy: true});
        default:
            return state;
    }
};

const initialStateContacts = [];
export const contacts = (state = initialStateContacts, action) => {
    switch (action.type) {
        case 'LOAD_CONTACTS':
            return action.contacts;
        case 'LOGOUT':
            return initialStateContacts;
        default:
            return state;
    }
};

const initialStateQueue = [];
export const queue = (state = initialStateQueue, action) => {
    switch (action.type) {
        case 'LOAD_QUEUE':
            return action.queue;
        case 'LOGOUT':
            return initialStateQueue;
        default:
            return state;
    }
};
