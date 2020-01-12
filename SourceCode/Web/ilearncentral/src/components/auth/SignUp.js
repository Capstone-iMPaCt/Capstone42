import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUp} from '../../store/actions/authActions'

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
                    <h5 className="grey-text text-darken-3">Sign Up</h5>
                    <div className = "input-field">
                        <label  className="input" htmlFor="username">Username</label>
                        <input type="text" id="username" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <label  className="input" htmlFor="password">Password</label>
                        <input type="password" id="password" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <label   className="input" htmlFor="confirmPassword">Confirm Password</label>
                        {/* <input type="password" id="confirmPassword" onChange={this.handleChange} required/> */}
                        <input id="confirmPassword" name="confirmPassword" type="password" pattern="^\S{6,}$" 
                        required/>
                    </div>
                    <div className = "input-field">
                        <label   className="input" htmlFor="email">Email</label>
                        <input type="email" id="email" onChange={this.handleChange} required/>
                    </div>
                    <div className = "input-field">
                        <button className="btn submits lighten-1 z-depth-0">Sign Up</button>
                    </div>
                    <div className="red-text center">
                        {authError ? <p>{authError}</p>: null}
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
        signUp: (newUser) => dispatch(signUp(newUser))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);