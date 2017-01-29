import React from 'react';
import { Navbar, NavItem, Nav } from 'react-bootstrap';

const PanelNavbar = ({username, logout, onViewChange}) => {
    return (
        <Navbar>
            <Navbar.Header>
                <Navbar.Brand>
                    {username}
                </Navbar.Brand>
            </Navbar.Header>
            <Nav>
                <NavItem onClick={() => onViewChange('main')}>Kontakty</NavItem>
                <NavItem onClick={() => onViewChange('queue')}>Historia</NavItem>
            </Nav>
            <Nav pullRight>
                <NavItem onClick={logout} style={{marginRight: 40}}>
                    Logout <i className="fa fa-sign-out"/>
                </NavItem>
            </Nav>
        </Navbar>
    );
};

export default PanelNavbar;
