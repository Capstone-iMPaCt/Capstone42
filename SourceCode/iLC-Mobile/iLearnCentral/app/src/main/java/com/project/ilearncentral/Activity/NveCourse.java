package com.project.ilearncentral.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.BankAccountList;
import com.project.ilearncentral.databinding.ActivityNveCourseBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NveCourse extends AppCompatActivity {

    private ActivityNveCourseBinding binding;

    private static final String TAG = "NveCourse";

    private OnBooleanChangeListener bankDataListener;
    private boolean others, typeNone;
    private DatePickerDialog datePickerDialog;
    private Calendar currentDate;

    private Course course;
    private Date dateStart;
    private Date dateEnd;

    private static List<BankAccountDetail> bankAccountList;
    private BankAccountDetail bankAccountDetailRef;

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
        initialize();

        binding.courseNveScheduleDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveCourse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.courseNveScheduleDateStart.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date Start");
                datePickerDialog.show();
            }
        });
        binding.courseNveScheduleDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(NveCourse.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        binding.courseNveScheduleDateEnd.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date End");
                datePickerDialog.show();
            }
        });
        binding.courseNveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                others = false;
                typeNone = false;
                if (i == 0) {
                    typeNone = true;
                }
                if (adapterView.getSelectedItem().toString().equals("Others")) {
                    binding.courseNveTypeLayout.setVisibility(View.VISIBLE);
                    others = true;
                } else {
                    binding.courseNveTypeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.courseNveTypeSpinner.requestFocus();
                Toast.makeText(getApplicationContext(), "Please select a Course Type", Toast.LENGTH_SHORT).show();
            }
        });

        binding.courseNveAddBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NveBankAccountDetail.class));
            }
        });
        binding.courseNvePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noError()) {
                    course = new Course();
                    if (others) {
                        course.setCourseType(binding.courseNveType.getText().toString());
                    } else {
                        course.setCourseType(binding.courseNveTypeSpinner.getSelectedItem().toString());
                    }
                    course.setCourseFee(Double.parseDouble(binding.courseNveFee.getText().toString()));
                    course.setScheduleFrom(new Timestamp(dateStart));
                    course.setScheduleTo(new Timestamp(dateEnd));
                    course.setCourseName(binding.courseNveName.getText().toString());
                    course.setCourseDescription(binding.courseNveDescription.getText().toString());
                    course.setToDB();
                    Toast.makeText(NveCourse.this, "Course saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void initialize() {
        binding = ActivityNveCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        binding.courseNveScheduleDateStart.setInputType(InputType.TYPE_NULL);
        binding.courseNveScheduleDateEnd.setInputType(InputType.TYPE_NULL);
        currentDate = Calendar.getInstance();
    }

    private void setBankDetails() {
        binding.courseNveBankAccountDetails.setText("");
        if (bankAccountList.isEmpty())
            binding.courseNveBankAccountDetails.setText("NONE");
        else {
            for (BankAccountDetail details : bankAccountList) {
                if (binding.courseNveBankAccountDetails.getText().equals("NONE")) {
                    binding.courseNveBankAccountDetails.setText("* " + details.getBankName()
                            + ": " + details.getBankAccountNumber() + "\n");
                } else {
                    binding.courseNveBankAccountDetails.append("* " + details.getBankName()
                            + ": " + details.getBankAccountNumber() + "\n");
                }
            }
        }
    }

    private boolean noError() {
        int errorCount = 0;
        if (binding.courseNveBankAccountDetails.getText().equals("NONE")) {
            AlertDialog alertDialog = new AlertDialog.Builder(NveCourse.this).create();
            alertDialog.setTitle("Bank Account Alert!");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Please add at least 1 bank account detail for enrolees to send their payments to.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            errorCount++;
        }
        if (others) {
            if (binding.courseNveType.getText().toString().isEmpty()) {
                binding.courseNveType.setError("Field is empty");
                errorCount++;
            }
        }
        if (typeNone) {
            binding.courseNveTypeSpinner.requestFocus();
            Toast.makeText(getApplicationContext(), "Please select a Course Type", Toast.LENGTH_LONG).show();
            errorCount++;
        }
        if (binding.courseNveFee.getText().toString().isEmpty()) {
            binding.courseNveFee.setError("Field is empty");
            errorCount++;
        }
        if (binding.courseNveScheduleDateStart.getText().toString().isEmpty()) {
            binding.courseNveFee.setError("Field is empty");
            errorCount++;
        }
        if (binding.courseNveScheduleDateEnd.getText().toString().isEmpty()) {
            binding.courseNveFee.setError("Field is empty");
            errorCount++;
        }
        if (!isDateEndAfterDateStart()) {
            Toast.makeText(getApplicationContext(), "Invalid schedule. Please fix the dates.", Toast.LENGTH_LONG).show();
            errorCount++;
        }
        if (binding.courseNveName.getText().toString().isEmpty()) {
            binding.courseNveName.setError("Field is empty");
            errorCount++;
        }
        if (binding.courseNveDescription.getText().toString().isEmpty()) {
            binding.courseNveDescription.setError("Field is empty");
            errorCount++;
        }

        if (errorCount > 0)
            return false;
        return true;
    }

    private boolean isDateEndAfterDateStart() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateStart = formatter.parse(binding.courseNveScheduleDateStart.getText().toString());
            dateEnd = formatter.parse(binding.courseNveScheduleDateEnd.getText().toString());
            if (dateStart.compareTo(dateEnd) < 0) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
