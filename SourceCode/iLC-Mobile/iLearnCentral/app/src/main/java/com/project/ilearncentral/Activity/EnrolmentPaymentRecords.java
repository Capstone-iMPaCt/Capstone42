package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.ilearncentral.Adapter.PaymentAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.databinding.ActivityEnrolmentPaymentRecordsBinding;

import java.util.ArrayList;
import java.util.List;

public class EnrolmentPaymentRecords extends AppCompatActivity {

    private ActivityEnrolmentPaymentRecordsBinding binding;

    private ObservableBoolean retrievedListener;
    private List<Enrolment> retrievedList;
    private List<Enrolment> paymentsList;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        retrievedListener.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    paymentsList.clear();
                    for (Enrolment enrolment : retrievedList) {
                        if (enrolment.getEnrolledDate() != null) {
                            paymentsList.add(enrolment);
                        }
                    }
                    retrievedList.clear();
                    retrievedList.addAll(paymentsList);
                    if (paymentsList.size() == 0) {
                        binding.enrolmentPaymentRecordsCoursesNone.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    binding.enrolmentPaymentRecordsAppBarSearchview
                            .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    query = query.toLowerCase();
                                    paymentsList.clear();
                                    for (Enrolment enrolment : retrievedList) {
                                        if (enrolment.getStudentName().toLowerCase().contains(query)
                                                || enrolment.getCourseEnrolled().toLowerCase().contains(query)
                                                || (enrolment.getEnrolmentFee() + "").contains(query)
                                                || (enrolment.getEnrolledDate() + "").contains(query)) {
                                            paymentsList.add(enrolment);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    return false;
                                }
                            });
                    binding.enrolmentPaymentRecordsAppBarSearchview
                            .setOnCloseListener(new SearchView.OnCloseListener() {
                                @Override
                                public boolean onClose() {
                                    paymentsList.clear();
                                    paymentsList.addAll(retrievedList);
                                    adapter.notifyDataSetChanged();
                                    return false;
                                }
                            });
                    binding.enrolmentPaymentRecordsPullToRefresh
                            .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    binding.enrolmentPaymentRecordsPullToRefresh
                                            .setRefreshing(false);
                                }
                            });
                }
            }
        });
    }

    private void initialize() {
        binding = ActivityEnrolmentPaymentRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrievedListener = new ObservableBoolean();
        paymentsList = new ArrayList<>();
        retrievedList = new ArrayList<>();
        binding.enrolmentPaymentRecordsRecylerview.setLayoutManager(new LinearLayoutManager(this));
        Enrolment.getDataByID("centerID", Account.getCenterId(), retrievedList, retrievedListener);
        adapter = new PaymentAdapter(EnrolmentPaymentRecords.this, paymentsList);
        binding.enrolmentPaymentRecordsRecylerview.setAdapter(adapter);
    }
}