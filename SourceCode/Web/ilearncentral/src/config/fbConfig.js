import firebase from 'firebase/app'
import 'firebase/firestore'
import 'firebase/auth'

var fbConfig  = {
    apiKey: "AIzaSyBtTnG-2SUlU-ih6aS0zmTcusVNH0k4q6A",
    authDomain: "ilearncentral-90d51.firebaseapp.com",
    databaseURL: "https://ilearncentral-90d51.firebaseio.com",
    projectId: "ilearncentral-90d51",
    storageBucket: "ilearncentral-90d51.appspot.com",
    messagingSenderId: "142649912088",
    appId: "1:142649912088:web:42838d04a263742140cc6d",
    measurementId: "G-WBS320GWP7"
  };

  firebase.initializeApp(fbConfig)
  firebase.firestore()
  

  export default firebase