import React from 'react'
import {connect} from 'react-redux'
import {firestoreConnect} from 'react-redux-firebase'
import {compose} from 'redux'
import {Redirect} from 'react-router-dom'

const CourseDetails = (props) => {
  const {course, auth} = props;
  if (!auth.uid) return <Redirect to='/signin' />

  if (course) {
   return (
      <div className = "container section course-details">
        <div className="card-content grey-text text-darken-3">
        <span className="card-title">{course.CourseName}</span>
            <p>{course.CourseDescription}</p>
            <p className="grey-text">{course.CourseType}</p>
        </div>
      </div>
    )
  } else {
    return (
      <div className = "container center">
        <p>Loading Course...</p>
      </div>
    )
  }
}

const mapStateToProps = (state, aProps) => {
  const id = aProps.match.params.id;
  const courses = state.firestore.data.Course;
  const course = courses ? courses[id] : null;
  return {
      course: course,
      auth:state.firebase.auth
  }
}

export default compose(
  connect(mapStateToProps),
  firestoreConnect([
      { collection: 'Course'}
  ])
)(CourseDetails)
