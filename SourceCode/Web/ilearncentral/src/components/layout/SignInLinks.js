import React from 'react'
import {NavLink} from 'react-router-dom'
import {connect} from 'react-redux'
import {signOut} from '../../store/actions/authActions'

const SignInLinks = (props) => {
    return (
       <ul className="right">
           <li><NavLink to ="/newcourse">New Course</NavLink></li>
           <li><a href='#' onClick={props.signOut}>Log Out</a></li>
           <li><NavLink className = "btn btn-floating pink lighten-1" to ="/">SS</NavLink></li>
       </ul>
    )
};

const mapDispatchToProps = (dispatch) => {
    return {
        signOut: () => dispatch(signOut())
    }
}
export default connect(null, mapDispatchToProps)(SignInLinks);