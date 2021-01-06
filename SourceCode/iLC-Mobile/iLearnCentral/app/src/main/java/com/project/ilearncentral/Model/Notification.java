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
import com.project.ilearncentral.MyClass.Utility;

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

    @Override
    public String toString() {
        return "Notification: " + notifId +
                "\nSubject: " + subject +
                "\nMessage: " + message +
                "\nDate: " + Utility.getDateTimeStringFromTimestamp(date) +
                "\nLink: " + link +
                "\nStatus: " + status;
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
        db.whereEqualTo("Username", username).whereEqualTo("Status", "unread").orderBy("Date").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                onCompleteRetrieve(done, task);
            }
        });
    }

    public static void onCompleteRetrieve (final ObservableBoolean done, final Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            retrieved.clear();
            for (QueryDocumentSnapshot document : task.getResult()) {
                Notification notification = new Notification();
                notification.setNotifId(document.getId());
                notification.setDate(document.getTimestamp("Date"));
                notification.setMessage(document.getString("Message"));
                notification.setLink(document.getString("Link"));
                notification.setStatus(document.getString("Status"));
                if (notification.getStatus().equalsIgnoreCase("new"))
                    notification.setStatus("unread");
                notification.setUsername(document.getString("Username"));
                notification.setSubject(document.getString("Subject"));
                retrieved.add(notification);
            }
            if (done!=null) done.set(true);
        } else {
            if (done != null) done.set(false);
            Log.d("getNotification", "Error getting documents: ", task.getException());
        }
    }

    public static boolean containsNotification(String notID) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getNotifId().equals(notID))
                return true;
        }
        return false;
    }

    public static int countUnread(List<Notification> notifications) {
        int count = 0;
        for(Notification notification:notifications) {
            if (notification.getStatus().equalsIgnoreCase("unread"))
                count++;
        }
        return count;
    }

    public static void newNotification(String username, String subject, String message, String link) {
        Notification notification = new Notification();
        notification.setDate(Timestamp.now());
        notification.setStatus("new");
        notification.setLink(link);
        notification.setMessage(message);
        notification.setUsername(username);
        notification.setSubject(subject);
        newNotification(notification);
    }
    public static void newNotification(final Notification notification) {
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
