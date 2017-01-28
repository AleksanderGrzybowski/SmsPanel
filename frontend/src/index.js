import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers, applyMiddleware } from 'redux';
import { connect, Provider } from 'react-redux';
import thunk from 'redux-thunk';
import { health, login } from './reducers';
import {
    healthCheck,
    login as loginAction,
    logout,
    validateToken,
} from './actions';
import App from './App';
import createLogger from 'redux-logger';
import 'font-awesome-webpack';


const store = createStore(
    combineReducers({health, login}),
    applyMiddleware(thunk, createLogger())
);

const mapStateToProps = (state) => state;
const mapDispatchToProps = (dispatch) => ({
    onLogin: (username, password) => dispatch(loginAction(username, password)),
    onLogout: () => dispatch(logout()),
});


const LiveApp = connect(mapStateToProps, mapDispatchToProps)(App);

ReactDOM.render(
    <Provider store={store}>
        <LiveApp/>
    </Provider>,
    document.getElementById('root')
);

store.dispatch(healthCheck());

if (localStorage.username) {
    store.dispatch(validateToken(localStorage.username, localStorage.access_token));
}
