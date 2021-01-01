package com.project.ilearncentral.Model;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Notification {
    private String notifId, link, message, status, username;
    private Timestamp date;
    private static List<Notification> retrieved = new ArrayList<>();

    public Notification() {
        notifId = link = message = status = username = "";
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



}
