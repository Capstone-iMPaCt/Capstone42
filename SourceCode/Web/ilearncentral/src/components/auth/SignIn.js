import React, {Component} from 'react'
import {connect} from 'react-redux'
import {signIn, resetAuthError} from '../../store/actions/authActions'
import {Redirect} from 'react-router-dom'

class SignIn extends Component {
    state = {
        username:'',
        password:''
    }
    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value,
        });
        if (document.getElementById('error').childNodes.length !== 0)
            this.props.resetAuthError()
    }
    handleSubmit = (e) => {
        e.preventDefault();
        this.props.signIn(this.state)
    }
    render () {
        const {authError, auth} = this.props;
        if (auth.uid) return <Redirect to='/' />
        return (
            <div className = "container">
                <form className="authForms" onSubmit={this.handleSubmit}>
                    <div className="logo"></div>
                    <div className="red-text center" id="error">
                            {authError ? <p>{authError}</p>: null}
                    </div>
                    <div className = "input-field">
                        <label  className="input" htmlFor="username"><i className="material-icons left">person</i>Username</label>
                        <input type="text" id="username" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field">
                        <label className="input" htmlFor="password"><i className="material-icons left">lock</i>Password</label>
                        <input type="password" id="password" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field ">
                        <button className="btn submits lighten-1 z-depth-0">Log In</button>
                        
                    </div>
                </form>
            </div>
        )
    }

}
const mapStateToProps = (state) => {
    console.log(state);
    return {
        authError:state.auth.authError,
        auth:state.firebase.auth
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        signIn: (creds) => dispatch(signIn(creds)),
        resetAuthError: () =>dispatch(resetAuthError())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);