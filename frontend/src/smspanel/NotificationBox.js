import React from 'react';
import { Alert } from 'react-bootstrap';

const NotificationBox = ({text, bsClass, onHide}) => {
    return text === '' ? null : (
            <Alert bsStyle={bsClass} onDismiss={onHide}>
                <h4>{text}</h4>
            </Alert>
        );
};


export default NotificationBox;
