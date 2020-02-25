package com.project.ilearncentral.Fragment;

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

public class UserActivitySchedules extends Fragment {

    public UserActivitySchedules() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_activity_schedules, container, false);

        return view;
    }

    public void inputTime(final View v) {
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog =
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = "";
                        if (hourOfDay == 0) {
                            time += 12;
                        } else if (hourOfDay <= 12) {
                            time += hourOfDay;
                        } else {
                            time += (hourOfDay - 12);
                        }
                        if (minute < 10)
                            time += ":0" + minute;
                        else
                            time += ":" + minute;
                        if (hourOfDay < 12)
                            time += " am";
                        else
                            time += " pm";
                        ((TextInputEditText) v).setText(time);
                        ((TextInputEditText) v).setError(null);
                    }
                }, mHour, mMinute, false);
        dialog.show();
    }
}
