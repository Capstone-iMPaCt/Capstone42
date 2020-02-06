import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUp, resetAuthError} from '../../store/actions/authActions'

import Input from '../forms/Input';  
import Select from '../forms/Select';

class Profile extends Component {

}
const mapStateToProps = (state) => {
    console.log(state);
    return {
        // authError:state.auth.authError,
        // auth:state.firebase.auth,
        // businessDetails:state.auth.businessDetails
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        // signUp: (newUser) => dispatch(signUp(newUser)),
        // resetAuthError: () => dispatch(resetAuthError())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Profile);