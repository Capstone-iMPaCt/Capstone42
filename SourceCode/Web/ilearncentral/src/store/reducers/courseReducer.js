const initState = {
    courses: [
        {id: '1', CourseName: 'Course 1', CourseDescription: "hello1", CourseType: "Tutorial"},
        {id: '2', CourseName: 'Course 2', CourseDescription: "hello2", CourseType: "Dance"},
        {id: '3', CourseName: 'Course 3', CourseDescription: "hello3", CourseType: "Daycare"}
    ]
}
const courseReducer = (state = initState, action) => {
    switch(action.type) {
        case "CREATE_COURSE": 
            console.log("Created course", action.course)
            return state;
        case "CREATE_COURSE_ERROR":
            console.log("Create course error", action.err)
            return state;
        default:
            return state;
    }
} 

export default courseReducer