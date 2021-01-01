package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.internal.Util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.project.ilearncentral.databinding.ActivityNveClassBinding;
import com.project.ilearncentral.databinding.ActivityNveCourseBinding;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NveClass extends AppCompatActivity {

    private ActivityNveClassBinding binding;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Calendar currentDate;
    private List<Class> classes;
    private Date dateStart, dateEnd;
    private Timestamp timeStart, timeEnd;
    private List<String> recurringDays;
    private List<Educator> educators;
    private String classID, courseID, action;

    private Spinner statusSpinner, eduSpinner;
    private RadioGroup typeGroup;
    private RadioButton singleRadio, recurringRadio;
    private LinearLayout singleLayout, recurringLayout;
    private TextView titleTextView, daysLabel, errorView;
    private TextInputEditText roomNo, timeStartInput, timeEndInput, dateInput, timeRStartInput, timeREndInput, dateRStartInput, dateREndInput;
    private CheckBox monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, sunCheck;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nve_class);

        bindViews();
        bindDateTimeInputs();
        recurringDays = new ArrayList<>();
        educators = new ArrayList<>();
        setEduSpinner();

        Intent i = getIntent();
        classID = i.getStringExtra("classID");
        courseID = i.getStringExtra("courseID");
        action = i.getStringExtra("action");

        setActionBased();

        singleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = (RadioButton) v;
                if (r.isChecked()) {
                    singleLayout.setVisibility(View.VISIBLE);
                    recurringLayout.setVisibility(View.GONE);
                } else {
                    singleLayout.setVisibility(View.GONE);
                    recurringLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        recurringRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = (RadioButton) v;
                if (r.isChecked()) {
                    recurringLayout.setVisibility(View.VISIBLE);
                    singleLayout.setVisibility(View.GONE);
                } else {
                    recurringLayout.setVisibility(View.GONE);
                    singleLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkErrors()) {
                    Educator edu = null;
                    Course course = Course.getCourseById(courseID);
                    if (!eduSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Educator")) {
                        edu = Educator
                                .getEduByFullname(eduSpinner.getSelectedItem().toString());
                    }
                    if (singleRadio.isChecked()) {
                        Class aClass = new Class();
                        aClass.setCourse(course);
                        aClass.setCourseID(courseID);
                        aClass.setEducator(edu);
                        if (edu == null)
                            aClass.setEduID("");
                        else
                            aClass.setEduID(edu.getUsername());
                        aClass.setRoomNo(roomNo.getText().toString());
                        if (statusSpinner.getSelectedItem().toString().equalsIgnoreCase("- Select Class Status -"))
                            aClass.setStatus("Open");
                        else
                            aClass.setStatus(statusSpinner.getSelectedItem().toString());
                        Timestamp tStart = getCompleteTimestamp("", dateInput, timeStartInput);
                        Timestamp tEnd = getCompleteTimestamp("", dateInput, timeEndInput);
                        aClass.setClassStart(tStart);
                        aClass.setClassEnd(tEnd);
                        Class.addNewClassToDB(aClass);
                    } else if (recurringRadio.isChecked()) {
                        Timestamp curDate = getCompleteTimestamp("", dateRStartInput, timeRStartInput);
                        Timestamp endDate = getCompleteTimestamp("", dateREndInput, timeREndInput);
                        Date dateCount = curDate.toDate();
                        if (monCheck.isChecked()) recurringDays.add("MONDAY");
                        if (tueCheck.isChecked()) recurringDays.add("TUESDAY");
                        if (wedCheck.isChecked()) recurringDays.add("WEDNESDAY");
                        if (thuCheck.isChecked()) recurringDays.add("THURSDAY");
                        if (friCheck.isChecked()) recurringDays.add("FRIDAY");
                        if (satCheck.isChecked()) recurringDays.add("SATURDAY");
                        if (sunCheck.isChecked()) recurringDays.add("SUNDAY");
                        while (dateCount.before(endDate.toDate())) {
                            if (recurringDays.contains(Utility.getDayString(dateCount))) {
                                Class aClass = new Class();
                                aClass.setCourse(course);
                                aClass.setCourseID(courseID);
                                aClass.setEducator(edu);
                                if (edu == null)
                                    aClass.setEduID("");
                                else
                                    aClass.setEduID(edu.getUsername());
                                aClass.setRoomNo(roomNo.getText().toString());
                                if (statusSpinner.getSelectedItem().toString().equalsIgnoreCase("- Select Class Status -"))
                                    aClass.setStatus("Open");
                                else
                                    aClass.setStatus(statusSpinner.getSelectedItem().toString());
                                Timestamp tStart = getCompleteTimestamp(Utility.getDateAsString(dateCount), null, timeStartInput);
                                Timestamp tEnd = getCompleteTimestamp(Utility.getDateAsString(dateCount), null, timeEndInput);
                                aClass.setClassStart(tStart);
                                aClass.setClassEnd(tEnd);
                                Class.addNewClassToDB(aClass);
                            }
                            dateCount = Utility.addDays(dateCount, 1);
                        }
                    }
                }
            }
        });
    }

    private Timestamp getCompleteTimestamp(String dateString, View dateView, View timeView) {
        Timestamp timestamp;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String timestampString = "";
        if (dateView!=null)
            timestampString = ((TextInputEditText)dateView).getText().toString() + " ";
        else
            timestampString = dateString + " ";
        timestampString += ((TextInputEditText)timeView).getText().toString();
        try {
            timestamp = new Timestamp(format.parse(timestampString));
        } catch (ParseException e) {
            timestamp = null;
        }
        return timestamp;
    }

    private void setActionBased() {
        if (action.equalsIgnoreCase("add")) {
            titleTextView.setText("Create New Class/es");
        } else if (action.equalsIgnoreCase("edit")) {
            typeGroup.setVisibility(View.GONE);
            titleTextView.setText("Edit Class " + classID);
        } else {

        }
    }

    private boolean checkErrors() {
        int errors = 0;
        boolean isEmpty = false;
        errorView.setText("Errors: ");
        errorView.setVisibility(View.GONE);
        if(singleRadio.isChecked()) {
            if (dateInput.getText().toString().isEmpty()) {
                dateInput.setError("Date is empty.");
                errorView.setText(errorView.getText() + "Date is empty. ");
            } else if (isDateEndAfterDateStart(dateInput, null, false)) {
                errors++;
            } else {
                dateInput.setError(null);
            }
            if (timeStartInput.getText().toString().isEmpty()) {
                timeStartInput.setError("Time is empty.");
                errorView.setText(errorView.getText() + "Time start is empty. ");
                isEmpty = true;
            }
            if (timeEndInput.getText().toString().isEmpty()) {
                timeEndInput.setError("Time is empty.");
                errorView.setText(errorView.getText() + "Time end is empty. ");
                isEmpty = true;
            }
            if (!isEmpty) {
                timeStartInput.setError(null);
                timeEndInput.setError(null);
                if (isDateEndAfterDateStart(timeStartInput, timeEndInput, false))
                    errors++;
            }
        } else if (recurringRadio.isChecked()) {
            if (dateRStartInput.getText().toString().isEmpty()) {
                dateRStartInput.setError("Date is empty.");
                errorView.setText(errorView.getText() + "Date start is empty. ");
                isEmpty = true;
            }
            if (dateREndInput.getText().toString().isEmpty()) {
                dateREndInput.setError("Date is empty.");
                errorView.setText(errorView.getText() + "Date end is empty. ");
                isEmpty = true;
            }
            if (!isEmpty) {
                dateRStartInput.setError(null);
                dateREndInput.setError(null);
                if (isDateEndAfterDateStart(dateRStartInput, dateREndInput, true))
                    errors++;
            }
            isEmpty = false;
            if (timeRStartInput.getText().toString().isEmpty()) {
                timeRStartInput.setError("Time is empty.");
                errorView.setText(errorView.getText() + "Time start is empty. ");
                isEmpty = true;
            }
            if (timeREndInput.getText().toString().isEmpty()) {
                timeREndInput.setError("Time is empty.");
                errorView.setText(errorView.getText() + "Time end is empty. ");
                isEmpty = true;
            }
            if (!isEmpty) {
                timeRStartInput.setError(null);
                timeREndInput.setError(null);
                if (isDateEndAfterDateStart(timeRStartInput, timeREndInput, false))
                    errors++;
            }
            int checked = 0;
            if (monCheck.isChecked()) checked++;
            if (tueCheck.isChecked()) checked++;
            if (wedCheck.isChecked()) checked++;
            if (thuCheck.isChecked()) checked++;
            if (friCheck.isChecked()) checked++;
            if (satCheck.isChecked()) checked++;
            if (sunCheck.isChecked()) checked++;
            if (checked == 0) {
                errors++;
                daysLabel.setText("Days Repeat (Please select at least one)");
                errorView.setText(errorView.getText() + "Select at least one day. ");
            } else {
                daysLabel.setText(null);
            }
        }

        if (errors == 0) {
            Toast.makeText(getApplicationContext(), "Please correct errors", Toast.LENGTH_SHORT)
                    .show();
            errorView.setVisibility(View.VISIBLE);
        } else {
            errorView.setText("Errors: ");
            errorView.setVisibility(View.GONE);
        }
        return errors == 0;
    }

    private boolean isDateEndAfterDateStart(View vStart, View vEnd, boolean isDate) {
        SimpleDateFormat formatter;
        if (isDate) {
            formatter = new SimpleDateFormat("MM/dd/yyyy");
        } else {
            formatter = new SimpleDateFormat("hh:mm a");
        }
        try {
            dateStart = formatter.parse(((TextInputEditText)vStart).getText().toString());
            ((TextInputEditText) vStart).setError("");
            if (dateStart.compareTo(currentDate.getTime()) > 0 || isDate) {
                if (vEnd!=null) {
                    dateEnd = formatter.parse(((TextInputEditText)vEnd).getText().toString());
                    ((TextInputEditText) vEnd).setError("");
                    if (dateStart.compareTo(dateEnd) < 0) {
                        return true;
                    } else {
                        ((TextInputEditText) vEnd).setError("End is before Start.");
                        if (isDate)
                            errorView.setText(errorView.getText() + "Date End is before Start. ");
                        else
                            errorView.setText(errorView.getText() + "Time end is before Start. ");
                    }
                } else {
                    return true;
                }
            } else {
                ((TextInputEditText) vStart).setError("Start is before now.");
                if (isDate)
                    errorView.setText(errorView.getText() + "Date Start is in the past. ");
                else
                    errorView.setText(errorView.getText() + "Time Start is in the past. ");
            }
        } catch (ParseException e) {
            ((TextInputEditText) vStart).setError("Date/Time parsing error");
            errorView.setText(errorView.getText() + "Date/Time parsing error. ");
            if (vEnd!=null) {
                ((TextInputEditText) vEnd).setError("Date/Time parsing error");
            }
            return false;
        }
        return false;
    }

    private void setEduSpinner() {
        for (Educator educator : Educator.getEducatorsByCenterId(Account.getCenterId())) {
            educators.add(educator);
        }
        List<String> eduSpin = new ArrayList<>();
        eduSpin.add("Select Educator");
        for (Educator educator: educators) {
            eduSpin.add(educator.getFullname());
        }
        ArrayAdapter<String> aadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eduSpin);
        aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eduSpinner.setAdapter(aadapter);
    }

    private void bindDateTimeInputs() {
        binding.classNveScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveClass.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.classNveScheduleDate.setText(month+1 + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        binding.classNveRDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveClass.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.classNveRDateStart.setText(month+1 + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date Start");
                datePickerDialog.show();
            }
        });
        binding.classNveRDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveClass.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.classNveRDateEnd.setText(month+1 + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date End");
                datePickerDialog.show();
            }
        });
        binding.classNveScheduleTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveClass.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.classNveScheduleTimeStart.setText(formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
            }
        });
        binding.classNveScheduleTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveClass.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.classNveScheduleTimeEnd.setText(formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
            }
        });
        binding.classNveRTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveClass.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.classNveRTimeStart.setText(formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
            }
        });
        binding.classNveRTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                final int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveClass.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.classNveRTimeEnd.setText(formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
            }
        });
    }

    private String formatTime(int hourOfDay, int minute) {
        String time = "";
        if (hourOfDay==0) {
            time = "12:" + minute + " am";
        } else if (hourOfDay<=11) {
            time = hourOfDay + ":" + minute + " am";
        } else if (hourOfDay==12) {
            time = "12:" + minute + " pm";
        } else {
            time = (hourOfDay-12) + ":" + minute + " pm";
        }
        return time;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void bindViews() {
        binding = ActivityNveClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.classNveScheduleDate.setInputType(InputType.TYPE_NULL);
        binding.classNveRDateStart.setInputType(InputType.TYPE_NULL);
        binding.classNveRDateEnd.setInputType(InputType.TYPE_NULL);
        currentDate = Calendar.getInstance();

        statusSpinner = findViewById(R.id.class_nve_status_spinner);
        eduSpinner = findViewById(R.id.class_nve_edu_spinner);
        typeGroup = findViewById(R.id.class_nve_type_radiogroup);
        singleRadio = findViewById(R.id.class_nve_single);
        recurringRadio = findViewById(R.id.class_nve_recurring);
        singleLayout = findViewById(R.id.class_nve_single_layout);
        recurringLayout = findViewById(R.id.class_nve_recurring_layout);
        titleTextView = findViewById(R.id.class_nve_title);
        errorView = findViewById(R.id.class_nve_schedule_error);
        daysLabel = findViewById(R.id.class_nve_days_label);
        roomNo = findViewById(R.id.class_nve_room);
        timeStartInput = findViewById(R.id.class_nve_schedule_time_start);
        timeEndInput = findViewById(R.id.class_nve_schedule_time_end);
        dateInput = findViewById(R.id.class_nve_schedule_date);
        timeRStartInput = findViewById(R.id.class_nve_r_time_start);
        timeREndInput = findViewById(R.id.class_nve_r_time_end);
        dateRStartInput = findViewById(R.id.class_nve_r_date_start);
        dateREndInput = findViewById(R.id.class_nve_r_date_end);
        monCheck = findViewById(R.id.class_nve_monday);
        tueCheck = findViewById(R.id.class_nve_tuesday);
        wedCheck = findViewById(R.id.class_nve_wednesday);
        thuCheck = findViewById(R.id.class_nve_thursday);
        friCheck = findViewById(R.id.class_nve_friday);
        satCheck = findViewById(R.id.class_nve_saturday);
        sunCheck = findViewById(R.id.class_nve_sunday);
        submit = findViewById(R.id.class_nve_ok_button);
    }
}