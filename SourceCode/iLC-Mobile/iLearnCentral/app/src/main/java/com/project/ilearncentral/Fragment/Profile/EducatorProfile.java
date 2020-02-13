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
import com.project.ilearncentral.Adapter.EducatorBackgroundAdapter;
import com.project.ilearncentral.Adapter.EducatorDetailsAdapter;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class EducatorProfile extends Fragment {

    private TextView email, pwd;
    private RecyclerView employmentHistoryRecyclerView, interestRecyclerView, qualitiesRecyclerView;
    private EducatorDetailsAdapter educatorDetailsAdapter;
    private EducatorBackgroundAdapter educatorBackgroundAdapter;
    private ArrayList<Resume> employmentHistory, educationalBackGround;
    private ArrayList<Resume> interests, qualities, references;

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

        educationalBackGround = new ArrayList<>();
        educationalBackGround.add(new Resume("Primary School","Address","Year"));
        educationalBackGround.add(new Resume("Elementary School","Address","Year"));
        educationalBackGround.add(new Resume("High School","Address","Year"));
        educationalBackGround.add(new Resume("College","Address","Year"));
        employmentHistoryRecyclerView = view.findViewById(R.id.educator_educational_background_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educatorBackgroundAdapter = new EducatorBackgroundAdapter(getContext(), educationalBackGround);
        employmentHistoryRecyclerView.setAdapter(educatorBackgroundAdapter);

        employmentHistory = new ArrayList<>();
        employmentHistory.add(new Resume("Company Name","Address","Duration"));
        employmentHistory.add(new Resume("Company Name","Address","Duration"));
        employmentHistoryRecyclerView = view.findViewById(R.id.educator_employment_history_recyclerview);
        employmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educatorBackgroundAdapter = new EducatorBackgroundAdapter(getContext(), employmentHistory);
        employmentHistoryRecyclerView.setAdapter(educatorBackgroundAdapter);

        interests = new ArrayList<>();
        interests.add(new Resume("Programming"));
        interests.add(new Resume("CISCO Networking"));
        interests.add(new Resume("Computer Troubleshooting"));
        interestRecyclerView = view.findViewById(R.id.educator_interests_recyclerview);
        interestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educatorDetailsAdapter = new EducatorDetailsAdapter(getContext(), interests);
        interestRecyclerView.setAdapter(educatorDetailsAdapter);

        qualities = new ArrayList<>();
        qualities.add(new Resume("AI Programming"));
        qualities.add(new Resume("Network Security"));
        qualities.add(new Resume("Game Developing"));
        qualitiesRecyclerView = view.findViewById(R.id.educator_qualities_recyclerview);
        qualitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        educatorDetailsAdapter = new EducatorDetailsAdapter(getContext(), qualities);
        qualitiesRecyclerView.setAdapter(educatorDetailsAdapter);

        return view;
    }
}
