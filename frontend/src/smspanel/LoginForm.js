import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { FormGroup, FormControl, Form, Button } from 'react-bootstrap';
import ErrorAlert from './ErrorAlert';

export default class LoginForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: '',
            password: '',
            repeatPassword: ''
        };
    }

    usernameChange = (e) => this.setState({username: e.target.value});
    passwordChange = (e) => this.setState({password: e.target.value});
    
    isFormValid = () => {
        const allNotEmpty = (values) => values.every(e => e.length > 0); // FP rocks!

        return allNotEmpty([this.state.username, this.state.password]);
    };

    login = () => this.props.onLogin(this.state.username, this.state.password);
    submitForm = () => {
        if (!this.isFormValid()) return;
        
        this.login();
    };

    onKeypress = (e) => {
        if (e.key === 'Enter') {
            this.submitForm();
        }
    };

    componentDidMount() {
        ReactDOM.findDOMNode(this.refs.loginInput).focus();
    }

    render() {
        const buttonDisabled = !this.isFormValid();

        const loginErrorMessage = (
            <ErrorAlert>
                Invalid username or password, please try again.
            </ErrorAlert>
        );

        return (
            <Form horizontal onKeyPress={this.onKeypress}>
                <FormGroup>
                    <FormControl
                        type="text"
                        ref="loginInput"
                        value={this.state.username}
                        placeholder={'Username'}
                        onChange={this.usernameChange}
                    />
                </FormGroup>

                <FormGroup>
                    <FormControl
                        type="password"
                        value={this.state.password}
                        placeholder={'Password'}
                        onChange={this.passwordChange}
                    />
                </FormGroup>

                <FormGroup>
                    <Button
                        block bsSize="large" bsStyle="primary"
                        onClick={this.submitForm}
                        disabled={buttonDisabled}
                    >
                        Login
                    </Button>
                </FormGroup>
                {this.props.loginError ? loginErrorMessage : null}
            </Form>
        )
    }
}
