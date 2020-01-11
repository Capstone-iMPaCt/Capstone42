import React from 'react'
import CourseSummary from './CourseSummary'
import {Link} from 'react-router-dom'

const CourseList = ({courses}) => {
    return (
        <div className="course-list section">
            {courses && courses.map(course => {
                return (
                    <Link to={'/course/' + course.id} key={course.id}>
                        <CourseSummary course={course} />
                    </Link>
                )
            })}
        </div>
    )
}
export default CourseList