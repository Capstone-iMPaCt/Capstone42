export const signIn = (credentials) => {
    return (dispatch, getState, {getFirebase}) => {
        const firebase = getFirebase();
        firebase.auth().signInWithEmailAndPassword(
            credentials.username+"@mailinator.com",
            credentials.password
        ).then(() => {  
            
            dispatch({type: 'LOGIN_SUCCESS'});
        }).catch((err) => {
            dispatch({type: 'LOGIN_ERROR', err});
        });
    }
}

export const signOut = () => {
    return (dispatch, getState, {getFirebase}) => {
        const firebase = getFirebase();
        firebase.auth().signOut()
        .then (() => {
            dispatch({type:'SIGNOUT_SUCCESS'});
        });
    }
}

export const signUp = (newUser) => {
    return (dispatch, getState, {getFirebase, getFirestore}) => {
        const firebase = getFirebase();
        const firestore = getFirestore();
        firebase.auth().createUserWithEmailAndPassword(
            newUser.username+"@mailinator.com",
            newUser.password
        ).then ((resp) => {
            return firestore.collection('User').doc(resp.user.uid).set({
                AccountStatus: "active",
                AccountType: "student",
                Username: newUser.username,
                Email: newUser.email
            })
        }).then(()=> {
            dispatch({type:'SIGNUP_SUCCESS'});
        }).catch(err=> {
            dispatch({type:'SIGNUP_ERROR', err});
        });
    }
}