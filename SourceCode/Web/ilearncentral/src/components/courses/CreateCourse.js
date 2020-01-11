import React, {Component} from 'react'
import {connect} from 'react-redux'
import {createCourse} from '../../store/actions/courseActions'

class CreateCourse extends Component {
    state = {
        CourseName:'',
        CourseDescription:'',
        CourseType:''
    }
    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
    }
    handleSubmit = (e) => {
        e.preventDefault();
        //console.log(this.state)
        this.props.createCourse(this.state)
    }
    render () {
        return (
            <div className = "container">
                <form className="white" onSubmit={this.handleSubmit}>
                    <h5 className="grey-text text-darken-3">Create New Course</h5>
                    <div className = "input-field">
                        <label htmlFor="CourseName">Course Name</label>
                        <input type="text" id="CourseName" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field">
                        <label htmlFor="CourseDescription">Course Description</label>
                        <textarea id="CourseDescription" onChange={this.handleChange}></textarea>
                    </div>
                    <div className = "input-field">
                        <label htmlFor="CourseType">Course Type</label>
                        <input type="text" id="CourseType" onChange={this.handleChange}/>
                    </div>
                    <div className = "input-field">
                        <button className="btn pink lighten-1 z-depth-0">Create</button>
                    </div>
                </form>
            </div>
        )
    }

}

const mapDispatchToProps = (dispatch) => {
    return {
        createCourse: (course) => dispatch(createCourse(course))
    }
}

export default connect(null, mapDispatchToProps)(CreateCourse);