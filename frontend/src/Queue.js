import React, { Component } from 'react';
import { ReactInterval } from 'react-interval';
import { Col, Table, Label } from 'react-bootstrap';

export default class Queue extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentPage: 0,
            maximumOnPage: 10
        }
    }

    componentDidMount() {
        this.props.fetchQueue();
    }

    slicePage(queue) {
        const fromIndexInclusive = this.state.currentPage * this.state.maximumOnPage;
        const toIndexExclusive = Math.min(fromIndexInclusive + this.state.maximumOnPage, queue.length);
        return queue.slice(fromIndexInclusive, toIndexExclusive);
    }

    switchPage(amount) {
        this.setState({currentPage: this.state.currentPage + amount});
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

        const rows = this.slicePage(this.props.queue).map(element => (
            <tr key={element.id}>
                <td>{element.dateSent}</td>
                <td>{element.contact.firstName} {element.contact.lastName}</td>
                <td>{element.content}</td>
                <td>{statusCell(element.status)}</td>
            </tr>
        ));

        const arrowStyle = {cursor: 'pointer'};

        const minPossiblePageNumber = 0;
        const maxPossiblePageNumber = Math.floor((this.props.queue.length - 1) / this.state.maximumOnPage);

        const leftArrow = (this.state.currentPage > minPossiblePageNumber) ? (
                <i
                    style={arrowStyle}
                    className="fa fa-arrow-left"
                    onClick={() => this.switchPage(-1)}
                />
            ) : null;

        const rightArrow = (this.state.currentPage < maxPossiblePageNumber) ? (
                <i
                    style={{marginLeft: 10, ...arrowStyle}}
                    className="fa fa-arrow-right"
                    onClick={() => this.switchPage(1)}
                />
            ) : null;

        const navigation = (
            <div className="pull-right" style={{fontSize: 20}}>
                {leftArrow}
                {rightArrow}
            </div>
        );

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
                {navigation}
                <ReactInterval callback={this.props.fetchQueue} enabled={true} timeout={2000}/>
            </Col>
        )
    }
}
