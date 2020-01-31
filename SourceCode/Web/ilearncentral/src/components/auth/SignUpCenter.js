import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'
import {signUp, resetAuthError} from '../../store/actions/authActions'

import Input from '../forms/Input';  
import Select from '../forms/Select';
import CheckBox from '../forms/CheckBox';

class SignUpCenter extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email:'',
            password:'',
            username: '',
            accountType:'center',
            contactNo:'',
            businessName:'',
            closingTime:'',
            openingTime:'',
            companyWebsite:'',
            contactEmail:'',
            serviceType:'',
            otherServiceType:'',
            operatingDays:['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
            subscriptionType:'',
            subscriptionEndDate:'',
            no: '',
            street: '',
            barangay: '',
            city: '',
            province: '',
            country: '',
            zipCode: ''
        }
        this.otherData = {
            operatingDays: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
            serviceTypes: ['Early Childhood', 'Language Studies', 'Music and Arts', 'Academic Tutorials', 'Others']
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
        if (e.target.id === 'serviceType') {
            if (e.target.value === "Others") 
                document.getElementById("otherServiceTypeDiv").hidden = false;
            else
                document.getElementById("otherServiceTypeDiv").hidden = true;
        }
        if (document.getElementById('error').childNodes.length !== 0)
            this.props.resetAuthError()
    }
    handleSubmit = (e) => {
        e.preventDefault();
        this.props.signUp(this.state);
    }
    handleCheckBox = (e) => {
        var checkbox = document.getElementById(e.target.id);
        var arr = this.state.operatingDays;
        if (checkbox.checked) {
            arr.push(checkbox.value)
        }
        else {
            var index = arr.indexOf(e.target.value)
            if (index !== -1) {
            arr.splice(index, 1);
            }
        }
        this.setState({operatingDays: arr});
    }
    capitalize = (str) => {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }
    render () {
        const {authError, auth} = this.props;
        if (auth.uid) return <Redirect to='/' />
        console.log(this.state);
        const title = this.capitalize(this.state.accountType);
        return (
            <div className = "container">
                <form onSubmit={this.handleSubmit}>

                <div className="forms2 row">
                    <h4 className="grey-text text-darken-3">{title} Sign Up</h4>
                    <div className=" col s12 m5">
                        <h5 className="grey-text text-darken-3">Admin Account Details</h5>
                        <div className="red-text center" id="error">
                            {authError ? <p>{authError}</p>: null}
                        </div>
                        <Input type={'text'}
                            title= {'Admin Username'} 
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
                            title= {'Admin Email'} 
                            name= {'email'}
                            value={this.state.email} 
                            handleChange = {this.handleChange}
                            icon={'email'}
                            required={'required'}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'email'}
                            title= {'Business Email'} 
                            name= {'email'}
                            value={this.state.email} 
                            handleChange = {this.handleChange}
                            icon={'email'}
                            required={'required'}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'tel'}
                            title= {'Business Contact No.'} 
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
                            title= {'Business Name'} 
                            name= {'businessName'}
                            value={this.state.businessName} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m12'}
                            required={'required'}
                            /> 
                        <Input type={'url'}
                            title= {'Company Website'} 
                            name= {'companyWebsite'}
                            value={this.state.companyWebsite} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m12'}
                            /> 
                        <Select title={'Service Types'}
                            name={'serviceType'}
                            options = {this.otherData.serviceTypes} 
                            value = {this.state.serviceType}
                            placeholder = {'Service Type'}
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'text'}
                            title= {'Other Service Type'} 
                            name= {'otherServiceType'}
                            value={this.state.otherServiceType} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m12'}
                            divHidden={true}
                            activeLabel={'active'}
                            divId={'otherServiceTypeDiv'}
                            required={'required'}
                            /> 
                        <CheckBox title={'Operating Days'}
                            name={'operatingDays'}
                            options = {this.otherData.operatingDays}
                            selectedOptions = {this.state.operatingDays} 
                            value = {this.state.operatingDays}
                            placeholder = {'Operating Days'}
                            handleChange = {this.handleCheckBox}
                            divClassName={'col s12 m12'}
                            />
                        <Input type={'time'}
                            title= {'Opening Time'} 
                            name= {'openingTime'}
                            activeLabel={'active'}
                            value={this.state.openingTime} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            /> 
                        <Input type={'time'}
                            title= {'Closing Time'} 
                            name= {'closingTime'}
                            activeLabel={'active'}
                            value={this.state.closingTime} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m6'}
                            /> 
                        </div>
                    
                        <div className=" col s12 m12">
                        <h6 className="grey-text text-darken-3">Business Address</h6>
                        <Input type={'text'}
                            title= {'No.'} 
                            name= {'no'}
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
                            />
                            
                        <Input type={'text'}
                            title= {'City'} 
                            name= {'city'}
                            value={this.state.city} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            />
                            
                        <Input type={'text'}
                            title= {'Province'} 
                            name= {'province'}
                            value={this.state.province} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m4'}
                            />
                            
                        <Input type={'text'}
                            title= {'Country'} 
                            name= {'country'}
                            value={this.state.country} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m5'}
                            />
                            
                        <Input type={'text'}
                            title= {'Zip Code'} 
                            name= {'zipCode'}
                            value={this.state.zipCode} 
                            handleChange = {this.handleChange}
                            divClassName={'col s12 m3'}
                            />
                    </div>
                    
                    <div className = "input-field col s12 m12">
                            <button className="btn submits lighten-1 z-depth-0">Sign Up</button>
                        </div>
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

export default connect(mapStateToProps, mapDispatchToProps)(SignUpCenter);