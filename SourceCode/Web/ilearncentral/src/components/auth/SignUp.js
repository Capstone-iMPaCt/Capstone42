import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUp, resetAuthError} from '../../store/actions/authActions'

import Input from '../forms/Input';  
import Select from '../forms/Select';

class SignUp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email:'',
            password:'',
            username: '',
            accountType:props.match.params.type,
            contactNo:'',
            firstName: '',
            middleName: '',
            lastName: '',
            extension: '',
            birthday:'',
            birthPlace:'',
            gender: '',
            religion:'',
            citizenship:'',
            maritalStatus:'',
            houseNo: '',
            street: '',
            barangay: '',
            city: '',
            province: '',
            country: '',
            zipCode: '',
            businessDetails:{}
        }
        this.otherData = {
            genderOptions: ['Male', 'Female', 'Others'],
            maritalOptions: ['Single', 'Married', 'Divorced', 'Separated', 'Widowed']
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.capitalize = this.capitalize.bind(this);
    }
    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
        if (e.target.id === 'password')
            document.getElementById("confirmPassword").pattern = document.getElementById("password").value;
        if (document.getElementById('error').childNodes.length !== 0)
            this.props.resetAuthError()
    }
    handleSubmit = (e) => {
        e.preventDefault();
        this.setState(
        {
            businessDetails: {
                ...this.props.businessDetails
            }
        },
        function() { 
            this.props.signUp(this.state);
        }
        )
    }
    capitalize = (str) => {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }
    render () {
        const {authError, auth, businessDetails} = this.props;
        if (auth.uid) return <Redirect to='/' />
        if (!businessDetails && this.state.accountType==='center') {
            return <Redirect to='/signupcenter' />
        }
        const title = this.capitalize(this.state.accountType);
        return (
            <div className = "container">
                <form onSubmit={this.handleSubmit}>

                <div className="forms2 row">
                    <h4 className="grey-text text-darken-3"> 
                            {businessDetails ? "Learning Center Administrator ": title} Sign Up</h4>
                    <div className=" col s12 m5">
                        <h5 className="grey-text text-darken-3">Account Details</h5>
                        <div className="red-text center" id="error">
                            {authError ? <p>{authError}</p>: null}
                        </div>
                        <Input type={'text'}
                            title= {'Username'} 
                            name= {'username'}
                            value={this.state.username} 
                            handleChange = {this.handleChange}
                            icon={'person'}
                            required={'required'}
                            pattern={"^\\S{3,}$"}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'password'}
                            title= {'Password'} 
                            name= {'password'}
                            value={this.state.password} 
                            handleChange = {this.handleChange}
                            icon={'lock'}
                            required={'required'}
                            pattern={"^\\S{6,}$"}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'password'}
                            title= {'Confirm Password'} 
                            name= {'confirmPassword'}
                            handleChange = {this.handleChange}
                            icon={'lock'}
                            required={'required'}
                            pattern={"^\\S{6,}$"}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'email'}
                            title= {'Email'} 
                            name= {'email'}
                            value={this.state.email} 
                            handleChange = {this.handleChange}
                            icon={'email'}
                            required={'required'}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'tel'}
                            title= {'Contact No.'} 
                            name= {'contactNo'}
                            value={this.state.contactNo} 
                            handleChange = {this.handleChange}
                            icon={'phone'}
                            divClassName={'col s12 m12'}
                            /> 
                    </div>
                    <div className="col s12 m7">
                        <h5 className="grey-text text-darken-3">Basic Details</h5>
                        <Input type={'text'}
                            title= {'First Name'} 
                            name= {'firstName'}
                            value={this.state.firstName} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m7'}
                            required={'required'}
                            /> 
                        <Input type={'text'}
                            title= {'Middle Name'} 
                            name= {'middleName'}
                            value={this.state.middleName} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m5'}
                            /> 
                        <Input type={'text'}
                            title= {'Last Name'} 
                            name= {'lastName'}
                            value={this.state.lastName} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m9'}
                            required={'required'}
                            /> 
                        <Input type={'text'}
                            title= {'Extension'} 
                            name= {'extension'}
                            value={this.state.extension} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            /> 
                        <Input type={'date'}
                            title= {'Birthday'} 
                            name= {'birthday'}
                            activeLabel={'active'}
                            value={this.state.birthday} 
                            handleChange = {this.handleChange}
                            placeholder={''}
                            divClassName={'col s12 m6'}
                            /> 
                        <Input type={'text'}
                            title= {'Birth Place'} 
                            name= {'birthPlace'}
                            value={this.state.birthPlace} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            /> 
                        <Select title={'Gender'}
                            name={'gender'}
                            options = {this.otherData.genderOptions} 
                            value = {this.state.gender}
                            placeholder = {'Gender'}
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            />
                        <Select title={'Marital Status'}
                            name={'maritalStatus'}
                            options = {this.otherData.maritalOptions} 
                            value = {this.state.maritalStatus}
                            placeholder = {'Marital Status'}
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            />
                        <Input type={'text'}
                            title= {'Religion'} 
                            name= {'religion'}
                            value={this.state.religion} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            /> 
                        <Input type={'text'}
                            title= {'Citizenship'} 
                            name= {'citizenship'}
                            value={this.state.citizenship} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            /> 
                        </div>
                    
                        <div className=" col s12 m12">
                        <Input type={'text'}
                            title= {'House No.'} 
                            name= {'houseNo'}
                            value={this.state.houseNo} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m2'}
                            />
                            
                        <Input type={'text'}
                            title= {'Street Name'} 
                            name= {'street'}
                            value={this.state.street} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m4'}
                            />
                            
                        <Input type={'text'}
                            title= {'Barangay'} 
                            name= {'barangay'}
                            value={this.state.barangay} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            required={'required'}
                            />
                            
                        <Input type={'text'}
                            title= {'City / Municipality'} 
                            name= {'city'}
                            value={this.state.city} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            required={'required'}
                            />
                            
                        <Input type={'text'}
                            title= {'Province'} 
                            name= {'province'}
                            value={this.state.province} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m4'}
                            required={'required'}
                            />
                            
                        <Input type={'text'}
                            title= {'Country'} 
                            name= {'country'}
                            value={this.state.country} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m5'}
                            required={'required'}
                            />
                            
                        <Input type={'text'}
                            title= {'Zip Code'} 
                            name= {'zipCode'}
                            value={this.state.zipCode} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            required={'required'}
                            />
                    </div>
                    
                    <div className = "input-field col s12 m12">
                            <button className="waves-effect btn submits lighten-1 z-depth-0">Sign Up</button>
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
        auth:state.firebase.auth,
        businessDetails:state.auth.businessDetails
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        signUp: (newUser) => dispatch(signUp(newUser)),
        resetAuthError: () => dispatch(resetAuthError())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);