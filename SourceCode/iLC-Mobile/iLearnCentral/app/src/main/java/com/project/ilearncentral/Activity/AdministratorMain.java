package com.project.ilearncentral.Activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.Adapter.AdministratorMainAdapter;
import com.project.ilearncentral.Fragment.PendingVerifications;
import com.project.ilearncentral.Fragment.SubscriptionSales;
import com.project.ilearncentral.databinding.ActivityAdministratorMainBinding;

public class AdministratorMain extends AppCompatActivity {

    ActivityAdministratorMainBinding binding;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setTitle("Exit").setMessage("Do you want to close iLearnCentral?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        binding = ActivityAdministratorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generateTabs();
    }

    private void generateTabs() {
        AdministratorMainAdapter adapter = new AdministratorMainAdapter(getSupportFragmentManager());
        adapter.addFragment(new SubscriptionSales(), "Subscription Sales");
        adapter.addFragment(new PendingVerifications(), "Pending Verifications");
        binding.htabViewpager.setAdapter(adapter);
        binding.slidingTabs.setupWithViewPager(binding.htabViewpager);
    }
}