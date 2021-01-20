package com.project.ilearncentral.Model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.Date;
import java.util.List;

public class Verification {
    private String name, folder, subfolder, permitFile, birFile;
    private Date date;

    public Verification() {
        name = subfolder = permitFile = birFile = "";
        folder = "business_proof";
        date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public String getSubfolder() {
        return subfolder;
    }

    private void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public String getPermitFile() {
        return permitFile;
    }

    private void setPermitFile(String permitFile) {
        this.permitFile = permitFile;
    }

    public String getBirFile() {
        return birFile;
    }

    private void setBirFile(String birFile) {
        this.birFile = birFile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static void getPendingVerifications(final List<Verification> verificationList, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("LearningCenter")
                .whereEqualTo("VerificationStatus", "pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        } else {
                            verificationList.clear();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                Verification verification = new Verification();
                                verification.setName(document.get("BusinessName").toString());
                                verification.setSubfolder(document.getId());
                                verification.setPermitFile("PERMIT_" + document.getId());
                                verification.setBirFile("BIR_" + document.getId());
                                verificationList.add(verification);
                            }
                            listener.set(true);
                        }
                    }
                });
    }
}
