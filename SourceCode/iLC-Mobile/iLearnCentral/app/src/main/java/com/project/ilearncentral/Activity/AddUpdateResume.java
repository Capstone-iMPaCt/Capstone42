package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.AddUpdateResumeGroupListAdapter;
import com.project.ilearncentral.Adapter.AddUpdateResumeReferenceAdapter;
import com.project.ilearncentral.Adapter.AddUpdateResumeSingleListAdapter;
import com.project.ilearncentral.Model.Resume;
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
    private List<Resume> educationalBackground, employmentHistory, skills, awards, qualities,
            interests, references;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_resume);

        bindButtons();
        setAdapters();
        setButtonClickListeners();

        // retrieving row data test
//        TextView t = findViewById(R.id.educ_bg);
//        t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("0~~~~~~~~~~~~" + educationalBackgroundAdapter.getData().get(0).getHeader());
//                System.out.println("1~~~~~~~~~~~~" + educationalBackgroundAdapter.getData().get(1).getHeader());
//            }
//        });
    }

    private void setAdapters() {
        educationalBackground = new ArrayList<>();
        educationalBackgroundRecyclerView = findViewById(R.id.add_update_resume_educational_background_recyclerview);
        educationalBackgroundRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        educationalBackgroundAdapter = new AddUpdateResumeGroupListAdapter(this, educationalBackground,
                "School Name","School Address","School Period");
        educationalBackgroundRecyclerView.setAdapter(educationalBackgroundAdapter);

        employmentHistory = new ArrayList<>();
        employmentHistoryRecyclerView = findViewById(R.id.add_update_resume_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employmentHistoryAdapter = new AddUpdateResumeGroupListAdapter(this, employmentHistory,
                "Employer Name","Employer Address","Employment Period");
        employmentHistoryRecyclerView.setAdapter(employmentHistoryAdapter);

        skills = new ArrayList<>();
        skillsRecyclerView = findViewById(R.id.add_update_resume_skills_recyclerview);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        skillsAdapter = new AddUpdateResumeSingleListAdapter(this, skills, "Skill");
        skillsRecyclerView.setAdapter(skillsAdapter);

        awards = new ArrayList<>();
        awardsRecyclerView = findViewById(R.id.add_update_resume_awards_recyclerview);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        awardsAdapter = new AddUpdateResumeSingleListAdapter(this, awards, "Award");
        awardsRecyclerView.setAdapter(awardsAdapter);

        qualities = new ArrayList<>();
        qualitiesRecyclerView = findViewById(R.id.add_update_resume_qualities_recyclerview);
        qualitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        qualitiesAdapter = new AddUpdateResumeSingleListAdapter(this, qualities, "Quality");
        qualitiesRecyclerView.setAdapter(qualitiesAdapter);

        interests = new ArrayList<>();
        interestsRecyclerView = findViewById(R.id.add_update_resume_interests_recyclerview);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        interestsAdapter = new AddUpdateResumeSingleListAdapter(this, interests, "Interest");
        interestsRecyclerView.setAdapter(interestsAdapter);

        references = new ArrayList<>();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_update_resume_educational_background_addbutton:
                educationalBackground.add(new Resume("","",""));
                educationalBackgroundAdapter.notifyItemInserted(educationalBackground.size());
                break;
            case R.id.add_update_resume_educational_background_removebutton:
                if (!educationalBackground.isEmpty())
                    educationalBackground.remove(educationalBackground.size() - 1);
                educationalBackgroundAdapter.notifyItemRemoved(educationalBackground.size());
                break;
            case R.id.add_update_resume_employment_history_addbutton:
                employmentHistory.add(new Resume("","",""));
                employmentHistoryAdapter.notifyItemInserted(employmentHistory.size());
                break;
            case R.id.add_update_resume_employment_history_removebutton:
                if (!employmentHistory.isEmpty())
                    employmentHistory.remove(employmentHistory.size() - 1);
                employmentHistoryAdapter.notifyItemRemoved(employmentHistory.size());
                break;
            case R.id.add_update_resume_skills_addbutton:
                skills.add(new Resume(""));
                skillsAdapter.notifyItemInserted(skills.size());
                break;
            case R.id.add_update_resume_skills_removebutton:
                if (!skills.isEmpty())
                    skills.remove(skills.size() - 1);
                skillsAdapter.notifyItemRemoved(skills.size());
                break;
            case R.id.add_update_resume_awards_addbutton:
                awards.add(new Resume(""));
                awardsAdapter.notifyItemInserted(awards.size());
                break;
            case R.id.add_update_resume_awards_removebutton:
                if (!awards.isEmpty())
                    awards.remove(awards.size() - 1);
                awardsAdapter.notifyItemRemoved(awards.size());
                break;
            case R.id.add_update_resume_qualities_addbutton:
                qualities.add(new Resume(""));
                qualitiesAdapter.notifyItemInserted(qualities.size());
                break;
            case R.id.add_update_resume_qualities_removebutton:
                if (!qualities.isEmpty())
                    qualities.remove(qualities.size() - 1);
                qualitiesAdapter.notifyItemRemoved(qualities.size());
                break;
            case R.id.add_update_resume_interests_addbutton:
                interests.add(new Resume(""));
                interestsAdapter.notifyItemInserted(interests.size());
                break;
            case R.id.add_update_resume_interests_removebutton:
                if (!interests.isEmpty())
                    interests.remove(interests.size() - 1);
                interestsAdapter.notifyItemRemoved(interests.size());
                break;
            case R.id.add_update_resume_references_addbutton:
                references.add(new Resume("","","",""));
                referencesAdapter.notifyItemInserted(references.size());
                break;
            case R.id.add_update_resume_references_removebutton:
                if (!references.isEmpty())
                    references.remove(references.size() - 1);
                referencesAdapter.notifyItemRemoved(references.size());
                break;
            case R.id.add_update_resume_confirm_button:
                // push to database code here
                break;
        }
    }
}
