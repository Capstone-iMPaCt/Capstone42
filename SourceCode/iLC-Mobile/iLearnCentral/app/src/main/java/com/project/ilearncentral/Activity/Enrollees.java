package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.project.ilearncentral.Adapter.EnrolleeAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.databinding.ActivityEnrolleesBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Enrollees extends AppCompatActivity {

    private final String TAG = "Enrollees";

    private ActivityEnrolleesBinding binding;

    private ObservableBoolean retrievedListener;
    private List<Enrolment> retrievedList;
    private List<Enrolment> enrolmentsList;
    private List<Enrolment> enroleeList;
    private List<Enrolment> pendingList;
    private EnrolleeAdapter adapter;
    private Comparator<Enrolment> compareByProcessedDate;
    private Comparator<Enrolment> compareByEnrolledDate;

    private Intent intent;
    private String option;

    @Override
    protected void onResume() {
        super.onResume();
        enrolmentsList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        retrievedListener.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    enrolmentsList.clear();
                    enroleeList.clear();
                    pendingList.clear();
                    enrolmentsList.addAll(retrievedList);
                    for (Enrolment enrolees : retrievedList) {
                        if (enrolees.getEnrolmentStatus().equals("enrolled")) {
                            enroleeList.add(enrolees);
                            Collections.sort(enroleeList, compareByEnrolledDate);
                        } else if (enrolees.getEnrolmentStatus().equals("pending")) {
                            pendingList.add(enrolees);
                            Collections.sort(pendingList, compareByProcessedDate);
                        }
                    }
                    if (option.equals("enrolees")) {
                        enrolmentsList.clear();
                        enrolmentsList.addAll(enroleeList);
                        if (enroleeList.size() == 0) {
                            binding.enrolleesContentMessage.setText("No Enrolled Student/s");
                            binding.enrolleesContentMessage.setVisibility(View.VISIBLE);
                        }
                        searchText(enrolmentsList);
                    } else if (option.equals("pendings")) {
                        enrolmentsList.clear();
                        enrolmentsList.addAll(pendingList);
                        if (pendingList.size() == 0) {
                            binding.enrolleesContentMessage.setText("No Pending Enrolee/s");
                            binding.enrolleesContentMessage.setVisibility(View.VISIBLE);
                        }
                        searchText(enrolmentsList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void searchText(final List<Enrolment> enrolmentsList) {
        final List<Enrolment> temp = new ArrayList<>(enrolmentsList);
        binding.enrolleesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                String date = "";
                enrolmentsList.clear();
                for (Enrolment enrolment : temp) {
                    if (option.equals("enrolees")) date = enrolment.getEnrolledDate() + "";
                    else if (option.equals("pendings")) date = enrolment.getProcessedDate() + "";

                    if (enrolment.getStudentName().toLowerCase().contains(query)
                            || enrolment.getCourseEnrolled().toLowerCase().contains(query)
                            || date.toLowerCase().contains(query)) {
                        enrolmentsList.add(enrolment);
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
        binding.enrolleesSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                enrolmentsList.clear();
                enrolmentsList.addAll(temp);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void initialize() {
        binding = ActivityEnrolleesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        option = intent.getStringExtra("option");

        retrievedListener = new ObservableBoolean();
        retrievedList = new ArrayList<>();
        enrolmentsList = new ArrayList<>();
        enroleeList = new ArrayList<>();
        pendingList = new ArrayList<>();
        Enrolment.getDocumentReferenceByID("centerID", Account.getCenterId()
                , retrievedList, retrievedListener);
        adapter = new EnrolleeAdapter(this, enrolmentsList, option);
        binding.enrolleesGridview.setAdapter(adapter);

        compareByProcessedDate = new Comparator<Enrolment>() {
            @Override
            public int compare(Enrolment o1, Enrolment o2) {
                return o1.getProcessedDate().compareTo(o2.getProcessedDate());
            }
        };
        compareByEnrolledDate = new Comparator<Enrolment>() {
            @Override
            public int compare(Enrolment o1, Enrolment o2) {
                return o1.getCourseEnrolled().compareTo(o2.getCourseEnrolled());
            }
        };
    }

    private void toastMessage(String message) {
        Toast.makeText(Enrollees.this, message, Toast.LENGTH_SHORT).show();
    }
}