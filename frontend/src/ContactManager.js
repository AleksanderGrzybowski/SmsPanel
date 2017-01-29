import React, { Component } from 'react';
import { Col, Table, Badge } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';

export default class ContactManager extends Component {
    render() {
        const groupCell = (groupName) => groupName.split(',').map(group => (
            <Badge key={group}>{group}</Badge>
        ));
        
        const rows = this.props.contacts.map(contact => (
            <tr key={contact.id}>
                <td>{contact.firstName}</td>
                <td>{contact.lastName}</td>
                <td>{groupCell(contact.groupName)}</td>
                <td>{contact.phone}</td>
            </tr>
        ));

        return (
            <Col md={8} mdOffset={2}>
                <Table>
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
            </Col>
        );
    }
}
