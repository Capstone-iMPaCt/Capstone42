package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.project.ilearncentral.R;

public class LearningCenterProfile extends Fragment {

    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;

    public LearningCenterProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_learning_center, container, false);

        sunday = (TextView)view.findViewById(R.id.lc_profile_op_day_sun);
        monday = (TextView)view.findViewById(R.id.lc_profile_op_day_mon);
        tuesday = (TextView)view.findViewById(R.id.lc_profile_op_day_tue);
        wednesday = (TextView)view.findViewById(R.id.lc_profile_op_day_wed);
        thursday = (TextView)view.findViewById(R.id.lc_profile_op_day_thu);
        friday = (TextView)view.findViewById(R.id.lc_profile_op_day_fri);
        saturday = (TextView)view.findViewById(R.id.lc_profile_op_day_sat);

        monday.setBackgroundResource(R.drawable.bg_selected_day_rounded);
        tuesday.setBackgroundResource(R.drawable.bg_selected_day_rounded);
        wednesday.setBackgroundResource(R.drawable.bg_selected_day_rounded);
        thursday.setBackgroundResource(R.drawable.bg_selected_day_rounded);
        friday.setBackgroundResource(R.drawable.bg_selected_day_rounded);
        return view;
    }
}
