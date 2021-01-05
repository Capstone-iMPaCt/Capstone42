package com.project.ilearncentral.Activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.ilearncentral.Adapter.AdminReportAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Sales;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.databinding.ActivityAdministratorViewBinding;

import java.util.ArrayList;
import java.util.List;

public class AdministratorView extends AppCompatActivity {

    private ActivityAdministratorViewBinding binding;

    private List<Sales> retrievedList;
    private List<Sales> salesList;
    private AdminReportAdapter adapter;
    private ObservableBoolean salesListener;

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

        salesListener.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    salesList.clear();
                    salesList.addAll(retrievedList);
                    adapter.notifyDataSetChanged();

                }
            }
        });
        binding.avReportsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                salesList.clear();
                for (Sales sale : retrievedList) {
                    if (sale.getCenterName().toLowerCase().contains(query)
                            || sale.getSubscriptionTitle().toLowerCase().contains(query)
                            || (sale.getFee() + "").contains(query)
                            || (Utility.getDateAsString(sale.getDate()).contains(query))) {
                        salesList.add(sale);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.avReportsSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                salesListener.set(true);
                return false;
            }
        });
    }

    private void initialize() {
        binding = ActivityAdministratorViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrievedList = new ArrayList<>();
        salesList = new ArrayList<>();
        salesListener = new ObservableBoolean();
        Sales.getSalesData(retrievedList, salesListener);
        adapter = new AdminReportAdapter(AdministratorView.this, salesList);
        binding.avReportsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.avReportsRecyclerview.setAdapter(adapter);
    }
}