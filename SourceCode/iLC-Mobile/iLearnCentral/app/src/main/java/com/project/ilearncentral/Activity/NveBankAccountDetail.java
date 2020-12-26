package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Adapter.AddUpdateBankAccountDetailListAdapter;
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.MyClass.BankAccountList;
import com.project.ilearncentral.databinding.ActivityNveBankAccountDetailBinding;

public class NveBankAccountDetail extends AppCompatActivity {

    private static final String TAG = "NveBankAccountDetail";
    private ActivityNveBankAccountDetailBinding layoutBinding;

    private AddUpdateBankAccountDetailListAdapter adapter;
    private BankAccountDetail data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        layoutBinding.nveBankAccountAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BankAccountList.data.add(new BankAccountDetail());
                adapter.notifyItemInserted(BankAccountList.data.size());
            }
        });

        layoutBinding.nveBankAccountRemovebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BankAccountList.data.isEmpty()) {
                    BankAccountList.data.remove(BankAccountList.data.size() - 1);
                    adapter.notifyItemRemoved(BankAccountList.data.size());
                }
            }
        });

        layoutBinding.nveBankAccountSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BankAccountList.data.isEmpty()) {
                    if (data.setDataToDB(BankAccountList.data)) {
                        Toast.makeText(NveBankAccountDetail.this, "Saving successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(NveBankAccountDetail.this, "There are empty fields.\nSaving failed.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(NveBankAccountDetail.this, "Please add at least 1 Bank Account.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        layoutBinding = ActivityNveBankAccountDetailBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());
        data = new BankAccountDetail();

        layoutBinding.nveBankAccountRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddUpdateBankAccountDetailListAdapter(this, BankAccountList.data);
        layoutBinding.nveBankAccountRecyclerview.setAdapter(adapter);
    }
}