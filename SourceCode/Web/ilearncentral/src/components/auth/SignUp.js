import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUp, resetAuthError} from '../../store/actions/authActions'

class SignUp extends Component {
    state = {
        email:'',
        password:'',
        username: ''
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
        this.setState({
            businessDetails: {
                ...this.props.businessDetails
            }
        })
        this.props.signUp(this.state);
    }
    render () {
        const {authError, auth} = this.props;
        if (auth.uid) return <Redirect to='/' />
        if (!businessDetails && this.state.accountType==='center') {
            return <Redirect to='/signupcenter' />
        }
        console.log(this.state);
        const title = this.capitalize(this.state.accountType);
        return (
            <div className = "container">
                <form className="forms" onSubmit={this.handleSubmit}>
                    <h5 className="grey-text text-darken-3">Sign Up</h5>
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
                        {/* <input type="password" id="confirmPassword" onChange={this.handleChange} required/> */}
                        <input id="confirmPassword" name="confirmPassword" type="password" pattern="^\S{6,}$" 
                        required/>
                    </div>
                    <div className = "input-field">
                        <label   className="input" htmlFor="email"><i className="material-icons left">email</i>Email</label>
                        <input type="email" id="email" onChange={this.handleChange} required/>
                    </div>
                    
                    <div className = "input-field col s12 m12">
                            <button className="waves-effect btn submits lighten-1 z-depth-0">Sign Up</button>
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
        signUp: (newUser) => dispatch(signUp(newUser)),
        resetAuthError: () => dispatch(resetAuthError())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);