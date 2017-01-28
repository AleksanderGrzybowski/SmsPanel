import React from 'react';
import { Alert } from 'react-bootstrap';

const ErrorAlert = ({children}) => (
    <Alert bsStyle="warning">
        {children}
    </Alert>
);

export default ErrorAlert;