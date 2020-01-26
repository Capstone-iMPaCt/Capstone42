const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});

const createNotification = (notification => {
    return admin.firestore().collection('Notifications')
    .add(notification)
    .then(doc => console.log('notification added', doc));
})

exports.documentCreated = functions.firestore.document('documentName/{documentId}').onCreate(doc => {
    const document = doc.data();
    const notification = {
        content: 'Added a new document',
        time: admin.firestore.FieldValue.serverTimestamp()
    }
    return createNotification(notification);
})

exports.userJoined = functions.auth.user().onCreate(user => {
    return admin.firestore().collection('User').doc(user.uid).get().then(doc => {
        const newUser = doc.data();
        const notification = {
            content: 'New user',
            user: `$newUser.username`,
            time: admin.firestore.FieldValue.serverTimestamp()
        }
    })
})