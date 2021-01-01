package com.project.ilearncentral.Model;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Notification {
    private String notifId, link, message, status, username, subject;
    private Timestamp date;
    public static List<Notification> retrieved = new ArrayList<>();

    public Notification() {
        notifId = link = message = status = username = subject = "";
        date = Timestamp.now();
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setNotification(Notification notification) {
        this.notifId = notification.getNotifId();
        this.date = notification.getDate();
        this.link = notification.getLink();
        this.message = notification.getMessage();
        this.status = notification.getStatus();
        this.username = notification.getUsername();
    }

    public static void retrieveNotificationsFromDB(final ObservableBoolean done, String query, String value) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("Notification");
        if (query.isEmpty()) {
            db.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            onCompleteRetrieve(done, task);
                        }
                    });
        } else if (query.equalsIgnoreCase("username")) {
            db.whereEqualTo("Username", value).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            retrieved.clear();
                            onCompleteRetrieve(done, task);
                        }
                    });
        }
    }
    public static void retrieveUnreadNotificationsOfUser(final ObservableBoolean done, String username) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("Notification");
        db.whereEqualTo("Username", username).whereEqualTo("Status", "unread").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                retrieved.clear();
                onCompleteRetrieve(done, task);
            }
        });
    }

    public static void onCompleteRetrieve (final ObservableBoolean done, final Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Notification notification = new Notification();
                notification.setNotifId(document.getId());
                notification.setDate(document.getTimestamp("Date"));
                notification.setMessage(document.getString("Message"));
                notification.setLink(document.getString("Link"));
                notification.setStatus(document.getString("Status"));
                notification.setUsername(document.getString("Username"));
                notification.setUsername(document.getString("Subject"));
                int pos = getNotifPositionById(document.getId());
                if (pos == -1) {
                    retrieved.add(notification);
                } else {
                    retrieved.get(pos).setNotification(notification);
                }
            }
            if (done!=null) done.set(true);
        } else {
            if (done != null) done.set(false);
            Log.d("getStudents", "Error getting documents: ", task.getException());
        }
    }

    public static int getNotifPositionById(String notifId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getNotifId().equals(notifId))
                return i;
        }
        return -1;
    }

    public static List<Notification> getNotifPositionByUsername(String username) {
        List<Notification> notifications = new ArrayList<>();
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getUsername().equals(username))
                notifications.add(retrieved.get(i));
        }
        return notifications;
    }


    public static void newNotification(String username, String subject, String message, String link) {
        Notification notification = new Notification();
        notification.setDate(Timestamp.now());
        notification.setStatus("unread");
        notification.setLink(link);
        notification.setMessage(message);
        notification.setUsername(username);
        notification.setSubject(subject);
        newNotification(notification);
    }
    public static void newNotification(final Notification notification) {
        System.out.println("New notification to " + notification.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("Status", notification.getStatus());
        data.put("Subject", notification.getSubject());
        data.put("Date", notification.getDate());
        data.put("Username", notification.getUsername());
        data.put("Link", notification.getLink());
        data.put("Message", notification.getMessage());
        FirebaseFirestore.getInstance().collection("Notification").add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    notification.setNotifId(documentReference.getId());
                }
            });
    }

}
