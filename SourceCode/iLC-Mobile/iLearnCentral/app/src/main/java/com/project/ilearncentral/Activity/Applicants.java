package com.project.ilearncentral.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.Adapter.ApplicantAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.Model.JobApplication;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class Applicants extends AppCompatActivity implements View.OnFocusChangeListener {

    private List<JobApplication> applicant;
    private GridView gridView;
    private ApplicantAdapter adapter;
    private ObservableBoolean updateList;
    private boolean loaded;

    private TextView applicants, hired, rejected, none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);

        bindLayout();

        applicants.setOnFocusChangeListener(this);
        hired.setOnFocusChangeListener(this);
        rejected.setOnFocusChangeListener(this);
        updateList = new ObservableBoolean();
        loaded = false;
        updateList.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    applicant.clear();
                    applicant.addAll(JobApplication.getApplicantsByCenterId(Account.getCenterId(),JobApplication.OPEN));
                    noItemsDisplay();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        applicants.setTextColor(Color.CYAN);
        applicant = new ArrayList<JobApplication>();

        adapter = new ApplicantAdapter(this, applicant, updateList);
        gridView.setAdapter(adapter);

        Thread thread = new Thread(){
            public void run() {
                while (!loaded) {
                    try {
                        applicant.clear();
                        applicant.addAll(JobApplication
                                .getApplicantsByCenterId(Account
                                        .getCenterId(), JobApplication.OPEN));
                        adapter.notifyDataSetChanged();
                        if (!applicant.isEmpty()) {
                            if ((applicant.get(0).getEducator() == null || applicant.get(0)
                                    .getJobVacancy() == null)) {
                                try {
                                    Thread.sleep(100);
                                    JobApplication.setEducators();
                                    JobApplication.setJobVacancies();
                                } catch (InterruptedException e) {
                                }
                            } else {
                                loaded = true;
                                break;
                            }
                        } else {
                            loaded = true;
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
                noItemsDisplay();
            }
        };
        thread.start();
    }

    private void noItemsDisplay() {
        if (applicant.isEmpty()) {
            gridView.setVisibility(View.GONE);
            none.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            none.setVisibility(View.GONE);
        }
    }

    private void bindLayout() {
        applicants = findViewById(R.id.applicants_option_applicants);
        hired = findViewById(R.id.applicants_option_hired);
        rejected = findViewById(R.id.applicants_option_rejected);
        none = findViewById(R.id.applicants_no_items);
        gridView = findViewById(R.id.applicants_gridview);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        applicants.setTextColor(Color.WHITE);
        hired.setTextColor(Color.WHITE);
        rejected.setTextColor(Color.WHITE);
        TextView v = (TextView) view;
        if (b) {
            v.setTextColor(Color.CYAN);
        } else {
            v.setTextColor(Color.WHITE);
        }
        setViewItems(v.getText().toString());
    }

    private void setViewItems(String status) {
        applicant.clear();
        switch (status) {
            case "APPLICANTS":
                applicant.addAll(JobApplication.getApplicantsByCenterId(Account.getCenterId(), JobApplication.OPEN));
                adapter.notifyDataSetChanged();
                break;
            case "HIRED":
                applicant.addAll(JobApplication.getApplicantsByCenterId(Account.getCenterId(), JobApplication.HIRED));
                adapter.notifyDataSetChanged();
                break;
            case "REJECTED":
                applicant.addAll(JobApplication.getApplicantsByCenterId(Account.getCenterId(), JobApplication.REJECTED));
                adapter.notifyDataSetChanged();
                break;
        }
        noItemsDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewItems("APPLICANTS");
        applicants.setTextColor(Color.CYAN);
        hired.setTextColor(Color.WHITE);
        rejected.setTextColor(Color.WHITE);
    }
}