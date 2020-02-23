package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.internal.Util;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.Adapter.AddUpdateResumeGroupListAdapter;
import com.project.ilearncentral.Adapter.AddUpdateResumeReferenceAdapter;
import com.project.ilearncentral.Adapter.AddUpdateResumeSingleListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateResume extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView educationalBackgroundRecyclerView, employmentHistoryRecyclerView,
            skillsRecyclerView, awardsRecyclerView, qualitiesRecyclerView, interestsRecyclerView,
            referencesRecyclerView;
    private AddUpdateResumeGroupListAdapter educationalBackgroundAdapter, employmentHistoryAdapter;
    private AddUpdateResumeSingleListAdapter skillsAdapter, awardsAdapter, qualitiesAdapter, interestsAdapter;
    private AddUpdateResumeReferenceAdapter referencesAdapter;
    private ImageButton educationalBackgroundAddButton, educationalBackgroundRemoveButton,
            employmentHistoryAddButton, employmentHistoryRemoveButton,
            skillsAddButton, skillsRemoveButton, awardsAddButton, awardsRemoveButton,
            qualitiesAddButton, qualitiesRemoveButton, interestsAddButton, interestsRemoveButton,
            referencesAddButton, referencesRemoveButton;
    private List<ResumeItem> educationalBackground, employmentHistory, skills, awards, qualities,
            interests, references;
    private Button confirm;
    private TextInputEditText objective;
    private ObservableString doneUpload;
    private String resumeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_resume);

        Intent i = getIntent();
        if (i.hasExtra("resumeId")) {
            resumeId = i.getStringExtra("resumeId");
        }
        else {
            resumeId = "";
        }

        educationalBackground = Resume.getEducationalBackground();
        employmentHistory = Resume.getEmploymentHistory();
        skills = Resume.getSkills();
        awards = Resume.getAwards();
        qualities = Resume.getQualities();
        interests = Resume.getInterest();
        references = Resume.getReferences();

        bindButtons();
        setAdapters();
        setButtonClickListeners();

        objective.setText(Resume.getObjective());

        doneUpload.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String resumeId) {
                Utility.buttonWait(confirm, false, "Confirm");
                if (resumeId.isEmpty()) {
                    Resume.resumeChange = false;
                }
                else {
                    Resume.resumeChange = true;
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void setAdapters() {
        educationalBackgroundRecyclerView = findViewById(R.id.add_update_resume_educational_background_recyclerview);
        educationalBackgroundRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        educationalBackgroundAdapter = new AddUpdateResumeGroupListAdapter(this, educationalBackground,
                "School Name","School Address","School Period");
        educationalBackgroundRecyclerView.setAdapter(educationalBackgroundAdapter);

        employmentHistoryRecyclerView = findViewById(R.id.add_update_resume_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employmentHistoryAdapter = new AddUpdateResumeGroupListAdapter(this, employmentHistory,
                "Employer Name","Employer Address","Employment Period");
        employmentHistoryRecyclerView.setAdapter(employmentHistoryAdapter);

        skillsRecyclerView = findViewById(R.id.add_update_resume_skills_recyclerview);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        skillsAdapter = new AddUpdateResumeSingleListAdapter(this, skills, "Skill");
        skillsRecyclerView.setAdapter(skillsAdapter);

        awardsRecyclerView = findViewById(R.id.add_update_resume_awards_recyclerview);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        awardsAdapter = new AddUpdateResumeSingleListAdapter(this, awards, "Award");
        awardsRecyclerView.setAdapter(awardsAdapter);

        qualitiesRecyclerView = findViewById(R.id.add_update_resume_qualities_recyclerview);
        qualitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        qualitiesAdapter = new AddUpdateResumeSingleListAdapter(this, qualities, "Quality");
        qualitiesRecyclerView.setAdapter(qualitiesAdapter);

        interestsRecyclerView = findViewById(R.id.add_update_resume_interests_recyclerview);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        interestsAdapter = new AddUpdateResumeSingleListAdapter(this, interests, "Interest");
        interestsRecyclerView.setAdapter(interestsAdapter);

        referencesRecyclerView = findViewById(R.id.add_update_resume_references_recyclerview);
        referencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        referencesAdapter = new AddUpdateResumeReferenceAdapter(this, references);
        referencesRecyclerView.setAdapter(referencesAdapter);
    }

    private void setButtonClickListeners() {
        educationalBackgroundAddButton.setOnClickListener(this);
        educationalBackgroundRemoveButton.setOnClickListener(this);
        employmentHistoryAddButton.setOnClickListener(this);
        employmentHistoryRemoveButton.setOnClickListener(this);
        skillsAddButton.setOnClickListener(this);
        skillsRemoveButton.setOnClickListener(this);
        awardsAddButton.setOnClickListener(this);
        awardsRemoveButton.setOnClickListener(this);
        qualitiesAddButton.setOnClickListener(this);
        qualitiesRemoveButton.setOnClickListener(this);
        interestsAddButton.setOnClickListener(this);
        interestsRemoveButton.setOnClickListener(this);
        referencesAddButton.setOnClickListener(this);
        referencesRemoveButton.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void bindButtons() {
        educationalBackgroundAddButton = findViewById(R.id.add_update_resume_educational_background_addbutton);
        educationalBackgroundRemoveButton = findViewById(R.id.add_update_resume_educational_background_removebutton);
        employmentHistoryAddButton = findViewById(R.id.add_update_resume_employment_history_addbutton);
        employmentHistoryRemoveButton = findViewById(R.id.add_update_resume_employment_history_removebutton);
        skillsAddButton = findViewById(R.id.add_update_resume_skills_addbutton);
        skillsRemoveButton = findViewById(R.id.add_update_resume_skills_removebutton);
        awardsAddButton = findViewById(R.id.add_update_resume_awards_addbutton);
        awardsRemoveButton = findViewById(R.id.add_update_resume_awards_removebutton);
        qualitiesAddButton = findViewById(R.id.add_update_resume_qualities_addbutton);
        qualitiesRemoveButton = findViewById(R.id.add_update_resume_qualities_removebutton);
        interestsAddButton = findViewById(R.id.add_update_resume_interests_addbutton);
        interestsRemoveButton = findViewById(R.id.add_update_resume_interests_removebutton);
        referencesAddButton = findViewById(R.id.add_update_resume_references_addbutton);
        referencesRemoveButton = findViewById(R.id.add_update_resume_references_removebutton);
        confirm = findViewById(R.id.add_update_resume_confirm_button);
        objective = findViewById(R.id.add_update_resume_career_objective);
        doneUpload = new ObservableString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_update_resume_educational_background_addbutton:
                educationalBackground.add(new ResumeItem("","",""));
                educationalBackgroundAdapter.notifyItemInserted(educationalBackground.size());
                break;
            case R.id.add_update_resume_educational_background_removebutton:
                if (!educationalBackground.isEmpty())
                    educationalBackground.remove(educationalBackground.size() - 1);
                educationalBackgroundAdapter.notifyItemRemoved(educationalBackground.size());
                break;
            case R.id.add_update_resume_employment_history_addbutton:
                employmentHistory.add(new ResumeItem("","",""));
                employmentHistoryAdapter.notifyItemInserted(employmentHistory.size());
                break;
            case R.id.add_update_resume_employment_history_removebutton:
                if (!employmentHistory.isEmpty())
                    employmentHistory.remove(employmentHistory.size() - 1);
                employmentHistoryAdapter.notifyItemRemoved(employmentHistory.size());
                break;
            case R.id.add_update_resume_skills_addbutton:
                skills.add(new ResumeItem(""));
                skillsAdapter.notifyItemInserted(skills.size());
                break;
            case R.id.add_update_resume_skills_removebutton:
                if (!skills.isEmpty())
                    skills.remove(skills.size() - 1);
                skillsAdapter.notifyItemRemoved(skills.size());
                break;
            case R.id.add_update_resume_awards_addbutton:
                awards.add(new ResumeItem(""));
                awardsAdapter.notifyItemInserted(awards.size());
                break;
            case R.id.add_update_resume_awards_removebutton:
                if (!awards.isEmpty())
                    awards.remove(awards.size() - 1);
                awardsAdapter.notifyItemRemoved(awards.size());
                break;
            case R.id.add_update_resume_qualities_addbutton:
                qualities.add(new ResumeItem(""));
                qualitiesAdapter.notifyItemInserted(qualities.size());
                break;
            case R.id.add_update_resume_qualities_removebutton:
                if (!qualities.isEmpty())
                    qualities.remove(qualities.size() - 1);
                qualitiesAdapter.notifyItemRemoved(qualities.size());
                break;
            case R.id.add_update_resume_interests_addbutton:
                interests.add(new ResumeItem(""));
                interestsAdapter.notifyItemInserted(interests.size());
                break;
            case R.id.add_update_resume_interests_removebutton:
                if (!interests.isEmpty())
                    interests.remove(interests.size() - 1);
                interestsAdapter.notifyItemRemoved(interests.size());
                break;
            case R.id.add_update_resume_references_addbutton:
                references.add(new ResumeItem("","","",""));
                referencesAdapter.notifyItemInserted(references.size());
                break;
            case R.id.add_update_resume_references_removebutton:
                if (!references.isEmpty())
                    references.remove(references.size() - 1);
                referencesAdapter.notifyItemRemoved(references.size());
                break;
            case R.id.add_update_resume_confirm_button:
                Utility.buttonWait(confirm, true);
                Resume.setAwards(awards);
                Resume.setEducationalBackground(educationalBackground);
                Resume.setEmploymentHistory(employmentHistory);
                Resume.setInterest(interests);
                Resume.setQualities(qualities);
                Resume.setReferences(references);
                Resume.setSkills(skills);
                Resume.setObjective(objective.getText().toString());
                if (resumeId.isEmpty())
                    Resume.addResume(doneUpload);
                else
                    Resume.updateResume(resumeId, doneUpload);
            break;
        }
    }
}
