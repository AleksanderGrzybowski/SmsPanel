import axios from 'axios';
import backendUrl from './backendUrl';

const backendNotHealthy = () => ({type: 'BACKEND_HEALTH_CHECK_FAIL'});
const backendHealthy = () => ({type: 'BACKEND_HEALTH_CHECK_SUCCESS'});
export const loginSuccessful = (username, token) => ({type: 'LOGIN_SUCCESSFULL', username, token});
const loginError = () => ({type: 'LOGIN_ERROR'});

const authConfig = (token) => ({
    headers: {
        'Authorization': `Bearer ${token}`
    }
});

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
};

export const validateToken = (username, access_token) => (dispatch) => {
    axios.get(`${backendUrl}/api/validate`, authConfig(access_token))
        .then(() => dispatch(loginSuccessful(username, access_token)))
        .catch(() => dispatch(logout()));
};
