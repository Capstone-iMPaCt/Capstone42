package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.databinding.ActivityNveClassBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestSchedChange extends AppCompatActivity {

    private ActivityNveClassBinding binding;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Calendar currentDate;
    private Class aClass;
    private String classID, oldDate, oldTimeStart, oldTimeEnd;
    private ObservableBoolean done;

    private TextView errorView;
    private TextInputEditText message, timeStartInput, timeEndInput, dateInput;
    private Button sendRequest, cancel, reset;
    private CheckBox toCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sched_change);

        Intent i = getIntent();
        classID = i.getStringExtra("classID");

        if (classID==null || classID.isEmpty())
            finish();
        aClass = Class.getClassById(classID);
        if (aClass==null)
            finish();

        bindViews();
        bindDateTime();
        setData();

        done = new ObservableBoolean();
        done.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                sendRequest.setText("Send Request");
                sendRequest.setEnabled(true);
                if (success)
                    Toast.makeText(getApplicationContext(), "Request Sent!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Unsent Request.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        toCancel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    resetSched();
                    dateInput.setEnabled(false);
                    timeStartInput.setEnabled(false);
                    timeEndInput.setEnabled(false);
                } else {
                    dateInput.setEnabled(true);
                    timeStartInput.setEnabled(true);
                    timeEndInput.setEnabled(true);
                }
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkErrors()) {
                    sendRequest.setEnabled(false);
                    sendRequest.setText("Please Wait.");
                    String msg = "Change schedule request: " +
                            "\nEducator: " + Account.getUsername();
                    if (toCancel.isChecked()) {
                        msg += "\nFor Cancellation." +
                            "\nMessage: " + message.getText().toString();
                    } else {
                        msg += "\nDate: " + dateInput.getText() +
                                "\nTime Start: " + timeStartInput.getText() +
                                "\nTime End: " + timeEndInput.getText();

                    }
                    Class.addScheduleRequest(classID, msg, done);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSched();
                message.setText("");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetSched() {
        dateInput.setText(oldDate);
        timeStartInput.setText(oldTimeStart);
        timeEndInput.setText(oldTimeEnd);
    }


    public boolean isDateEndAfterDateStart(View vStart, View vEnd, boolean isDate) {
        SimpleDateFormat formatter;
        Date dateStart = Timestamp.now().toDate();
        Date dateEnd = Timestamp.now().toDate();
        boolean valid = true;
        if (isDate) formatter = new SimpleDateFormat("MM/dd/yyyy");
        else formatter = new SimpleDateFormat("hh:mm a");
        try {
            dateStart = formatter.parse(((TextInputEditText)vStart).getText().toString());
            ((TextInputEditText) vStart).setError(null);
            if (isDate && dateStart.before(Utility.addDays(Timestamp.now().toDate(), -1))) {
                valid = false;
                ((TextInputEditText) vStart).setError(addError("Date Start is in the past. "));
            }
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
        if (!toCancel.isChecked() && oldDate.equalsIgnoreCase(dateInput.getText().toString()) &&
                oldTimeStart.equalsIgnoreCase(timeStartInput.getText().toString()) &&
                oldTimeEnd.equalsIgnoreCase(timeEndInput.getText().toString())) {
            addError(" No changes to schedule given. ");
            errors++;
        }
        if (toCancel.isChecked() && message.getText().toString().isEmpty()) {
            addError(" Add a reason for cancellation of class. ");
            errors++;
        }
        if (errors == 0) {
            errorView.setVisibility(View.GONE);
        } else {
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

    private void setData() {
        oldDate = Utility.getStringFromDate(aClass.getClassStart());
        oldTimeStart = Utility.getStringFromTime(aClass.getClassStart());
        oldTimeEnd = Utility.getStringFromTime(aClass.getClassEnd());
        errorView.setVisibility(View.GONE);

        dateInput.setText(oldDate);
        timeStartInput.setText(oldTimeStart);
        timeEndInput.setText(oldTimeEnd);
    }

    private void bindDateTime() {
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RequestSchedChange.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        dateInput.setText(Utility.formatDate(year, month, dayOfMonth));
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
                dateInput.setError(null);
            }
        });
        timeStartInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                final int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(RequestSchedChange.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEndInput.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeStartInput.setError(null);
            }
        });
        timeEndInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                view.setBackgroundColor(Color.WHITE);
                final int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int min = currentDate.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(RequestSchedChange.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEndInput.setText(Utility.formatTime(hourOfDay, minute));
                    }
                }, hour, min, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
                timeEndInput.setError(null);
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void bindViews() {
        errorView = findViewById(R.id.class_request_error);
        message = findViewById(R.id.class_request_message);
        timeStartInput = findViewById(R.id.class_request_schedule_time_start);
        timeEndInput = findViewById(R.id.class_request_schedule_time_end);
        dateInput = findViewById(R.id.class_request_schedule_date);
        sendRequest = findViewById(R.id.class_request_ok);
        cancel = findViewById(R.id.class_request_cancel);
        reset = findViewById(R.id.class_request_reset);
        toCancel = findViewById(R.id.class_request_check);
        currentDate = Calendar.getInstance();
    }
}