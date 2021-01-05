package com.project.ilearncentral.Fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.ilearncentral.Adapter.ResumeGroupListAdapter;
import com.project.ilearncentral.Adapter.ResumeReferenceAdapter;
import com.project.ilearncentral.Adapter.ResumeSingleListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class ViewResumeFragment extends Fragment {

    private Educator educator;
    private TextView address, birthday, citizenship, religion, maritalStatus;
    private TextView email, contactNo, centerName, careerObjective;
    private LinearLayout centerLayout, resumeDetails, educationalLayout, employmentLayout, skillsLayout, awardsLayout;
    private CardView objective, qualitiesLayout, interestLayout, referencesLayout;
    private RecyclerView educationalBackgroundRecyclerView, employmentHistoryRecyclerView,
            skillsRecyclerView, awardsRecyclerView, qualitiesRecyclerView, interestsRecyclerView,
            referencesRecyclerView;
    private ResumeGroupListAdapter educationalBackgroundAdapter, employmentHistoryAdapter;
    private ResumeSingleListAdapter skillsAdapter, awardsAdapter, qualitiesAdapter, interestsAdapter;
    private ResumeReferenceAdapter referencesAdapter;
    private List<ResumeItem> employmentHistory, educationalBackGround,
            skills, awards, interests, qualities, references;
    private Resume resume;
    private boolean init;

    public ViewResumeFragment() {
        // Required empty public constructor
    }

    public ViewResumeFragment(Educator educator) {
        this.educator = educator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_view_resume, container, false);

        setRetainInstance(true);
        // Codes here
        address = view.findViewById(R.id.view_resume_edu_aboutme_address);
        birthday = view.findViewById(R.id.view_resume_edu_aboutme_birthdate);
        citizenship = view.findViewById(R.id.view_resume_edu_aboutme_citizenship);
        religion = view.findViewById(R.id.view_resume_edu_aboutme_religion);
        maritalStatus = view.findViewById(R.id.view_resume_edu_aboutme_marital_status);
        email = view.findViewById(R.id.view_resume_edu_email);
        contactNo = view.findViewById(R.id.view_resume_edu_contact);
        centerName = view.findViewById(R.id.view_resume_edu_center);
        centerLayout = view.findViewById(R.id.view_resume_edu_center_layout);
        careerObjective = view.findViewById(R.id.view_resume_edu_career_objective);
        resumeDetails = view.findViewById(R.id.view_resume_edu_resume_details);
        objective = view.findViewById(R.id.view_resume_edu_objective);
        educationalLayout = view.findViewById(R.id.view_resume_edu_educational_background_layout );
        employmentLayout = view.findViewById(R.id.view_resume_edu_employment_history_layout);
        skillsLayout = view.findViewById(R.id.view_resume_edu_skills_layout);
        awardsLayout = view.findViewById(R.id.view_resume_edu_awards_layout);
        qualitiesLayout = view.findViewById(R.id.view_resume_edu_qualities_layout);
        interestLayout = view.findViewById(R.id.view_resume_edu_interests_layout);
        referencesLayout = view.findViewById(R.id.view_resume_edu_references_layout);
        init = true;

        final ObservableBoolean resumeLoaded = new ObservableBoolean();
        resumeLoaded.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    careerObjective.setText(resume.getObjective());
                    if (init) initResumeDetails(view);
                    else setResumeDetails();

                    if (educationalBackGround.isEmpty())
                        educationalLayout.setVisibility(View.GONE);
                    else
                        educationalLayout.setVisibility(View.VISIBLE);
                    if (employmentHistory.isEmpty())
                        employmentLayout.setVisibility(View.GONE);
                    else
                        employmentLayout.setVisibility(View.VISIBLE);
                    if (skills.isEmpty())
                        skillsLayout.setVisibility(View.GONE);
                    else
                        skillsLayout.setVisibility(View.VISIBLE);
                    if (awards.isEmpty())
                        awardsLayout.setVisibility(View.GONE);
                    else
                        awardsLayout.setVisibility(View.VISIBLE);
                    if (qualities.isEmpty())
                        qualitiesLayout.setVisibility(View.GONE);
                    else
                        qualitiesLayout.setVisibility(View.VISIBLE);
                    if (interests.isEmpty())
                        interestLayout.setVisibility(View.GONE);
                    else
                        interestLayout.setVisibility(View.VISIBLE);
                    if (references.isEmpty())
                        referencesLayout.setVisibility(View.GONE);
                    else
                        referencesLayout.setVisibility(View.VISIBLE);

                    resumeDetails.setVisibility(View.VISIBLE);
                    objective.setVisibility(View.VISIBLE);
                }
                else {
                    resumeDetails.setVisibility(View.GONE);
                    objective.setVisibility(View.GONE);
                }
            }
        });

        ObservableBoolean update = new ObservableBoolean();
        update.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    User user = User.getUserByUsername(educator.getUsername());
                    if (user!=null) {
                        contactNo.setText(user.getContactNo());
                        email.setText(user.getEmail());
                    }
                    address.setText(educator.getAddress());
                    birthday.setText(Utility.getDateStringFromTimestamp(educator.getBirthday()));
                    religion.setText(educator.getReligion());
                    citizenship.setText(educator.getCitizenship());
                    maritalStatus.setText(educator.getMartitalStatus());
                    LearningCenter lc = LearningCenter.getLCById(educator.getCenterId());
                    if (lc == null) {
                        centerLayout.setVisibility(View.GONE);
                    } else {
                        centerLayout.setVisibility(View.VISIBLE);
                        centerName.setText(lc.getBusinessName());
                    }
                    resumeLoaded.set(true);
                }
            }
        });

        if (educator!=null) {
            resume = Resume.getResumeFromDB(educator.getUsername(), update);
            if (resume==null) resume = new Resume(educator.getUsername());
        }

        return view;
    }

    private void setResumeDetails() {
        educationalBackgroundAdapter.notifyDataSetChanged();
        employmentHistoryAdapter.notifyDataSetChanged();
        skillsAdapter.notifyDataSetChanged();
        awardsAdapter.notifyDataSetChanged();
        qualitiesAdapter.notifyDataSetChanged();
        interestsAdapter.notifyDataSetChanged();
        referencesAdapter.notifyDataSetChanged();
    }

    private void initResumeDetails(View view) {
        init = false;
        educationalBackGround = resume.getEducationalBackground();
        educationalBackgroundRecyclerView = view.findViewById(R.id.view_resume_edu_educational_background_recyclerview);
        educationalBackgroundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educationalBackgroundAdapter = new ResumeGroupListAdapter(getContext(), educationalBackGround);
        educationalBackgroundRecyclerView.setAdapter(educationalBackgroundAdapter);

        employmentHistory = resume.getEmploymentHistory();
        employmentHistoryRecyclerView = view.findViewById(R.id.view_resume_edu_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        employmentHistoryAdapter = new ResumeGroupListAdapter(getContext(), employmentHistory);
        employmentHistoryRecyclerView.setAdapter(employmentHistoryAdapter);

        skills = resume.getSkills();
        skillsRecyclerView = view.findViewById(R.id.view_resume_edu_skills_recyclerview);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        skillsAdapter = new ResumeSingleListAdapter(getContext(), skills);
        skillsRecyclerView.setAdapter(skillsAdapter);

        awards = resume.getAwards();
        awardsRecyclerView = view.findViewById(R.id.view_resume_edu_awards_recyclerview);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        awardsAdapter = new ResumeSingleListAdapter(getContext(), awards);
        awardsRecyclerView.setAdapter(awardsAdapter);

        qualities = resume.getQualities();
        qualitiesRecyclerView = view.findViewById(R.id.view_resume_edu_qualities_recyclerview);
        qualitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        qualitiesAdapter = new ResumeSingleListAdapter(getContext(), qualities);
        qualitiesRecyclerView.setAdapter(qualitiesAdapter);

        interests = resume.getInterest();
        interestsRecyclerView = view.findViewById(R.id.view_resume_edu_interests_recyclerview);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        interestsAdapter = new ResumeSingleListAdapter(getContext(), interests);
        interestsRecyclerView.setAdapter(interestsAdapter);

        references = resume.getReferences();
        referencesRecyclerView = view.findViewById(R.id.view_resume_edu_references_recyclerview);
        referencesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        referencesAdapter = new ResumeReferenceAdapter(getContext(), references);
        referencesRecyclerView.setAdapter(referencesAdapter);

    }
}
