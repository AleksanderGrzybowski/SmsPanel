import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers, applyMiddleware } from 'redux';
import { connect, Provider } from 'react-redux';
import thunk from 'redux-thunk';
import { health, login, contacts, queue, view, account } from './reducers';
import {
    healthCheck,
    login as loginAction,
    logout,
    validateTokenAndLogIn,
    sendMessages,
    changeView,
    fetchQueue,
    hideNotification,
    fetchBalance
} from './actions';
import App from './App';
import createLogger from 'redux-logger';
import 'bootstrap/dist/css/bootstrap.css';
import 'font-awesome-webpack';


const store = createStore(
    combineReducers({health, view, login, contacts, queue, account}),
    applyMiddleware(thunk, createLogger())
);

const mapStateToProps = (state) => state;
const mapDispatchToProps = (dispatch) => ({
    onLogin: (username, password) => dispatch(loginAction(username, password)),
    onLogout: () => dispatch(logout()),
    onViewChange: (viewName) => dispatch(changeView(viewName)),
    onSend: (messages) => dispatch(sendMessages(messages)),
    fetchQueue: () => dispatch(fetchQueue()),
    hideNotification: () => dispatch(hideNotification()),
    fetchBalance: () => dispatch(fetchBalance())
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
    store.dispatch(validateTokenAndLogIn(localStorage.username, localStorage.access_token));
}
