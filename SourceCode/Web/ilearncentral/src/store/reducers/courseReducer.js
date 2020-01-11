const initState = {
    courses: [
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