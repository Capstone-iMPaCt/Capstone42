package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.EnrolmentGroupListAdapter;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class StudentProfile extends Fragment {

    private RecyclerView groupListRecyclerView;
    private EnrolmentGroupListAdapter enrolmentListAdapter;
    private ArrayList<Enrolment> enrolmentHistory;

    public StudentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_student, container, false);

        // Adding Enrolment History
        enrolmentHistory = new ArrayList<>();
        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
        groupListRecyclerView = view.findViewById(R.id.student_profile_enrolment_history_recyclerview);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        enrolmentListAdapter = new EnrolmentGroupListAdapter(getContext(), enrolmentHistory);
        groupListRecyclerView.setAdapter(enrolmentListAdapter);

        return view;
    }
}
