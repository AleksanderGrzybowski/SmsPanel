import React from 'react';
import { Alert } from 'react-bootstrap';

const ErrorPage = () => (
    <Alert bsStyle="warning">
        <h1 className="text-center">Oops, something went wrong</h1>
        <p className="text-center">Please try again later.</p>
    </Alert>
);

export default ErrorPage;