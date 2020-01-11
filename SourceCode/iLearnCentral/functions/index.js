const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});

exports.getCourses = functions.https.onRequest((req, res)=> {
    admin.firestore().collection("Course").get()
        .then(data => {
            let courses = [];
            data.forEach(doc => {
                courses.push(doc.data());
            });
            return res.jason(courses);
        })
        .catch(err => console.error(err));
});