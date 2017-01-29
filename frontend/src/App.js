import React, { Component } from 'react';
import LoginForm from './smspanel/LoginForm';
import { Grid, Row, Col } from 'react-bootstrap';
import PanelNavbar from './PanelNavbar';
import ErrorPage from './smspanel/ErrorPage';
import 'bootstrap/dist/css/bootstrap.css';
import ContactManager from './ContactManager';
import Queue from './Queue';
import NotificationBox from './NotificationBox';

export default class App extends Component {
    render() {
        if (!this.props.health.healthy) {
            return <ErrorPage/>
        }

        const loginView = (
            <Grid>
                <Row>
                    <Col md={6} mdOffset={3} xs={12}>
                        <h1 className="text-center">
                            <i className="fa fa-comments"/> SMS Panel
                        </h1>
                        <LoginForm
                            onLogin={this.props.onLogin}
                            loginError={this.props.login.loginError}
                        />
                    </Col>
                </Row>
            </Grid>
        );

        const navbar = (
            <PanelNavbar
                username={this.props.login.username}
                logout={this.props.onLogout}
                onViewChange={this.props.onViewChange}
            />
        );

        const mainView = (
            <Grid fluid>
                {navbar}
                <Row>
                    <ContactManager
                        contacts={this.props.contacts}
                        onSend={this.props.onSend}
                        sendingInProgress={this.props.view.sendingInProgress}
                    />
                </Row>
                <Row style={{marginTop: 20}}>
                    <Col md={8} mdOffset={2}>
                        <NotificationBox
                            text={this.props.view.notification}
                            bsClass={this.props.view.bsClass}
                            onHide={this.props.hideNotification}
                        />
                    </Col>
                </Row>
            </Grid>
        );

        const queueView = (
            <Grid fluid>
                {navbar}
                <Row>
                    <Queue
                        queue={this.props.queue}
                        fetchQueue={this.props.fetchQueue}
                    />
                </Row>
            </Grid>
        );

        switch (this.props.view.currentView) {
            case 'login':
                return loginView;
            case 'main':
                return mainView;
            case 'queue':
                return queueView;
            default:
                return <div>Not yet implemented</div>;
        }
    }
}

