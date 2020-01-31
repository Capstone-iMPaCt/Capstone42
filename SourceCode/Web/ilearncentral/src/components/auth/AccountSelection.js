import React, {Component} from 'react'
import {Link} from 'react-router-dom'

class AccountSelection extends Component {
    render () {
        return (
            <div className = "container">
                <div className="row">
                    <Link to ="/signupcenter" className = "largeButton">
                        <div className="card buttonText col s12 m3 small">
                            <div className="card-content">
                                <span className="card-title">Create a <b><u>Learning Center</u></b> account</span>
                                <p>Click here to create your establishment's administrative account.</p>
                            </div>
                        </div>
                    </Link>
                    <div className="col s12 m1"></div>
                    <Link to ="/signup/educator" className = "largeButton">
                        <div className="card buttonText col s12 m3 small">
                            <div className="card-content">
                                <span className="card-title">Create an <b><u>Educator</u></b> account</span>
                                <p>Click here to create an account for job-seeking & employed educators.</p>
                            </div>
                        </div>
                    </Link>
                    <div className="col s12 m1"></div>
                    <Link to ="/signup/student"className = "largeButton">
                        <div className="card buttonText col s12 m3 small">
                            <div className="card-content">
                                <span className="card-title">Create a <b><u>Student</u></b> account</span>
                                <p>Click here to create a generic school account for students.</p>
                            </div>
                        </div>
                    </Link>
                </div>
            </div>
        )
    }
}

export default AccountSelection;
