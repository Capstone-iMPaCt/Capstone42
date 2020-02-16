package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Adapter.ResumeGroupListAdapter;
import com.project.ilearncentral.Adapter.ResumeReferenceAdapter;
import com.project.ilearncentral.Adapter.ResumeSingleListAdapter;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class EducatorProfile extends Fragment {

    private TextView email, pwd;
    private RecyclerView groupListRecyclerView, singleListRecyclerView, resumeReferenceRecyclerView;
    private ResumeGroupListAdapter resumeGroupListAdapter;
    private ResumeSingleListAdapter resumeSingleListAdapter;
    private ResumeReferenceAdapter resumeReferenceAdapter;
    private ArrayList<Resume> employmentHistory, educationalBackGround;
    private ArrayList<Resume> skills, awards, interests, qualities, references;

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
        View view = inflater.inflate(R.layout.fragment_profile_educator, container, false);
        // Codes here
        email = (TextView) view.findViewById(R.id.email_textview);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Adding Educational Background
        educationalBackGround = new ArrayList<>();
        educationalBackGround.add(new Resume("Primary School","Address","Year"));
        educationalBackGround.add(new Resume("Elementary School","Address","Year"));
        educationalBackGround.add(new Resume("High School","Address","Year"));
        educationalBackGround.add(new Resume("College","Address","Year"));
        groupListRecyclerView = view.findViewById(R.id.educator_profile_educational_background_recyclerview);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        groupListRecyclerView.setNestedScrollingEnabled(false);
        resumeGroupListAdapter = new ResumeGroupListAdapter(getContext(), educationalBackGround);
        groupListRecyclerView.setAdapter(resumeGroupListAdapter);

        // Adding Employment History
        employmentHistory = new ArrayList<>();
        employmentHistory.add(new Resume("Company Name","Address","Duration"));
        employmentHistory.add(new Resume("Company Name","Address","Duration"));
        groupListRecyclerView = view.findViewById(R.id.educator_profile_employment_history_recyclerview);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        groupListRecyclerView.setNestedScrollingEnabled(false);
        resumeGroupListAdapter = new ResumeGroupListAdapter(getContext(), employmentHistory);
        groupListRecyclerView.setAdapter(resumeGroupListAdapter);

        // Adding Skills
        skills = new ArrayList<>();
        skills.add(new Resume("Programming"));
        skills.add(new Resume("CISCO Networking"));
        skills.add(new Resume("Computer Troubleshooting"));
        skills.add(new Resume("Carpentry"));
        singleListRecyclerView = view.findViewById(R.id.educator_profile_skills_recyclerview);
        singleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        singleListRecyclerView.setNestedScrollingEnabled(false);
        resumeSingleListAdapter = new ResumeSingleListAdapter(getContext(), skills);
        singleListRecyclerView.setAdapter(resumeSingleListAdapter);

        // Adding Awards
        awards = new ArrayList<>();
        awards.add(new Resume("Programming"));
        awards.add(new Resume("CISCO Networking"));
        awards.add(new Resume("Computer Troubleshooting"));
        awards.add(new Resume("Carpentry"));
        singleListRecyclerView = view.findViewById(R.id.educator_profile_awards_recyclerview);
        singleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        singleListRecyclerView.setNestedScrollingEnabled(false);
        resumeSingleListAdapter = new ResumeSingleListAdapter(getContext(), awards);
        singleListRecyclerView.setAdapter(resumeSingleListAdapter);

        // Adding Qualities
        qualities = new ArrayList<>();
        qualities.add(new Resume("Programming"));
        qualities.add(new Resume("CISCO Networking"));
        qualities.add(new Resume("Computer Troubleshooting"));
        qualities.add(new Resume("Carpentry"));
        singleListRecyclerView = view.findViewById(R.id.educator_profile_qualities_recyclerview);
        singleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        singleListRecyclerView.setNestedScrollingEnabled(false);
        resumeSingleListAdapter = new ResumeSingleListAdapter(getContext(), qualities);
        singleListRecyclerView.setAdapter(resumeSingleListAdapter);

        // Adding Interests
        interests = new ArrayList<>();
        interests.add(new Resume("AI Programming"));
        interests.add(new Resume("Network Security"));
        interests.add(new Resume("Game Developing"));
        singleListRecyclerView = view.findViewById(R.id.educator_profile_interests_recyclerview);
        singleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        singleListRecyclerView.setNestedScrollingEnabled(false);
        resumeSingleListAdapter = new ResumeSingleListAdapter(getContext(), interests);
        singleListRecyclerView.setAdapter(resumeSingleListAdapter);

        // Adding References
        references = new ArrayList<>();
        references.add(new Resume("Person","Job Title","Company Name", "Contact Number"));
        references.add(new Resume("Person","Job Title","Company Name", "Contact Number"));
        references.add(new Resume("Person","Job Title","Company Name", "Contact Number"));
        resumeReferenceRecyclerView = view.findViewById(R.id.educator_profile_references_recyclerview);
        resumeReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        resumeReferenceRecyclerView.setNestedScrollingEnabled(false);
        resumeReferenceAdapter = new ResumeReferenceAdapter(getContext(), references);
        resumeReferenceRecyclerView.setAdapter(resumeReferenceAdapter);

        return view;
    }
}
