import React from 'react'
import {Link} from 'react-router-dom'
import SignInLinks from './SignInLinks'
import SignOutLinks from './SignOutLinks'
import {connect} from 'react-redux'

const Navbar = (props) => {
    const {auth} = props;
    //console.log(auth);
    const links = auth.uid ? <SignInLinks/> : <SignOutLinks/>;
    return (
        <nav className = "nav-wrapper header darken-3">
            <div className = "container">
                <Link to ="/" className = "brand-logo">iLearnCentral</Link>
                {links}
            </div>
        </nav>
    )
};

const mapStateToProps = (state) => {
    return {
        auth: state.firebase.auth
    }
}

export default connect(mapStateToProps)(Navbar);