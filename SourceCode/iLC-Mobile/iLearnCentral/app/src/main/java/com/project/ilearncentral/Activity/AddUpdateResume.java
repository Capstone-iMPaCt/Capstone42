package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.AddUpdateResumeGroupListAdapter;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateResume extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView educationalBackgroundRecyclerView, employmentHistoryRecyclerView;
    private AddUpdateResumeGroupListAdapter educationalBackgroundAdapter, employmentHistoryAdapter;
    private ImageButton educationalBackgroundAddButton, educationalBackgroundRemoveButton,
            employmentHistoryAddButton, employmentHistoryRemoveButton;
    private List<Resume> educationalBackground, employmentHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_resume);

        educationalBackgroundAddButton = findViewById(R.id.add_update_resume_educational_background_addbutton);
        educationalBackgroundRemoveButton = findViewById(R.id.add_update_resume_educational_background_removebutton);
        employmentHistoryAddButton = findViewById(R.id.add_update_resume_employment_history_addbutton);
        employmentHistoryRemoveButton = findViewById(R.id.add_update_resume_employment_history_removebutton);

        educationalBackground = new ArrayList<>();
        educationalBackgroundRecyclerView = findViewById(R.id.add_update_resume_educational_background_recyclerview);
        educationalBackgroundRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        educationalBackgroundRecyclerView.setNestedScrollingEnabled(false);
        educationalBackgroundAdapter = new AddUpdateResumeGroupListAdapter(this, educationalBackground);
        educationalBackgroundRecyclerView.setAdapter(educationalBackgroundAdapter);

        employmentHistory = new ArrayList<>();
        employmentHistoryRecyclerView = findViewById(R.id.add_update_resume_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        employmentHistoryRecyclerView.setNestedScrollingEnabled(false);
        employmentHistoryAdapter = new AddUpdateResumeGroupListAdapter(this, employmentHistory);
        employmentHistoryRecyclerView.setAdapter(employmentHistoryAdapter);

        educationalBackgroundAddButton.setOnClickListener(this);
        educationalBackgroundRemoveButton.setOnClickListener(this);
        employmentHistoryAddButton.setOnClickListener(this);
        employmentHistoryRemoveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_update_resume_educational_background_addbutton:
                educationalBackground.add(new Resume());
                educationalBackgroundAdapter.notifyItemInserted(educationalBackground.size());
                break;
            case R.id.add_update_resume_educational_background_removebutton:
                if (!educationalBackground.isEmpty())
                    educationalBackground.remove(educationalBackground.size()-1);
                educationalBackgroundAdapter.notifyItemRemoved(educationalBackground.size());
                break;
            case R.id.add_update_resume_employment_history_addbutton:
                employmentHistory.add(new Resume());
                employmentHistoryAdapter.notifyItemInserted(employmentHistory.size());
                break;
            case R.id.add_update_resume_employment_history_removebutton:
                if (!employmentHistory.isEmpty())
                    employmentHistory.remove(employmentHistory.size()-1);
                employmentHistoryAdapter.notifyItemRemoved(employmentHistory.size());
                break;
        }
    }
}
