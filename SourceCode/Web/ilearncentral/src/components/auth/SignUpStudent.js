import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUpStudent, resetAuthError} from '../../store/actions/authActions'

class SignUpStudent extends Component {
    state = {
        email:'',
        password:'',
        username: '',
        accountType:'student'
    }
    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
        document.getElementById("confirmPassword").pattern = document.getElementById("password").value;
        if (document.getElementById('error').childNodes.length !== 0)
            this.props.resetAuthError()
    }
    handleSubmit = (e) => {
        e.preventDefault();
        this.props.signUp(this.state);
    }
    render () {
        const {authError, auth} = this.props;
        if (auth.uid) return <Redirect to='/' />
        
        return (
            <div className = "container">
                <form className="forms" onSubmit={this.handleSubmit}>
                    <h5 className="grey-text text-darken-3">Student Sign Up</h5>
                    <div className="red-text center" id="error">
                        {authError ? <p>{authError}</p>: null}
                    </div>
                    <div className = "input-field">
                        <label  className="input" htmlFor="username"><i className="material-icons left">person</i>Username</label>
                        <input type="text" id="username" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <label  className="input" htmlFor="password"><i className="material-icons left">lock</i>Password</label>
                        <input type="password" id="password" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <label   className="input" htmlFor="confirmPassword"><i className="material-icons left">lock</i>Confirm Password</label>
                        <input id="confirmPassword" name="confirmPassword" type="password" pattern="^\S{6,}$" 
                        required/>
                    </div>
                    <div className = "input-field">
                        <label   className="input" htmlFor="email"><i className="material-icons left">email</i>Email</label>
                        <input type="email" id="email" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <button className="btn submits lighten-1 z-depth-0">Sign Up</button>
                    </div>
                </form>
            </div>
        )
    }

}
const mapStateToProps = (state) => {
    return {
        authError:state.auth.authError,
        auth:state.firebase.auth
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        signUpStudent: (newUser) => dispatch(signUpStudent(newUser)),
        resetAuthError: () => dispatch(resetAuthError())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignUpStudent);