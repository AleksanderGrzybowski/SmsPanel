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
        if (e.target.value.length > 160) {
            return;
        }
        this.setState({text: e.target.value})
    };


    render() {
        const style = (this.state.text.length > 140) ? {color: 'red'} : null;
        const charsLeftMessage = (
            <span style={style}>(pozostało: {160 - this.state.text.length})</span>
        );

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
                <Button block bsStyle="primary" onClick={() => this.props.onSend(this.state.text)}>
                    Wyślij
                </Button>
            </form>
        )
    }
}
