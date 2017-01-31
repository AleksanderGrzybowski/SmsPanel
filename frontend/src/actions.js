import axios from 'axios';
import backendUrl from './backendUrl';

const authConfig = (token) => ({
    headers: {
        'Authorization': `Bearer ${token}`
    }
});

export const changeView = (view) => ({type: 'CHANGE_VIEW', view});

const backendNotHealthy = () => ({type: 'BACKEND_HEALTH_CHECK_FAIL'});
const backendHealthy = () => ({type: 'BACKEND_HEALTH_CHECK_SUCCESS'});

const loadContacts = contacts => ({type: 'LOAD_CONTACTS', contacts});
const loadQueue = queue => ({type: 'LOAD_QUEUE', queue});
const loadBalance = balance => ({type: 'LOAD_BALANCE', balance});

const messageSendingStart = () => ({type: 'MESSAGE_SENDING_START'});
const messageSendingFinish = () => ({type: 'MESSAGE_SENDING_FINISH'});

const showNotification = (bsClass, text) => ({type: 'SHOW_NOTIFICATION', text, bsClass});
export const hideNotification = () => ({type: 'HIDE_NOTIFICATION'});

export const fetchContacts = () => (dispatch, getState) => {
    axios.get(`${backendUrl}/api/contacts`, authConfig(getState().login.token))
        .then(({data}) => dispatch(loadContacts(data)))
        .catch((err) => console.log(err));
};

export const fetchQueue = () => (dispatch, getState) => {
    axios.get(`${backendUrl}/api/queue`, authConfig(getState().login.token))
        .then(({data}) => dispatch(loadQueue(data)))
        .catch((err) => console.log(err));
};

export const fetchBalance = () => (dispatch, getState) => {
    axios.get(`${backendUrl}/api/accountBalance`, authConfig(getState().login.token))
        .then(({data}) => dispatch(loadBalance(data.balance)))
        .catch((err) => console.log(err));
};

export const loginSuccessful = (username, token) => (dispatch) => {
    dispatch({type: 'LOGIN_SUCCESSFULL', username, token});
    dispatch(changeView('main'));
    dispatch(fetchContacts());
    dispatch(fetchQueue());
};

const loginError = () => ({type: 'LOGIN_ERROR'});

export const healthCheck = () => (dispatch) => {
    axios.get(`${backendUrl}/health`, {timeout: 10000})
        .then(() => {
            dispatch(backendHealthy());
        })
        .catch(err => {
            console.log(err);
            dispatch(backendNotHealthy());
        })
};

export const login = (username, password) => (dispatch) => {
    axios.post(`${backendUrl}/api/login`, {
        username, password
    })
        .then(({data}) => {
            dispatch(loginSuccessful(username, data.access_token));
            localStorage.username = username;
            localStorage.access_token = data.access_token;
        })
        .catch(() => dispatch(loginError()));
};

export const logout = () => (dispatch) => {
    localStorage.removeItem('username');
    localStorage.removeItem('access_token');
    dispatch({type: 'LOGOUT'});
    dispatch(changeView('login'));
};

export const validateTokenAndLogIn = (username, access_token) => (dispatch) => {
    axios.get(`${backendUrl}/api/validate`, authConfig(access_token))
        .then(() => dispatch(loginSuccessful(username, access_token)))
        .catch(() => dispatch(logout()));
};

const messagesSent = () => ({type: 'MESSAGES_SENT'});

export const sendMessages = (messages) => (dispatch, getState) => {
    dispatch(messageSendingStart());
    axios.post(`${backendUrl}/api/queue`, {messages}, authConfig(getState().login.token))
        .then(({data}) => {
            dispatch(showNotification('success', `Pomyślnie zakolejkowano ${data.sentCount} wiadomości`));
            dispatch(messagesSent());
            dispatch(messageSendingFinish());
        })
        .catch((err) => {
            dispatch(showNotification('danger', 'Błąd przy wysyłaniu wiadomości'));
            dispatch(messageSendingFinish());
            console.log(err)
        });
};
