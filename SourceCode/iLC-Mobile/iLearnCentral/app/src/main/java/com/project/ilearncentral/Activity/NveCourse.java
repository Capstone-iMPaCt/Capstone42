package com.project.ilearncentral.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.MyClass.BankAccountList;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NveCourse extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NveCourse";
    private TextInputEditText timeStart, timeEnd, dateStart, dateEnd;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Calendar currentTime, currentDate;

    private TextView sun, mon, tue, wed, thu, fri, sat, banksDetails;
    private boolean sunSelected, monSelected, tueSelected, wedSelected, thuSelected, friSelected, satSelected;
    private Button addBankDetail, post;

    public static List<BankAccountDetail> bankAccountList;
    private BankAccountDetail bankAccountDetailRef;

    private OnBooleanChangeListener bankDataListener;

    @Override
    protected void onResume() {
        super.onResume();
        // initialize bank account details
        BankAccountList.data.clear();
        bankAccountDetailRef.getDataFromDB(bankAccountList, bankDataListener);
        setBankDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nve_course);

        initialize();

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm;
                        if (hourOfDay < 12) {
                            am_pm = "AM";
                        } else {
                            hourOfDay -= 12;
                            am_pm = "PM";
                        }
                        timeStart.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute) + " " + am_pm);
                        timeStart.setError(null);
                    }
                }, hour, minute, false);
                timePickerDialog.setTitle("Select Time Start");
                timePickerDialog.show();
            }
        });
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(NveCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm;
                        if (hourOfDay < 12) {
                            am_pm = "AM";
                        } else {
                            hourOfDay -= 12;
                            am_pm = "PM";
                        }
                        timeEnd.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute) + " " + am_pm);
                        timeEnd.setError(null);
                    }
                }, hour, minute, false);
                timePickerDialog.setTitle("Select Time End");
                timePickerDialog.show();
            }
        });
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveCourse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        dateStart.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date Start");
                datePickerDialog.show();
            }
        });
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveCourse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        dateEnd.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date End");
                datePickerDialog.show();
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
        addBankDetail.setOnClickListener(this);
    }

    private void initialize() {
        bankAccountDetailRef = new BankAccountDetail();
        bankAccountList = new ArrayList<>();
        bankDataListener = new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    BankAccountList.data = bankAccountList;
                    setBankDetails();
                }
            }
        };

        banksDetails = findViewById(R.id.course_nve_bank_account_details);
        addBankDetail = findViewById(R.id.course_nve_add_bank_account);
        post = findViewById(R.id.course_nve_button);
        timeStart = findViewById(R.id.course_nve_schedule_time_start);
        timeStart.setInputType(InputType.TYPE_NULL);
        timeEnd = findViewById(R.id.course_nve_schedule_time_end);
        timeEnd.setInputType(InputType.TYPE_NULL);
        dateStart = findViewById(R.id.course_nve_schedule_date_start);
        dateStart.setInputType(InputType.TYPE_NULL);
        dateEnd = findViewById(R.id.course_nve_schedule_date_end);
        dateEnd.setInputType(InputType.TYPE_NULL);
        currentTime = Calendar.getInstance();
        currentDate = Calendar.getInstance();
        sun = findViewById(R.id.course_sched_day_sun);
        mon = findViewById(R.id.course_sched_day_mon);
        tue = findViewById(R.id.course_sched_day_tue);
        wed = findViewById(R.id.course_sched_day_wed);
        thu = findViewById(R.id.course_sched_day_thu);
        fri = findViewById(R.id.course_sched_day_fri);
        sat = findViewById(R.id.course_sched_day_sat);
    }

    private void setBankDetails() {
        banksDetails.setText("");
        if (bankAccountList.isEmpty())
            banksDetails.setText("NONE");
        else {
            for (BankAccountDetail details : bankAccountList) {
                if (banksDetails.getText().equals("NONE"))
                    banksDetails.setText("* " + details.getBankName() + ": " + details.getBankAccountNumber() + "\n");
                else
                    banksDetails.append("* " + details.getBankName() + ": " + details.getBankAccountNumber() + "\n");
            }
        }
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
            case R.id.course_nve_add_bank_account:
                startActivity(new Intent(getApplicationContext(), NveBankAccountDetail.class));
                break;
        }
    }

    private boolean setSelection(View view, boolean bool) {
        if (!bool) {
            view.setBackgroundResource(R.drawable.bg_selected_day_rounded);
            return true;
        } else {
            view.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
            return false;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(timeStart.getWindowToken(), 0);
    }
}
