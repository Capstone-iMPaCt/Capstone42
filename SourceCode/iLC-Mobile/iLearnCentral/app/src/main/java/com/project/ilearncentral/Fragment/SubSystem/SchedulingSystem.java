package com.project.ilearncentral.Fragment.SubSystem;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.R;

import java.util.Calendar;

public class SchedulingSystem extends Fragment {
    
    public SchedulingSystem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subsystem_scheduling, container, false);
        super.onCreate(savedInstanceState);
        initialize(view);

        return view;
    }

    private void initialize(View view){

    }
}
