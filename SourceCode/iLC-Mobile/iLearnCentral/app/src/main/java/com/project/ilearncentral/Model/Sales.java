package com.project.ilearncentral.Model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.Date;
import java.util.List;

public class Sales {
    private String centerID;
    private Date date;
    private Double fee;
    private String subscriptionTitle;

    private String centerName = "";

    public Sales() {
    }

    public String getCenterID() {
        return centerID;
    }

    public void setCenterID(String centerID) {
        this.centerID = centerID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getSubscriptionTitle() {
        return subscriptionTitle;
    }

    public void setSubscriptionTitle(String subscriptionTitle) {
        this.subscriptionTitle = subscriptionTitle;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerID, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("LearningCenter")
                .document(centerID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                        view.setText(value.get("BusinessName").toString());
//                        setCenterName(value.get("BusinessName").toString());
                        centerName = value.get("BusinessName").toString();
                        listener.set(true);
                    }
                });
    }

    public static void getSalesData(final List<Sales> salesList, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("Sales")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        salesList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Sales sale = new Sales();
                            sale.setCenterID(document.get("CenterID").toString());
                            sale.setCenterName(document.get("CenterID").toString(), listener);
                            sale.setSubscriptionTitle(document.get("SubscriptionTitle").toString());
                            sale.setFee(document.getDouble("Fee"));
                            sale.setDate(document.getDate("Date"));
                            salesList.add(sale);
                        }
                        listener.set(true);
                    }
                });
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Sales sale = new Sales();
//                                sale.setCenterID(document.get("CenterID").toString());
//                                sale.setCenterName(document.get("CenterID").toString(), listener);
//                                sale.setSubscriptionTitle(document.get("SubscriptionTitle").toString());
//                                sale.setFee(document.getDouble("Fee"));
//                                sale.setDate(document.getDate("Date"));
//                                salesList.add(sale);
//                            }
//                            listener.set(true);
//                        }
//                    }
//                });
    }
}
