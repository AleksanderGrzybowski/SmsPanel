import React, { Component } from 'react';
import { ReactInterval } from 'react-interval';
import { Col, Table, Label } from 'react-bootstrap';

export default class Queue extends Component {

    componentDidMount() {
        this.props.fetchQueue();
    }

    render() {
        const statusCell = (status) => {
            const mapping = {
                SENT: {bsClass: 'success', text: 'Wysłana'},
                PENDING: {bsClass: 'default', text: 'Oczekuje'},
                FAILED: {bsClass: 'danger', text: 'Niepowodzenie'}
            };
            return <Label bsStyle={mapping[status].bsClass}>{mapping[status].text}</Label>
        };


        const rows = this.props.queue.map(element => (
            <tr key={element.id}>
                <td>{element.dateSent}</td>
                <td>{element.contact.firstName} {element.contact.lastName}</td>
                <td>{element.content}</td>
                <td>{statusCell(element.status)}</td>
            </tr>
        ));

        return (
            <Col md={8} mdOffset={2}>
                <Table>
                    <thead>
                    <tr>
                        <th>Data wysłania</th>
                        <th>Imię i nazwisko</th>
                        <th>Treść</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </Table>
                <ReactInterval callback={this.props.fetchQueue} enabled={true} timeout={2000}/>
            </Col>
        )

    }
}
