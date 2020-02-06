import React from 'react'
import {NavLink} from 'react-router-dom'
import {connect} from 'react-redux'
import {signOut} from '../../store/actions/authActions'

const SignInLinks = (props) => {
    return (
       <ul className="right">
           {/* <li><NavLink to ="/newcourse">New Course</NavLink></li> */}
           <li><NavLink className = "btn btn-floating transparent lighten-1" to ="/profile">
                <i className="material-icons left">notifications</i>
                </NavLink></li>
           <li><NavLink className = "btn btn-floating transparent lighten-1" to ="/profile">
                <i className="material-icons left">message</i>
                </NavLink></li>
           <li><NavLink className = "btn btn-floating pink lighten-1" to ="/profile">
               {props.username ? props.username.charAt(0).toUpperCase(): props.username}
               </NavLink></li>
           <li><a href='#' onClick={props.signOut}>Log Out</a></li>
       </ul>
    )
};
const mapStateToProps = (state) => {
    console.log(state);
    return {
        username: state.firebase.profile.Username
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        signOut: () => dispatch(signOut())
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(SignInLinks);