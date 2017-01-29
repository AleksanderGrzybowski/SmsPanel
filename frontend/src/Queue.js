import React from 'react';
import { ReactInterval } from 'react-interval';
import { Col, Table, Badge } from 'react-bootstrap';

const Queue = ({queue, fetchQueue}) => {
    const rows = queue.map(element => (
        <tr key={element.id}>
            <td>{element.dateSent}</td>
            <td>{element.contact.firstName} {element.contact.lastName}</td>
            <td>{element.content}</td>
            <td>{element.status}</td>
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
            <ReactInterval callback={fetchQueue} enabled={true} timeout={2000}/>
        </Col>
    )
};

export default Queue;
