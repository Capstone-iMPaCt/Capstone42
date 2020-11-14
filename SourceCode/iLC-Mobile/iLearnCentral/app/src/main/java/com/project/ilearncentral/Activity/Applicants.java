package com.project.ilearncentral.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.Adapter.ApplicantAdapter;
import com.project.ilearncentral.Model.Applicant;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class Applicants extends AppCompatActivity implements View.OnFocusChangeListener {

    private ArrayList<Applicant> applicant;
    private GridView gridView;
    private ApplicantAdapter adapter;

    private TextView applicants, starred, hired, rejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);

        bindLayout();

        applicants.setOnFocusChangeListener(this);
        starred.setOnFocusChangeListener(this);
        rejected.setOnFocusChangeListener(this);

        applicant = new ArrayList<>();
        gridView = findViewById(R.id.applicants_gridview);
        applicant.add(new Applicant("Full Name", "Job Position Applied Job Position Applied Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        applicant.add(new Applicant("Full Name", "Job Position Applied"));
        adapter = new ApplicantAdapter(this, applicant);
        gridView.setAdapter(adapter);
    }

    private void bindLayout() {
        applicants = findViewById(R.id.applicants_option_applicants);
        starred = findViewById(R.id.applicants_option_starred);
        rejected = findViewById(R.id.applicants_option_rejected);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        TextView v = (TextView) view;
        if (b) {
            v.setTextColor(Color.CYAN);
        } else {
            v.setTextColor(Color.WHITE);
        }
    }
}