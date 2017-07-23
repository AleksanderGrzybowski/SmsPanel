import React from 'react';
import ErrorAlert from './ErrorAlert';

const ErrorPage = () => (
    <ErrorAlert>
        <h1 className="text-center">Przepraszamy, wystąpił błąd.</h1>
        <p className="text-center">Proszę spróbować ponownie później.</p>
    </ErrorAlert>
);

export default ErrorPage;