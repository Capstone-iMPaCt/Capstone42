export const createCourse = (course) => {
    return (dispatch, getState, {getFirebase, getFirestore}) => {
        const firestore = getFirestore();
        firestore.collection('Course').add({
            ...course,
            CenterID: 'Dur90Jf9wUFEZrb7LwMz'
        }).then(() => {
            dispatch({type:"CREATE_COURSE", course});
        }).catch((err) => {
            dispatch({type:"CREATE_COURSE_ERROR", err});
        })
    }
};