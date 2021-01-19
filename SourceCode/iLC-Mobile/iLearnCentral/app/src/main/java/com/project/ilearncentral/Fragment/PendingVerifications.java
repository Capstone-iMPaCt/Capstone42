package com.project.ilearncentral.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.AdminReportAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Sales;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class PendingVerifications extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;

//    private List<Sales> retrievedList;
//    private List<Sales> salesList;

    private AdminReportAdapter adapter;
    private ObservableBoolean salesListener;

    public PendingVerifications() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pending_verifications, container, false);
        initialize(view);

        salesListener.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
//                    salesList.clear();
//                    salesList.addAll(retrievedList);
                    adapter.notifyDataSetChanged();

                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
//                salesList.clear();
//                for (Sales sale : retrievedList) {
//                    if (sale.getCenterName().toLowerCase().contains(query)
//                            || sale.getSubscriptionTitle().toLowerCase().contains(query)
//                            || (sale.getFee() + "").contains(query)
//                            || (Utility.getDateAsString(sale.getDate()).contains(query))) {
//                        salesList.add(sale);
//                    }
//                }
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                salesListener.set(true);
                return false;
            }
        });

        return view;
    }

    private void initialize(View view) {
        searchView = view.findViewById(R.id.admin_pv_search);
        recyclerView = view.findViewById(R.id.admin_pv_recyclerview);

//        retrievedList = new ArrayList<>();
//        salesList = new ArrayList<>();
        salesListener = new ObservableBoolean();
//        Sales.getSalesData(retrievedList, salesListener);
//        adapter = new AdminReportAdapter(getContext(), salesList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
    }
}