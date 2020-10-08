package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.R;

import java.text.Format;
import java.util.Calendar;

public class NveCourse extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText timeStart, timeEnd;
    private TimePickerDialog timePicker;
    private  Calendar currentTime;

    private TextView sun, mon, tue, wed, thu, fri, sat;
    private boolean sunSelected, monSelected, tueSelected, wedSelected, thuSelected, friSelected, satSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nve_course);

        timeStart = findViewById(R.id.course_nve_schedule_start);
        timeStart.setInputType(InputType.TYPE_NULL);
        timeEnd = findViewById(R.id.course_nve_schedule_end);
        timeEnd.setInputType(InputType.TYPE_NULL);
        currentTime = Calendar.getInstance();
        sun = findViewById(R.id.course_sched_day_sun);
        mon = findViewById(R.id.course_sched_day_mon);
        tue = findViewById(R.id.course_sched_day_tue);
        wed = findViewById(R.id.course_sched_day_wed);
        thu = findViewById(R.id.course_sched_day_thu);
        fri = findViewById(R.id.course_sched_day_fri);
        sat = findViewById(R.id.course_sched_day_sat);

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(NveCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm ;
                        if(hourOfDay < 12) {
                            am_pm = "AM";
                        } else {
                            hourOfDay -= 12;
                            am_pm = "PM";
                        }
                        timeStart.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute) + " " + am_pm);
                        timeStart.setError(null);
                    }
                }, hour, minute, false);
                timePicker.setTitle("Select Time Start");
                timePicker.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(NveCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm ;
                        if(hourOfDay < 12) {
                            am_pm = "AM";
                        } else {
                            hourOfDay -= 12;
                            am_pm = "PM";
                        }
                        timeEnd.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute) + " " + am_pm);
                        timeEnd.setError(null);
                    }
                }, hour, minute, false);
                timePicker.setTitle("Select Time End");
                timePicker.show();
            }
        });

        sunSelected = false;
        monSelected = false;
        tueSelected = false;
        wedSelected = false;
        thuSelected = false;
        friSelected = false;
        satSelected = false;
        sun.setOnClickListener(this);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_sched_day_sun:
                sunSelected = setSelection(sun, sunSelected);
                break;
            case R.id.course_sched_day_mon:
                monSelected = setSelection(mon, monSelected);
                break;
            case R.id.course_sched_day_tue:
                tueSelected = setSelection(tue, tueSelected);
                break;
            case R.id.course_sched_day_wed:
                wedSelected = setSelection(wed, wedSelected);
                break;
            case R.id.course_sched_day_thu:
                thuSelected = setSelection(thu, thuSelected);
                break;
            case R.id.course_sched_day_fri:
                friSelected = setSelection(fri, friSelected);
                break;
            case R.id.course_sched_day_sat:
                satSelected = setSelection(sat, satSelected);
                break;
        }
    }

    private boolean setSelection(View view, boolean bool){
        if (!bool){
            view.setBackgroundResource(R.drawable.bg_selected_day_rounded);
            return true;
        }
        else{
            view.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
            return false;
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(timeStart.getWindowToken(), 0);
    }
}
