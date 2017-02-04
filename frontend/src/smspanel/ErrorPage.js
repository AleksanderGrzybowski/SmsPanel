import React from 'react';
import { ErrorAlert } from './ErrorAlert';

const ErrorPage = () => (
    <ErrorAlert>
        <h1 className="text-center">Oops, something went wrong</h1>
        <p className="text-center">Please try again later.</p>
    </ErrorAlert>
);

export default ErrorPage;