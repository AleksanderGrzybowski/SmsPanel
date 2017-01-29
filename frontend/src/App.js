import React, { Component } from 'react';
import LoginForm from './smspanel/LoginForm';
import { Grid, Row, Col } from 'react-bootstrap';
import PanelNavbar from './PanelNavbar';
import ErrorPage from './smspanel/ErrorPage';
import 'bootstrap/dist/css/bootstrap.css';
import ContactManager from './ContactManager';

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

        const mainView = (
            <Grid fluid>
                <PanelNavbar
                    username={this.props.login.username}
                    logout={this.props.onLogout}
                />
                <Row>
                    <ContactManager contacts={this.props.contacts}/>
                </Row>
            </Grid>
        );

        switch (this.props.view.currentView) {
            case 'login':
                return loginView;
            case 'main':
                return mainView;
            default:
                return <div>Not yet implemented</div>;
        }
    }
}

