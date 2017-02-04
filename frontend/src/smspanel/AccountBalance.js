import React, { Component } from 'react';
import { ReactInterval } from 'react-interval';

export default class AccountBalance extends Component {
    componentDidMount() {
        this.props.fetchBalance();
    }

    render() {
        return (
            <span>
                {this.props.currentBalance} PLN
                <ReactInterval callback={this.props.fetchBalance} enabled={true} timeout={30000}/>
            </span>
        )
    }
}
