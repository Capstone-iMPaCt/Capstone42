package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Adapter.EnrolmentGroupListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class StudentProfile extends Fragment {

    private TextView email, contactNo;
    private RecyclerView groupListRecyclerView;
    private EnrolmentGroupListAdapter enrolmentListAdapter;
    private ArrayList<Enrolment> enrolmentHistory;
    private CardView enrolmentLayout;

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

        setRetainInstance(true);

        email = view.findViewById(R.id.student_profile_email);
        contactNo = view.findViewById(R.id.student_profile_contact);
        enrolmentLayout = view.findViewById(R.id.student_profile_enrolment_history_layout);

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ObservableBoolean update = new ObservableBoolean();
        update.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    contactNo.setText(Account.getStringData("contactNo"));
                    if (enrolmentHistory.isEmpty()) {
                        enrolmentLayout.setVisibility(View.GONE);
                    } else {
                        enrolmentLayout.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        Account.updateObservables.add(update);
        if (Account.profileSet) update.set(true);

        // Adding Enrolment History
        enrolmentHistory = new ArrayList<>();
//        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
//        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
//        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
//        enrolmentHistory.add(new Enrolment("Learning Center Name","Course Enrolled","Date Enrolled"));
        groupListRecyclerView = view.findViewById(R.id.student_profile_enrolment_history_recyclerview);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        enrolmentListAdapter = new EnrolmentGroupListAdapter(getContext(), enrolmentHistory);
        groupListRecyclerView.setAdapter(enrolmentListAdapter);

        return view;
    }
}
