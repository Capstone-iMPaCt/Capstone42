import React from 'react'

const CourseSummary = ({course}) => {
    return (
        <div className="card z-depth-0 course-summary">
            <div className="card-content grey-text text-darken-3">
    <span className="card-title">{course.CourseName}</span>
    <p>{course.CourseDescription}</p>
    <p className="grey-text">{course.CourseType}</p>
            </div>
        </div>
    )
}
export default CourseSummary