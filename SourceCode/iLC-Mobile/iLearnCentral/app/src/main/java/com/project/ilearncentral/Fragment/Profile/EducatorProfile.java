package com.project.ilearncentral.Fragment.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Activity.AddUpdateResume;
import com.project.ilearncentral.Adapter.ResumeGroupListAdapter;
import com.project.ilearncentral.Adapter.ResumeReferenceAdapter;
import com.project.ilearncentral.Adapter.ResumeSingleListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Resume;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class EducatorProfile extends Fragment {

    private int UPDATE_RESUME = 15;
    private TextView email, contactNo, centerName, careerObjective, followers, following, rating;
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
    private Button updateResume;
    private boolean init;

    public EducatorProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_educator, container, false);

        setRetainInstance(true);
        // Codes here
        email = view.findViewById(R.id.educator_profile_email);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        contactNo = view.findViewById(R.id.educator_profile_contact);
        centerName = view.findViewById(R.id.educator_profile_center);
        centerLayout = view.findViewById(R.id.educator_profile_center_layout);
        careerObjective = view.findViewById(R.id.educator_career_objective);
        followers = view.findViewById(R.id.educator_followers);
        following = view.findViewById(R.id.educator_following);
        rating = view.findViewById(R.id.educator_rating);
        resumeDetails = view.findViewById(R.id.educator_profile_resume_details);
        updateResume = view.findViewById(R.id.educator_profile_update_resume_button);
        objective = view.findViewById(R.id.educator_profile_objective);
        educationalLayout = view.findViewById(R.id.educator_profile_educational_background_layout );
        employmentLayout = view.findViewById(R.id.educator_profile_employment_history_layout);
        skillsLayout = view.findViewById(R.id.educator_profile_skills_layout);
        awardsLayout = view.findViewById(R.id.educator_profile_awards_layout);
        qualitiesLayout = view.findViewById(R.id.educator_profile_qualities_layout);
        interestLayout = view.findViewById(R.id.educator_profile_interests_layout);
        referencesLayout = view.findViewById(R.id.educator_profile_references_layout);
        init = true;

        updateResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddUpdateResume.class), UPDATE_RESUME);
            }
        });

        final ObservableBoolean resumeLoaded = new ObservableBoolean();
        resumeLoaded.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    careerObjective.setText(Resume.getObjective());
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


                    updateResume.setVisibility(View.GONE);
                    resumeDetails.setVisibility(View.VISIBLE);
                    objective.setVisibility(View.VISIBLE);
                }
                else {
                    updateResume.setVisibility(View.VISIBLE);
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
                    contactNo.setText(Account.getStringData("contactNo"));
                    if (Account.getStringData("centerId").isEmpty()) {
                        centerLayout.setVisibility(View.GONE);
                    } else {
                        followers.setText(Utility.processCount(Account.me.getFollowers()));
                        following.setText(Utility.processCount(Account.me.getFollowing()));
                        rating.setText(Account.me.getRating()+"");
                        centerLayout.setVisibility(View.VISIBLE);
                        centerName.setText(Account.getBusinessName());
                    }
                    if (Resume.resumeChange) {
                        Resume.getResumeFromDB(Account.getUsername(), resumeLoaded);
                        Resume.resumeChange = false;
                    } else {
                        resumeLoaded.set(!Resume.getId().isEmpty());
                    }
                }
            }
        });
        Account.updateObservables.add(update);
        if (Account.profileSet) update.set(true);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        educationalBackGround = Resume.getEducationalBackground();
        educationalBackgroundRecyclerView = view.findViewById(R.id.educator_profile_educational_background_recyclerview);
        educationalBackgroundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educationalBackgroundAdapter = new ResumeGroupListAdapter(getContext(), educationalBackGround);
        educationalBackgroundRecyclerView.setAdapter(educationalBackgroundAdapter);

        employmentHistory = Resume.getEmploymentHistory();
        employmentHistoryRecyclerView = view.findViewById(R.id.educator_profile_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        employmentHistoryAdapter = new ResumeGroupListAdapter(getContext(), employmentHistory);
        employmentHistoryRecyclerView.setAdapter(employmentHistoryAdapter);

        skills = Resume.getSkills();
        skillsRecyclerView = view.findViewById(R.id.educator_profile_skills_recyclerview);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        skillsAdapter = new ResumeSingleListAdapter(getContext(), skills);
        skillsRecyclerView.setAdapter(skillsAdapter);

        awards = Resume.getAwards();
        awardsRecyclerView = view.findViewById(R.id.educator_profile_awards_recyclerview);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        awardsAdapter = new ResumeSingleListAdapter(getContext(), awards);
        awardsRecyclerView.setAdapter(awardsAdapter);

        qualities = Resume.getQualities();
        qualitiesRecyclerView = view.findViewById(R.id.educator_profile_qualities_recyclerview);
        qualitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        qualitiesAdapter = new ResumeSingleListAdapter(getContext(), qualities);
        qualitiesRecyclerView.setAdapter(qualitiesAdapter);

        interests = Resume.getInterest();
        interestsRecyclerView = view.findViewById(R.id.educator_profile_interests_recyclerview);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        interestsAdapter = new ResumeSingleListAdapter(getContext(), interests);
        interestsRecyclerView.setAdapter(interestsAdapter);

        references = Resume.getReferences();
        referencesRecyclerView = view.findViewById(R.id.educator_profile_references_recyclerview);
        referencesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        referencesAdapter = new ResumeReferenceAdapter(getContext(), references);
        referencesRecyclerView.setAdapter(referencesAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        followers.setText(Utility.processCount(Account.me.getFollowers()));
        following.setText(Utility.processCount(Account.me.getFollowing()));
        rating.setText(Account.me.getRating()+"");
    }
}
