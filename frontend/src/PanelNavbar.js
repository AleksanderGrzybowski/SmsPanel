import React from 'react';
import { Navbar, NavItem, Nav } from 'react-bootstrap';

const PanelNavbar = ({currentView, logout, onViewChange}) => {
    return (
        <Navbar>
            <Nav>
                <NavItem active={currentView === 'main'} onClick={() => onViewChange('main')}>Kontakty</NavItem>
                <NavItem active={currentView === 'queue'} onClick={() => onViewChange('queue')}>Kolejka</NavItem>
            </Nav>
            <Nav pullRight>
                <NavItem onClick={logout} style={{marginRight: 40}}>
                    Wyloguj<i className="fa fa-sign-out"/>
                </NavItem>
            </Nav>
        </Navbar>
    );
};

export default PanelNavbar;
