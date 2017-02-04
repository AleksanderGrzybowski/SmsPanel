import React from 'react';
import { Navbar, NavItem, Nav } from 'react-bootstrap';
import AccountBalance from './AccountBalance';

const PanelNavbar = ({currentView, logout, onViewChange, fetchBalance, currentBalance}) => {
    return (
        <Navbar>
            <Navbar.Header>
                <Navbar.Brand>
                    <a href="#" style={{cursor: 'default'}}> {/* forgive me master */}
                        <AccountBalance
                            fetchBalance={fetchBalance}
                            currentBalance={currentBalance}
                        />
                    </a>
                </Navbar.Brand>
            </Navbar.Header>
            <Nav>
                <NavItem active={currentView === 'main'} onClick={() => onViewChange('main')}>Kontakty</NavItem>
                <NavItem active={currentView === 'queue'} onClick={() => onViewChange('queue')}>Kolejka</NavItem>
            </Nav>
            <Nav pullRight>
                <NavItem onClick={logout}>
                    Wyloguj<i className="fa fa-sign-out"/>
                </NavItem>
            </Nav>
        </Navbar>
    );
};

export default PanelNavbar;
