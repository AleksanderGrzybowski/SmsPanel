import React, { Component } from 'react';
import { Col, Table, Badge } from 'react-bootstrap';
import NewMessageInput from './NewMessageInput';

export default class ContactManager extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedContactIds: [],
            selectedGroups: []
        };
    }

    getAllGroups() {
        const allGroupsRepeating = this.props.contacts.map(contact => contact.groups.split(','))
            .reduce((left, right) => [...left, ...right], []);

        return [...(new Set(allGroupsRepeating))].sort();
    }

    isGroupSelected(group) {
        return this.state.selectedGroups.includes(group);
    }

    toggleSelected = (id) => {
        this.setState({
            selectedContactIds: this.state.selectedContactIds.includes(id) ? (
                    this.state.selectedContactIds.filter(i => i !== id)
                ) : (
                    this.state.selectedContactIds.concat([id])                )
        });
    };

    toggleGroup = (group) => {
        let selectedGroups = this.state.selectedGroups;
        let selectedContactIds = this.state.selectedContactIds;

        const filterByGroup = () => this.props.contacts.filter(contact => contact.groups.includes(group));

        if (this.isGroupSelected(group)) {
            selectedGroups = selectedGroups.filter(g => g !== group);

            filterByGroup().forEach(({id}) => {
                if (selectedContactIds.includes(id)) {
                    selectedContactIds = selectedContactIds.filter(cid => cid !== id)
                }
            });
        } else {
            selectedGroups.push(group);

            filterByGroup().forEach(({id}) => {
                if (!selectedContactIds.includes(id)) {
                    selectedContactIds.push(id);
                }
            });
        }

        this.setState({selectedGroups, selectedContactIds});
    };

    send = (content) => {
        const messages = this.state.selectedContactIds.map(id => ({contactId: id, content}));
        this.props.onSend(messages);

        this.setState({selectedContactIds: [], selectedGroups: []});
    };

    render() {
        const groupCell = (groups) => groups.split(',').map(group => (
            <Badge key={group} style={{marginRight: 5}}>{group}</Badge>
        ));

        const checkCell = (checked) => (
            <i
                className="fa fa-check"
                style={{cursor: 'pointer', color: checked ? '#337ab7' : 'lightgray'}}
            />
        );

        const isContactSelected = (id) => this.state.selectedContactIds.includes(id);

        const rows = this.props.contacts.map(contact => (
            <tr
                key={contact.id}
                className={isContactSelected(contact.id) && 'info'}
                onClick={() => this.toggleSelected(contact.id)}
            >
                <td className="col-md-1">{checkCell(isContactSelected(contact.id))}</td>
                <td>{contact.name}</td>
                <td>{groupCell(contact.groups)}</td>
                <td>{contact.phone}</td>
            </tr>
        ));

        const groupSelect = this.getAllGroups().map(group => (
            <Badge
                bsClass={'badge ' + (this.isGroupSelected(group) && 'alert-info')}
                key={group}
                onClick={() => this.toggleGroup(group)}
                style={{cursor: 'pointer', marginRight: 5}}
            >
                {group}
            </Badge>
        ));

        return (
            <Col md={8} mdOffset={2}>
                <Table>
                    <thead>
                    <tr>
                        <th> </th>
                        <th>Imię</th>
                        <th>Grupa</th>
                        <th>Nr telefonu</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </Table>
                <div style={{marginBottom: 10, userSelect: 'none'}}>
                    <span style={{marginRight: 10}}>Zaznacz grupowo:</span>
                    {groupSelect}
                </div>
                <NewMessageInput
                    sendingDisabled={this.state.selectedContactIds.length === 0}
                    onSend={this.send}
                    sendingInProgress={this.props.sendingInProgress}
                />
            </Col>
        );
    }
}
