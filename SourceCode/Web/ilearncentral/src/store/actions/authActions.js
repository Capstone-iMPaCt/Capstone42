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
                AccountType: newUser.accountType,
                Username: newUser.username,
                Email: newUser.email
            })
        }).then(()=> {
            dispatch({type:'SIGNUP_SUCCESS'});
        }).catch(err=> {
            dispatch({type:'SIGNUP_ERROR', err});
        });
        switch (newUser.accountType) {
            case "student":
                firestore.collection('Student').add({
                    Username:newUser.username,
                    Name: {
                        FirstName:newUser.firstName,
                        MiddleName:newUser.middleName,
                        LastName:newUser.lastName,
                        Extension:newUser.extension
                    },
                    Birthday: new Date(Date.parse(newUser.birthday)),
                    CenterID:'',
                    Citizenship:newUser.citizenship,
                    EnrolmentStatus:'none',
                    Gender:newUser.gender,
                    MaritalStatus:newUser.maritalStatus,
                    Religion:newUser.religion,
                    Address: [
                        {
                            HouseNo: newUser.houseNo,
                            Street: newUser.street,
                            Brangay: newUser.barangay,
                            City: newUser.city,
                            Province: newUser.province,
                            Country: newUser.country,
                            ZipCode: newUser.zipCode,
                            CurrentAddress: true
                        }
                    ],
                    ContactNo:[
                        newUser.contactNo
                    ]
                }).then(()=> {
                    dispatch({type:'PROFILE_SIGNUP_SUCCESS'});
                }).catch(err=> {
                    dispatch({type:'PROFILE_SIGNUP_ERROR', err});
                }); 
                break;
            case "educator":
                firestore.collection('Educator').add({
                    Username:newUser.username,
                    Name: {
                        FirstName:newUser.firstName,
                        MiddleName:newUser.middleName,
                        LastName:newUser.lastName,
                        Extension:newUser.extension
                    },
                    Birthday: new Date(Date.parse(newUser.birthday)),
                    CenterID:'',
                    Citizenship:newUser.citizenship,
                    EmailAddress:newUser.email,
                    EmploymentStatus:'none',
                    Position:'none',
                    Gender:newUser.gender,
                    MaritalStatus:newUser.maritalStatus,
                    Religion:newUser.religion,
                    Address: [
                        {
                            HouseNo: newUser.houseNo,
                            Street: newUser.street,
                            Brangay: newUser.barangay,
                            City: newUser.city,
                            Province: newUser.province,
                            Country: newUser.country,
                            ZipCode: newUser.zipCode,
                            CurrentAddress: true
                        }
                    ],
                    ContactNo:[
                        newUser.contactNo
                    ]
                }).then(()=> {
                    dispatch({type:'PROFILE_SIGNUP_SUCCESS'});
                }).catch(err=> {
                    dispatch({type:'PROFILE_SIGNUP_ERROR', err});
                }); 
                break;
            case "center":
                var serviceType = (newUser.business.serviceType === "Others") ? newUser.business.otherServiceType : newUser.business.serviceType;
                firestore.collection('LearningCenter').add({
                    Accounts: {
                            Username:newUser.username,
                        Name: {
                            FirstName:newUser.firstName,
                            MiddleName:newUser.middleName,
                            LastName:newUser.lastName,
                            Extension:newUser.extension
                        },
                        Birthday: new Date(Date.parse(newUser.birthday)),
                        Citizenship:newUser.citizenship,
                        EmailAddress:newUser.email,
                        Status:'active',
                        AccessLevel:'administrator',
                        Gender:newUser.gender,
                        MaritalStatus:newUser.maritalStatus,
                        Religion:newUser.religion,
                        Address: [
                            {
                                HouseNo: newUser.houseNo,
                                Street: newUser.street,
                                Brangay: newUser.barangay,
                                City: newUser.city,
                                Province: newUser.province,
                                Country: newUser.country,
                                ZipCode: newUser.zipCode,
                                CurrentAddress: true
                            }
                        ],
                        ContactNo:[
                            newUser.contactNo
                        ]
                    },
                    BusinessAddress: {
                        No: newUser.business.no,
                        Street: newUser.business.street,
                        Brangay: newUser.business.barangay,
                        City: newUser.business.city,
                        Province: newUser.business.province,
                        Country: newUser.business.country,
                        ZipCode: newUser.business.zipCode
                    },
                    BusinessName:newUser.business.businessName,
                    CompanyWebsite:newUser.business.companyWebsite,
                    ContactNo:newUser.business.contactNo,
                    ContactEmail:newUser.business.contactEmail,
                    ServiceType:serviceType,
                    ClosingTime:newUser.business.ClosingTime,
                    OpeningTime:newUser.business.OpeningTime,
                    OperatingDays:[...newUser.business.OperatingDays],
                    SubscriptionType:'Free',
                }).then(()=> {
                    dispatch({type:'PROFILE_SIGNUP_SUCCESS'});
                }).catch(err=> {
                    dispatch({type:'PROFILE_SIGNUP_ERROR', err});
                }); 
                break;
            default:
                break;
        }
    }
}

export const signUpCenter = (business) => {
    return(dispatch, getState) => {
        dispatch({type: 'SAVE_BUSINESS_DETAILS', business});
    }

}

export const resetAuthError = () =>{
    return(dispatch, getState) => {
        dispatch({type: 'AUTH_ERROR_RESET'});
    }
}