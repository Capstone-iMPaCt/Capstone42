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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.CustomBehavior.ObservableInteger;
import com.project.ilearncentral.CustomInterface.OnIntegerChangeListener;
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
    private Class updateClass;
    private List<Class> classes;
    private ObservableInteger classesAdded;
    private List<String> recurringDays;
    private List<Educator> educators;
    private String classID, courseID, action;
    private boolean doneTraverse;
    private ArrayAdapter<String> eduAdapter;

    private Spinner statusSpinner, eduSpinner;
    private RadioGroup typeGroup;
    private RadioButton singleRadio, recurringRadio;
    private LinearLayout singleLayout, recurringLayout;
    private TextView titleTextView, daysLabel, errorView, message;
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
        classes = new ArrayList<>();
        updateClass = new Class();
        doneTraverse = false;
        setEduSpinner();

        Intent i = getIntent();
        classID = i.getStringExtra("classID");
        courseID = i.getStringExtra("courseID");
        action = i.getStringExtra("action");

        setActionBased();
        classesAdded = new ObservableInteger();
        classesAdded.set(0);
        classesAdded.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int value) {
                if (value>0 && doneTraverse && value == classes.size()) {
                    submit.setText("SUBMIT");
                    submit.setEnabled(true);
                    updateUI();
                }
            }
        });

        errorView.setVisibility(View.GONE);

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
                        Class aClass;
                        if (action.equals("add"))
                            aClass = new Class();
                        else
                            aClass = updateClass;
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
                        Timestamp tStart = Utility.getCompleteTimestamp("", dateInput, timeStartInput);
                        Timestamp tEnd = Utility.getCompleteTimestamp("", dateInput, timeEndInput);
                        aClass.setClassStart(tStart);
                        aClass.setClassEnd(tEnd);
                        if (action.equals("add")) {
                            Class.addNewClassToDB(aClass, null);
                        } else {
                            Class.editClassToDB(aClass);
                        }
                        updateUI();
                    } else if (recurringRadio.isChecked()) {
                        Timestamp curDate = Utility.getCompleteTimestamp("", dateRStartInput, timeRStartInput);
                        Timestamp endDate = Utility.getCompleteTimestamp("", dateREndInput, timeREndInput);
                        Date dateCount = curDate.toDate();
                        if (monCheck.isChecked()) recurringDays.add("2");
                        if (tueCheck.isChecked()) recurringDays.add("3");
                        if (wedCheck.isChecked()) recurringDays.add("4");
                        if (thuCheck.isChecked()) recurringDays.add("5");
                        if (friCheck.isChecked()) recurringDays.add("6");
                        if (satCheck.isChecked()) recurringDays.add("7");
                        if (sunCheck.isChecked()) recurringDays.add("1");
                        doneTraverse = false;
                        submit.setEnabled(false);
                        submit.setText("Please Wait.");
                        classes.clear();
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
                                Timestamp tStart = Utility.getCompleteTimestamp(Utility.getDateAsString(dateCount), null, timeRStartInput);
                                Timestamp tEnd = Utility.getCompleteTimestamp(Utility.getDateAsString(dateCount), null, timeREndInput);
                                aClass.setClassStart(tStart);
                                aClass.setClassEnd(tEnd);
                                Class.addNewClassToDB(aClass, classesAdded);
                                classes.add(aClass);
                            }
                            dateCount = Utility.addDays(dateCount, 1);
                        }
                        doneTraverse = true;
                    }
                }
            }
        });
    }

    private void setActionBased() {
        if (action.equalsIgnoreCase("add")) {
            titleTextView.setText("Create New Class/es");
        } else if (action.equalsIgnoreCase("edit")) {
            Class c = Class.getClassById(classID);
            if (c!=null) {
                updateClass.setClass(c);
                typeGroup.setVisibility(View.GONE);
                titleTextView.setText("Edit Class " + classID);
                if ((c.getStatus().equalsIgnoreCase("Pending") || c.getStatus().equalsIgnoreCase("Cancelled")) && (updateClass.getRequestMessage()!=null || !updateClass.getRequestMessage().isEmpty())) {
                    message.setVisibility(View.VISIBLE);
                    message.setText(String
                            .format("%s%s", message.getText(), updateClass.getRequestMessage()));
                }
                dateInput.setText(Utility.getStringFromDate(c.getClassStart()));
                timeStartInput.setText(Utility.getStringFromTime(c.getClassStart()));
                timeEndInput.setText(Utility.getStringFromTime(c.getClassEnd()));
                roomNo.setText(c.getRoomNo());
                if (c.getEducator()!=null)
                    eduSpinner.setSelection(eduAdapter.getPosition(c.getEducator().getFullname()));
                String[] temp = getResources().getStringArray(R.array.class_status);
                for (int i=0; i< temp.length; i++) {
                    if (temp[i].equalsIgnoreCase(c.getStatus())) {
                        statusSpinner.setSelection(i);
                        break;
                    } else {
                        statusSpinner.setSelection(0);
                    }
                }
                submit.setText("UPDATE");
            } else {
                setResult(RESULT_CANCELED);
                Toast.makeText(getApplicationContext(), "Class not retrieved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

        }
    }

    public boolean isDateEndAfterDateStart(View vStart, View vEnd, boolean isDate) {
        SimpleDateFormat formatter;
        Date dateStart = Timestamp.now().toDate();
        Date dateEnd = Timestamp.now().toDate();
        boolean valid = true;
        if (isDate) formatter = new SimpleDateFormat("MM/dd/yyyy");
        else formatter = new SimpleDateFormat("hh:mm a");
        try {
            ((TextInputEditText) vStart).setError(null);
            if (isDate && action.equalsIgnoreCase("edit")
                     && dateStart.before(Utility.addDays(Timestamp.now().toDate(), -1))) {
                valid = false;
                ((TextInputEditText) vStart).setError(addError("Date Start is in the past. "));
            }
            dateStart = formatter.parse(((TextInputEditText)vStart).getText().toString());
            if (vEnd!=null) {
                dateEnd = formatter.parse(((TextInputEditText)vEnd).getText().toString());
                ((TextInputEditText) vEnd).setError(null);
                if (dateStart.after(dateEnd)) {
                    valid = false;
                    if (isDate)
                        ((TextInputEditText) vEnd).setError(addError("Date End is before Start. "));
                    else
                        ((TextInputEditText) vEnd).setError(addError("Time End is before Start. "));
                }
            }
        } catch (ParseException e) {
            ((TextInputEditText) vStart).setError(addError("Date/Time Start parsing error. " + dateStart + ". "));
            if (vEnd!=null) {
                ((TextInputEditText) vEnd).setError(addError("Date/Time End parsing error. " + dateEnd + ". "));
            }
            valid = false;
        }
        return !valid;
    }

    private boolean checkErrors() {
        int errors = 0;
        boolean isEmpty = false;
        addError("Errors:", false);
        errorView.setVisibility(View.GONE);
        if(singleRadio.isChecked()) {
            if (dateInput.getText().toString().isEmpty()) {
                errors++;
                dateInput.setError(addError("Date is empty."));
            } else if (isDateEndAfterDateStart(dateInput, null, true)) {
                errors++;
            } else {
                dateInput.setError(null);
            }
            if (timeStartInput.getText().toString().isEmpty()) {
                errors++;
                timeStartInput.setError(addError("Time start is empty. "));
                isEmpty = true;
            }
            if (timeEndInput.getText().toString().isEmpty()) {
                errors++;
                timeEndInput.setError(addError("Time end is empty. "));
                isEmpty = true;
            }
            if (!isEmpty) {
                timeStartInput.setError(null);
                timeEndInput.setError(null);
                if (isDateEndAfterDateStart(timeStartInput, timeEndInput, false)) {
                    errors++;
                    addError("Time Start after End. ");
                }
            }
        } else if (recurringRadio.isChecked()) {
            if (dateRStartInput.getText().toString().isEmpty()) {
                errors++;
                dateRStartInput.setError(addError("Date start is empty. "));
                isEmpty = true;
            }
            if (dateREndInput.getText().toString().isEmpty()) {
                errors++;
                dateREndInput.setError(addError("Date end is empty."));
                isEmpty = true;
            }
            if (!isEmpty) {
                dateRStartInput.setError(null);
                dateREndInput.setError(null);
                if (isDateEndAfterDateStart(dateRStartInput, dateREndInput, true)) {
                    errors++;
                    addError("Date Start after End. ");
                }
            }
            isEmpty = false;
            if (timeRStartInput.getText().toString().isEmpty()) {
                errors++;
                timeRStartInput.setError(addError("Time start is empty. "));
                isEmpty = true;
            }
            if (timeREndInput.getText().toString().isEmpty()) {
                errors++;
                timeREndInput.setError(addError("Time end is empty. "));
                isEmpty = true;
            }
            if (!isEmpty) {
                timeRStartInput.setError(null);
                timeREndInput.setError(null);
                if (isDateEndAfterDateStart(timeRStartInput, timeREndInput, false)) {
                    errors++;
                    addError("Time Start after End. ");
                }
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
                addError("Select at least one day. ");
            } else {
                daysLabel.setText(null);
            }
        }

        if (errors == 0) {
            addError("Errors(" + errors + "): ");
            errorView.setVisibility(View.GONE);
        } else {
            addError("Errors(" + errors + "): ");
            Toast.makeText(getApplicationContext(), "Please correct errors", Toast.LENGTH_SHORT)
                    .show();
            errorView.setVisibility(View.VISIBLE);
        }
        return errors == 0;
    }

    private String addError(String errorString) {
        return addError(errorString, true);
    }
    private String addError(String errorString, boolean append) {
        if (append) {
            errorView.setText(String.format("%s %s", errorView.getText().toString(), errorString));
        } else {
            errorView.setText(errorString);
        }
        return errorString;
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
        eduAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eduSpin);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eduSpinner.setAdapter(eduAdapter);
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
                        binding.classNveScheduleDate.setText(Utility.formatDate(year, month, dayOfMonth));
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
                dateInput.setError(null);
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
                        binding.classNveRDateStart.setText(Utility.formatDate(year, month, dayOfMonth));
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date Start");
                datePickerDialog.show();
                dateRStartInput.setError(null);
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
                        binding.classNveRDateEnd.setText(Utility.formatDate(year, month, dayOfMonth));
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date End");
                datePickerDialog.show();
                dateREndInput.setError(null);
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
                        binding.classNveScheduleTimeStart.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeStartInput.setError(null);
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
                        binding.classNveScheduleTimeEnd.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeEndInput.setError(null);
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
                        binding.classNveRTimeStart.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeRStartInput.setError(null);
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
                        binding.classNveRTimeEnd.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeREndInput.setError(null);
            }
        });
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
        message = findViewById(R.id.class_nve_schedule_message);
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

    public void updateUI() {
        Toast.makeText(getApplicationContext(), "Successfully Added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}