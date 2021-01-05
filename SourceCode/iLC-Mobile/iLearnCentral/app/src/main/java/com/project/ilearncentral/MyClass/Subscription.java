package com.project.ilearncentral.MyClass;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Subscription {
    private static final String TAG = "Subscription";

    private static FirebaseFirestore db;
    private static DocumentReference ref;
    private static boolean enrolmentSubscriptionStatus;
    private static boolean schedulingSubscriptionStatus;
    private static Date enrolmentSubscriptionExpiry;
    private static Date schedulingSubscriptionExpiry;

    private static void setRef() {
        db = FirebaseFirestore.getInstance();
        ref = db.collection("Subscription")
                .document(Account.getCenterId());
    }

    public static boolean isEnrolmentSubscribed() {
        return enrolmentSubscriptionStatus;
    }

    public static void setEnrolmentSubscriptionStatus(boolean enrolmentSubscriptionStatus) {
        Subscription.enrolmentSubscriptionStatus = enrolmentSubscriptionStatus;
    }

    public static boolean isSchedulingSubscribed() {
        return schedulingSubscriptionStatus;
    }

    public static void setSchedulingSubscriptionStatus(boolean schedulingSubscriptionStatus) {
        Subscription.schedulingSubscriptionStatus = schedulingSubscriptionStatus;
    }

    public static Date getEnrolmentSubscriptionExpiry() {
        return enrolmentSubscriptionExpiry;
    }

    private static void setEnrolmentSubscriptionExpiry(Date enrolmentSubscriptionExpiry) {
        Subscription.enrolmentSubscriptionExpiry = enrolmentSubscriptionExpiry;
    }

    public static Date getSchedulingSubscriptionExpiry() {
        return schedulingSubscriptionExpiry;
    }

    private static void setSchedulingSubscriptionExpiry(Date schedulingSubscriptionExpiry) {
        Subscription.schedulingSubscriptionExpiry = schedulingSubscriptionExpiry;
    }

    public static void setSubscriptionStatus(String title, double fee) {
        setRef();
        String systemName = title.replaceAll("\\s+", "");
        Date currentDate = new Date();
        Calendar dateExpire = Calendar.getInstance();
        dateExpire.setTime(currentDate);
        dateExpire.add(Calendar.MONTH, 1);

        final Map<String, Object> subscription = new HashMap<>();
        Map<String, Object> system = new HashMap<>();
        system.put("SubscriptionExpiry", dateExpire.getTime());
        subscription.put(systemName, system);

        db.collection("Subscription")
                .document(Account.getCenterId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            ref.set(subscription);
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ref.update(subscription);
                    }
                });

        Map<String, Object> sale = new HashMap<>();
        sale.put("CenterID", Account.getCenterId());
        sale.put("Date", new Date());
        sale.put("SubscriptionTitle", title);
        sale.put("Fee", fee);
        db.collection("Sales").add(sale);
    }

    public static void getEnrolmentSubscriptionDetails(final TextView textView) {
        FirebaseFirestore.getInstance().collection("Subscription")
                .document(Account.getCenterId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getData().containsKey("EnrolmentSystem")) {
                            Map<String, Object> data = (Map<String, Object>) value.get("EnrolmentSystem");
                            Timestamp timestamp = (com.google.firebase.Timestamp) data.get("SubscriptionExpiry");
                            Date dateNow = new Date();
                            if (dateNow.compareTo(timestamp.toDate()) < 0) {
                                // If dateNow occurs before SubscriptionExpiry
                                textView.setText("Subscription expires on: "
                                        + Subscription.getSchedulingSubscriptionExpiry());
                                setEnrolmentSubscriptionExpiry(timestamp.toDate());
                                setEnrolmentSubscriptionStatus(true);
                            }
                        } else {
                            textView.setText("Subscribe to enable this feature.");
                            setEnrolmentSubscriptionStatus(false);
                        }
                    }
                });
    }

    public static void getSchedulingSubscriptionDetails(final TextView textView) {
        FirebaseFirestore.getInstance().collection("Subscription")
                .document(Account.getCenterId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getData().containsKey("SchedulingSystem")) {
                            Map<String, Object> data = (Map<String, Object>) value.get("SchedulingSystem");
                            Timestamp timestamp = (com.google.firebase.Timestamp) data.get("SubscriptionExpiry");
                            Date dateNow = new Date();
                            if (dateNow.compareTo(timestamp.toDate()) < 0) {
                                // If dateNow occurs before SubscriptionExpiry
                                setSchedulingSubscriptionExpiry(timestamp.toDate());
                                setSchedulingSubscriptionStatus(true);
                                textView.setText("Subscription expires on: "
                                        + Subscription.getSchedulingSubscriptionExpiry());
                            }
                        } else {
                            setSchedulingSubscriptionStatus(false);
                            textView.setText("Subscribe to enable this feature.");
                        }
                    }
                });
    }
}
