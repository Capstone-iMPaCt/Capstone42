import React from 'react'
import CourseSummary from './CourseSummary'
import {Link} from 'react-router-dom'

const CourseList = ({courses}) => {
    return (
        <div className="course-list section">
            {courses && courses.map(course => {
                return (
                    <Link to={'/course/' + course.id}>
                        <CourseSummary course={course} key={course.id}/>
                    </Link>
                )
            })}
        </div>
    )
}
export default CourseList