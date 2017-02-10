import React, { Component } from 'react';
import { Button, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';

export default class NewMessageInput extends Component {
    constructor(props) {
        super(props);

        this.state = {
            text: ''
        }
    }

    handleChange = (e) => {
        this.setState({text: e.target.value})
    };


    render() {
        const charsLeftMessage = (this.state.text.length <= 160) ? (
                <span>(pozostało: {160 - this.state.text.length})</span>
            ) : (
                <span style={{color: 'red'}}>(przekroczono: {this.state.text.length - 160})</span>
            );

        const buttonContent = this.props.sendingInProgress ? (
                <i className="fa fa-spin fa-spinner"/>
            ) : ( 'Wyślij!' );

        const formDisabled = this.state.text.length === 0
            || this.state.text.length > 160
            || this.props.sendingInProgress
            || this.props.sendingDisabled;

        return (
            <form>
                <FormGroup>
                    <ControlLabel>Treść wiadomości {charsLeftMessage}</ControlLabel>
                    <FormControl
                        componentClass="textarea"
                        onChange={this.handleChange}
                        value={this.state.text}
                    />
                </FormGroup>
                <Button
                    block
                    bsStyle="primary"
                    bsSize="large"
                    disabled={formDisabled}
                    onClick={() => this.props.onSend(this.state.text)}
                >
                    {buttonContent}
                </Button>
            </form>
        )
    }
}
