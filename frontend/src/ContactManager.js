import React, { Component } from 'react';
import { Col, Table, Badge } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import NewMessageInput from './NewMessageInput';

export default class ContactManager extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedContactIds: []
        }
    }

    toggleSelected = (id) => {
        if (this.state.selectedContactIds.includes(id)) {
            this.setState({
                selectedContactIds: this.state.selectedContactIds.filter(i => i !== id)
            });
        } else {
            this.setState({
                selectedContactIds: this.state.selectedContactIds.concat([id])
            });
        }
    };

    send = (content) => {
        const messages = this.state.selectedContactIds.map(id => ({contactId: id, content}));
        this.props.onSend(messages);
        this.setState({selectedContactIds: []});
    };

    render() {
        const groupCell = (groupName) => groupName.split(',').map(group => (
            <Badge key={group}>{group}</Badge>
        ));

        const isContactSelected = (id) => this.state.selectedContactIds.includes(id);

        const rows = this.props.contacts.map(contact => (
            <tr
                key={contact.id}
                className={isContactSelected(contact.id) && 'success'}
                onClick={() => this.toggleSelected(contact.id)}
            >
                <td>{contact.firstName}</td>
                <td>{contact.lastName}</td>
                <td>{groupCell(contact.groupName)}</td>
                <td>{contact.phone}</td>
            </tr>
        ));

        return (
            <Col md={8} mdOffset={2}>
                <Table style={{userSelect: 'none', cursor: 'default'}}>
                    <thead>
                    <tr>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Grupa</th>
                        <th>Nr telefonu</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </Table>
                <NewMessageInput
                    sendingDisabled={this.state.selectedContactIds.length === 0}
                    onSend={this.send}
                    sendingInProgress={this.props.sendingInProgress}
                />
            </Col>
        );
    }
}
