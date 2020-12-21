package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccountDetail {
    private final String TAG = "BankAccountDetail";

    private FirebaseFirestore db;
    private DocumentReference ref;

    private String bankName, bankAccountNumber;

    public BankAccountDetail() {
        db = FirebaseFirestore.getInstance();
        ref = db.collection("LearningCenter")
                .document(Account.getCenterId());

        bankName = "";
        bankAccountNumber = "";
    }

    public BankAccountDetail(String bankName, String bankAccountNumber) {
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void getDataFromDB(final List<BankAccountDetail> bankAccountDetailList, final OnBooleanChangeListener changeListener) {
        FirebaseFirestore.getInstance().collection("LearningCenter")
                .document(Account.getCenterId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, String>> bankAccounts = (List<Map<String, String>>)
                                    task.getResult().get("BankAccounts");
                            if (bankAccounts != null) {
                                for (Map<String, String> detail : bankAccounts) {
                                    BankAccountDetail temp = new BankAccountDetail();
                                    for (Map.Entry entry : detail.entrySet()) {
                                        if (entry.getKey().equals("BankName")) {
                                            temp.setBankName(entry.getValue().toString());
                                        } else if (entry.getKey().equals("AccountNo")) {
                                            temp.setBankAccountNumber(entry.getValue().toString());
                                        }
                                    }
                                    bankAccountDetailList.add(temp);
                                }
                                changeListener.onBooleanChanged(true);
                            }
                        }
                    }
                });
    }

    public boolean setDataToDB(List<BankAccountDetail> details) {
        HashMap<String, Object> bankAccount = new HashMap<>();
        List<Map<String, String>> arrayMap = new ArrayList<>();
        for (BankAccountDetail item : details) {
            Map<String, String> row = new HashMap<>();
            row.put("BankName", item.getBankName());
            row.put("AccountNo", item.getBankAccountNumber());
            if (item.getBankName().isEmpty() || item.getBankAccountNumber().isEmpty()) {
                return false;
            }
            arrayMap.add(row);
            bankAccount.put("BankAccounts", arrayMap);
        }
        Log.d(TAG, bankAccount.get("BankAccounts").toString());
        ref.update(bankAccount);
        return true;
    }
}
