const initState = {
    authError: null
}
const authReducer = (state = initState, action) => {
    switch(action.type) {
        case "LOGIN_ERROR" :
            console.log('login error');
            return {
                ...state,
                authError: 'Login failed'
            };
        case "LOGIN_SUCCESS":
            console.log('login success');
            return {
                ...state,
                authError: null
            };
        case "SIGNOUT_SUCCESS":
            console.log('signout success');
            return state;
        case "SIGNUP_SUCCESS":
            console.log('signup success');
            return {
                ...state,
                authError: null
            }
        case "SIGNUP_ERROR":
            console.log('signup error')
            return {
                ...state,
                authError: action.err.message
            }
        case "PROFILE_SIGNUP_SUCCESS":
            console.log('signup success');
            return {
                ...state,
                authError: null
            }
        case "PROFILE_SIGNUP_ERROR":
            console.log('signup error')
            return {
                ...state,
                authError: action.err.message
            }
        case "AUTH_ERROR_RESET":
            console.log('reset auth error')
            return {
                ...state,
                authError: null
            }
        case "SAVE_BUSINESS_DETAILS":
            console.log('save business details')
            return {
                ...state,
                businessDetails: {
                    ...action.business
                }
            }
        case "RETRIEVED_PROFILE":
            console.log('retrieve full profile')
            return {
                ...state,
                profile: {
                    ...action.profile
                }
            }
        default:
            return state;
    }
} 

export default authReducer