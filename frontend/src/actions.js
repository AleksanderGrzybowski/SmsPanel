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

export const fetchContacts = () => (dispatch, getState) => {
    axios.get(`${backendUrl}/api/contacts`, authConfig(getState().login.token))
        .then(({data}) => dispatch(loadContacts(data)))
        .catch((err) => console.log(err));
};

export const loginSuccessful = (username, token) => (dispatch) => {
    dispatch({type: 'LOGIN_SUCCESSFULL', username, token});
    dispatch(changeView('main'));
    dispatch(fetchContacts());
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
    console.log(messages);
    axios.post(`${backendUrl}/api/queue`, {messages}, authConfig(getState().login.token))
        .then(({data}) => dispatch(messagesSent()))
        .catch((err) => console.log(err));
};
