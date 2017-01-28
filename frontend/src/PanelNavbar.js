import React from 'react';
import { Navbar, NavItem, Nav } from 'react-bootstrap';

const PanelNavbar = ({username, logout}) => (
    <Navbar>
        <Navbar.Header>
            <Navbar.Brand>
                {username}
            </Navbar.Brand>
        </Navbar.Header>
        <Nav pullRight>
            <NavItem onClick={logout} style={{marginRight: 40}}>
                Logout <i className="fa fa-sign-out"/>
            </NavItem>
        </Nav>
    </Navbar>
);

export default PanelNavbar;
