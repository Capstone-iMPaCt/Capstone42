import React, {Component} from 'react'
import {connect} from 'react-redux'
import {signIn} from '../../store/actions/authActions'
import {Redirect} from 'react-router-dom'

class SignIn extends Component {
    state = {
        username:'',
        password:''
    }
    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        });
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
                <form className="forms" onSubmit={this.handleSubmit}>
                    <div className="logo"></div>
                    <div className = "input-field">
                        <label  className="input" htmlFor="username">Username</label>
                        <input type="text" id="username" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field">
                        <label className="input" htmlFor="password">Password</label>
                        <input type="password" id="password" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field ">
                        <button className="btn submits lighten-1 z-depth-0">Log In</button>
                        <div className="red-text center">
                            {authError ? <p>{authError}</p>: null}
                        </div>
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
        signIn: (creds) => dispatch(signIn(creds))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);