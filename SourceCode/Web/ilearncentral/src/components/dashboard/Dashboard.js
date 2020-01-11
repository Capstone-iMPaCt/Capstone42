import React, {Component} from 'react'
import Notifications from './Notifications'
import CourseList from '../courses/CourseList'
import {connect} from 'react-redux'
import {firestoreConnect} from 'react-redux-firebase'
import {compose} from 'redux'
class Dashboard extends Component {
    render () {
        const {courses}=this.props;
        return (
            <div className="dashboard container">
                <div className="row">
                    <div className="col s12 m6">
                        <CourseList courses={courses}/>
                    </div>
                    <div className="col s12 m5 offset-m1">
                        <Notifications/>
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    console.log(state);
    return {
        courses: state.firestore.ordered.Course
    }
}

export default compose(
    connect(mapStateToProps),
    firestoreConnect([
        { collection: 'Course'}
    ])
    )(Dashboard)