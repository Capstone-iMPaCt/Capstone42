import React from 'react'
import {NavLink} from 'react-router-dom'

const SignInLinks = () => {
    return (
       <ul className="right">
           <li><NavLink to ="/newcourse">New Course</NavLink></li>
           <li><NavLink to ="/">Log Out</NavLink></li>
           <li><NavLink className = "btn btn-floating pink lighten-1" to ="/">SS</NavLink></li>
       </ul>
    )
};

export default SignInLinks;